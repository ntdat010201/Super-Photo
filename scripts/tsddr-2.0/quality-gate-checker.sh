#!/bin/bash

# TSDDR 2.0 Quality Gate Checker
# Validates quality gates before stage transitions

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
QUALITY_REPORT_DIR="quality-reports"
MIN_COVERAGE_THRESHOLD=85
MIN_AI_COVERAGE_THRESHOLD=80
MIN_REVENUE_COVERAGE_THRESHOLD=90

# Functions
show_usage() {
    echo "Usage: $0 <stage> [options]"
    echo ""
    echo "Stages: requirements, design, tasks, execution, test, review"
    echo ""
    echo "Options:"
    echo "  --strict     - Strict mode (fail on warnings)"
    echo "  --report     - Generate detailed quality report"
    echo "  --fix        - Attempt to auto-fix issues"
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

log_quality() {
    echo -e "${CYAN}🔍 $1${NC}"
}

log_stage() {
    echo -e "${PURPLE}🎯 $1${NC}"
}

# Setup quality check environment
setup_quality_env() {
    mkdir -p "$QUALITY_REPORT_DIR"
    
    # Load configuration
    if [ ! -f "$CONFIG_FILE" ]; then
        log_error "TSDDR configuration not found: $CONFIG_FILE"
        exit 1
    fi
}

# Check if file exists and is not empty
check_file_exists() {
    local file=$1
    local description=$2
    
    if [ -f "$file" ] && [ -s "$file" ]; then
        log_success "$description found: $file"
        return 0
    else
        log_error "$description missing or empty: $file"
        return 1
    fi
}

# Check code quality metrics
check_code_quality() {
    local issues=0
    
    log_quality "Checking code quality metrics..."
    
    # ESLint check for JavaScript/TypeScript projects
    if [ -f "package.json" ] && command -v eslint &> /dev/null; then
        log_info "Running ESLint..."
        if eslint . --ext .js,.jsx,.ts,.tsx --format json > "$QUALITY_REPORT_DIR/eslint-report.json" 2>/dev/null; then
            local eslint_errors=$(jq '[.[] | select(.errorCount > 0)] | length' "$QUALITY_REPORT_DIR/eslint-report.json" 2>/dev/null || echo "0")
            if [ "$eslint_errors" -eq 0 ]; then
                log_success "ESLint: No errors found"
            else
                log_error "ESLint: $eslint_errors files with errors"
                ((issues++))
            fi
        else
            log_warning "ESLint check failed or not configured"
        fi
    fi
    
    # SwiftLint check for iOS projects
    if [ -f "*.xcodeproj" ] || [ -f "*.xcworkspace" ]; then
        if command -v swiftlint &> /dev/null; then
            log_info "Running SwiftLint..."
            if swiftlint lint --reporter json > "$QUALITY_REPORT_DIR/swiftlint-report.json" 2>/dev/null; then
                local swift_violations=$(jq 'length' "$QUALITY_REPORT_DIR/swiftlint-report.json" 2>/dev/null || echo "0")
                if [ "$swift_violations" -eq 0 ]; then
                    log_success "SwiftLint: No violations found"
                else
                    log_warning "SwiftLint: $swift_violations violations found"
                    if [ "$STRICT_MODE" = "true" ]; then
                        ((issues++))
                    fi
                fi
            else
                log_warning "SwiftLint check failed or not configured"
            fi
        fi
    fi
    
    # Android Lint check
    if [ -f "build.gradle" ] || [ -f "app/build.gradle" ]; then
        log_info "Running Android Lint..."
        if ./gradlew lint 2>/dev/null; then
            log_success "Android Lint: Check completed"
        else
            log_warning "Android Lint check failed"
        fi
    fi
    
    return $issues
}

# Check test coverage
check_test_coverage() {
    local issues=0
    
    log_quality "Checking test coverage..."
    
    # Jest coverage check
    if [ -f "package.json" ] && grep -q "jest" package.json; then
        if [ -f "coverage/lcov-report/index.html" ]; then
            # Extract coverage percentage from HTML report
            local coverage=$(grep -o '[0-9]\+\.[0-9]\+%' coverage/lcov-report/index.html | head -1 | sed 's/%//')
            if [ -n "$coverage" ]; then
                local coverage_int=${coverage%.*}
                if [ "$coverage_int" -ge "$MIN_COVERAGE_THRESHOLD" ]; then
                    log_success "Test coverage: ${coverage}% (threshold: ${MIN_COVERAGE_THRESHOLD}%)"
                else
                    log_error "Test coverage: ${coverage}% below threshold (${MIN_COVERAGE_THRESHOLD}%)"
                    ((issues++))
                fi
            else
                log_warning "Could not extract coverage percentage"
            fi
        else
            log_warning "Coverage report not found. Run tests with --coverage flag"
            if [ "$STRICT_MODE" = "true" ]; then
                ((issues++))
            fi
        fi
    fi
    
    # Xcode coverage check
    if [ -f "*.xcodeproj" ] || [ -f "*.xcworkspace" ]; then
        log_info "Checking Xcode test coverage..."
        # This would require parsing Xcode's coverage reports
        log_warning "Xcode coverage check not implemented yet"
    fi
    
    # Android coverage check (JaCoCo)
    if [ -f "build.gradle" ] && grep -q "jacoco" build.gradle; then
        if [ -f "app/build/reports/jacoco/testDebugUnitTestCoverage/html/index.html" ]; then
            log_success "Android coverage report found"
        else
            log_warning "Android coverage report not found"
        fi
    fi
    
    return $issues
}

# Check AI-specific quality gates
check_ai_quality_gates() {
    local issues=0
    
    log_quality "Checking AI-specific quality gates..."
    
    # Check for AI test files
    local ai_test_files=$(find . -name "*.ai.test.*" -o -name "*.ai.spec.*" -o -name "*AI*Test*" 2>/dev/null | wc -l)
    if [ "$ai_test_files" -gt 0 ]; then
        log_success "AI test files found: $ai_test_files"
    else
        log_warning "No AI-specific test files found"
        if [ "$STRICT_MODE" = "true" ]; then
            ((issues++))
        fi
    fi
    
    # Check for AI model validation
    if [ -f "src/ai/models" ] || [ -f "lib/ai" ] || [ -f "app/ai" ]; then
        log_success "AI model directory structure found"
    else
        log_info "No AI model directory found (may not be required)"
    fi
    
    # Check for AI configuration files
    local ai_config_files=$(find . -name "ai-config.*" -o -name "model-config.*" -o -name "*.aiconfig" 2>/dev/null | wc -l)
    if [ "$ai_config_files" -gt 0 ]; then
        log_success "AI configuration files found: $ai_config_files"
    else
        log_info "No AI configuration files found"
    fi
    
    return $issues
}

# Check revenue optimization quality gates
check_revenue_quality_gates() {
    local issues=0
    
    log_quality "Checking revenue optimization quality gates..."
    
    # Check for revenue test files
    local revenue_test_files=$(find . -name "*.revenue.test.*" -o -name "*Revenue*Test*" -o -name "*Monetization*Test*" 2>/dev/null | wc -l)
    if [ "$revenue_test_files" -gt 0 ]; then
        log_success "Revenue test files found: $revenue_test_files"
    else
        log_warning "No revenue-specific test files found"
        if [ "$STRICT_MODE" = "true" ]; then
            ((issues++))
        fi
    fi
    
    # Check for IAP (In-App Purchase) implementation
    local iap_files=$(find . -name "*IAP*" -o -name "*Purchase*" -o -name "*Billing*" 2>/dev/null | wc -l)
    if [ "$iap_files" -gt 0 ]; then
        log_success "IAP implementation files found: $iap_files"
    else
        log_info "No IAP implementation found (may not be required)"
    fi
    
    # Check for ad integration
    local ad_files=$(find . -name "*Ad*" -o -name "*Advertisement*" 2>/dev/null | grep -v node_modules | wc -l)
    if [ "$ad_files" -gt 0 ]; then
        log_success "Ad integration files found: $ad_files"
    else
        log_info "No ad integration found (may not be required)"
    fi
    
    return $issues
}

# Check regional adaptation quality gates
check_regional_quality_gates() {
    local issues=0
    
    log_quality "Checking regional adaptation quality gates..."
    
    # Check for localization files
    local i18n_files=$(find . -name "*.json" -path "*/i18n/*" -o -name "*.json" -path "*/locales/*" -o -name "Localizable.strings" 2>/dev/null | wc -l)
    if [ "$i18n_files" -gt 0 ]; then
        log_success "Localization files found: $i18n_files"
    else
        log_warning "No localization files found"
        if [ "$STRICT_MODE" = "true" ]; then
            ((issues++))
        fi
    fi
    
    # Check for regional configuration
    local regional_config_files=$(find . -name "*regional*" -o -name "*locale*" -o -name "*country*" 2>/dev/null | grep -v node_modules | wc -l)
    if [ "$regional_config_files" -gt 0 ]; then
        log_success "Regional configuration files found: $regional_config_files"
    else
        log_info "No regional configuration found"
    fi
    
    # Check for Vietnam-specific adaptations (as mentioned in requirements)
    local vietnam_files=$(find . -name "*vietnam*" -o -name "*vn*" -o -name "*VN*" 2>/dev/null | grep -v node_modules | wc -l)
    if [ "$vietnam_files" -gt 0 ]; then
        log_success "Vietnam-specific files found: $vietnam_files"
    else
        log_info "No Vietnam-specific adaptations found"
    fi
    
    return $issues
}

# Check stage-specific quality gates
check_stage_quality_gates() {
    local stage=$1
    local issues=0
    
    log_stage "Checking $stage stage quality gates"
    
    case $stage in
        "requirements")
            check_file_exists "docs/requirements.md" "Requirements document" || ((issues++))
            check_file_exists "docs/acceptance-criteria.md" "Acceptance criteria" || ((issues++))
            
            # Check for AI and revenue requirements
            if [ -f "docs/requirements.md" ]; then
                if grep -q -i "ai\|artificial intelligence\|machine learning" docs/requirements.md; then
                    log_success "AI requirements documented"
                else
                    log_warning "AI requirements not found in requirements.md"
                fi
                
                if grep -q -i "revenue\|monetization\|purchase\|ads" docs/requirements.md; then
                    log_success "Revenue requirements documented"
                else
                    log_warning "Revenue requirements not found in requirements.md"
                fi
            fi
            ;;
            
        "design")
            check_file_exists "docs/architecture.md" "Architecture document" || ((issues++))
            check_file_exists "docs/ai-integration-design.md" "AI integration design" || ((issues++))
            check_file_exists "docs/revenue-optimization-plan.md" "Revenue optimization plan" || ((issues++))
            
            # Check for design artifacts
            local design_files=$(find . -name "*.png" -o -name "*.jpg" -o -name "*.svg" -o -name "*.figma" 2>/dev/null | wc -l)
            if [ "$design_files" -gt 0 ]; then
                log_success "Design artifacts found: $design_files"
            else
                log_info "No design artifacts found"
            fi
            ;;
            
        "tasks")
            check_file_exists "docs/task-breakdown.md" "Task breakdown" || ((issues++))
            check_file_exists "docs/test-specifications.md" "Test specifications" || ((issues++))
            
            # Check for task management integration
            if [ -f ".github/ISSUE_TEMPLATE" ] || [ -f ".github/issue_template.md" ]; then
                log_success "Issue templates found"
            else
                log_info "No issue templates found"
            fi
            ;;
            
        "execution")
            # Check for source code
            if [ -d "src" ] || [ -d "lib" ] || [ -d "app" ]; then
                log_success "Source code directory found"
            else
                log_error "No source code directory found"
                ((issues++))
            fi
            
            # Run code quality checks
            check_code_quality || ((issues++))
            
            # Check AI and revenue implementation
            check_ai_quality_gates || ((issues++))
            check_revenue_quality_gates || ((issues++))
            ;;
            
        "test")
            # Run comprehensive testing checks
            check_test_coverage || ((issues++))
            check_ai_quality_gates || ((issues++))
            check_revenue_quality_gates || ((issues++))
            check_regional_quality_gates || ((issues++))
            
            # Check for test reports
            if [ -d "test-results" ] || [ -d "reports" ]; then
                log_success "Test reports directory found"
            else
                log_warning "No test reports directory found"
            fi
            ;;
            
        "review")
            # Final comprehensive check
            check_code_quality || ((issues++))
            check_test_coverage || ((issues++))
            check_ai_quality_gates || ((issues++))
            check_revenue_quality_gates || ((issues++))
            check_regional_quality_gates || ((issues++))
            
            # Check for deployment readiness
            if [ -f "Dockerfile" ] || [ -f "docker-compose.yml" ] || [ -f ".github/workflows" ]; then
                log_success "Deployment configuration found"
            else
                log_info "No deployment configuration found"
            fi
            ;;
            
        *)
            log_error "Unknown stage: $stage"
            return 1
            ;;
    esac
    
    return $issues
}

