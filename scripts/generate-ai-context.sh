#!/bin/bash

# AI Context Generator Script
# Generates portable context files for different AI tools (Claude, Gemini, Trae, etc.)
# Based on .cursor/rules/context-generator.mdc

set -e

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Configuration
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
CONTEXT_OUTPUT_DIR="$PROJECT_ROOT/.cursor/generated"
RULES_DIR="$PROJECT_ROOT/.cursor/rules"
KIRO_SPECS_DIR="$PROJECT_ROOT/.kiro/specs"

# Ensure output directory exists
mkdir -p "$CONTEXT_OUTPUT_DIR"
mkdir -p "$CONTEXT_OUTPUT_DIR/archive"

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to get project info from .project-identity
get_project_info() {
    local project_identity="$PROJECT_ROOT/.project-identity"
    if [[ -f "$project_identity" ]]; then
        PROJECT_NAME=$(grep -E '"projectName":\s*"[^"]*"' "$project_identity" | sed 's/.*"projectName":\s*"\([^"]*\)".*/\1/' || echo "Unknown Project")
        PROJECT_TYPE=$(grep -E '"projectType":\s*"[^"]*"' "$project_identity" | sed 's/.*"projectType":\s*"\([^"]*\)".*/\1/' || echo "unknown")
        PROJECT_VERSION=$(grep -E '"version":\s*"[^"]*"' "$project_identity" | sed 's/.*"version":\s*"\([^"]*\)".*/\1/' || echo "1.0.0")
    else
        PROJECT_NAME="Unknown Project"
        PROJECT_TYPE="unknown"
        PROJECT_VERSION="1.0.0"
        print_warning "No .project-identity file found, using defaults"
    fi
}

# Function to collect tech stack info
get_tech_stack() {
    local tech_stack=""
    
    # Check package.json
    if [[ -f "$PROJECT_ROOT/package.json" ]]; then
        tech_stack="${tech_stack}JavaScript/Node.js, "
    fi
    
    # Check requirements.txt or setup.py
    if [[ -f "$PROJECT_ROOT/requirements.txt" ]] || [[ -f "$PROJECT_ROOT/setup.py" ]]; then
        tech_stack="${tech_stack}Python, "
    fi
    
    # Check for Android
    if [[ -f "$PROJECT_ROOT/build.gradle" ]] || [[ -f "$PROJECT_ROOT/app/build.gradle" ]]; then
        tech_stack="${tech_stack}Android/Kotlin, "
    fi
    
    # Check for iOS
    if [[ -d "$PROJECT_ROOT.xcodeproj" ]] || [[ -d "$PROJECT_ROOT.xcworkspace" ]]; then
        tech_stack="${tech_stack}iOS/Swift, "
    fi
    
    # Remove trailing comma and space
    tech_stack="${tech_stack%, }"
    
    if [[ -z "$tech_stack" ]]; then
        tech_stack="Not detected - please specify in .project-identity"
    fi
    
    echo "$tech_stack"
}

