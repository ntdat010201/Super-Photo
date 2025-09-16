#!/bin/bash

# TSDDR 2.0 Utilities Script
# Provides utility functions and tools for TSDDR 2.0 workflow

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
TEMPLATES_DIR="scripts/tsddr-2.0/templates"
REPORTS_DIR="tsddr-reports"
BACKUP_DIR=".tsddr-backups"

# Functions
show_usage() {
    echo "Usage: $0 <command> [options]"
    echo ""
    echo "Commands:"
    echo "  generate-docs <stage>       - Generate documentation templates"
    echo "  create-templates            - Create all TSDDR templates"
    echo "  backup-config               - Backup TSDDR configuration"
    echo "  restore-config <backup>     - Restore TSDDR configuration"
    echo "  validate-structure          - Validate TSDDR project structure"
    echo "  generate-report <type>      - Generate various reports"
    echo "  setup-hooks                 - Setup Git hooks for TSDDR"
    echo "  health-check                - Run TSDDR health check"
    echo "  migrate-config              - Migrate old TDD config to TSDDR 2.0"
    echo "  export-metrics              - Export TSDDR metrics"
    echo ""
    echo "Report Types: progress, quality, coverage, ai-metrics, revenue-metrics"
    echo ""
    echo "Options:"
    echo "  --output <dir>   - Output directory"
    echo "  --format <fmt>   - Output format (json, md, html)"
    echo "  --verbose        - Verbose output"
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

log_utility() {
    echo -e "${CYAN}🔧 $1${NC}"
}

log_stage() {
    echo -e "${PURPLE}🎯 $1${NC}"
}

# Setup utility environment
setup_utility_env() {
    mkdir -p "$TEMPLATES_DIR"
    mkdir -p "$REPORTS_DIR"
    mkdir -p "$BACKUP_DIR"
}

# Generate documentation templates for specific stage
generate_docs() {
    local stage=$1
    
    if [ -z "$stage" ]; then
        log_error "Stage is required"
        show_usage
        exit 1
    fi
    
    log_utility "Generating documentation templates for $stage stage"
    
    local docs_dir="docs"
    mkdir -p "$docs_dir"
    
    case $stage in
        "requirements")
            # Requirements document
            if [ ! -f "$docs_dir/requirements.md" ]; then
                cat > "$docs_dir/requirements.md" << 'EOF'
# Requirements Document

## Project Overview
- **Project Name**: [Project Name]
- **Version**: 1.0.0
- **Date**: [Date]
- **Author**: [Author]

## Functional Requirements

### Core Features
- [ ] Feature 1: [Description]
- [ ] Feature 2: [Description]
- [ ] Feature 3: [Description]

### AI Integration Requirements
- [ ] AI Feature 1: [Description]
- [ ] AI Feature 2: [Description]
- [ ] Machine Learning Models: [Specifications]
- [ ] Data Processing: [Requirements]

### Revenue Optimization Requirements
- [ ] Monetization Strategy: [Description]
- [ ] In-App Purchases: [Specifications]
- [ ] Advertisement Integration: [Requirements]
- [ ] Subscription Model: [Details]

### Regional Adaptation Requirements
- [ ] Localization: [Languages]
- [ ] Cultural Adaptations: [Specifications]
- [ ] Regional Compliance: [Requirements]
- [ ] Local Payment Methods: [Support]

## Non-Functional Requirements

### Performance
- [ ] Response Time: < 2 seconds
- [ ] Throughput: [Specifications]
- [ ] Scalability: [Requirements]

### Security
- [ ] Data Encryption: [Standards]
- [ ] Authentication: [Methods]
- [ ] Authorization: [Levels]

### Reliability
- [ ] Uptime: 99.9%
- [ ] Error Rate: < 0.1%
- [ ] Recovery Time: < 5 minutes

## Constraints
- [ ] Technical Constraints
- [ ] Business Constraints
- [ ] Regulatory Constraints

## Assumptions
- [ ] Assumption 1
- [ ] Assumption 2
- [ ] Assumption 3
EOF
                log_success "Created requirements.md"
            fi
            
            # Acceptance criteria
            if [ ! -f "$docs_dir/acceptance-criteria.md" ]; then
                cat > "$docs_dir/acceptance-criteria.md" << 'EOF'
# Acceptance Criteria

## Feature 1: [Feature Name]

### Scenario 1: [Scenario Name]
**Given** [Initial condition]  
**When** [Action performed]  
**Then** [Expected result]  

### Scenario 2: [Scenario Name]
**Given** [Initial condition]  
**When** [Action performed]  
**Then** [Expected result]  

## AI Integration Acceptance Criteria

### AI Feature 1: [AI Feature Name]
**Given** [AI model is trained and deployed]  
**When** [User interacts with AI feature]  
**Then** [AI provides accurate response within 2 seconds]  

### AI Feature 2: [AI Feature Name]
**Given** [Data is available for processing]  
**When** [AI processes the data]  
**Then** [Results meet accuracy threshold of 95%]  

## Revenue Optimization Acceptance Criteria

### Monetization Feature 1: [Feature Name]
**Given** [User meets criteria for monetization]  
**When** [Monetization feature is triggered]  
**Then** [Revenue event is tracked and processed]  

### Regional Pricing
**Given** [User is in specific region]  
**When** [User views pricing]  
**Then** [Prices are displayed in local currency and format]  

## Regional Adaptation Acceptance Criteria

### Localization
**Given** [User selects language preference]  
**When** [App loads content]  
**Then** [All text is displayed in selected language]  

### Cultural Adaptation
**Given** [User is in specific cultural region]  
**When** [App displays content]  
**Then** [Content respects cultural norms and preferences]  
EOF
                log_success "Created acceptance-criteria.md"
            fi
            ;;
            
        "design")
            # Architecture document
            if [ ! -f "$docs_dir/architecture.md" ]; then
                cat > "$docs_dir/architecture.md" << 'EOF'
