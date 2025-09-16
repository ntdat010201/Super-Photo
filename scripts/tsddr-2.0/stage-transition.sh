#!/bin/bash

# TSDDR 2.0 Stage Transition Script
# Automates stage transitions with quality gate validation

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

# Configuration
CONFIG_FILE=".tsddr-config.json"
STAGES=("requirements" "design" "tasks" "execution" "test" "review")

# Functions
show_usage() {
    echo "Usage: $0 <command> [stage]"
    echo ""
    echo "Commands:"
    echo "  start <stage>     - Start working on a specific stage"
    echo "  validate <stage>  - Validate quality gates for a stage"
    echo "  next             - Move to next stage (with validation)"
    echo "  status           - Show current stage status"
    echo "  list             - List all stages"
    echo ""
    echo "Stages: ${STAGES[*]}"
}

log_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

log_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

log_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

log_error() {
    echo -e "${RED}❌ $1${NC}"
}

log_stage() {
    echo -e "${PURPLE}🎯 $1${NC}"
}

# Check if config file exists
check_config() {
    if [ ! -f "$CONFIG_FILE" ]; then
        log_error "TSDDR 2.0 configuration file not found: $CONFIG_FILE"
        log_info "Run: ./scripts/tsddr-2.0/init-tsddr-branch.sh first"
        exit 1
    fi
}

# Get current git branch
get_current_branch() {
    git branch --show-current
}