# Generate quality report
generate_quality_report() {
    local stage=$1
    local report_file="$QUALITY_REPORT_DIR/quality-report-${stage}-$(date +%Y%m%d-%H%M%S).md"
    
    log_info "Generating quality report: $report_file"
    
    cat > "$report_file" << EOF
# TSDDR 2.0 Quality Gate Report

**Stage:** $stage  
**Generated:** $(date)  
**Branch:** $(git branch --show-current)  
**Commit:** $(git rev-parse --short HEAD)  

## Quality Gate Results

### Code Quality

$(if [ -f "$QUALITY_REPORT_DIR/eslint-report.json" ]; then echo "- ESLint: $(jq '[.[] | select(.errorCount > 0)] | length' "$QUALITY_REPORT_DIR/eslint-report.json") files with errors"; fi)
$(if [ -f "$QUALITY_REPORT_DIR/swiftlint-report.json" ]; then echo "- SwiftLint: $(jq 'length' "$QUALITY_REPORT_DIR/swiftlint-report.json") violations"; fi)

### Test Coverage

$(if [ -f "coverage/lcov-report/index.html" ]; then echo "- Overall Coverage: $(grep -o '[0-9]\+\.[0-9]\+%' coverage/lcov-report/index.html | head -1)"; else echo "- Coverage report not available"; fi)

### AI Quality Gates

- AI Test Files: $(find . -name "*.ai.test.*" -o -name "*.ai.spec.*" 2>/dev/null | wc -l)
- AI Configuration: $(find . -name "ai-config.*" -o -name "model-config.*" 2>/dev/null | wc -l) files

### Revenue Quality Gates

- Revenue Test Files: $(find . -name "*.revenue.test.*" -o -name "*Revenue*Test*" 2>/dev/null | wc -l)
- IAP Implementation: $(find . -name "*IAP*" -o -name "*Purchase*" 2>/dev/null | wc -l) files

### Regional Quality Gates

- Localization Files: $(find . -name "*.json" -path "*/i18n/*" -o -name "Localizable.strings" 2>/dev/null | wc -l)
- Regional Config: $(find . -name "*regional*" -o -name "*locale*" 2>/dev/null | wc -l) files

## Recommendations

- Maintain test coverage above $MIN_COVERAGE_THRESHOLD%
- Ensure all AI features have corresponding tests
- Validate revenue optimization implementations
- Complete regional adaptations for target markets

## Next Steps

$(if [ "$stage" != "review" ]; then echo "- Address any failing quality gates"; echo "- Run: ./scripts/tsddr-2.0/stage-transition.sh next"; else echo "- Ready for deployment"; echo "- Consider merging to main branch"; fi)

EOF
    
    log_success "Quality report generated: $report_file"
}