# System Architecture

## Overview
[High-level system description]

## Architecture Patterns
- **Pattern**: [e.g., MVC, MVVM, Clean Architecture]
- **Rationale**: [Why this pattern was chosen]

## System Components

### Frontend Layer
- **Technology**: [React/Vue/Angular/Swift/Kotlin]
- **Responsibilities**: [UI, User Interaction, State Management]

### Backend Layer
- **Technology**: [Node.js/Laravel/Spring Boot]
- **Responsibilities**: [API, Business Logic, Data Processing]

### Database Layer
- **Technology**: [PostgreSQL/MongoDB/Firebase]
- **Responsibilities**: [Data Storage, Queries, Transactions]

### AI Integration Layer
- **Technology**: [TensorFlow/PyTorch/OpenAI API]
- **Responsibilities**: [ML Models, AI Processing, Predictions]

### Revenue Optimization Layer
- **Technology**: [Stripe/AdMob/Firebase]
- **Responsibilities**: [Payments, Ads, Analytics]

## Data Flow
1. [Step 1: User interaction]
2. [Step 2: Frontend processing]
3. [Step 3: Backend processing]
4. [Step 4: AI/Revenue processing]
5. [Step 5: Response delivery]

## Security Architecture
- **Authentication**: [JWT/OAuth2/Firebase Auth]
- **Authorization**: [RBAC/ABAC]
- **Data Encryption**: [AES-256/TLS 1.3]

## Scalability Considerations
- **Horizontal Scaling**: [Load balancers, microservices]
- **Vertical Scaling**: [Resource optimization]
- **Caching Strategy**: [Redis/Memcached]

## Regional Architecture
- **CDN**: [CloudFlare/AWS CloudFront]
- **Regional Databases**: [Data locality]
- **Compliance**: [GDPR/CCPA/Local regulations]
EOF
                log_success "Created architecture.md"
            fi
            
            # AI integration design
            if [ ! -f "$docs_dir/ai-integration-design.md" ]; then
                cat > "$docs_dir/ai-integration-design.md" << 'EOF'
# AI Integration Design

## AI Strategy Overview
[Description of AI integration approach]

## AI Components

### Machine Learning Models
- **Model 1**: [Name and purpose]
  - **Type**: [Classification/Regression/NLP/Computer Vision]
  - **Framework**: [TensorFlow/PyTorch/Scikit-learn]
  - **Input**: [Data format and structure]
  - **Output**: [Prediction format]
  - **Accuracy Target**: [Performance metrics]

### AI Services
- **Service 1**: [OpenAI GPT/Google AI/Custom]
  - **Purpose**: [Natural language processing/Image recognition]
  - **API Integration**: [REST/GraphQL/SDK]
  - **Rate Limits**: [Requests per minute/hour]
  - **Fallback Strategy**: [When service is unavailable]

## Data Pipeline

### Data Collection
- **Sources**: [User interactions, external APIs, sensors]
- **Format**: [JSON/CSV/Images/Text]
- **Volume**: [Expected data volume per day]

### Data Processing
- **Preprocessing**: [Cleaning, normalization, feature extraction]
- **Storage**: [Data lake/warehouse solution]
- **Privacy**: [Data anonymization, GDPR compliance]

### Model Training
- **Training Data**: [Dataset specifications]
- **Training Pipeline**: [Automated/Manual process]
- **Model Versioning**: [MLOps strategy]
- **Performance Monitoring**: [Metrics and alerts]

## AI-Revenue Integration

### Personalized Recommendations
- **Algorithm**: [Collaborative filtering/Content-based]
- **Revenue Impact**: [Increased engagement/purchases]
- **A/B Testing**: [Optimization strategy]

### Dynamic Pricing
- **ML Model**: [Price optimization algorithm]
- **Factors**: [Demand, competition, user behavior]
- **Regional Adaptation**: [Local market conditions]

## Regional AI Considerations

### Language Support
- **NLP Models**: [Multilingual support]
- **Translation**: [Real-time/Batch processing]
- **Cultural Context**: [Local idioms and expressions]

