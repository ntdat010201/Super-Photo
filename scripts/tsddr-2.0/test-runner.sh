#!/bin/bash

# TSDDR 2.0 Enhanced Test Runner
# Runs comprehensive test suites for AI and revenue features

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
TEST_RESULTS_DIR="test-results"
COVERAGE_DIR="coverage"
REPORTS_DIR="reports"

# Test categories
TEST_CATEGORIES=("unit" "integration" "ai" "revenue" "regional" "e2e" "performance")

# Functions
show_usage() {
    echo "Usage: $0 <category> [options]"
    echo ""
    echo "Categories:"
    echo "  unit         - Run unit tests"
    echo "  integration  - Run integration tests"
    echo "  ai           - Run AI-specific tests"
    echo "  revenue      - Run revenue optimization tests"
    echo "  regional     - Run regional adaptation tests"
    echo "  e2e          - Run end-to-end tests"
    echo "  performance  - Run performance tests"
    echo "  all          - Run all test categories"
    echo "  stage        - Run tests for current TSDDR stage"
    echo ""
    echo "Options:"
    echo "  --coverage   - Generate coverage reports"
    echo "  --verbose    - Verbose output"
    echo "  --parallel   - Run tests in parallel"
    echo "  --watch      - Watch mode for development"
    echo "  --ci         - CI mode (no interactive prompts)"
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

log_test() {
    echo -e "${CYAN}🧪 $1${NC}"
}

log_stage() {
    echo -e "${PURPLE}🎯 $1${NC}"
}

# Setup test environment
setup_test_env() {
    log_info "Setting up test environment..."
    
    # Create directories
    mkdir -p "$TEST_RESULTS_DIR"
    mkdir -p "$COVERAGE_DIR"
    mkdir -p "$REPORTS_DIR"
    
    # Set environment variables
    export NODE_ENV=test
    export TSDDR_TEST_MODE=true
    export TSDDR_VERSION=2.0
    
    # Load test configuration if exists
    if [ -f ".env.test" ]; then
        source .env.test
    fi
    
    log_success "Test environment ready"
}

# Detect project type and test framework
detect_test_framework() {
    local framework="unknown"
    
    if [ -f "package.json" ]; then
        if grep -q "jest" package.json; then
            framework="jest"
        elif grep -q "mocha" package.json; then
            framework="mocha"
        elif grep -q "vitest" package.json; then
            framework="vitest"
        fi
    elif [ -f "*.xcodeproj" ] || [ -f "*.xcworkspace" ]; then
        framework="xctest"
    elif [ -f "build.gradle" ] || [ -f "app/build.gradle" ]; then
        framework="android"
    elif [ -f "pubspec.yaml" ]; then
        framework="flutter"
    fi
    
    echo "$framework"
}

# Run unit tests
run_unit_tests() {
    local framework=$(detect_test_framework)
    local coverage_flag=""
    
    if [ "$GENERATE_COVERAGE" = "true" ]; then
        coverage_flag="--coverage"
    fi
    
    log_test "Running unit tests with $framework"
    
    case $framework in
        "jest")
            if [ "$WATCH_MODE" = "true" ]; then
                npm test -- --watch $coverage_flag
            else
                npm test -- $coverage_flag --testPathPattern=".*\.(unit|spec)\.(js|ts|jsx|tsx)$"
            fi
            ;;
        "vitest")
            if [ "$WATCH_MODE" = "true" ]; then
                npx vitest --watch $coverage_flag
            else
                npx vitest run $coverage_flag --reporter=verbose
            fi
            ;;
        "xctest")
            if [ -f "*.xcworkspace" ]; then
                xcodebuild test -workspace *.xcworkspace -scheme "$(basename *.xcworkspace .xcworkspace)" -destination 'platform=iOS Simulator,name=iPhone 14'
            else
                xcodebuild test -project *.xcodeproj -scheme "$(basename *.xcodeproj .xcodeproj)" -destination 'platform=iOS Simulator,name=iPhone 14'
            fi
            ;;
        "android")
            ./gradlew testDebugUnitTest
            ;;
        "flutter")
            flutter test
            ;;
        *)
            log_warning "Unknown test framework, trying generic npm test"
            npm test 2>/dev/null || log_error "No test command found"
            ;;
    esac
}

# Run integration tests
run_integration_tests() {
    log_test "Running integration tests"
    
    local framework=$(detect_test_framework)
    
    case $framework in
        "jest")
            npm test -- --testPathPattern=".*\.integration\.(js|ts|jsx|tsx)$" --runInBand
            ;;
        "vitest")
            npx vitest run --reporter=verbose --testNamePattern="integration"
            ;;
        "xctest")
            # Run integration tests on iOS
            if [ -f "*.xcworkspace" ]; then
                xcodebuild test -workspace *.xcworkspace -scheme "$(basename *.xcworkspace .xcworkspace)IntegrationTests" -destination 'platform=iOS Simulator,name=iPhone 14'
            fi
            ;;
        "android")
            ./gradlew connectedAndroidTest
            ;;
        "flutter")
            flutter test integration_test/
            ;;
        *)
            log_warning "Integration tests not configured for this framework"
            ;;
    esac
}

