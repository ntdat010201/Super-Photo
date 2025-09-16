#!/bin/bash

# Setup branch protection rules for TSDDR 2.0 branches
# Requires GitHub CLI (gh) to be installed and authenticated

set -e

if ! command -v gh &> /dev/null; then
    echo "❌ GitHub CLI (gh) is required but not installed"
    echo "Install it from: https://cli.github.com/"
    exit 1
fi

# Get repository info
REPO=$(gh repo view --json nameWithOwner -q .nameWithOwner)

echo "🔒 Setting up branch protection for TSDDR 2.0 branches in $REPO"

# Stages to protect
STAGES=("requirements" "design" "tasks" "execution" "test" "review")

for stage in "${STAGES[@]}"; do
    branch="tsddr/${stage}"
    echo "🔒 Protecting branch: $branch"
    
    gh api repos/$REPO/branches/$branch/protection \
        --method PUT \
        --field required_status_checks='{"strict":true,"contexts":["'$stage'-validation"]}' \
        --field enforce_admins=true \
        --field required_pull_request_reviews='{"required_approving_review_count":1,"dismiss_stale_reviews":true}' \
        --field restrictions=null \
        || echo "⚠️  Could not set protection for $branch (may not exist on remote)"
done

echo "✅ Branch protection setup complete"