### Regulatory Compliance
- **AI Ethics**: [Bias detection and mitigation]
- **Data Governance**: [Local data protection laws]
- **Transparency**: [Explainable AI requirements]
EOF
                log_success "Created ai-integration-design.md"
            fi
            
            # Revenue optimization plan
            if [ ! -f "$docs_dir/revenue-optimization-plan.md" ]; then
                cat > "$docs_dir/revenue-optimization-plan.md" << 'EOF'
# Revenue Optimization Plan

## Revenue Strategy Overview
[Description of monetization approach]

## Revenue Streams

### Primary Revenue Streams
1. **In-App Purchases (IAP)**
   - **Products**: [Premium features, virtual goods, subscriptions]
   - **Pricing Strategy**: [Freemium/Premium/Tiered]
   - **Target Revenue**: [Monthly/Annual goals]

2. **Advertising Revenue**
   - **Ad Types**: [Banner/Interstitial/Rewarded/Native]
   - **Ad Networks**: [AdMob/Facebook/Unity Ads]
   - **Expected eCPM**: [Revenue per thousand impressions]

3. **Subscription Model**
   - **Tiers**: [Basic/Premium/Enterprise]
   - **Pricing**: [Monthly/Annual options]
   - **Features**: [Tier-specific benefits]

### Secondary Revenue Streams
- **Data Monetization**: [Anonymized insights, market research]
- **Partnerships**: [Revenue sharing with third parties]
- **White-label Solutions**: [B2B offerings]

## AI-Driven Revenue Optimization

### Personalized Pricing
- **Dynamic Pricing Model**: [ML-based price optimization]
- **Factors**: [User behavior, willingness to pay, market conditions]
- **A/B Testing**: [Price sensitivity analysis]

### Intelligent Ad Placement
- **ML Algorithm**: [Optimal ad timing and placement]
- **User Segmentation**: [High-value vs. price-sensitive users]
- **Revenue Maximization**: [Balance between UX and revenue]

### Churn Prevention
- **Predictive Model**: [Identify users likely to churn]
- **Retention Strategies**: [Personalized offers, engagement campaigns]
- **Lifetime Value Optimization**: [Maximize long-term revenue]

## Regional Revenue Strategies

### Market-Specific Pricing
- **Vietnam Market**: [Local pricing strategy]
  - **Currency**: Vietnamese Dong (VND)
  - **Price Points**: [Optimized for local purchasing power]
  - **Payment Methods**: [Local payment gateways]

- **Global Markets**: [Region-specific adaptations]
  - **Developed Markets**: [Premium pricing strategy]
  - **Emerging Markets**: [Volume-based strategy]

### Cultural Revenue Optimization
- **Local Preferences**: [Region-specific features and content]
- **Seasonal Campaigns**: [Local holidays and events]
- **Cultural Sensitivity**: [Appropriate monetization approaches]

## Revenue Analytics and KPIs

### Key Metrics
- **ARPU**: Average Revenue Per User
- **LTV**: Customer Lifetime Value
- **CAC**: Customer Acquisition Cost
- **Conversion Rate**: Free to paid conversion
- **Churn Rate**: User retention metrics

### Tracking and Reporting
- **Analytics Platform**: [Firebase/Mixpanel/Custom]
- **Real-time Dashboards**: [Revenue monitoring]
- **Automated Alerts**: [Performance thresholds]

## Implementation Timeline

### Phase 1: Foundation (Weeks 1-4)
- [ ] Basic IAP implementation
- [ ] Analytics setup
- [ ] A/B testing framework

### Phase 2: Optimization (Weeks 5-8)
- [ ] AI-driven personalization
- [ ] Advanced analytics
- [ ] Regional adaptations

### Phase 3: Scale (Weeks 9-12)
- [ ] Advanced ML models
- [ ] Multi-market expansion
- [ ] Revenue optimization automation
EOF
                log_success "Created revenue-optimization-plan.md"
            fi
            ;;
            
        "tasks")
            # Task breakdown
            if [ ! -f "$docs_dir/task-breakdown.md" ]; then
                cat > "$docs_dir/task-breakdown.md" << 'EOF'
# Task Breakdown Structure

## Epic 1: Core Application Development

### User Story 1: [User Story Title]
**As a** [user type]  
**I want** [functionality]  
**So that** [benefit]  

#### Tasks:
- [ ] Task 1.1: [Description] (Estimate: 2 days)
- [ ] Task 1.2: [Description] (Estimate: 3 days)
- [ ] Task 1.3: [Description] (Estimate: 1 day)

#### Acceptance Criteria:
- [ ] Criteria 1
- [ ] Criteria 2
- [ ] Criteria 3

## Epic 2: AI Integration

### User Story 2: AI-Powered Features
**As a** user  
**I want** AI-powered recommendations  
**So that** I can discover relevant content  

#### Tasks:
- [ ] Task 2.1: Set up ML pipeline (Estimate: 5 days)
- [ ] Task 2.2: Implement recommendation engine (Estimate: 4 days)
- [ ] Task 2.3: Create AI API endpoints (Estimate: 3 days)
- [ ] Task 2.4: Integrate AI with frontend (Estimate: 2 days)