# Auto-fix common issues
auto_fix_issues() {
    log_info "Attempting to auto-fix common issues..."
    
    # Fix ESLint issues
    if command -v eslint &> /dev/null && [ -f "package.json" ]; then
        log_info "Auto-fixing ESLint issues..."
        eslint . --ext .js,.jsx,.ts,.tsx --fix || log_warning "Some ESLint issues could not be auto-fixed"
    fi
    
    # Fix SwiftLint issues
    if command -v swiftlint &> /dev/null; then
        log_info "Auto-fixing SwiftLint issues..."
        swiftlint --fix || log_warning "Some SwiftLint issues could not be auto-fixed"
    fi
    
    # Create missing documentation templates
    if [ ! -f "docs/requirements.md" ]; then
        mkdir -p docs
        cat > docs/requirements.md << 'EOF'
# Requirements

## Functional Requirements

### AI Features
- [ ] AI requirement 1
- [ ] AI requirement 2

### Revenue Features
- [ ] Revenue requirement 1
- [ ] Revenue requirement 2

### Regional Features
- [ ] Regional requirement 1
- [ ] Regional requirement 2

## Non-Functional Requirements

- [ ] Performance requirements
- [ ] Security requirements
- [ ] Scalability requirements
EOF
        log_success "Created requirements.md template"
    fi
    
    log_success "Auto-fix completed"
}

