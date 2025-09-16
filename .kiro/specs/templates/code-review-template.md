# {PROJECT_NAME} - Code Review & Quality Assessment

## 1. Review Overview

**Feature**: {FEATURE_NAME}  
**Review Date**: {REVIEW_DATE}  
**Reviewer**: AI Code Review System  
**Review Type**: {REVIEW_TYPE} (Post-Implementation/Incremental/Final)  
**Git Commit Range**: {FROM_COMMIT}..{TO_COMMIT}  
**Files Changed**: {CHANGED_FILES_COUNT} files  
**Lines Added/Removed**: +{LINES_ADDED}/-{LINES_REMOVED}  

## 2. Git Diff Analysis

### 2.1 Changed Files Summary

| File Path | Change Type | Lines Changed | Risk Level | Review Status |
|-----------|-------------|---------------|------------|---------------|
| {FILE_1} | {CHANGE_TYPE} | +{ADDED}/-{REMOVED} | {RISK_LEVEL} | {STATUS} |
| {FILE_2} | {CHANGE_TYPE} | +{ADDED}/-{REMOVED} | {RISK_LEVEL} | {STATUS} |
| {FILE_3} | {CHANGE_TYPE} | +{ADDED}/-{REMOVED} | {RISK_LEVEL} | {STATUS} |

**Change Types**:
- `New`: Newly created file
- `Modified`: Existing file modified
- `Deleted`: File removed
- `Renamed`: File moved/renamed
- `Refactored`: Major structural changes

**Risk Levels**:
- 🟢 `Low`: Minor changes, low impact
- 🟡 `Medium`: Moderate changes, requires attention
- 🔴 `High`: Major changes, critical review needed

### 2.2 Code Duplication Detection

#### 2.2.1 Duplicate Code Blocks Found

**Duplication #1**: {DUPLICATE_DESCRIPTION}
- **Files**: {FILE_1}, {FILE_2}
- **Lines**: {FILE_1}:{LINE_RANGE_1} ↔ {FILE_2}:{LINE_RANGE_2}
- **Similarity**: {SIMILARITY_PERCENTAGE}%
- **Recommendation**: {REFACTOR_SUGGESTION}

```diff
# File: {FILE_1}
+ {DUPLICATE_CODE_BLOCK_1}

# File: {FILE_2}
+ {DUPLICATE_CODE_BLOCK_2}
```

**Suggested Refactor**:
```{LANGUAGE}
// Extract to shared utility/component
{REFACTORED_CODE_SUGGESTION}
```

#### 2.2.2 Similar Logic Patterns

**Pattern #1**: {PATTERN_DESCRIPTION}
- **Occurrences**: {OCCURRENCE_COUNT} times
- **Files**: {AFFECTED_FILES}
- **Consolidation Opportunity**: {CONSOLIDATION_SUGGESTION}

### 2.3 Code Synchronization Issues

#### 2.3.1 Inconsistent Implementations

**Issue #1**: {INCONSISTENCY_DESCRIPTION}
- **Affected Components**: {COMPONENT_LIST}
- **Problem**: {PROBLEM_DESCRIPTION}
- **Impact**: {IMPACT_ASSESSMENT}
- **Solution**: {SOLUTION_RECOMMENDATION}

**Example Inconsistency**:
```diff
# Component A (Correct Implementation)
+ {CORRECT_IMPLEMENTATION}

# Component B (Inconsistent Implementation)
+ {INCONSISTENT_IMPLEMENTATION}
```

#### 2.3.2 Missing Feature Connections

**Missing Connection #1**: {CONNECTION_DESCRIPTION}
- **Feature A**: {FEATURE_A_NAME}
- **Feature B**: {FEATURE_B_NAME}
- **Expected Integration**: {EXPECTED_INTEGRATION}
- **Current Status**: {CURRENT_STATUS}
- **Implementation Gap**: {GAP_DESCRIPTION}

**Recommended Integration**:
```{LANGUAGE}
// Add integration point
{INTEGRATION_CODE_SUGGESTION}
```