#### AI-Specific Testing Tasks:
- [ ] Unit tests for AI components
- [ ] Model accuracy validation
- [ ] Performance benchmarking
- [ ] A/B testing setup

## Epic 3: Revenue Optimization

### User Story 3: Monetization Features
**As a** business owner  
**I want** multiple revenue streams  
**So that** the app generates sustainable income  

#### Tasks:
- [ ] Task 3.1: Implement IAP system (Estimate: 4 days)
- [ ] Task 3.2: Integrate ad networks (Estimate: 3 days)
- [ ] Task 3.3: Set up subscription model (Estimate: 5 days)
- [ ] Task 3.4: Create revenue analytics (Estimate: 3 days)

#### Revenue Testing Tasks:
- [ ] IAP flow testing
- [ ] Ad integration testing
- [ ] Payment processing testing
- [ ] Revenue tracking validation

## Epic 4: Regional Adaptation

### User Story 4: Localization
**As a** user in different regions  
**I want** localized content and pricing  
**So that** the app feels native to my culture  

#### Tasks:
- [ ] Task 4.1: Implement i18n framework (Estimate: 3 days)
- [ ] Task 4.2: Create translation system (Estimate: 4 days)
- [ ] Task 4.3: Implement regional pricing (Estimate: 3 days)
- [ ] Task 4.4: Add cultural adaptations (Estimate: 2 days)

#### Regional Testing Tasks:
- [ ] Multi-language testing
- [ ] Cultural appropriateness testing
- [ ] Regional payment testing
- [ ] Compliance validation

## Testing Strategy

### Unit Testing
- [ ] Core functionality tests
- [ ] AI component tests
- [ ] Revenue feature tests
- [ ] Localization tests

### Integration Testing
- [ ] API integration tests
- [ ] Third-party service tests
- [ ] End-to-end workflow tests
- [ ] Cross-platform compatibility

### Performance Testing
- [ ] Load testing
- [ ] AI model performance
- [ ] Revenue tracking accuracy
- [ ] Multi-region latency

## Risk Mitigation

### Technical Risks
- [ ] AI model accuracy issues
- [ ] Third-party API limitations
- [ ] Performance bottlenecks
- [ ] Security vulnerabilities

### Business Risks
- [ ] Revenue targets not met
- [ ] User adoption challenges
- [ ] Regulatory compliance issues
- [ ] Market competition

## Timeline

### Sprint 1 (Weeks 1-2): Foundation
- Core app structure
- Basic AI integration
- Initial revenue features

### Sprint 2 (Weeks 3-4): Enhancement
- Advanced AI features
- Revenue optimization
- Basic localization

### Sprint 3 (Weeks 5-6): Integration
- Full feature integration
- Comprehensive testing
- Regional adaptations

### Sprint 4 (Weeks 7-8): Polish
- Performance optimization
- Final testing
- Deployment preparation
EOF
                log_success "Created task-breakdown.md"
            fi
            
            # Test specifications
            if [ ! -f "$docs_dir/test-specifications.md" ]; then
                cat > "$docs_dir/test-specifications.md" << 'EOF'
# Test Specifications

## Testing Strategy Overview

This document outlines the comprehensive testing approach for TSDDR 2.0, covering traditional functionality, AI integration, revenue optimization, and regional adaptation.

## Test Categories

### 1. Unit Testing

#### Core Functionality Tests
- **Coverage Target**: 90%
- **Framework**: [Jest/XCTest/JUnit]
- **Focus Areas**:
  - Business logic validation
  - Data transformation
  - Error handling
  - Edge cases

#### AI Component Tests
- **Coverage Target**: 85%
- **Framework**: [pytest/Jest/Custom]
- **Focus Areas**:
  - Model input/output validation
  - Prediction accuracy
  - Performance benchmarks
  - Fallback mechanisms

#### Revenue Feature Tests
- **Coverage Target**: 95%
- **Framework**: [Jest/XCTest/JUnit]
- **Focus Areas**:
  - Payment processing
  - Subscription management
  - Ad integration
  - Analytics tracking

### 2. Integration Testing

#### API Integration Tests
- **Framework**: [Supertest/Postman/Custom]
- **Test Scenarios**:
  - Authentication flows
  - Data synchronization
  - Third-party service integration
  - Error handling and retries

#### AI Integration Tests
- **Framework**: [Custom AI testing framework]
- **Test Scenarios**:
  - Model deployment validation
  - Real-time prediction testing
  - Data pipeline integrity
  - Performance under load

#### Revenue Integration Tests
- **Framework**: [Stripe Test Mode/AdMob Test Ads]
- **Test Scenarios**:
  - End-to-end purchase flows
  - Subscription lifecycle
  - Ad serving and tracking
  - Revenue attribution

### 3. End-to-End Testing

#### User Journey Tests
- **Framework**: [Cypress/Selenium/Appium]
- **Test Scenarios**:
  - Complete user onboarding
  - Feature discovery and usage
  - Purchase and subscription flows
  - Multi-language experiences