# Run AI-specific tests
run_ai_tests() {
    log_test "Running AI-specific tests"
    
    # Check if AI test suite exists
    if [ -f "scripts/tsddr-2.0/ai-test-suite.sh" ]; then
        ./scripts/tsddr-2.0/ai-test-suite.sh
    else
        local framework=$(detect_test_framework)
        
        case $framework in
            "jest")
                npm test -- --testPathPattern=".*\.ai\.(js|ts|jsx|tsx)$" --testNamePattern="AI|Machine Learning|Neural|Model"
                ;;
            "vitest")
                npx vitest run --reporter=verbose --testNamePattern="AI|ML|Model"
                ;;
            *)
                log_warning "AI tests not configured. Create scripts/tsddr-2.0/ai-test-suite.sh"
                ;;
        esac
    fi
}

# Run revenue optimization tests
run_revenue_tests() {
    log_test "Running revenue optimization tests"
    
    # Check if revenue test suite exists
    if [ -f "scripts/tsddr-2.0/revenue-test-suite.sh" ]; then
        ./scripts/tsddr-2.0/revenue-test-suite.sh
    else
        local framework=$(detect_test_framework)
        
        case $framework in
            "jest")
                npm test -- --testPathPattern=".*\.revenue\.(js|ts|jsx|tsx)$" --testNamePattern="Revenue|Monetization|IAP|Ads"
                ;;
            "vitest")
                npx vitest run --reporter=verbose --testNamePattern="Revenue|Monetization|Purchase"
                ;;
            *)
                log_warning "Revenue tests not configured. Create scripts/tsddr-2.0/revenue-test-suite.sh"
                ;;
        esac
    fi
}

# Run regional adaptation tests
run_regional_tests() {
    log_test "Running regional adaptation tests"
    
    # Check if regional test suite exists
    if [ -f "scripts/tsddr-2.0/regional-test-suite.sh" ]; then
        ./scripts/tsddr-2.0/regional-test-suite.sh
    else
        local framework=$(detect_test_framework)
        
        case $framework in
            "jest")
                npm test -- --testPathPattern=".*\.regional\.(js|ts|jsx|tsx)$" --testNamePattern="Regional|Localization|i18n"
                ;;
            "vitest")
                npx vitest run --reporter=verbose --testNamePattern="Regional|Locale|i18n"
                ;;
            *)
                log_warning "Regional tests not configured. Create scripts/tsddr-2.0/regional-test-suite.sh"
                ;;
        esac
    fi
}

# Run end-to-end tests
run_e2e_tests() {
    log_test "Running end-to-end tests"
    
    if command -v cypress &> /dev/null; then
        if [ "$CI_MODE" = "true" ]; then
            npx cypress run
        else
            npx cypress open
        fi
    elif command -v playwright &> /dev/null; then
        npx playwright test
    elif [ -f "e2e" ] && command -v detox &> /dev/null; then
        # React Native Detox
        detox test
    else
        log_warning "No E2E testing framework found (Cypress, Playwright, or Detox)"
    fi
}

# Run performance tests
run_performance_tests() {
    log_test "Running performance tests"
    
    if command -v lighthouse &> /dev/null; then
        log_info "Running Lighthouse performance audit"
        lighthouse http://localhost:3000 --output=json --output-path="$REPORTS_DIR/lighthouse-report.json"
    fi
    
    if [ -f "package.json" ] && grep -q "@storybook/test-runner" package.json; then
        log_info "Running Storybook performance tests"
        npm run test-storybook
    fi
    
    # Custom performance tests
    local framework=$(detect_test_framework)
    case $framework in
        "jest")
            npm test -- --testPathPattern=".*\.performance\.(js|ts|jsx|tsx)$"
            ;;
        *)
            log_warning "Performance tests not configured for this framework"
            ;;
    esac
}

