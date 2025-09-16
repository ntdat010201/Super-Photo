# Absolute Rules - Non-Negotiable Development Standards

> **🚫 ZERO TOLERANCE POLICIES**  
> These rules are ABSOLUTE and CANNOT be violated under any circumstances

## Rule Classification

**Enforcement Level**: ABSOLUTE (No exceptions, no workarounds)  
**Violation Consequence**: Immediate task termination and restart  
**Override Authority**: None (System-level enforcement)  
**Monitoring**: Real-time validation with automated blocking

---

## 🚫 ABSOLUTE RULE #1: NO PARTIAL IMPLEMENTATION

### Definition
**NEVER** leave code in a partially implemented state. Every feature, function, or component MUST be fully functional before task completion.

### Enforcement Criteria
```markdown
❌ **VIOLATIONS**:
- Functions with TODO comments in production code
- Incomplete error handling
- Missing required parameters
- Placeholder implementations
- Commented-out critical code sections
- Half-implemented features
- Missing return statements
- Incomplete validation logic

✅ **COMPLIANCE**:
- All functions have complete implementations
- Comprehensive error handling
- All code paths tested and functional
- No TODO/FIXME in production code
- Complete feature functionality
- Proper input validation
- Appropriate return values
```

### Validation Protocol
```bash
# Automated checks before task completion
grep -r "TODO\|FIXME\|HACK\|XXX" --include="*.{js,ts,py,java,kt,swift}" .
grep -r "throw new Error(\"Not implemented\"\|NotImplementedError\|fatalError" .
grep -r "console\.log\|print\|Log\.d" --include="*.{js,ts,py,java,kt,swift}" .
```

### Exception Handling
**NO EXCEPTIONS ALLOWED**  
If implementation cannot be completed:
1. **STOP** current task immediately
2. **DOCUMENT** blocking issues
3. **REQUEST** additional requirements
4. **NEVER** submit partial work

---

## 🚫 ABSOLUTE RULE #2: NO CODE DUPLICATION

### Definition
**NEVER** duplicate code logic. Any repeated code patterns MUST be extracted into reusable functions, classes, or modules.

### Duplication Thresholds
```markdown
🔴 **ZERO TOLERANCE** (Immediate violation):
- Identical functions (>3 lines)
- Copy-pasted code blocks
- Repeated business logic
- Duplicate validation patterns
- Identical error handling

🟡 **ACCEPTABLE REPETITION**:
- Different variable names for same concept
- Platform-specific implementations
- Configuration constants
- Simple getters/setters
- Test data setup (with shared utilities)
```

### DRY Enforcement Strategies
```markdown
✅ **REQUIRED PATTERNS**:
- Extract common functions
- Create utility classes
- Use inheritance/composition
- Implement design patterns
- Create shared constants
- Build reusable components
- Establish common interfaces
```

### Detection Methods
```bash
# Automated duplication detection
jscpd --min-lines 3 --min-tokens 50 .
sonar-scanner -Dsonar.projectKey=project -Dsonar.sources=.
pylint --disable=all --enable=duplicate-code .
```

### Refactoring Requirements
When duplication is detected:
1. **IMMEDIATE** refactoring required
2. **EXTRACT** common functionality
3. **TEST** refactored code thoroughly
4. **DOCUMENT** new shared components
5. **UPDATE** all usage points

---

## 🚫 ABSOLUTE RULE #3: NO DEAD CODE

### Definition
**NEVER** leave unused, unreachable, or obsolete code in the codebase. All code MUST serve a purpose and be actively used.

### Dead Code Categories
```markdown
❌ **IMMEDIATE REMOVAL REQUIRED**:
- Unused functions/methods
- Unreachable code blocks
- Commented-out code sections
- Unused imports/dependencies
- Obsolete configuration
- Dead variables/constants
- Unused CSS classes
- Orphaned files
- Deprecated API calls
- Unused database tables/columns
```

### Detection Protocol
```bash
# Automated dead code detection
# JavaScript/TypeScript
npx ts-unused-exports tsconfig.json
npx depcheck

# Python
vulture . --min-confidence 80

# Java/Kotlin
./gradlew unusedCode

# Swift
periphery scan --workspace MyApp.xcworkspace
```

### Cleanup Requirements
```markdown
✅ **MANDATORY ACTIONS**:
1. Remove unused imports
2. Delete unreachable code
3. Clean up commented code
4. Remove obsolete functions
5. Update documentation
6. Clean dependency lists
7. Remove unused assets
8. Archive obsolete files
```

### Code Retention Policy
**ZERO RETENTION** for:
- Code unused for >30 days
- Commented code without explanation
- Experimental code not in use
- Legacy code with modern alternatives

**DOCUMENTATION REQUIRED** for:
- Temporarily disabled features
- Platform-specific conditional code
- Future implementation placeholders

---

## 🚫 ABSOLUTE RULE #4: NO TEST CHEATING

### Definition
**NEVER** write tests that don't actually validate the intended functionality. All tests MUST provide genuine verification of code behavior.

### Test Cheating Violations
```markdown
❌ **FORBIDDEN PRACTICES**:
- Tests that always pass (hardcoded true)
- Mocking everything without real validation
- Tests that don't assert anything meaningful
- Copy-paste tests without adaptation
- Tests that test the test framework
- Overly permissive assertions
- Tests that ignore error conditions
- Fake test data that doesn't represent reality
- Tests that bypass actual business logic
- Assertions that can never fail
```

### Genuine Testing Requirements
```markdown
✅ **MANDATORY TEST QUALITIES**:
- Tests actual business logic
- Validates edge cases
- Checks error conditions
- Uses realistic test data
- Asserts specific outcomes
- Tests integration points
- Validates performance requirements
- Checks security constraints
- Tests user workflows
- Validates data integrity
```