#### Cross-Platform Tests
- **Platforms**: [iOS/Android/Web]
- **Test Scenarios**:
  - Feature parity validation
  - Performance consistency
  - UI/UX consistency
  - Data synchronization

### 4. AI-Specific Testing

#### Model Validation Tests
- **Accuracy Testing**:
  - Precision and recall metrics
  - F1 score validation
  - Confusion matrix analysis
  - ROC curve evaluation

- **Bias Testing**:
  - Demographic parity
  - Equal opportunity
  - Calibration testing
  - Fairness metrics

- **Robustness Testing**:
  - Adversarial input testing
  - Edge case handling
  - Data drift detection
  - Model degradation monitoring

#### AI Performance Tests
- **Latency Testing**:
  - Prediction response time
  - Batch processing performance
  - Real-time inference speed
  - Scalability under load

- **Resource Usage**:
  - Memory consumption
  - CPU utilization
  - GPU usage (if applicable)
  - Network bandwidth

### 5. Revenue Optimization Testing

#### Monetization Flow Tests
- **IAP Testing**:
  - Purchase flow validation
  - Receipt verification
  - Restore purchases
  - Subscription management

- **Ad Integration Testing**:
  - Ad loading and display
  - Click tracking
  - Revenue attribution
  - Ad blocker handling

#### A/B Testing Framework
- **Test Setup**:
  - Experiment configuration
  - User segmentation
  - Statistical significance
  - Results analysis

- **Revenue Metrics**:
  - Conversion rate testing
  - ARPU optimization
  - LTV improvement
  - Churn reduction

### 6. Regional Adaptation Testing

#### Localization Tests
- **Language Testing**:
  - Translation accuracy
  - Text rendering
  - RTL language support
  - Character encoding

- **Cultural Testing**:
  - Content appropriateness
  - Color and imagery
  - Date and number formats
  - Cultural sensitivity

#### Regional Compliance Tests
- **Data Protection**:
  - GDPR compliance
  - CCPA compliance
  - Local privacy laws
  - Data residency

- **Payment Testing**:
  - Local payment methods
  - Currency conversion
  - Tax calculation
  - Regional pricing

## Test Data Management

### Test Data Strategy
- **Synthetic Data**: AI-generated test data
- **Anonymized Production Data**: Privacy-compliant real data
- **Mock Data**: Controlled test scenarios
- **Regional Data**: Location-specific test cases

### Data Privacy
- **PII Handling**: No real personal data in tests
- **Data Anonymization**: Automated scrubbing
- **Compliance**: GDPR/CCPA requirements
- **Retention**: Automated cleanup

## Performance Testing

### Load Testing
- **User Load**: Concurrent user simulation
- **AI Load**: Model inference under load
- **Revenue Load**: Payment processing capacity
- **Regional Load**: Multi-region performance

### Stress Testing
- **Breaking Point**: System limits
- **Recovery Testing**: Graceful degradation
- **Failover Testing**: Backup systems
- **Chaos Engineering**: Resilience validation

## Security Testing

### Application Security
- **OWASP Top 10**: Common vulnerabilities
- **Authentication**: Security flow testing
- **Authorization**: Access control validation
- **Data Encryption**: End-to-end security

### AI Security
- **Model Security**: Adversarial attacks
- **Data Poisoning**: Training data integrity
- **Privacy**: Differential privacy
- **Explainability**: Model transparency

## Test Automation

### CI/CD Integration
- **Automated Testing**: Every commit
- **Quality Gates**: Deployment blockers
- **Test Reporting**: Comprehensive dashboards
- **Failure Notifications**: Immediate alerts

### Test Environments
- **Development**: Local testing
- **Staging**: Pre-production validation
- **Production**: Monitoring and validation
- **Regional**: Multi-region testing

## Quality Metrics

### Coverage Targets
- **Unit Tests**: 90%
- **Integration Tests**: 80%
- **E2E Tests**: 70%
- **AI Tests**: 85%

### Performance Targets
- **Response Time**: < 2 seconds
- **AI Inference**: < 500ms
- **Revenue Processing**: < 1 second
- **Page Load**: < 3 seconds

### Quality Gates
- **Code Quality**: SonarQube score > 8.0
- **Security**: No high/critical vulnerabilities
- **Performance**: All targets met
- **Accessibility**: WCAG 2.1 AA compliance
EOF
                log_success "Created test-specifications.md"
            fi
            ;;
            
        *)
            log_warning "No specific templates for stage: $stage"
            log_info "Creating generic documentation template"
            
            if [ ! -f "$docs_dir/${stage}-documentation.md" ]; then
                cat > "$docs_dir/${stage}-documentation.md" << EOF
# $stage Stage Documentation

## Overview
[Description of $stage stage]

## Objectives
- [ ] Objective 1
- [ ] Objective 2
- [ ] Objective 3

## Deliverables
- [ ] Deliverable 1
- [ ] Deliverable 2
- [ ] Deliverable 3

## AI Integration Considerations
- [ ] AI requirement 1
- [ ] AI requirement 2

## Revenue Optimization Considerations
- [ ] Revenue requirement 1
- [ ] Revenue requirement 2

