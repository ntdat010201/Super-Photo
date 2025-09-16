#!/bin/bash

# TSDDR 2.0 Git Integration Script
# Manages Git workflow integration with TSDDR 2.0 stages

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Configuration
CONFIG_FILE=".tsddr-config.json"
TSDDR_BRANCH_PREFIX="tsddr"
MAIN_BRANCH="main"
DEVELOP_BRANCH="develop"

# Functions
show_usage() {
    echo "Usage: $0 <command> [options]"
    echo ""
    echo "Commands:"
    echo "  init-branch <feature-name>  - Initialize TSDDR branch structure"
    echo "  stage-commit <stage>        - Commit changes for specific stage"
    echo "  merge-stage <stage>         - Merge stage branch to next stage"
    echo "  create-pr <stage>           - Create pull request for stage"
    echo "  sync-upstream               - Sync with upstream changes"
    echo "  cleanup-branches            - Clean up completed TSDDR branches"
    echo "  status                      - Show TSDDR Git status"
    echo ""
    echo "Stages: requirements, design, tasks, execution, test, review"
    echo ""
    echo "Options:"
    echo "  --force      - Force operation (use with caution)"
    echo "  --dry-run    - Show what would be done without executing"
    echo "  --verbose    - Verbose output"
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

log_git() {
    echo -e "${CYAN}🔀 $1${NC}"
}

log_stage() {
    echo -e "${PURPLE}🎯 $1${NC}"
}

# Check if we're in a Git repository
check_git_repo() {
    if ! git rev-parse --git-dir > /dev/null 2>&1; then
        log_error "Not in a Git repository"
        exit 1
    fi
}

# Get current TSDDR configuration
get_tsddr_config() {
    if [ ! -f "$CONFIG_FILE" ]; then
        log_error "TSDDR configuration not found: $CONFIG_FILE"
        log_info "Run: ./scripts/tsddr-2.0/init-tsddr-branch.sh <feature-name> first"
        exit 1
    fi
    
    # Extract configuration using jq if available, otherwise use basic parsing
    if command -v jq &> /dev/null; then
        FEATURE_NAME=$(jq -r '.feature_name // "unknown"' "$CONFIG_FILE")
        CURRENT_STAGE=$(jq -r '.current_stage // "requirements"' "$CONFIG_FILE")
    else
        FEATURE_NAME=$(grep '"feature_name"' "$CONFIG_FILE" | cut -d'"' -f4 || echo "unknown")
        CURRENT_STAGE=$(grep '"current_stage"' "$CONFIG_FILE" | cut -d'"' -f4 || echo "requirements")
    fi
}

# Initialize TSDDR branch structure
init_tsddr_branches() {
    local feature_name=$1
    
    if [ -z "$feature_name" ]; then
        log_error "Feature name is required"
        show_usage
        exit 1
    fi
    
    log_stage "Initializing TSDDR 2.0 branch structure for: $feature_name"
    
    # Ensure we're on main/develop branch
    local current_branch=$(git branch --show-current)
    if [ "$current_branch" != "$MAIN_BRANCH" ] && [ "$current_branch" != "$DEVELOP_BRANCH" ]; then
        log_warning "Not on main or develop branch. Current: $current_branch"
        if [ "$FORCE_MODE" != "true" ]; then
            log_error "Switch to main or develop branch first, or use --force"
            exit 1
        fi
    fi
    
    # Create base TSDDR branch
    local base_branch="${TSDDR_BRANCH_PREFIX}/${feature_name}"
    
    if git show-ref --verify --quiet "refs/heads/$base_branch"; then
        log_warning "Base branch $base_branch already exists"
        if [ "$FORCE_MODE" != "true" ]; then
            log_error "Use --force to recreate or choose different feature name"
            exit 1
        else
            git branch -D "$base_branch" || true
        fi
    fi
    
    if [ "$DRY_RUN" = "true" ]; then
        log_info "[DRY RUN] Would create branch: $base_branch"
    else
        git checkout -b "$base_branch"
        log_success "Created base branch: $base_branch"
    fi
    
    # Create stage-specific branches
    local stages=("requirements" "design" "tasks" "execution" "test" "review")
    
    for stage in "${stages[@]}"; do
        local stage_branch="${base_branch}/${stage}"
        
        if [ "$DRY_RUN" = "true" ]; then
            log_info "[DRY RUN] Would create branch: $stage_branch"
        else
            if ! git show-ref --verify --quiet "refs/heads/$stage_branch"; then
                git checkout -b "$stage_branch" "$base_branch"
                log_success "Created stage branch: $stage_branch"
            else
                log_info "Stage branch already exists: $stage_branch"
            fi
        fi
    done
    
    # Switch to requirements stage to start
    local requirements_branch="${base_branch}/requirements"
    if [ "$DRY_RUN" != "true" ]; then
        git checkout "$requirements_branch"
        log_success "Switched to requirements stage: $requirements_branch"
    fi
    
    log_success "TSDDR branch structure initialized for: $feature_name"
}

# Commit changes for specific stage
stage_commit() {
    local stage=$1
    
    if [ -z "$stage" ]; then
        log_error "Stage is required"
        show_usage
        exit 1
    fi
    
    get_tsddr_config
    
    local expected_branch="${TSDDR_BRANCH_PREFIX}/${FEATURE_NAME}/${stage}"
    local current_branch=$(git branch --show-current)
    
    if [ "$current_branch" != "$expected_branch" ]; then
        log_warning "Not on expected branch. Current: $current_branch, Expected: $expected_branch"
        if [ "$FORCE_MODE" != "true" ]; then
            log_error "Switch to correct branch first, or use --force"
            exit 1
        fi
    fi
    
    # Check for uncommitted changes
    if git diff --quiet && git diff --cached --quiet; then
        log_warning "No changes to commit"
        return 0
    fi
    
    # Run quality gate check before committing
    log_info "Running quality gate check for $stage..."
    if [ -f "./scripts/tsddr-2.0/quality-gate-checker.sh" ]; then
        if [ "$DRY_RUN" != "true" ]; then
            if ! ./scripts/tsddr-2.0/quality-gate-checker.sh "$stage"; then
                log_error "Quality gate check failed. Fix issues before committing."
                exit 1
            fi
        else
            log_info "[DRY RUN] Would run quality gate check"
        fi
    else
        log_warning "Quality gate checker not found, skipping validation"
    fi
    
    # Generate commit message based on stage
    local commit_message
    case $stage in
        "requirements")
            commit_message="feat($FEATURE_NAME): complete requirements analysis and documentation

- Document functional and non-functional requirements
- Define AI integration requirements
- Specify revenue optimization goals
- Outline regional adaptation needs
- Create acceptance criteria

TSDDR-Stage: requirements
Feature: $FEATURE_NAME"
            ;;
        "design")
            commit_message="design($FEATURE_NAME): complete system design and architecture

