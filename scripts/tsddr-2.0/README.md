# TSDDR 2.0 Automation Scripts

This directory contains automation scripts for Test Spec Driven Development & Revenue (TSDDR) 2.0 workflow.

## Scripts Overview

### Core Workflow Scripts
- `init-tsddr-branch.sh` - Initialize TSDDR 2.0 branch structure
- `stage-transition.sh` - Automate stage transitions with quality gates
- `test-runner.sh` - Enhanced test runner for AI and revenue features
- `quality-gate-checker.sh` - Validate quality gates before stage transitions

### Git Integration Scripts
- `create-feature-branch.sh` - Create feature branches with TSDDR 2.0 naming
- `merge-with-validation.sh` - Merge branches with automated validation
- `sync-branches.sh` - Sync development branches with main

### AI & Revenue Testing Scripts
- `ai-test-suite.sh` - Run AI-specific test suites
- `revenue-test-suite.sh` - Run revenue optimization tests
- `regional-test-suite.sh` - Run regional adaptation tests

### Utility Scripts
- `setup-environment.sh` - Setup TSDDR 2.0 development environment
- `generate-reports.sh` - Generate comprehensive test and quality reports
- `cleanup-branches.sh` - Clean up old feature branches

## Usage

All scripts are designed to work with the TSDDR 2.0 workflow defined in `.cursor/rules/tdd-mobile-workflow.mdc`.

### Prerequisites
- Git configured with proper credentials
- Node.js/npm or appropriate development environment
- Test frameworks installed (Jest, XCTest, Espresso, etc.)

### Getting Started

1. Setup environment:
```bash
./setup-environment.sh
```

2. Initialize TSDDR 2.0 branch structure:
```bash
./init-tsddr-branch.sh
```

3. Start development with stage transitions:
```bash
./stage-transition.sh requirements
```

## Configuration

Scripts use configuration from:
- `.tsddr-config.json` - Main TSDDR 2.0 configuration
- Environment variables for sensitive data
- Git hooks for automated validation

## Integration

These scripts integrate with:
- GitHub Actions workflows
- Local development environment
- CI/CD pipelines
- Quality gate systems