## Regional Adaptation Considerations
- [ ] Regional requirement 1
- [ ] Regional requirement 2

## Quality Criteria
- [ ] Quality criterion 1
- [ ] Quality criterion 2

## Next Steps
- [ ] Next step 1
- [ ] Next step 2
EOF
                log_success "Created ${stage}-documentation.md"
            fi
            ;;
    esac
    
    log_success "Documentation templates generated for $stage stage"
}

# Create all TSDDR templates
create_templates() {
    log_utility "Creating all TSDDR 2.0 templates"
    
    mkdir -p "$TEMPLATES_DIR"
    
    # GitHub issue template
    mkdir -p ".github/ISSUE_TEMPLATE"
    cat > ".github/ISSUE_TEMPLATE/tsddr-feature.md" << 'EOF'
---
name: TSDDR 2.0 Feature Request
about: Create a feature request following TSDDR 2.0 methodology
title: '[TSDDR] Feature: '
labels: 'tsddr, feature'
assignees: ''
---

## TSDDR 2.0 Feature Request

### Feature Overview
**Feature Name**: [Name]
**Priority**: [High/Medium/Low]
**Stage**: [requirements/design/tasks/execution/test/review]

### Requirements
- [ ] Functional requirement 1
- [ ] Functional requirement 2
- [ ] Non-functional requirement 1

### AI Integration
- [ ] AI requirement 1 (if applicable)
- [ ] AI requirement 2 (if applicable)

### Revenue Impact
- [ ] Revenue requirement 1 (if applicable)
- [ ] Revenue requirement 2 (if applicable)

### Regional Considerations
- [ ] Regional requirement 1 (if applicable)
- [ ] Regional requirement 2 (if applicable)

### Acceptance Criteria
- [ ] Criteria 1
- [ ] Criteria 2
- [ ] Criteria 3

### Definition of Done
- [ ] Requirements documented
- [ ] Design completed
- [ ] Tasks defined
- [ ] Implementation finished
- [ ] Tests passing
- [ ] Review approved
EOF
    
    # Pull request template
    cat > ".github/pull_request_template.md" << 'EOF'
## TSDDR 2.0 Pull Request

### Stage Information
- **TSDDR Stage**: [requirements/design/tasks/execution/test/review]
- **Feature**: [Feature name]
- **Branch**: [Branch name]

### Changes Made
- [ ] Change 1
- [ ] Change 2
- [ ] Change 3

### AI Integration Changes
- [ ] AI change 1 (if applicable)
- [ ] AI change 2 (if applicable)

### Revenue Optimization Changes
- [ ] Revenue change 1 (if applicable)
- [ ] Revenue change 2 (if applicable)

### Regional Adaptation Changes
- [ ] Regional change 1 (if applicable)
- [ ] Regional change 2 (if applicable)

### Quality Checklist
- [ ] Code quality checks passed
- [ ] Unit tests added/updated
- [ ] Integration tests added/updated
- [ ] AI tests added/updated (if applicable)
- [ ] Revenue tests added/updated (if applicable)
- [ ] Regional tests added/updated (if applicable)
- [ ] Documentation updated
- [ ] Performance impact assessed
- [ ] Security review completed

### Testing
- [ ] Manual testing completed
- [ ] Automated tests passing
- [ ] Cross-platform testing (if applicable)
- [ ] Regional testing (if applicable)

### Deployment
- [ ] Ready for staging deployment
- [ ] Ready for production deployment
- [ ] Rollback plan documented
EOF
    
    # Pre-commit hook template
    mkdir -p ".git/hooks"
    cat > ".git/hooks/pre-commit" << 'EOF'
#!/bin/bash

# TSDDR 2.0 Pre-commit Hook

echo "🔍 Running TSDDR 2.0 pre-commit checks..."

# Run quality gate checker if available
if [ -f "./scripts/tsddr-2.0/quality-gate-checker.sh" ]; then
    echo "Running quality gate checks..."
    if ! ./scripts/tsddr-2.0/quality-gate-checker.sh execution --strict; then
        echo "❌ Quality gate checks failed. Commit aborted."
        exit 1
    fi
fi

# Run tests
if [ -f "package.json" ]; then
    echo "Running JavaScript/TypeScript tests..."
    npm test
fi

if [ -f "Gemfile" ]; then
    echo "Running Ruby tests..."
    bundle exec rspec
fi

if [ -f "requirements.txt" ]; then
    echo "Running Python tests..."
    python -m pytest
fi

echo "✅ Pre-commit checks passed!"
EOF
    chmod +x ".git/hooks/pre-commit"
    
    log_success "All TSDDR templates created"
}

# Backup TSDDR configuration
backup_config() {
    log_utility "Backing up TSDDR configuration"
    
    local timestamp=$(date +%Y%m%d-%H%M%S)
    local backup_file="$BACKUP_DIR/tsddr-config-$timestamp.json"
    
    if [ -f "$CONFIG_FILE" ]; then
        cp "$CONFIG_FILE" "$backup_file"
        log_success "Configuration backed up to: $backup_file"
    else
        log_warning "No TSDDR configuration found to backup"
    fi
}

