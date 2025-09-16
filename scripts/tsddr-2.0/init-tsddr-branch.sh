#!/bin/bash

# TSDDR 2.0 Branch Structure Initialization Script
# Creates the complete branch structure for Test Spec Driven Development & Revenue 2.0

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
MAIN_BRANCH=""
DEVELOP_BRANCH="develop"
FEATURE_PREFIX="feature"
RELEASE_PREFIX="release"
HOTFIX_PREFIX="hotfix"

# TSDDR 2.0 Stage branches
STAGES=("requirements" "design" "tasks" "execution" "test" "review")

echo -e "${BLUE}🚀 Initializing TSDDR 2.0 Branch Structure${NC}"
echo "================================================"

# Check if we're in a git repository
if ! git rev-parse --git-dir > /dev/null 2>&1; then
    echo -e "${RED}❌ Error: Not in a git repository${NC}"
    exit 1
fi

# Function to create branch if it doesn't exist
create_branch_if_not_exists() {
    local branch_name=$1
    local base_branch=$2
    
    if git show-ref --verify --quiet refs/heads/$branch_name; then
        echo -e "${YELLOW}⚠️  Branch '$branch_name' already exists${NC}"
    else
        git checkout $base_branch
        git pull origin $base_branch 2>/dev/null || echo "Could not pull from origin"
        git checkout -b $branch_name
        git push -u origin $branch_name 2>/dev/null || echo "Could not push to origin"
        echo -e "${GREEN}✅ Created branch '$branch_name'${NC}"
    fi
}

# Function to setup branch protection and workflows
setup_branch_workflows() {
    local branch_name=$1
    local stage=$2
    
    # Create stage-specific workflow file
    mkdir -p .github/workflows
    local stage_title=$(echo "${stage}" | sed 's/^./\U&/')
    cat > .github/workflows/tsddr-${stage}.yml << EOF
name: TSDDR 2.0 - ${stage_title} Stage

on:
  push:
    branches: [ tsddr/${stage} ]
  pull_request:
    branches: [ tsddr/${stage} ]

jobs:
  ${stage}-validation:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    
    - name: Setup Node.js
      uses: actions/setup-node@v3
      with:
        node-version: '18'
        
    - name: Install dependencies
      run: npm install
      
    - name: Run ${stage} stage validation
      run: |
        chmod +x scripts/tsddr-2.0/stage-transition.sh
        ./scripts/tsddr-2.0/stage-transition.sh validate ${stage}
        
    - name: Run tests
      run: |
        chmod +x scripts/tsddr-2.0/test-runner.sh
        ./scripts/tsddr-2.0/test-runner.sh ${stage}
EOF
    
    echo -e "${GREEN}✅ Created workflow for ${stage} stage${NC}"
}

echo -e "${BLUE}📋 Step 1: Creating main development branches${NC}"

# Check if main or master branch exists
if git show-ref --verify --quiet refs/heads/main; then
    MAIN_BRANCH="main"
elif git show-ref --verify --quiet refs/heads/master; then
    MAIN_BRANCH="master"
else
    echo -e "${RED}❌ Neither 'main' nor 'master' branch exists${NC}"
    exit 1
fi

echo -e "${BLUE}📍 Using '$MAIN_BRANCH' as the main branch${NC}"

# Create develop branch
create_branch_if_not_exists $DEVELOP_BRANCH $MAIN_BRANCH

echo -e "\n${BLUE}📋 Step 2: Creating TSDDR 2.0 stage branches${NC}"

# Create TSDDR 2.0 stage branches
for stage in "${STAGES[@]}"; do
    branch_name="tsddr/${stage}"
    create_branch_if_not_exists $branch_name $DEVELOP_BRANCH
    setup_branch_workflows $branch_name $stage
done

echo -e "\n${BLUE}📋 Step 3: Creating TSDDR 2.0 configuration${NC}"

