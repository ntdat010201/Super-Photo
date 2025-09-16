#!/bin/bash

# Project Identity Automated Update Script
# Implements the update protocol defined in .project-identity

set -e

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Configuration
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
PROJECT_IDENTITY="$PROJECT_ROOT/.project-identity"
LOCK_FILE="$PROJECT_ROOT/.project-identity.lock"
BACKUP_DIR="$PROJECT_ROOT/.project-identity.backup"
ARCHIVE_DIR="$PROJECT_ROOT/.project-identity.archive"

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

# Function to acquire lock
acquire_lock() {
    local max_wait=30
    local wait_time=0
    
    while [ -f "$LOCK_FILE" ] && [ $wait_time -lt $max_wait ]; do
        print_warning "Waiting for lock file to be released..."
        sleep 1
        ((wait_time++))
    done
    
    if [ -f "$LOCK_FILE" ]; then
        print_error "Could not acquire lock after ${max_wait} seconds"
        exit 1
    fi
    
    # Create lock file with timestamp and tool info
    echo "Locked at: $(date)" > "$LOCK_FILE"
    echo "AI Tool: $1" >> "$LOCK_FILE"
    echo "PID: $$" >> "$LOCK_FILE"
}

# Function to release lock
release_lock() {
    if [ -f "$LOCK_FILE" ]; then
        rm -f "$LOCK_FILE"
        print_status "Lock released"
    fi
}

# Cleanup on exit
trap release_lock EXIT

# Function to create backup
create_backup() {
    mkdir -p "$BACKUP_DIR"
    local timestamp=$(date +%Y%m%d_%H%M%S)
    cp "$PROJECT_IDENTITY" "$BACKUP_DIR/project-identity_${timestamp}.json"
    print_status "Backup created: project-identity_${timestamp}.json"
}

# Function to update timestamp in JSON
update_timestamp() {
    local temp_file=$(mktemp)
    local current_date=$(date -u +"%Y-%m-%dT%H:%M:%S.%3NZ")
    
    # Update lastUpdated field in JSON
    jq ".lastUpdated = \"$current_date\"" "$PROJECT_IDENTITY" > "$temp_file"
    mv "$temp_file" "$PROJECT_IDENTITY"
}

# Function to update work status
update_work_status() {
    local tool="$1"
    local task="$2"
    local status="$3"
    local timestamp=$(date -u +"%Y-%m-%dT%H:%M:%S.%3NZ")
    
    print_status "Updating work status for $tool"
    
    # Convert status to project-identity format
    local project_status
    case "$status" in
        "Working"|"In Progress") project_status="in_progress" ;;
        "Completed"|"Done") project_status="completed" ;;
        "Blocked"|"Waiting") project_status="blocked" ;;
        "Idle"|"Ready") project_status="ready" ;;
        *) project_status="in_progress" ;;
    esac
    
    # Update currentWorkingStatus using jq
    jq --arg tool "$tool" --arg task "$task" --arg status "$project_status" --arg timestamp "$timestamp" '
        .currentWorkingStatus = {
            "aiTool": $tool,
            "workIntent": $task,
            "status": $status,
            "lastUpdate": $timestamp
        }
    ' "$PROJECT_IDENTITY" > "$PROJECT_IDENTITY.tmp" && mv "$PROJECT_IDENTITY.tmp" "$PROJECT_IDENTITY"
}