# Restore TSDDR configuration
restore_config() {
    local backup_file=$1
    
    if [ -z "$backup_file" ]; then
        log_error "Backup file is required"
        log_info "Available backups:"
        ls -la "$BACKUP_DIR"/tsddr-config-*.json 2>/dev/null || log_info "No backups found"
        exit 1
    fi
    
    if [ -f "$backup_file" ]; then
        cp "$backup_file" "$CONFIG_FILE"
        log_success "Configuration restored from: $backup_file"
    else
        log_error "Backup file not found: $backup_file"
        exit 1
    fi
}

# Validate TSDDR project structure
validate_structure() {
    log_utility "Validating TSDDR project structure"
    
    local issues=0
    
    # Check for required files
    local required_files=(
        "$CONFIG_FILE"
        "docs/requirements.md"
        "docs/acceptance-criteria.md"
        ".github/ISSUE_TEMPLATE/tsddr-feature.md"
        ".github/pull_request_template.md"
    )
    
    for file in "${required_files[@]}"; do
        if [ -f "$file" ]; then
            log_success "Found: $file"
        else
            log_warning "Missing: $file"
            ((issues++))
        fi
    done
    
    # Check for recommended directories
    local recommended_dirs=(
        "docs"
        "scripts/tsddr-2.0"
        "tests"
        ".github/workflows"
    )
    
    for dir in "${recommended_dirs[@]}"; do
        if [ -d "$dir" ]; then
            log_success "Found directory: $dir"
        else
            log_info "Recommended directory missing: $dir"
        fi
    done
    
    # Check Git hooks
    if [ -f ".git/hooks/pre-commit" ]; then
        log_success "Pre-commit hook installed"
    else
        log_info "Pre-commit hook not installed"
    fi
    
    if [ $issues -eq 0 ]; then
        log_success "TSDDR project structure is valid! 🎉"
    else
        log_warning "Found $issues structural issues"
        log_info "Run: $0 create-templates to fix missing templates"
    fi
}

# Generate various reports
generate_report() {
    local report_type=$1
    local output_format=${OUTPUT_FORMAT:-"md"}
    local output_dir=${OUTPUT_DIR:-"$REPORTS_DIR"}
    
    if [ -z "$report_type" ]; then
        log_error "Report type is required"
        show_usage
        exit 1
    fi
    
    mkdir -p "$output_dir"
    local timestamp=$(date +%Y%m%d-%H%M%S)
    local report_file="$output_dir/tsddr-${report_type}-report-${timestamp}.${output_format}"
    
    log_utility "Generating $report_type report in $output_format format"
    
    case $report_type in
        "progress")
            generate_progress_report "$report_file"
            ;;
        "quality")
            generate_quality_report "$report_file"
            ;;
        "coverage")
            generate_coverage_report "$report_file"
            ;;
        "ai-metrics")
            generate_ai_metrics_report "$report_file"
            ;;
        "revenue-metrics")
            generate_revenue_metrics_report "$report_file"
            ;;
        *)
            log_error "Unknown report type: $report_type"
            exit 1
            ;;
    esac
    
    log_success "Report generated: $report_file"
}

# Generate progress report
generate_progress_report() {
    local report_file=$1
    
    cat > "$report_file" << EOF
# TSDDR 2.0 Progress Report

**Generated**: $(date)  
**Project**: $(basename "$(pwd)")  
**Branch**: $(git branch --show-current 2>/dev/null || echo "N/A")  

## Current Status

$(if [ -f "$CONFIG_FILE" ]; then
    if command -v jq &> /dev/null; then
        echo "**Feature**: $(jq -r '.feature_name // "Unknown"' "$CONFIG_FILE")"
        echo "**Current Stage**: $(jq -r '.current_stage // "Unknown"' "$CONFIG_FILE")"
        echo "**Started**: $(jq -r '.created_at // "Unknown"' "$CONFIG_FILE")"
    else
        echo "**Configuration**: Found"
    fi
else
    echo "**Configuration**: Not found"
fi)

## Stage Progress

### Requirements Stage
- [$([ -f "docs/requirements.md" ] && echo "x" || echo " ")] Requirements documented
- [$([ -f "docs/acceptance-criteria.md" ] && echo "x" || echo " ")] Acceptance criteria defined

### Design Stage
- [$([ -f "docs/architecture.md" ] && echo "x" || echo " ")] Architecture documented
- [$([ -f "docs/ai-integration-design.md" ] && echo "x" || echo " ")] AI integration designed
- [$([ -f "docs/revenue-optimization-plan.md" ] && echo "x" || echo " ")] Revenue plan created

### Implementation Progress
- **Source Files**: $(find . -name "*.js" -o -name "*.ts" -o -name "*.swift" -o -name "*.kt" 2>/dev/null | wc -l) files
- **Test Files**: $(find . -name "*.test.*" -o -name "*.spec.*" 2>/dev/null | wc -l) files
- **AI Files**: $(find . -name "*ai*" -o -name "*AI*" -o -name "*ml*" 2>/dev/null | wc -l) files
- **Revenue Files**: $(find . -name "*revenue*" -o -name "*monetiz*" -o -name "*purchase*" 2>/dev/null | wc -l) files

## Quality Metrics

$(if [ -f "coverage/lcov-report/index.html" ]; then
    echo "**Test Coverage**: $(grep -o '[0-9]\+\.[0-9]\+%' coverage/lcov-report/index.html | head -1)"
else
    echo "**Test Coverage**: Not available"
fi)

## Next Steps

- [ ] Complete current stage deliverables
- [ ] Run quality gate validation
- [ ] Proceed to next TSDDR stage

---
*Generated by TSDDR 2.0 Utilities*
EOF
}