## 3. Code Quality Assessment

### 3.1 Architecture Compliance

| Aspect | Score | Status | Comments |
|--------|-------|--------|---------|
| Clean Architecture | {SCORE}/10 | {STATUS} | {COMMENTS} |
| SOLID Principles | {SCORE}/10 | {STATUS} | {COMMENTS} |
| Design Patterns | {SCORE}/10 | {STATUS} | {COMMENTS} |
| Code Organization | {SCORE}/10 | {STATUS} | {COMMENTS} |
| Naming Conventions | {SCORE}/10 | {STATUS} | {COMMENTS} |

### 3.2 Feature Integration Analysis

#### 3.2.1 Navigation Flow Validation

**Screen Connections Implemented**:
- ✅ {SCREEN_A} → {SCREEN_B}: {IMPLEMENTATION_STATUS}
- ✅ {SCREEN_B} → {SCREEN_C}: {IMPLEMENTATION_STATUS}
- ❌ {SCREEN_C} → {SCREEN_D}: {MISSING_IMPLEMENTATION}

**Missing Navigation Links**:
- {MISSING_LINK_1}: {DESCRIPTION_AND_IMPACT}
- {MISSING_LINK_2}: {DESCRIPTION_AND_IMPACT}

#### 3.2.2 Data Flow Consistency

**Data Passing Between Screens**:
- {SCREEN_A} → {SCREEN_B}: {DATA_PASSED} ✅
- {SCREEN_B} → {SCREEN_C}: {DATA_PASSED} ❌ Missing
- {SCREEN_C} → {SCREEN_D}: {DATA_PASSED} ⚠️ Inconsistent

### 3.3 Performance Impact Analysis

#### 3.3.1 Code Efficiency

**Performance Issues Detected**:
- **Issue #1**: {PERFORMANCE_ISSUE_1}
  - Location: {FILE_PATH}:{LINE_NUMBER}
  - Impact: {IMPACT_DESCRIPTION}
  - Suggestion: {OPTIMIZATION_SUGGESTION}

- **Issue #2**: {PERFORMANCE_ISSUE_2}
  - Location: {FILE_PATH}:{LINE_NUMBER}
  - Impact: {IMPACT_DESCRIPTION}
  - Suggestion: {OPTIMIZATION_SUGGESTION}

#### 3.3.2 Resource Usage

**Memory Usage**:
- Potential memory leaks: {LEAK_COUNT} detected
- Large object allocations: {LARGE_ALLOCATION_COUNT}
- Unused resources: {UNUSED_RESOURCE_COUNT}

**Network Efficiency**:
- API call optimization opportunities: {API_OPTIMIZATION_COUNT}
- Redundant network requests: {REDUNDANT_REQUEST_COUNT}
- Caching implementation: {CACHING_STATUS}

## 4. Security Review

### 4.1 Security Vulnerabilities

**Critical Issues** 🔴:
- {CRITICAL_SECURITY_ISSUE_1}
- {CRITICAL_SECURITY_ISSUE_2}

**Medium Issues** 🟡:
- {MEDIUM_SECURITY_ISSUE_1}
- {MEDIUM_SECURITY_ISSUE_2}

**Low Issues** 🟢:
- {LOW_SECURITY_ISSUE_1}
- {LOW_SECURITY_ISSUE_2}

### 4.2 Data Protection Compliance

- **Input Validation**: {VALIDATION_STATUS}
- **Data Encryption**: {ENCRYPTION_STATUS}
- **Authentication**: {AUTH_STATUS}
- **Authorization**: {AUTHZ_STATUS}

## 5. Testing Coverage Analysis

### 5.1 Test Coverage Report

| Component | Unit Tests | Integration Tests | UI Tests | Coverage % |
|-----------|------------|-------------------|----------|------------|
| {COMPONENT_1} | {UNIT_COUNT} | {INTEGRATION_COUNT} | {UI_COUNT} | {COVERAGE}% |
| {COMPONENT_2} | {UNIT_COUNT} | {INTEGRATION_COUNT} | {UI_COUNT} | {COVERAGE}% |
| {COMPONENT_3} | {UNIT_COUNT} | {INTEGRATION_COUNT} | {UI_COUNT} | {COVERAGE}% |