### Test Quality Validation
```bash
# Test quality checks
# Mutation testing
npx stryker run
python -m mutmut run

# Coverage analysis
npx nyc --reporter=text --reporter=html npm test
coverage run -m pytest && coverage report

# Test smell detection
npx test-smells-detector
pylint tests/ --disable=all --enable=test-smells
```

### Test Integrity Standards
```markdown
🎯 **QUALITY METRICS**:
- Code coverage: >90%
- Branch coverage: >85%
- Mutation score: >80%
- Test-to-code ratio: 1:1 minimum
- Zero flaky tests
- Zero ignored tests
- All tests run in <5 minutes
- Tests are deterministic
```

### Test Categories Required
```markdown
✅ **COMPREHENSIVE TESTING**:
1. **Unit Tests**: Individual function validation
2. **Integration Tests**: Component interaction
3. **End-to-End Tests**: User workflow validation
4. **Performance Tests**: Speed and resource usage
5. **Security Tests**: Vulnerability validation
6. **Error Tests**: Failure scenario handling
7. **Edge Case Tests**: Boundary condition validation
8. **Regression Tests**: Previous bug prevention
```

---

## Enforcement Mechanisms

### Automated Validation

**Pre-Commit Hooks**:
```bash
#!/bin/bash
# .git/hooks/pre-commit

# Rule #1: Check for partial implementation
if grep -r "TODO\|FIXME\|NotImplemented" --include="*.{js,ts,py,java,kt,swift}" .; then
    echo "❌ ABSOLUTE RULE VIOLATION: Partial implementation detected"
    exit 1
fi

# Rule #2: Check for code duplication
if jscpd --min-lines 3 --threshold 5 .; then
    echo "❌ ABSOLUTE RULE VIOLATION: Code duplication detected"
    exit 1
fi

# Rule #3: Check for dead code
if vulture . --min-confidence 80 | grep -v "# unused"; then
    echo "❌ ABSOLUTE RULE VIOLATION: Dead code detected"
    exit 1
fi

# Rule #4: Check test quality
if npm run test:coverage | grep -E "(Statements|Branches|Functions|Lines).*[0-8][0-9]%"; then
    echo "❌ ABSOLUTE RULE VIOLATION: Insufficient test coverage"
    exit 1
fi
```

**CI/CD Pipeline Validation**:
```yaml
# .github/workflows/absolute-rules.yml
name: Absolute Rules Enforcement

on: [push, pull_request]

jobs:
  absolute-rules:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Rule #1 - No Partial Implementation
        run: |
          if grep -r "TODO\|FIXME" --include="*.{js,ts,py}" .; then
            echo "::error::Partial implementation detected"
            exit 1
          fi
      
      - name: Rule #2 - No Code Duplication
        run: |
          npx jscpd --threshold 5 .
      
      - name: Rule #3 - No Dead Code
        run: |
          npx ts-unused-exports tsconfig.json
          npx depcheck
      
      - name: Rule #4 - No Test Cheating
        run: |
          npm run test:coverage
          npx stryker run
```

### Manual Review Checklist

**Code Review Requirements**:
```markdown
☐ **Rule #1 Verification**:
  ☐ All functions fully implemented
  ☐ No TODO/FIXME comments
  ☐ Complete error handling
  ☐ All code paths functional

☐ **Rule #2 Verification**:
  ☐ No duplicate code blocks
  ☐ Common logic extracted
  ☐ Reusable components used
  ☐ DRY principles followed

☐ **Rule #3 Verification**:
  ☐ No unused imports
  ☐ No commented code
  ☐ No unreachable code
  ☐ All functions used

☐ **Rule #4 Verification**:
  ☐ Tests validate real functionality
  ☐ Meaningful assertions
  ☐ Edge cases covered
  ☐ Error conditions tested
```

### Violation Response Protocol

**Immediate Actions**:
1. **STOP** all development work
2. **IDENTIFY** specific violation
3. **FIX** violation completely
4. **VALIDATE** fix with automated tools
5. **DOCUMENT** resolution
6. **RESUME** development work

**Escalation Process**:
- **Level 1**: Automated tool detection
- **Level 2**: Peer review identification
- **Level 3**: Quality assurance validation
- **Level 4**: Architecture review

### Metrics & Monitoring

**Violation Tracking**:
```markdown
📊 **ABSOLUTE RULE METRICS**:
- Violation count per rule per sprint
- Time to resolution per violation
- Repeat violation patterns
- Team compliance scores
- Automated detection accuracy
- Manual review effectiveness
```

**Quality Gates**:
- **Zero violations** required for production deployment
- **100% compliance** required for code merge
- **Automated validation** must pass before manual review
- **Continuous monitoring** in production environment

---

## Integration with .god Ecosystem

### Sub-Agent Integration

**Bug Hunter Coordination**:
- Absolute rule violations treated as critical bugs
- Automated violation detection triggers bug analysis
- Root cause analysis for repeated violations

**Test Executor Integration**:
- Absolute rule validation integrated into test execution
- Quality gates enforce rule compliance
- Test failure on rule violations

**Security Auditor Collaboration**:
- Security implications of rule violations
- Dead code security risk assessment
- Test cheating security vulnerability analysis

### Workflow Integration

**TSDDR 2.0 Compliance**:
- Absolute rules enforced in Test phase
- Design phase includes rule compliance planning
- Review phase validates rule adherence

**Kiro Workflow Integration**:
- Task completion blocked by rule violations
- Progress tracking includes rule compliance
- Quality metrics include absolute rule scores

---

**Enforcement**: Automated + Manual (Zero Tolerance)  
**Monitoring**: Real-time + Continuous  
**Integration**: All .god workflows and sub-agents  
**Override**: None (System-level enforcement)