# Generate quality report (placeholder)
generate_quality_report() {
    local report_file=$1
    echo "# Quality Report - Coming Soon" > "$report_file"
}

# Generate coverage report (placeholder)
generate_coverage_report() {
    local report_file=$1
    echo "# Coverage Report - Coming Soon" > "$report_file"
}

# Generate AI metrics report (placeholder)
generate_ai_metrics_report() {
    local report_file=$1
    echo "# AI Metrics Report - Coming Soon" > "$report_file"
}

# Generate revenue metrics report (placeholder)
generate_revenue_metrics_report() {
    local report_file=$1
    echo "# Revenue Metrics Report - Coming Soon" > "$report_file"
}

# Setup Git hooks
setup_hooks() {
    log_utility "Setting up Git hooks for TSDDR"
    
    if [ ! -d ".git" ]; then
        log_error "Not in a Git repository"
        exit 1
    fi
    
    mkdir -p ".git/hooks"
    
    # Pre-commit hook
    cat > ".git/hooks/pre-commit" << 'EOF'
#!/bin/bash
echo "🔍 TSDDR 2.0 Pre-commit validation..."
if [ -f "./scripts/tsddr-2.0/quality-gate-checker.sh" ]; then
    ./scripts/tsddr-2.0/quality-gate-checker.sh execution
fi
EOF
    chmod +x ".git/hooks/pre-commit"
    
    log_success "Git hooks installed"
}

# Run TSDDR health check
health_check() {
    log_utility "Running TSDDR 2.0 health check"
    
    local issues=0
    
    # Check configuration
    if [ -f "$CONFIG_FILE" ]; then
        log_success "TSDDR configuration found"
    else
        log_error "TSDDR configuration missing"
        ((issues++))
    fi
    
    # Check scripts
    local required_scripts=(
        "scripts/tsddr-2.0/init-tsddr-branch.sh"
        "scripts/tsddr-2.0/stage-transition.sh"
        "scripts/tsddr-2.0/quality-gate-checker.sh"
        "scripts/tsddr-2.0/git-integration.sh"
    )
    
    for script in "${required_scripts[@]}"; do
        if [ -f "$script" ] && [ -x "$script" ]; then
            log_success "Script available: $script"
        else
            log_warning "Script missing or not executable: $script"
            ((issues++))
        fi
    done
    
    # Check Git repository
    if git rev-parse --git-dir > /dev/null 2>&1; then
        log_success "Git repository detected"
    else
        log_warning "Not in a Git repository"
    fi
    
    # Summary
    if [ $issues -eq 0 ]; then
        log_success "TSDDR 2.0 health check passed! 🎉"
    else
        log_warning "Found $issues issues in TSDDR setup"
    fi
    
    return $issues
}

# Migrate old TDD config to TSDDR 2.0
migrate_config() {
    log_utility "Migrating TDD 1.0 config to TSDDR 2.0"
    
    # This is a placeholder for migration logic
    log_info "Migration functionality coming soon"
}

# Export TSDDR metrics
export_metrics() {
    log_utility "Exporting TSDDR metrics"
    
    # This is a placeholder for metrics export
    log_info "Metrics export functionality coming soon"
}

# Parse command line arguments
COMMAND=""
OUTPUT_DIR=""
OUTPUT_FORMAT="md"
VERBOSE=false

while [[ $# -gt 0 ]]; do
    case $1 in
        --output)
            OUTPUT_DIR="$2"
            shift 2
            ;;
        --format)
            OUTPUT_FORMAT="$2"
            shift 2
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

# Setup environment
setup_utility_env

# Execute command
case $COMMAND in
    "generate-docs")
        generate_docs "$1"
        ;;
    "create-templates")
        create_templates
        ;;
    "backup-config")
        backup_config
        ;;
    "restore-config")
        restore_config "$1"
        ;;
    "validate-structure")
        validate_structure
        ;;
    "generate-report")
        generate_report "$1"
        ;;
    "setup-hooks")
        setup_hooks
        ;;
    "health-check")
        health_check
        ;;
    "migrate-config")
        migrate_config
        ;;
    "export-metrics")
        export_metrics
        ;;
    *)
        log_error "Unknown command: $COMMAND"
        show_usage
        exit 1
        ;;
esac

log_success "TSDDR utilities completed successfully! 🎉"