# Run tests for current TSDDR stage
run_stage_tests() {
    local current_branch=$(git branch --show-current)
    local stage="unknown"
    
    if [[ $current_branch == tsddr/* ]]; then
        stage=${current_branch#tsddr/}
    else
        log_error "Not on a TSDDR stage branch"
        exit 1
    fi
    
    log_stage "Running tests for $stage stage"
    
    case $stage in
        "requirements")
            log_info "Requirements stage - running validation tests"
            run_unit_tests
            ;;
        "design")
            log_info "Design stage - running unit and integration tests"
            run_unit_tests
            run_integration_tests
            ;;
        "tasks")
            log_info "Tasks stage - running comprehensive test suite"
            run_unit_tests
            run_integration_tests
            run_ai_tests
            ;;
        "execution")
            log_info "Execution stage - running all implementation tests"
            run_unit_tests
            run_integration_tests
            run_ai_tests
            run_revenue_tests
            run_regional_tests
            ;;
        "test")
            log_info "Test stage - running full test suite"
            run_all_tests
            ;;
        "review")
            log_info "Review stage - running final validation"
            run_all_tests
            run_performance_tests
            ;;
        *)
            log_warning "Unknown stage: $stage, running basic tests"
            run_unit_tests
            ;;
    esac
}

# Run all tests
run_all_tests() {
    log_stage "Running comprehensive test suite"
    
    local start_time=$(date +%s)
    local failed_categories=()
    
    # Run each category and track failures
    for category in "unit" "integration" "ai" "revenue" "regional" "e2e"; do
        log_info "Running $category tests..."
        
        case $category in
            "unit") run_unit_tests ;;
            "integration") run_integration_tests ;;
            "ai") run_ai_tests ;;
            "revenue") run_revenue_tests ;;
            "regional") run_regional_tests ;;
            "e2e") run_e2e_tests ;;
        esac
        
        if [ $? -ne 0 ]; then
            failed_categories+=("$category")
        fi
    done
    
    local end_time=$(date +%s)
    local duration=$((end_time - start_time))
    
    echo ""
    log_stage "Test Suite Summary"
    echo "=================="
    echo "Duration: ${duration}s"
    
    if [ ${#failed_categories[@]} -eq 0 ]; then
        log_success "All test categories passed! 🎉"
    else
        log_error "Failed categories: ${failed_categories[*]}"
        exit 1
    fi
}

# Generate test report
generate_report() {
    log_info "Generating test report..."
    
    local report_file="$REPORTS_DIR/test-report-$(date +%Y%m%d-%H%M%S).md"
    
    cat > "$report_file" << EOF
# TSDDR 2.0 Test Report

Generated: $(date)
Branch: $(git branch --show-current)
Commit: $(git rev-parse --short HEAD)

## Test Results

### Coverage Summary

$(if [ -f "$COVERAGE_DIR/lcov-report/index.html" ]; then echo "Coverage report available at: $COVERAGE_DIR/lcov-report/index.html"; else echo "No coverage report generated"; fi)

### Test Categories

- Unit Tests: $(if [ -f "$TEST_RESULTS_DIR/unit.xml" ]; then echo "✅ Passed"; else echo "❓ Not run"; fi)
- Integration Tests: $(if [ -f "$TEST_RESULTS_DIR/integration.xml" ]; then echo "✅ Passed"; else echo "❓ Not run"; fi)
- AI Tests: $(if [ -f "$TEST_RESULTS_DIR/ai.xml" ]; then echo "✅ Passed"; else echo "❓ Not run"; fi)
- Revenue Tests: $(if [ -f "$TEST_RESULTS_DIR/revenue.xml" ]; then echo "✅ Passed"; else echo "❓ Not run"; fi)
- Regional Tests: $(if [ -f "$TEST_RESULTS_DIR/regional.xml" ]; then echo "✅ Passed"; else echo "❓ Not run"; fi)
- E2E Tests: $(if [ -f "$TEST_RESULTS_DIR/e2e.xml" ]; then echo "✅ Passed"; else echo "❓ Not run"; fi)

## Performance Metrics

$(if [ -f "$REPORTS_DIR/lighthouse-report.json" ]; then echo "Lighthouse report available"; else echo "No performance metrics available"; fi)

## Recommendations

- Maintain test coverage above 85%
- Ensure all AI and revenue tests pass
- Validate regional adaptations
- Monitor performance metrics

EOF
    
    log_success "Test report generated: $report_file"
}

# Parse command line arguments
CATEGORY=""
GENERATE_COVERAGE=false
VERBOSE=false
PARALLEL=false
WATCH_MODE=false
CI_MODE=false

while [[ $# -gt 0 ]]; do
    case $1 in
        --coverage)
            GENERATE_COVERAGE=true
            shift
            ;;
        --verbose)
            VERBOSE=true
            shift
            ;;
        --parallel)
            PARALLEL=true
            shift
            ;;
        --watch)
            WATCH_MODE=true
            shift
            ;;
        --ci)
            CI_MODE=true
            shift
            ;;
        -*)
            log_error "Unknown option: $1"
            show_usage
            exit 1
            ;;
        *)
            if [ -z "$CATEGORY" ]; then
                CATEGORY=$1
            else
                log_error "Multiple categories specified"
                show_usage
                exit 1
            fi
            shift
            ;;
    esac
done

if [ -z "$CATEGORY" ]; then
    show_usage
    exit 1
fi

# Setup test environment
setup_test_env

# Run tests based on category
case $CATEGORY in
    "unit")
        run_unit_tests
        ;;
    "integration")
        run_integration_tests
        ;;
    "ai")
        run_ai_tests
        ;;
    "revenue")
        run_revenue_tests
        ;;
    "regional")
        run_regional_tests
        ;;
    "e2e")
        run_e2e_tests
        ;;
    "performance")
        run_performance_tests
        ;;
    "all")
        run_all_tests
        ;;
    "stage")
        run_stage_tests
        ;;
    *)
        log_error "Unknown category: $CATEGORY"
        show_usage
        exit 1
        ;;
esac

# Generate report if not in watch mode
if [ "$WATCH_MODE" != "true" ] && [ "$CI_MODE" != "true" ]; then
    generate_report
fi

log_success "Test execution completed! 🎉"