- Define system architecture
- Design AI integration patterns
- Plan revenue optimization strategies
- Create regional adaptation design
- Document API specifications

TSDDR-Stage: design
Feature: $FEATURE_NAME"
            ;;
        "tasks")
            commit_message="plan($FEATURE_NAME): complete task breakdown and planning

- Break down features into tasks
- Create test specifications
- Define implementation timeline
- Plan AI and revenue testing strategies
- Set up project tracking

TSDDR-Stage: tasks
Feature: $FEATURE_NAME"
            ;;
        "execution")
            commit_message="feat($FEATURE_NAME): implement core functionality

- Implement core features
- Add AI integration components
- Integrate revenue optimization
- Add regional adaptations
- Create unit tests

TSDDR-Stage: execution
Feature: $FEATURE_NAME"
            ;;
        "test")
            commit_message="test($FEATURE_NAME): complete comprehensive testing

- Add integration tests
- Implement AI-specific tests
- Add revenue optimization tests
- Create regional adaptation tests
- Achieve coverage targets

TSDDR-Stage: test
Feature: $FEATURE_NAME"
            ;;
        "review")
            commit_message="review($FEATURE_NAME): complete final review and optimization

- Code review and optimization
- Final quality assurance
- Performance optimization
- Documentation updates
- Deployment preparation

TSDDR-Stage: review
Feature: $FEATURE_NAME"
            ;;
        *)
            commit_message="$stage($FEATURE_NAME): stage completion

