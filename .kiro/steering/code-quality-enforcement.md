# KIRO System - Code Quality Enforcement Integration

## 🎯 MANDATORY KIRO COMPLIANCE
***BẮT BUỘC*** tích hợp code quality enforcement vào tất cả KIRO workflows và task execution. Không có exception nào được phép.

## 🔗 Integration với Core Rules
***BẮT BUỘC*** KIRO system phải enforce các rules sau:

### Primary Code Quality Rules (MANDATORY)
- **[AI Code Quality Automation](../../.cursor/rules/ai-code-quality-automation.mdc)** - Manual workflow
- **[AI Manual Code Review Process](../../.cursor/rules/ai-manual-code-review-process.mdc)** - Review protocols
- **[AI Execution Templates](../../.cursor/rules/ai-execution-templates.mdc)** - Execution templates

### KIRO-Specific Rules (REQUIRED)
- **[Kiro System Overview](../../.cursor/rules/kiro-system-overview.mdc)** - System integration
- **[Kiro Dynamic Workflow](../../.cursor/rules/kiro-dynamic-workflow.mdc)** - Tự động tạo/cập nhật specs khi cần thiết

## 🤖 KIRO Code Quality Integration Points

### Task Execution Enforcement
```yaml
# MANDATORY: Code quality checks cho mọi KIRO task
kiro_task_execution:
  pre_task_validation:
    - code_quality_check: MANDATORY
    - configuration_syntax: REQUIRED
    - dependency_validation: REQUIRED
    - file_path_verification: REQUIRED
  
  during_task_execution:
    - real_time_validation: ENABLED
    - auto_fix_application: CONDITIONAL
    - confidence_threshold_check: REQUIRED
    - error_prevention: ACTIVE
  
  post_task_validation:
    - complete_quality_scan: MANDATORY
    - compliance_verification: REQUIRED
    - success_criteria_check: REQUIRED
    - documentation_update: AUTOMATIC
```

### Configuration File Enforcement
```yaml
# MANDATORY: Configuration quality standards
configuration_enforcement:
  yaml_syntax:
    validation: STRICT
    auto_fix_confidence: 0.95
    error_tolerance: ZERO
  
  json_syntax:
    validation: STRICT
    auto_fix_confidence: 0.95
    error_tolerance: ZERO
  
  dependency_management:
    resolution_check: MANDATORY
    circular_dependency: BLOCK
    version_compatibility: VALIDATE
  
  file_references:
    path_validation: STRICT
    existence_check: MANDATORY
    permission_verification: REQUIRED
```

## 🚨 KIRO Critical Enforcement Rules

### Task Execution Blockers (ZERO TOLERANCE)
```markdown
***NGHIÊM CẤM*** proceed with task execution if:

❌ Configuration syntax errors detected
❌ Missing required dependencies
❌ Invalid file path references
❌ Circular dependency conflicts
❌ Permission/access violations

ACTION: BLOCK task execution, REQUIRE manual intervention
```

### High Priority Issues (FIX BEFORE TASK EXECUTION)
```markdown
***BẮT BUỘC*** resolve before proceeding:

⚠️ YAML/JSON formatting inconsistencies  
⚠️ Deprecated dependency versions
⚠️ Missing error handling in task definitions
⚠️ Incomplete task documentation
⚠️ Resource cleanup procedures missing

ACTION: Auto-fix with 85%+ confidence or ESCALATE
```

## 📋 KIRO Quality Workflow Integration

### Pre-Task Quality Gate
```markdown
MANDATORY checklist before EVERY task execution:

☐ 1. CONFIGURATION VALIDATION (15 seconds max)
   - YAML/JSON syntax verification
   - Schema compliance check
   - Required field validation

☐ 2. DEPENDENCY RESOLUTION (30 seconds max)  
   - Dependency graph analysis
   - Version compatibility check
   - Circular dependency detection

☐ 3. RESOURCE VERIFICATION (10 seconds max)
   - File path existence check
   - Permission validation
   - Resource availability confirmation

☐ 4. TASK READINESS ASSESSMENT (5 seconds max)
   - Prerequisites validation
   - Error handling verification
   - Success criteria definition
```

### During Task Execution
```markdown
CONTINUOUS monitoring requirements:

☐ 1. REAL-TIME VALIDATION
   - Configuration changes monitoring
   - Resource usage tracking
   - Error condition detection

☐ 2. AUTO-CORRECTION (Confidence >= 85%)
   - YAML indentation fixes
   - Missing dependency additions
   - Path reference corrections

☐ 3. ESCALATION TRIGGERS
   - Complex configuration errors
   - Security-related issues
   - Resource availability problems
```