# Function to get current development stage
get_current_stage() {
    local stage="Development"
    
    # Check for active brainstorm files
    if ls "$PROJECT_ROOT"/Brainstorm_*.md 1> /dev/null 2>&1; then
        stage="Planning/Brainstorming"
    fi
    
    # Check for active Kiro tasks
    if [[ -d "$KIRO_SPECS_DIR" ]]; then
        for project_dir in "$KIRO_SPECS_DIR"/*; do
            if [[ -f "$project_dir/tasks.md" ]]; then
                # Count tasks by status
                local in_progress=$(grep -c "**Status**: In Progress" "$project_dir/tasks.md" 2>/dev/null || echo 0)
                if [[ $in_progress -gt 0 ]]; then
                    stage="Active Development (Tasks in Progress)"
                    break
                fi
            fi
        done
    fi
    
    echo "$stage"
}

# Function to collect all rules
collect_rules() {
    local output_file="$1"
    local format="$2"
    
    print_status "Collecting rules from $RULES_DIR..."
    
    # Priority order for rules
    local priority_rules=(
        "base-rules.mdc"
        "development-rules.mdc"
        "planning-workflow.mdc"
        "kiro-task-execution.mdc"
        "context-generator.mdc"
        "import-analyzer.mdc"
        "async-validator.mdc"
    )
    
    # Collect priority rules first
    for rule in "${priority_rules[@]}"; do
        if [[ -f "$RULES_DIR/$rule" ]]; then
            echo -e "\n### Rule: $rule\n" >> "$output_file"
            # Extract content without front matter
            sed '1,/^---$/d' "$RULES_DIR/$rule" | sed '1,/^---$/d' >> "$output_file"
        fi
    done
    
    # Collect remaining rules
    for rule_file in "$RULES_DIR"/*.mdc; do
        local basename=$(basename "$rule_file")
        # Skip if already processed
        if [[ ! " ${priority_rules[@]} " =~ " ${basename} " ]]; then
            echo -e "\n### Rule: $basename\n" >> "$output_file"
            sed '1,/^---$/d' "$rule_file" | sed '1,/^---$/d' >> "$output_file"
        fi
    done
}

# Function to collect current context (brainstorm, tasks, etc.)
collect_current_context() {
    local output_file="$1"
    
    print_status "Collecting current context..."
    
    echo -e "\n## Current Development Context\n" >> "$output_file"
    
    # Check for active brainstorm files
    if ls "$PROJECT_ROOT"/Brainstorm_*.md 1> /dev/null 2>&1; then
        echo "### Active Brainstorm Sessions" >> "$output_file"
        for brainstorm in "$PROJECT_ROOT"/Brainstorm_*.md; do
            local name=$(basename "$brainstorm" .md)
            echo -e "\n#### $name\n" >> "$output_file"
            # Get first 20 lines as summary
            head -n 20 "$brainstorm" >> "$output_file"
            echo -e "\n[... see full file at $(basename "$brainstorm") ...]\n" >> "$output_file"
        done
    fi
    
    # Check for Kiro tasks
    if [[ -d "$KIRO_SPECS_DIR" ]]; then
        echo -e "\n### Active Kiro Tasks\n" >> "$output_file"
        for project_dir in "$KIRO_SPECS_DIR"/*; do
            if [[ -f "$project_dir/tasks.md" ]]; then
                local project_name=$(basename "$project_dir")
                echo -e "\n#### Project: $project_name\n" >> "$output_file"
                # Extract active tasks
                grep -A 5 "**Status**: In Progress\|**Status**: Not Started" "$project_dir/tasks.md" | head -n 20 >> "$output_file" || true
            fi
        done
    fi
    
    # Check for recent instructions
    if [[ -f "$PROJECT_ROOT/Instruction.md" ]]; then
        echo -e "\n### Project Instructions Summary\n" >> "$output_file"
        head -n 30 "$PROJECT_ROOT/Instruction.md" >> "$output_file"
    fi
}

# Function to generate Claude context
generate_claude_context() {
    local output_file="$CONTEXT_OUTPUT_DIR/CLAUDE.md"
    
    print_status "Generating Claude context..."
    
    cat > "$output_file" << EOF
# Claude Context for $PROJECT_NAME

*Generated on: $(date)*

## Project Overview
- **Name**: $PROJECT_NAME
- **Type**: $PROJECT_TYPE
- **Version**: $PROJECT_VERSION
- **Tech Stack**: $(get_tech_stack)
- **Current Stage**: $(get_current_stage)

## Communication Guidelines
- Use Vietnamese for discussions with a young, playful tone
- Code should be in English
- Focus on clarity and simplicity
- Be proactive but always explain reasoning

## Core Development Principles
EOF

    # Add core rules
    collect_rules "$output_file" "claude"
    
    # Add current context
    collect_current_context "$output_file"
    
    print_success "Claude context generated at $output_file"
}

# Function to generate Gemini context
generate_gemini_context() {
    local output_file="$CONTEXT_OUTPUT_DIR/GEMINI.md"
    
    print_status "Generating Gemini context..."
    
    cat > "$output_file" << EOF
# Gemini Context for $PROJECT_NAME

*Generated on: $(date)*

## System Instructions
You are an AI assistant working on $PROJECT_NAME with the following context and guidelines.

## Project Context
- **Project**: $PROJECT_NAME ($PROJECT_TYPE)
- **Version**: $PROJECT_VERSION
- **Technologies**: $(get_tech_stack)
- **Development Stage**: $(get_current_stage)

## Development Guidelines
You must follow these rules and workflows strictly:

EOF

    # Add rules
    collect_rules "$output_file" "gemini"
    
    # Add current context
    collect_current_context "$output_file"
    
    print_success "Gemini context generated at $output_file"
}

# Function to generate Trae context
generate_trae_context() {
    local output_file="$CONTEXT_OUTPUT_DIR/TRAE.md"
    
    print_status "Generating Trae context..."
    
    cat > "$output_file" << EOF
# Trae Context for $PROJECT_NAME

*Generated on: $(date)*

## MCP Integration Configuration
- Context7 Auto-Check: ENABLED
- Import Analysis: ENABLED
- Async Validation: ENABLED

## Project Configuration
- **Name**: $PROJECT_NAME
- **Type**: $PROJECT_TYPE
- **Stack**: $(get_tech_stack)
- **Stage**: $(get_current_stage)

## Important: Trae-Specific Handling
- Always use explicit imports (see Import Analyzer rule)
- Ensure all functions with await are marked async (see Async Validator rule)
- Use Context7 for architecture validation when available

## Project Rules and Workflows
EOF

    # Add rules with Trae-specific emphasis
    collect_rules "$output_file" "trae"
    
    # Add current context
    collect_current_context "$output_file"
    
    print_success "Trae context generated at $output_file"
}

# Function to generate generic context
generate_generic_context() {
    local output_file="$CONTEXT_OUTPUT_DIR/GENERIC.md"
    
    print_status "Generating generic AI context..."
    
    cat > "$output_file" << EOF
# AI Assistant Context for $PROJECT_NAME

*Generated on: $(date)*

## Essential Information
- **Project**: $PROJECT_NAME
- **Type**: $PROJECT_TYPE
- **Stack**: $(get_tech_stack)
- **Stage**: $(get_current_stage)

## Core Principles
1. Always analyze user intent before implementing
2. Follow established workflows in .cursor/rules/
3. Maintain code quality and consistency
4. Use Kiro task system for task management
5. Apply Context7 validation when available

## Current Focus
EOF

    # Add current context first for generic
    collect_current_context "$output_file"
    
    echo -e "\n## Development Rules\n" >> "$output_file"
    
    # Add essential rules only
    local essential_rules=(
        "base-rules.mdc"
        "development-rules.mdc"
        "planning-workflow.mdc"
    )
    
    for rule in "${essential_rules[@]}"; do
        if [[ -f "$RULES_DIR/$rule" ]]; then
            echo -e "\n### Essential Rule: $rule\n" >> "$output_file"
            sed '1,/^---$/d' "$RULES_DIR/$rule" | sed '1,/^---$/d' | head -n 50 >> "$output_file"
            echo -e "\n[... truncated for brevity ...]\n" >> "$output_file"
        fi
    done
    
    print_success "Generic context generated at $output_file"
}

# Function to create symlinks
create_symlinks() {
    print_status "Creating symlinks for easy access..."
    
    # Create symlink for generic context
    ln -sf "$CONTEXT_OUTPUT_DIR/GENERIC.md" "$PROJECT_ROOT/.ai-context"
    
    # Tool-specific symlinks
    ln -sf "$CONTEXT_OUTPUT_DIR/CLAUDE.md" "$PROJECT_ROOT/.claude-context"
    ln -sf "$CONTEXT_OUTPUT_DIR/GEMINI.md" "$PROJECT_ROOT/.gemini-context"
    ln -sf "$CONTEXT_OUTPUT_DIR/TRAE.md" "$PROJECT_ROOT/.trae-context"
    
    print_success "Symlinks created"
}

# Function to archive old contexts
archive_old_contexts() {
    local archive_dir="$CONTEXT_OUTPUT_DIR/archive/$(date +%Y-%m-%d_%H-%M-%S)"
    
    # Check if there are existing files to archive
    if ls "$CONTEXT_OUTPUT_DIR"/*.md 1> /dev/null 2>&1; then
        print_status "Archiving previous contexts..."
        mkdir -p "$archive_dir"
        cp "$CONTEXT_OUTPUT_DIR"/*.md "$archive_dir/" 2>/dev/null || true
    fi
}

# Function to show usage
show_usage() {
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Options:"
    echo "  -a, --all        Generate all context formats (default)"
    echo "  -c, --claude     Generate only Claude context"
    echo "  -g, --gemini     Generate only Gemini context"
    echo "  -t, --trae       Generate only Trae context"
    echo "  -u, --universal  Generate only universal/generic context"
    echo "  -h, --help       Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0              # Generate all contexts"
    echo "  $0 --claude     # Generate only Claude context"
    echo "  $0 -c -g        # Generate Claude and Gemini contexts"
}

# Main execution
main() {
    # Parse command line arguments
    local generate_all=true
    local generate_claude=false
    local generate_gemini=false
    local generate_trae=false
    local generate_generic=false
    
    while [[ $# -gt 0 ]]; do
        case $1 in
            -a|--all)
                generate_all=true
                shift
                ;;
            -c|--claude)
                generate_all=false
                generate_claude=true
                shift
                ;;
            -g|--gemini)
                generate_all=false
                generate_gemini=true
                shift
                ;;
            -t|--trae)
                generate_all=false
                generate_trae=true
                shift
                ;;
            -u|--universal)
                generate_all=false
                generate_generic=true
                shift
                ;;
            -h|--help)
                show_usage
                exit 0
                ;;
            *)
                print_error "Unknown option: $1"
                show_usage
                exit 1
                ;;
        esac
    done
    
    print_status "Starting AI Context Generation..."
    
    # Get project information
    get_project_info
    
    # Archive old contexts
    archive_old_contexts
    
    # Generate requested contexts
    if [[ "$generate_all" == true ]]; then
        generate_claude_context
        generate_gemini_context
        generate_trae_context
        generate_generic_context
    else
        [[ "$generate_claude" == true ]] && generate_claude_context
        [[ "$generate_gemini" == true ]] && generate_gemini_context
        [[ "$generate_trae" == true ]] && generate_trae_context
        [[ "$generate_generic" == true ]] && generate_generic_context
    fi
    
    # Create symlinks
    create_symlinks
    
    print_success "Context generation completed!"
    echo ""
    echo "Generated files:"
    ls -la "$CONTEXT_OUTPUT_DIR"/*.md 2>/dev/null | awk '{print "  - " $9}'
    echo ""
    echo "Symlinks created:"
    echo "  - .ai-context -> Generic context for any AI tool"
    echo "  - .claude-context -> Claude-specific context"
    echo "  - .gemini-context -> Gemini-specific context" 
    echo "  - .trae-context -> Trae-specific context"
}

# Run main function
main "$@"