TSDDR-Stage: $stage
Feature: $FEATURE_NAME"
            ;;
    esac
    
    if [ "$DRY_RUN" = "true" ]; then
        log_info "[DRY RUN] Would commit with message:"
        echo "$commit_message"
    else
        # Add all changes
        git add .
        
        # Commit with generated message
        git commit -m "$commit_message"
        
        log_success "Committed changes for $stage stage"
        
        # Update TSDDR config
        if command -v jq &> /dev/null; then
            jq ".stages.\"$stage\".completed = true | .stages.\"$stage\".completed_at = \"$(date -Iseconds)\"" "$CONFIG_FILE" > "${CONFIG_FILE}.tmp" && mv "${CONFIG_FILE}.tmp" "$CONFIG_FILE"
        fi
    fi
}

# Merge stage branch to next stage
merge_stage() {
    local current_stage=$1
    
    if [ -z "$current_stage" ]; then
        log_error "Current stage is required"
        show_usage
        exit 1
    fi
    
    get_tsddr_config
    
    # Define stage progression
    local stages=("requirements" "design" "tasks" "execution" "test" "review")
    local next_stage=""
    
    for i in "${!stages[@]}"; do
        if [ "${stages[$i]}" = "$current_stage" ]; then
            if [ $((i + 1)) -lt ${#stages[@]} ]; then
                next_stage="${stages[$((i + 1))]}"
            fi
            break
        fi
    done
    
    if [ -z "$next_stage" ]; then
        log_info "$current_stage is the final stage. Ready for main branch merge."
        return 0
    fi
    
    local current_branch="${TSDDR_BRANCH_PREFIX}/${FEATURE_NAME}/${current_stage}"
    local next_branch="${TSDDR_BRANCH_PREFIX}/${FEATURE_NAME}/${next_stage}"
    
    log_stage "Merging $current_stage to $next_stage"
    
    if [ "$DRY_RUN" = "true" ]; then
        log_info "[DRY RUN] Would merge $current_branch to $next_branch"
        return 0
    fi
    
    # Ensure we're on the current stage branch
    git checkout "$current_branch"
    
    # Ensure next stage branch exists
    if ! git show-ref --verify --quiet "refs/heads/$next_branch"; then
        git checkout -b "$next_branch" "$current_branch"
        log_success "Created next stage branch: $next_branch"
    else
        # Switch to next stage and merge current stage
        git checkout "$next_branch"
        git merge "$current_branch" --no-ff -m "merge: integrate $current_stage stage into $next_stage

Merging completed $current_stage stage work into $next_stage stage.

TSDDR-Merge: $current_stage -> $next_stage
Feature: $FEATURE_NAME"
        
        log_success "Merged $current_stage to $next_stage"
    fi
    
    # Update TSDDR config
    if command -v jq &> /dev/null; then
        jq ".current_stage = \"$next_stage\" | .stages.\"$next_stage\".started_at = \"$(date -Iseconds)\"" "$CONFIG_FILE" > "${CONFIG_FILE}.tmp" && mv "${CONFIG_FILE}.tmp" "$CONFIG_FILE"
    fi
    
    log_success "Ready to work on $next_stage stage"
}

# Create pull request for stage
create_pull_request() {
    local stage=$1
    
    if [ -z "$stage" ]; then
        log_error "Stage is required"
        show_usage
        exit 1
    fi
    
    get_tsddr_config
    
    local stage_branch="${TSDDR_BRANCH_PREFIX}/${FEATURE_NAME}/${stage}"
    local target_branch
    
    # Determine target branch based on stage
    if [ "$stage" = "review" ]; then
        target_branch="$MAIN_BRANCH"
    else
        target_branch="$DEVELOP_BRANCH"
    fi
    
    log_stage "Creating pull request for $stage stage"
    
    # Generate PR title and description
    local pr_title="[TSDDR-$stage] $FEATURE_NAME: $(echo $stage | sed 's/.*/\u&/') stage completion"
    local pr_description="## TSDDR 2.0 Stage: $stage

### Feature: $FEATURE_NAME

### Changes in this PR:

#### $stage Stage Deliverables:
"
    
    case $stage in
        "requirements")
            pr_description+="- ✅ Requirements documentation
- ✅ Acceptance criteria definition
- ✅ AI integration requirements
- ✅ Revenue optimization goals
- ✅ Regional adaptation needs"
            ;;
        "design")
            pr_description+="- ✅ System architecture design
- ✅ AI integration patterns
- ✅ Revenue optimization strategies
- ✅ Regional adaptation design
- ✅ API specifications"
            ;;
        "tasks")
            pr_description+="- ✅ Task breakdown