### Post-Task Quality Verification
```markdown
MANDATORY validation after task completion:

☐ 1. EXECUTION SUCCESS VALIDATION (20 seconds max)
   - Task completion verification
   - Output quality assessment
   - Side effects evaluation

☐ 2. CONFIGURATION CLEANUP (10 seconds max)
   - Temporary file removal
   - Resource deallocation
   - Configuration state reset

☐ 3. DOCUMENTATION UPDATE (15 seconds max)
   - Task execution log update
   - Success metrics recording
   - Issue resolution documentation
```

## 🎯 KIRO-Specific Quality Metrics

### Task Execution Metrics (MANDATORY)
```markdown
✅ Configuration syntax accuracy: > 95%
✅ Dependency resolution success: > 98%
✅ Task execution completion: > 90%
✅ Error handling coverage: > 85%
✅ Resource cleanup success: > 95%
```

### Performance Requirements
```markdown
📈 Configuration validation time: < 15 seconds
📈 Dependency resolution time: < 30 seconds
📈 Task execution efficiency: > 90%
📈 Auto-fix application rate: > 85%
📈 False positive rate: < 3%
```

### Quality Compliance Tracking
```markdown
🤖 YAML/JSON syntax compliance: > 99%
🤖 Dependency management accuracy: > 95%
🤖 File path resolution success: > 98%
🤖 Task completion rate: > 92%
🤖 Error prevention effectiveness: > 88%
```

## 🔄 KIRO Quality Monitoring

### Real-Time Monitoring Dashboard
```yaml
monitoring_components:
  configuration_health:
    - syntax_error_count: 0
    - validation_success_rate: "> 95%"
    - auto_fix_efficiency: "> 85%"
  
  task_execution_health:
    - completion_rate: "> 90%"
    - error_rate: "< 5%"
    - performance_metrics: "within_thresholds"
  
  dependency_management:
    - resolution_success: "> 98%"
    - conflict_detection: "active"
    - version_compliance: "> 95%"
```

### Escalation Procedures
```yaml
escalation_matrix:
  critical_failures:
    trigger: "task_execution_blocked"
    action: "immediate_manual_intervention"
    notification: "project_lead"
  
  quality_degradation:
    trigger: "compliance_below_thresholds"
    action: "automated_adjustment"
    fallback: "manual_review"
  
  configuration_issues:
    trigger: "syntax_errors_detected"
    action: "auto_fix_with_validation"
    backup: "configuration_rollback"
```

## 🚀 KIRO Integration Implementation

### Task Definition Enhancement
```yaml
# MANDATORY: Enhanced task definition với quality gates
enhanced_task_definition:
  task_id: "TASK-XXX"
  quality_requirements:
    pre_execution_checks: MANDATORY
    real_time_validation: ENABLED
    post_execution_verification: REQUIRED
  
  auto_fix_configuration:
    confidence_threshold: 0.85
    allowed_fixes: 
      - yaml_syntax
      - dependency_resolution
      - path_correction
    
  escalation_rules:
    critical_issues: "block_execution"
    quality_degradation: "notify_and_continue"
    configuration_errors: "auto_fix_or_escalate"
```

### Workflow Integration Points
```markdown
### With Planning Workflow
- Quality requirements definition
- Risk assessment integration
- Success criteria validation

### With Development Workflow  
- Code quality enforcement
- Review gate integration
- Continuous compliance monitoring

### With Deployment Workflow
- Pre-deployment quality checks
- Configuration validation
- Resource readiness verification
```

## 📊 KIRO Quality Reporting

### Daily Quality Reports
```yaml
daily_report_structure:
  executive_summary:
    - overall_compliance_rate
    - critical_issues_resolved
    - quality_improvements
  
  detailed_metrics:
    - task_execution_statistics
    - configuration_quality_trends
    - auto_fix_effectiveness
  
  action_items:
    - quality_threshold_adjustments
    - process_improvement_recommendations
    - escalation_procedure_refinements
```

### Quality Trend Analysis
```markdown
- Weekly quality compliance trends
- Monthly performance optimization
- Quarterly workflow effectiveness review
- Annual quality strategy assessment
```

## 🎯 Implementation Checklist

### Phase 1: Core Integration (IMMEDIATE)
```markdown
☐ Integrate quality checks into task execution pipeline
☐ Configure auto-fix confidence thresholds
☐ Set up real-time monitoring dashboard
☐ Implement escalation procedures
```

### Phase 2: Advanced Features (WEEK 2)
```markdown
☐ Enhanced reporting capabilities
☐ Predictive quality analytics  
☐ Advanced auto-fix algorithms
☐ Cross-workflow integration
```

### Phase 3: Optimization (MONTH 1)
```markdown
☐ Performance optimization
☐ Quality threshold refinement
☐ Workflow automation enhancement
☐ User experience improvements
```

---

**KIRO INTEGRATION STATUS**: 🔒 MANDATORY ENFORCEMENT
**COMPLIANCE LEVEL**: 100% Required for all tasks
**MONITORING**: Real-time, 24/7
**ESCALATION**: Automatic với manual fallback