# Get current stage from branch name
get_current_stage() {
    local branch=$(get_current_branch)
    if [[ $branch == tsddr/* ]]; then
        echo "${branch#tsddr/}"
    else
        echo "none"
    fi
}

# Validate stage name
validate_stage() {
    local stage=$1
    for valid_stage in "${STAGES[@]}"; do
        if [ "$stage" = "$valid_stage" ]; then
            return 0
        fi
    done
    return 1
}

# Get next stage
get_next_stage() {
    local current_stage=$1
    for i in "${!STAGES[@]}"; do
        if [ "${STAGES[$i]}" = "$current_stage" ]; then
            if [ $((i + 1)) -lt ${#STAGES[@]} ]; then
                echo "${STAGES[$((i + 1))]}"
                return 0
            fi
        fi
    done
    return 1
}

# Check if deliverables exist
check_deliverables() {
    local stage=$1
    local deliverables=$(jq -r ".stages.${stage}.deliverables[]" "$CONFIG_FILE" 2>/dev/null)
    local missing_count=0
    
    log_info "Checking deliverables for $stage stage..."
    
    while IFS= read -r deliverable; do
        if [ -n "$deliverable" ] && [ "$deliverable" != "null" ]; then
            if [[ "$deliverable" == *.md ]]; then
                # Check for markdown files
                if [ ! -f "docs/$deliverable" ] && [ ! -f "$deliverable" ]; then
                    log_warning "Missing deliverable: $deliverable"
                    ((missing_count++))
                else
                    log_success "Found deliverable: $deliverable"
                fi
            elif [[ "$deliverable" == "source_code" ]]; then
                # Check for source code (basic check)
                if [ ! -d "src" ] && [ ! -d "lib" ] && [ ! -d "app" ]; then
                    log_warning "Missing source code directory"
                    ((missing_count++))
                else
                    log_success "Found source code"
                fi
            else
                # Generic file check
                if [ ! -f "$deliverable" ] && [ ! -d "$deliverable" ]; then
                    log_warning "Missing deliverable: $deliverable"
                    ((missing_count++))
                else
                    log_success "Found deliverable: $deliverable"
                fi
            fi
        fi
    done <<< "$deliverables"
    
    return $missing_count
}

# Run quality gate checks
run_quality_gates() {
    local stage=$1
    local quality_gates=$(jq -r ".stages.${stage}.quality_gates[]" "$CONFIG_FILE" 2>/dev/null)
    local failed_count=0
    
    log_info "Running quality gates for $stage stage..."
    
    while IFS= read -r gate; do
        if [ -n "$gate" ] && [ "$gate" != "null" ]; then
            case $gate in
                "requirements_documented")
                    if [ -f "docs/requirements.md" ] || [ -f "requirements.md" ]; then
                        log_success "Quality gate passed: $gate"
                    else
                        log_error "Quality gate failed: $gate"
                        ((failed_count++))
                    fi
                    ;;
                "acceptance_criteria_defined")
                    if [ -f "docs/acceptance-criteria.md" ] || [ -f "acceptance-criteria.md" ]; then
                        log_success "Quality gate passed: $gate"
                    else
                        log_error "Quality gate failed: $gate"
                        ((failed_count++))
                    fi
                    ;;
                "unit_tests_pass")
                    if command -v npm &> /dev/null && [ -f "package.json" ]; then
                        if npm test 2>/dev/null; then
                            log_success "Quality gate passed: $gate"
                        else
                            log_error "Quality gate failed: $gate"
                            ((failed_count++))
                        fi
                    else
                        log_warning "Skipping $gate - no npm/package.json found"
                    fi
                    ;;
                "integration_tests_pass")
                    if [ -f "scripts/tsddr-2.0/test-runner.sh" ]; then
                        if ./scripts/tsddr-2.0/test-runner.sh integration 2>/dev/null; then
                            log_success "Quality gate passed: $gate"
                        else
                            log_error "Quality gate failed: $gate"
                            ((failed_count++))
                        fi
                    else
                        log_warning "Skipping $gate - test runner not found"
                    fi
                    ;;
                "ai_revenue_tests_pass")
                    if [ -f "scripts/tsddr-2.0/ai-test-suite.sh" ]; then
                        if ./scripts/tsddr-2.0/ai-test-suite.sh 2>/dev/null; then
                            log_success "Quality gate passed: $gate"
                        else
                            log_error "Quality gate failed: $gate"
                            ((failed_count++))
                        fi
                    else
                        log_warning "Skipping $gate - AI test suite not found"
                    fi
                    ;;
                "code_review_approved")
                    # Check for review approval (simplified)
                    if git log --oneline -1 | grep -q "review\|approve"; then
                        log_success "Quality gate passed: $gate"
                    else
                        log_warning "Quality gate needs manual verification: $gate"
                    fi
                    ;;
                *)
                    log_info "Custom quality gate: $gate (manual verification required)"
                    ;;
            esac
        fi
    done <<< "$quality_gates"
    
    return $failed_count
}

# Start working on a stage
start_stage() {
    local stage=$1
    
    if ! validate_stage "$stage"; then
        log_error "Invalid stage: $stage"
        show_usage
        exit 1
    fi
    
    local branch="tsddr/$stage"
    local current_branch=$(get_current_branch)
    
    log_stage "Starting $stage stage"
    
    # Switch to stage branch
    if [ "$current_branch" != "$branch" ]; then
        log_info "Switching to branch: $branch"
        git checkout "$branch" || {
            log_error "Failed to checkout branch: $branch"
            log_info "Make sure the branch exists. Run: ./scripts/tsddr-2.0/init-tsddr-branch.sh"
            exit 1
        }
    fi
    
    # Create stage directory if it doesn't exist
    mkdir -p "docs/$stage"
    
    # Create stage-specific files based on deliverables
    local deliverables=$(jq -r ".stages.${stage}.deliverables[]" "$CONFIG_FILE" 2>/dev/null)
    
    while IFS= read -r deliverable; do
        if [ -n "$deliverable" ] && [ "$deliverable" != "null" ]; then
            if [[ "$deliverable" == *.md ]] && [ ! -f "docs/$deliverable" ]; then
                log_info "Creating template: docs/$deliverable"
                cat > "docs/$deliverable" << EOF
# ${deliverable%.md}

## Overview

This document covers the ${deliverable%.md} for the $stage stage of TSDDR 2.0.

## Status

- [ ] In Progress
- [ ] Review Required
- [ ] Completed

## Content

<!-- Add your content here -->

## Quality Gates

$(jq -r ".stages.${stage}.quality_gates[]" "$CONFIG_FILE" 2>/dev/null | sed 's/^/- [ ] /')

## Notes

<!-- Add any additional notes -->
EOF
            fi
        fi
    done <<< "$deliverables"
    
    log_success "Started working on $stage stage"
    log_info "Branch: $branch"
    log_info "Documentation: docs/$stage/"
    
    # Show quality gates
    echo ""
    log_info "Quality gates for this stage:"
    jq -r ".stages.${stage}.quality_gates[]" "$CONFIG_FILE" 2>/dev/null | sed 's/^/  • /'
}

# Validate current stage
validate_stage_gates() {
    local stage=$1
    
    if [ "$stage" = "none" ]; then
        log_error "Not on a TSDDR stage branch"
        exit 1
    fi
    
    if ! validate_stage "$stage"; then
        log_error "Invalid stage: $stage"
        exit 1
    fi
    
    log_stage "Validating $stage stage"
    
    # Check deliverables
    check_deliverables "$stage"
    local deliverable_issues=$?
    
    # Run quality gates
    run_quality_gates "$stage"
    local quality_gate_failures=$?
    
    echo ""
    if [ $deliverable_issues -eq 0 ] && [ $quality_gate_failures -eq 0 ]; then
        log_success "All quality gates passed for $stage stage! 🎉"
        return 0
    else
        log_error "Quality gate validation failed:"
        [ $deliverable_issues -gt 0 ] && log_error "  • $deliverable_issues missing deliverables"
        [ $quality_gate_failures -gt 0 ] && log_error "  • $quality_gate_failures failed quality gates"
        return 1
    fi
}

# Move to next stage
move_to_next_stage() {
    local current_stage=$(get_current_stage)
    
    if [ "$current_stage" = "none" ]; then
        log_error "Not on a TSDDR stage branch"
        log_info "Use: $0 start <stage> to begin"
        exit 1
    fi
    
    # Validate current stage first
    log_info "Validating current stage: $current_stage"
    if ! validate_stage_gates "$current_stage"; then
        log_error "Cannot proceed to next stage. Fix quality gate issues first."
        exit 1
    fi
    
    # Get next stage
    local next_stage=$(get_next_stage "$current_stage")
    if [ $? -ne 0 ]; then
        log_success "You're at the final stage: $current_stage"
        log_info "Consider merging to develop branch"
        exit 0
    fi
    
    log_success "Current stage ($current_stage) validation passed!"
    log_info "Moving to next stage: $next_stage"
    
    # Commit current changes
    if ! git diff --quiet; then
        log_info "Committing current changes..."
        git add .
        git commit -m "Complete $current_stage stage - ready for $next_stage"
    fi
    
    # Start next stage
    start_stage "$next_stage"
}

# Show current status
show_status() {
    local current_stage=$(get_current_stage)
    local current_branch=$(get_current_branch)
    
    echo -e "${BLUE}📊 TSDDR 2.0 Status${NC}"
    echo "=================="
    echo "Current Branch: $current_branch"
    echo "Current Stage: $current_stage"
    
    if [ "$current_stage" != "none" ]; then
        echo ""
        echo "Quality Gates:"
        jq -r ".stages.${current_stage}.quality_gates[]" "$CONFIG_FILE" 2>/dev/null | sed 's/^/  • /'
        
        echo ""
        echo "Deliverables:"
        jq -r ".stages.${current_stage}.deliverables[]" "$CONFIG_FILE" 2>/dev/null | sed 's/^/  • /'
        
        # Quick validation
        echo ""
        log_info "Quick validation:"
        validate_stage_gates "$current_stage" > /dev/null 2>&1
        if [ $? -eq 0 ]; then
            log_success "All quality gates are passing"
        else
            log_warning "Some quality gates need attention"
        fi
    fi
}

# List all stages
list_stages() {
    echo -e "${BLUE}📋 TSDDR 2.0 Stages${NC}"
    echo "=================="
    
    local current_stage=$(get_current_stage)
    
    for i in "${!STAGES[@]}"; do
        local stage="${STAGES[$i]}"
        local marker="  "
        
        if [ "$stage" = "$current_stage" ]; then
            marker="▶ "
        fi
        
        echo -e "${marker}$((i + 1)). $stage"
        
        # Show quality gates count
        local gate_count=$(jq -r ".stages.${stage}.quality_gates | length" "$CONFIG_FILE" 2>/dev/null)
        echo "     Quality Gates: $gate_count"
        
        # Show deliverables count
        local deliverable_count=$(jq -r ".stages.${stage}.deliverables | length" "$CONFIG_FILE" 2>/dev/null)
        echo "     Deliverables: $deliverable_count"
        echo ""
    done
}

# Main script logic
check_config

if [ $# -eq 0 ]; then
    show_usage
    exit 1
fi

command=$1
stage=$2

case $command in
    "start")
        if [ -z "$stage" ]; then
            log_error "Stage name required"
            show_usage
            exit 1
        fi
        start_stage "$stage"
        ;;
    "validate")
        if [ -z "$stage" ]; then
            stage=$(get_current_stage)
        fi
        validate_stage_gates "$stage"
        ;;
    "next")
        move_to_next_stage
        ;;
    "status")
        show_status
        ;;
    "list")
        list_stages
        ;;
    *)
        log_error "Unknown command: $command"
        show_usage
        exit 1
        ;;
esac