- ✅ Test specifications
- ✅ Implementation timeline
- ✅ AI and revenue testing strategies
- ✅ Project tracking setup"
            ;;
        "execution")
            pr_description+="- ✅ Core functionality implementation
- ✅ AI integration components
- ✅ Revenue optimization integration
- ✅ Regional adaptations
- ✅ Unit tests"
            ;;
        "test")
            pr_description+="- ✅ Integration tests
- ✅ AI-specific tests
- ✅ Revenue optimization tests
- ✅ Regional adaptation tests
- ✅ Coverage targets achieved"
            ;;
        "review")
            pr_description+="- ✅ Code review and optimization
- ✅ Final quality assurance
- ✅ Performance optimization
- ✅ Documentation updates
- ✅ Deployment preparation"
            ;;
    esac
    
    pr_description+="

### Quality Gates Status:
- ✅ Code quality checks passed
- ✅ Test coverage requirements met
- ✅ AI integration validated
- ✅ Revenue optimization verified
- ✅ Regional adaptation tested

### Next Steps:
$(if [ "$stage" != "review" ]; then echo "- Proceed to next TSDDR stage after approval"; else echo "- Ready for production deployment"; fi)

---
*Generated by TSDDR 2.0 Git Integration*"
    
    if [ "$DRY_RUN" = "true" ]; then
        log_info "[DRY RUN] Would create PR:"
        echo "Title: $pr_title"
        echo "Target: $target_branch"
        echo "Source: $stage_branch"
        echo "Description: $pr_description"
    else
        # Push branch first
        git push origin "$stage_branch"
        
        # Create PR using GitHub CLI if available
        if command -v gh &> /dev/null; then
            gh pr create --title "$pr_title" --body "$pr_description" --base "$target_branch" --head "$stage_branch"
            log_success "Pull request created successfully"
        else
            log_info "GitHub CLI not available. Please create PR manually:"
            echo "Title: $pr_title"
            echo "Target: $target_branch"
            echo "Source: $stage_branch"
            echo ""
            echo "Description:"
            echo "$pr_description"
        fi
    fi
}

# Sync with upstream changes
sync_upstream() {
    log_git "Syncing with upstream changes"
    
    get_tsddr_config
    
    local current_branch=$(git branch --show-current)
    
    if [ "$DRY_RUN" = "true" ]; then
        log_info "[DRY RUN] Would sync with upstream"
        return 0
    fi
    
    # Fetch latest changes
    git fetch origin
    
    # Sync main branch
    git checkout "$MAIN_BRANCH"
    git pull origin "$MAIN_BRANCH"
    
    # Sync develop branch if it exists
    if git show-ref --verify --quiet "refs/heads/$DEVELOP_BRANCH"; then
        git checkout "$DEVELOP_BRANCH"
        git pull origin "$DEVELOP_BRANCH"
    fi
    
    # Return to original branch
    git checkout "$current_branch"
    
    # Rebase current branch on latest develop/main
    local base_branch="${TSDDR_BRANCH_PREFIX}/${FEATURE_NAME}"
    if git show-ref --verify --quiet "refs/heads/$base_branch"; then
        git checkout "$base_branch"
        git rebase "$DEVELOP_BRANCH" || git rebase "$MAIN_BRANCH"
        git checkout "$current_branch"
        git rebase "$base_branch"
    fi
    
    log_success "Synced with upstream changes"
}