### 5.2 Missing Test Cases

**Critical Missing Tests**:
- {MISSING_TEST_1}: {DESCRIPTION_AND_IMPORTANCE}
- {MISSING_TEST_2}: {DESCRIPTION_AND_IMPORTANCE}

**Suggested Test Cases**:
```{LANGUAGE}
// Test case for {FUNCTIONALITY}
{SUGGESTED_TEST_CODE}
```

## 6. Recommendations & Action Items

### 6.1 Critical Issues (Must Fix) 🔴

- [ ] **{CRITICAL_ISSUE_1}**
  - Priority: High
  - Estimated Effort: {EFFORT_ESTIMATE}
  - Impact: {IMPACT_DESCRIPTION}
  - Solution: {SOLUTION_STEPS}

- [ ] **{CRITICAL_ISSUE_2}**
  - Priority: High
  - Estimated Effort: {EFFORT_ESTIMATE}
  - Impact: {IMPACT_DESCRIPTION}
  - Solution: {SOLUTION_STEPS}

### 6.2 Improvement Opportunities (Should Fix) 🟡

- [ ] **{IMPROVEMENT_1}**
  - Priority: Medium
  - Estimated Effort: {EFFORT_ESTIMATE}
  - Benefit: {BENEFIT_DESCRIPTION}
  - Implementation: {IMPLEMENTATION_STEPS}

- [ ] **{IMPROVEMENT_2}**
  - Priority: Medium
  - Estimated Effort: {EFFORT_ESTIMATE}
  - Benefit: {BENEFIT_DESCRIPTION}
  - Implementation: {IMPLEMENTATION_STEPS}

### 6.3 Code Refactoring Plan

#### 6.3.1 Duplicate Code Elimination

**Phase 1**: Extract common utilities
- [ ] Create shared utility class for {FUNCTIONALITY}
- [ ] Refactor {FILE_LIST} to use shared utility
- [ ] Update tests to cover shared utility

**Phase 2**: Consolidate similar components
- [ ] Merge similar components: {COMPONENT_LIST}
- [ ] Create base component with configurable behavior
- [ ] Update all usages to use consolidated component

#### 6.3.2 Feature Integration Improvements

**Navigation Fixes**:
- [ ] Implement missing navigation: {SCREEN_A} → {SCREEN_B}
- [ ] Add data passing for: {DATA_FLOW_DESCRIPTION}
- [ ] Update navigation stack management

**Data Synchronization**:
- [ ] Implement consistent data models across features
- [ ] Add data validation at integration points
- [ ] Create shared state management for cross-feature data

## 7. Review Summary

### 7.1 Overall Assessment

**Code Quality Score**: {OVERALL_SCORE}/100

**Breakdown**:
- Architecture & Design: {ARCHITECTURE_SCORE}/25
- Code Quality: {CODE_QUALITY_SCORE}/25
- Feature Integration: {INTEGRATION_SCORE}/25
- Testing & Security: {TESTING_SECURITY_SCORE}/25

### 7.2 Approval Status

**Review Status**: {REVIEW_STATUS}
- ✅ **Approved**: Ready for production
- ⚠️ **Approved with Conditions**: Address medium issues
- ❌ **Rejected**: Critical issues must be fixed

**Next Steps**:
1. {NEXT_STEP_1}
2. {NEXT_STEP_2}
3. {NEXT_STEP_3}

### 7.3 Follow-up Review

**Schedule Next Review**: {NEXT_REVIEW_DATE}
**Focus Areas**: {FOCUS_AREAS}
**Success Criteria**: {SUCCESS_CRITERIA}

---

**Review Generated**: {TIMESTAMP}  
**AI Review System Version**: {AI_VERSION}  
**Git Commit**: {CURRENT_COMMIT_HASH}