# Function to add to recent changes
add_recent_change() {
    local change="$1"
    local timestamp=$(date -u +"%Y-%m-%dT%H:%M:%S.%3NZ")
    
    # Add to recentChanges array using jq
    jq --arg change "$change" --arg timestamp "$timestamp" '
        .recentChanges = ([
            {
                "description": $change,
                "timestamp": $timestamp
            }
        ] + (.recentChanges // [])) | .recentChanges[0:10]
    ' "$PROJECT_IDENTITY" > "$PROJECT_IDENTITY.tmp" && mv "$PROJECT_IDENTITY.tmp" "$PROJECT_IDENTITY"
}

# Function to update metrics
update_metrics() {
    local timestamp=$(date -u +"%Y-%m-%dT%H:%M:%S.%3NZ")
    
    # Update metrics using jq
    jq --arg timestamp "$timestamp" '
        .metrics = (.metrics // {}) |
        .metrics.conflictsPrevented = ((.metrics.conflictsPrevented // 0) + 1) |
        .metrics.lastMetricsUpdate = $timestamp
    ' "$PROJECT_IDENTITY" > "$PROJECT_IDENTITY.tmp" && mv "$PROJECT_IDENTITY.tmp" "$PROJECT_IDENTITY"
}

# Function to check for conflicts
check_conflicts() {
    print_status "Checking for potential conflicts..."
    
    # Check current working status
    local current_status=$(jq -r '.currentWorkingStatus.status // "ready"' "$PROJECT_IDENTITY")
    local current_tool=$(jq -r '.currentWorkingStatus.aiTool // "none"' "$PROJECT_IDENTITY")
    
    if [ "$current_status" = "in_progress" ] && [ "$current_tool" != "none" ]; then
        print_warning "Tool '$current_tool' is currently working. Potential conflict!"
        return 1
    fi
    
    return 0
}

# Function to archive old backups
archive_old_backups() {
    mkdir -p "$ARCHIVE_DIR"
    
    # Archive backups older than 24 hours
    find "$BACKUP_DIR" -name "project-identity_*.json" -mtime +1 -exec mv {} "$ARCHIVE_DIR/" \;
    
    # Keep only last 24 hourly backups
    local backup_count=$(ls -1 "$BACKUP_DIR"/project-identity_*.json 2>/dev/null | wc -l)
    if [ $backup_count -gt 24 ]; then
        ls -1t "$BACKUP_DIR"/project-identity_*.json | tail -n +25 | xargs rm -f
    fi
}

# Function to show usage
show_usage() {
    echo "Usage: $0 [command] [options]"
    echo ""
    echo "Commands:"
    echo "  status <tool> <task> <status>   Update work status for an AI tool"
    echo "  change <description>             Add a recent change"
    echo "  check                           Check for conflicts"
    echo "  backup                          Create a backup"
    echo "  metrics                         Update metrics"
    echo ""
    echo "Status values:"
    echo "  'Working', 'In Progress'        → in_progress"
    echo "  'Completed', 'Done'             → completed"
    echo "  'Blocked', 'Waiting'            → blocked"
    echo "  'Idle', 'Ready'                 → ready"
    echo ""
    echo "Options:"
    echo "  -h, --help                      Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 status Claude 'Implementing feature X' 'In Progress'"
    echo "  $0 change 'Updated planning-workflow.mdc with EARS format'"
    echo "  $0 check"
}

# Main execution
main() {
    if [ $# -eq 0 ] || [ "$1" == "-h" ] || [ "$1" == "--help" ]; then
        show_usage
        exit 0
    fi
    
    local command="$1"
    shift
    
    case "$command" in
        status)
            if [ $# -lt 3 ]; then
                print_error "Usage: $0 status <tool> <task> <status>"
                exit 1
            fi
            
            local tool="$1"
            local task="$2"
            local status="$3"
            
            acquire_lock "$tool"
            create_backup
            update_timestamp
            update_work_status "$tool" "$task" "$status"
            update_metrics
            archive_old_backups
            
            print_success "Project identity updated successfully"
            ;;
            
        change)
            if [ $# -lt 1 ]; then
                print_error "Usage: $0 change <description>"
                exit 1
            fi
            
            local change="$1"
            
            acquire_lock "Script"
            create_backup
            update_timestamp
            add_recent_change "$change"
            
            print_success "Recent change added successfully"
            ;;
            
        check)
            check_conflicts
            if [ $? -eq 0 ]; then
                print_success "No conflicts detected"
            else
                print_error "Conflicts detected! Another tool is currently working."
                exit 1
            fi
            ;;
            
        backup)
            create_backup
            archive_old_backups
            print_success "Backup created and old backups archived"
            ;;
            
        metrics)
            acquire_lock "Script"
            update_metrics
            print_success "Metrics updated"
            ;;
            
        *)
            print_error "Unknown command: $command"
            show_usage
            exit 1
            ;;
    esac
}

# Run main function
main "$@"