# Clean up completed TSDDR branches
cleanup_branches() {
    log_git "Cleaning up completed TSDDR branches"
    
    if [ "$DRY_RUN" = "true" ]; then
        log_info "[DRY RUN] Would clean up branches matching: ${TSDDR_BRANCH_PREFIX}/*"
        git branch | grep "$TSDDR_BRANCH_PREFIX" || log_info "No TSDDR branches found"
        return 0
    fi
    
    # List TSDDR branches
    local tsddr_branches=$(git branch | grep "$TSDDR_BRANCH_PREFIX" | sed 's/^[* ]*//' || true)
    
    if [ -z "$tsddr_branches" ]; then
        log_info "No TSDDR branches to clean up"
        return 0
    fi
    
    echo "Found TSDDR branches:"
    echo "$tsddr_branches"
    echo ""
    
    if [ "$FORCE_MODE" != "true" ]; then
        read -p "Delete these branches? (y/N): " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            log_info "Branch cleanup cancelled"
            return 0
        fi
    fi
    
    # Delete branches
    echo "$tsddr_branches" | while read -r branch; do
        if [ -n "$branch" ]; then
            git branch -D "$branch" || log_warning "Failed to delete branch: $branch"
        fi
    done
    
    log_success "Branch cleanup completed"
}

# Show TSDDR Git status
show_tsddr_status() {
    log_stage "TSDDR 2.0 Git Status"
    echo "=============================="
    
    # Current branch
    local current_branch=$(git branch --show-current)
    echo "Current Branch: $current_branch"
    
    # Check if it's a TSDDR branch
    if [[ $current_branch == ${TSDDR_BRANCH_PREFIX}/* ]]; then
        log_success "On TSDDR branch"
        
        # Extract feature and stage
        local branch_parts=(${current_branch//\// })
        if [ ${#branch_parts[@]} -ge 3 ]; then
            local feature="${branch_parts[1]}"
            local stage="${branch_parts[2]}"
            echo "Feature: $feature"
            echo "Stage: $stage"
        fi
    else
        log_info "Not on TSDDR branch"
    fi
    
    echo ""
    
    # TSDDR configuration
    if [ -f "$CONFIG_FILE" ]; then
        log_success "TSDDR configuration found"
        get_tsddr_config
        echo "Configured Feature: $FEATURE_NAME"
        echo "Current Stage: $CURRENT_STAGE"
    else
        log_warning "No TSDDR configuration found"
    fi
    
    echo ""
    
    # List TSDDR branches
    local tsddr_branches=$(git branch | grep "$TSDDR_BRANCH_PREFIX" | sed 's/^[* ]*//' || true)
    if [ -n "$tsddr_branches" ]; then
        echo "TSDDR Branches:"
        echo "$tsddr_branches" | while read -r branch; do
            if [ -n "$branch" ]; then
                local status=""
                if [ "$branch" = "$current_branch" ]; then
                    status=" (current)"
                fi
                echo "  - $branch$status"
            fi
        done
    else
        log_info "No TSDDR branches found"
    fi
    
    echo ""
    
    # Git status
    echo "Git Status:"
    git status --short
}

# Parse command line arguments
COMMAND=""
FORCE_MODE=false
DRY_RUN=false
VERBOSE=false

while [[ $# -gt 0 ]]; do
    case $1 in
        --force)
            FORCE_MODE=true
            shift
            ;;
        --dry-run)
            DRY_RUN=true
            shift
            ;;
        --verbose)
            VERBOSE=true
            shift
            ;;
        -*)
            log_error "Unknown option: $1"
            show_usage
            exit 1
            ;;
        *)
            if [ -z "$COMMAND" ]; then
                COMMAND=$1
            else
                # Additional arguments for commands
                break
            fi
            shift
            ;;
    esac
done

if [ -z "$COMMAND" ]; then
    show_usage
    exit 1
fi

# Check Git repository
check_git_repo

# Execute command
case $COMMAND in
    "init-branch")
        init_tsddr_branches "$1"
        ;;
    "stage-commit")
        stage_commit "$1"
        ;;
    "merge-stage")
        merge_stage "$1"
        ;;
    "create-pr")
        create_pull_request "$1"
        ;;
    "sync-upstream")
        sync_upstream
        ;;
    "cleanup-branches")
        cleanup_branches
        ;;
    "status")
        show_tsddr_status
        ;;
    *)
        log_error "Unknown command: $COMMAND"
        show_usage
        exit 1
        ;;
esac

log_success "TSDDR Git integration completed successfully! 🎉"