# Create TSDDR 2.0 configuration file
cat > .tsddr-config.json << EOF
{
  "version": "2.0",
  "stages": {
    "requirements": {
      "branch": "tsddr/requirements",
      "quality_gates": [
        "requirements_documented",
        "acceptance_criteria_defined",
        "ai_revenue_requirements_specified"
      ],
      "deliverables": [
        "requirements.md",
        "acceptance-criteria.md",
        "ai-revenue-specs.md"
      ]
    },
    "design": {
      "branch": "tsddr/design",
      "quality_gates": [
        "architecture_designed",
        "ai_integration_planned",
        "revenue_optimization_designed"
      ],
      "deliverables": [
        "architecture.md",
        "ai-integration-design.md",
        "revenue-optimization-plan.md"
      ]
    },
    "tasks": {
      "branch": "tsddr/tasks",
      "quality_gates": [
        "tasks_broken_down",
        "test_specs_created",
        "ai_revenue_tasks_prioritized"
      ],
      "deliverables": [
        "task-breakdown.md",
        "test-specifications.md",
        "priority-matrix.md"
      ]
    },
    "execution": {
      "branch": "tsddr/execution",
      "quality_gates": [
        "code_implemented",
        "ai_features_integrated",
        "revenue_optimization_implemented"
      ],
      "deliverables": [
        "source_code",
        "ai_integration_code",
        "revenue_optimization_code"
      ]
    },
    "test": {
      "branch": "tsddr/test",
      "quality_gates": [
        "unit_tests_pass",
        "integration_tests_pass",
        "ai_revenue_tests_pass",
        "regional_tests_pass"
      ],
      "deliverables": [
        "test_results",
        "coverage_reports",
        "ai_revenue_test_reports"
      ]
    },
    "review": {
      "branch": "tsddr/review",
      "quality_gates": [
        "code_review_approved",
        "ai_revenue_review_approved",
        "performance_review_passed"
      ],
      "deliverables": [
        "review_reports",
        "performance_metrics",
        "deployment_readiness"
      ]
    }
  },
  "ai_revenue_config": {
    "ai_testing": {
      "frameworks": ["jest", "xctest", "espresso"],
      "coverage_threshold": 85
    },
    "revenue_testing": {
      "frameworks": ["revenue-test-suite"],
      "metrics": ["conversion_rate", "arpu", "retention"]
    },
    "regional_testing": {
      "regions": ["vietnam", "global"],
      "localization_coverage": 90
    }
  }
}
EOF

echo -e "${GREEN}✅ Created TSDDR 2.0 configuration${NC}"

echo -e "\n${BLUE}📋 Step 4: Setting up Git hooks${NC}"

# Create pre-commit hook for quality gates
mkdir -p .git/hooks
cat > .git/hooks/pre-commit << 'EOF'
#!/bin/bash

# TSDDR 2.0 Pre-commit Quality Gate
echo "🔍 Running TSDDR 2.0 pre-commit quality gates..."

# Get current branch
current_branch=$(git branch --show-current)

# Check if it's a TSDDR stage branch
if [[ $current_branch == tsddr/* ]]; then
    stage=${current_branch#tsddr/}
    echo "📋 Validating $stage stage quality gates..."
    
    # Run stage-specific validation
    if [ -f "scripts/tsddr-2.0/quality-gate-checker.sh" ]; then
        ./scripts/tsddr-2.0/quality-gate-checker.sh $stage
        if [ $? -ne 0 ]; then
            echo "❌ Quality gate validation failed for $stage stage"
            exit 1
        fi
    fi
fi

echo "✅ Pre-commit quality gates passed"
EOF

chmod +x .git/hooks/pre-commit
echo -e "${GREEN}✅ Created pre-commit hook${NC}"

echo -e "\n${BLUE}📋 Step 5: Creating branch protection setup script${NC}"

# Create GitHub branch protection setup script
cat > scripts/tsddr-2.0/setup-branch-protection.sh << 'EOF'
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
EOF

chmod +x scripts/tsddr-2.0/setup-branch-protection.sh
echo -e "${GREEN}✅ Created branch protection setup script${NC}"

# Return to develop branch
git checkout $DEVELOP_BRANCH

echo -e "\n${GREEN}🎉 TSDDR 2.0 Branch Structure Initialization Complete!${NC}"
echo "================================================"
echo -e "${BLUE}📋 Created branches:${NC}"
for stage in "${STAGES[@]}"; do
    echo "  • tsddr/${stage}"
done
echo -e "\n${BLUE}📋 Next steps:${NC}"
echo "  1. Run: ./scripts/tsddr-2.0/setup-branch-protection.sh (if using GitHub)"
echo "  2. Start development: ./scripts/tsddr-2.0/stage-transition.sh requirements"
echo "  3. Follow TSDDR 2.0 workflow in .cursor/rules/tdd-mobile-workflow.mdc"
echo -e "\n${YELLOW}💡 Configuration saved in .tsddr-config.json${NC}"