# Parse command line arguments
STAGE=""
STRICT_MODE=false
GENERATE_REPORT=false
AUTO_FIX=false
VERBOSE=false

while [[ $# -gt 0 ]]; do
    case $1 in
        --strict)
            STRICT_MODE=true
            shift
            ;;
        --report)
            GENERATE_REPORT=true
            shift
            ;;
        --fix)
            AUTO_FIX=true
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
            if [ -z "$STAGE" ]; then
                STAGE=$1
            else
                log_error "Multiple stages specified"
                show_usage
                exit 1
            fi
            shift
            ;;
    esac
done

if [ -z "$STAGE" ]; then
    show_usage
    exit 1
fi

# Setup environment
setup_quality_env

# Auto-fix if requested
if [ "$AUTO_FIX" = "true" ]; then
    auto_fix_issues
fi

# Run quality gate checks
log_stage "Running TSDDR 2.0 Quality Gate Validation for $STAGE stage"
echo "================================================================"

check_stage_quality_gates "$STAGE"
result=$?

# Generate report if requested
if [ "$GENERATE_REPORT" = "true" ]; then
    generate_quality_report "$STAGE"
fi

echo ""
if [ $result -eq 0 ]; then
    log_success "All quality gates passed for $STAGE stage! 🎉"
    echo "Ready to proceed to next stage."
else
    log_error "$result quality gate(s) failed for $STAGE stage"
    echo "Please address the issues before proceeding."
    
    if [ "$AUTO_FIX" != "true" ]; then
        echo "Tip: Use --fix flag to auto-fix common issues"
    fi
    
    exit 1
fi