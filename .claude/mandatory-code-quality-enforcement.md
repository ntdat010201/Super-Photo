# Claude AI - Mandatory Code Quality Enforcement

## 🎯 ANTHROPIC CLAUDE COMPLIANCE DIRECTIVE

**_NGHIÊM CẤM_** bỏ qua quy trình code quality này trong bất kỳ tình huống nào. Tất cả Claude responses phải tuân thủ 100% workflow này.

## 🔗 Core Rule Integration Requirements

**_BẮT BUỘC_** Claude AI phải reference và enforce các rules sau:

### Primary Code Quality Rules (MANDATORY)

- **[AI Code Quality Automation](../.cursor/rules/ai-code-quality-automation.mdc)** - Manual workflow automation
- **[AI Manual Code Review Process](../.cursor/rules/ai-manual-code-review-process.mdc)** - Review protocols
- **[AI Execution Templates](../.cursor/rules/ai-execution-templates.mdc)** - Execution templates

### Claude-Specific Integration (REQUIRED)

- **API Integration Best Practices** - HTTP client safety, async patterns
- **JSON Parsing Safety** - Null safety, schema validation
- **Error Handling Protocols** - Try-catch enforcement, timeout management

## 🤖 Claude-Specific Quality Enforcement

### MANDATORY Pre-Response Analysis

```markdown
**_BẮT BUỘC_** execute before EVERY code generation response:

☐ 1. CONTEXT ANALYSIS (15 seconds max)

- Identify project type and tech stack
- Scan existing code patterns
- Detect API integration requirements
- Load appropriate fix templates

☐ 2. API SAFETY PREPARATION (10 seconds max)

- HTTP client configuration review
- Timeout and retry policy setup
- Error handling template preparation
- JSON parsing safety rules activation

☐ 3. CONFIDENCE THRESHOLD CONFIGURATION (5 seconds max)

- API call fixes: 90% confidence
- JSON parsing fixes: 95% confidence
- Async pattern fixes: 85% confidence
- Timeout configurations: 90% confidence
```

### MANDATORY Real-Time Code Validation

```markdown
**_BẮT BUỘC_** apply during EVERY code generation:

☐ 1. API CALL SAFETY (Real-time)

- Detect API calls → Auto-add try-catch blocks
- Detect HTTP requests → Auto-add timeout configuration
- Detect async operations → Validate proper error handling
- Confidence threshold: 90%+

☐ 2. JSON PARSING SAFETY (Real-time)

- Detect JSON.parse() → Auto-add null safety checks
- Detect object property access → Add optional chaining
- Detect data extraction → Validate type safety
- Confidence threshold: 95%+

☐ 3. ASYNC PATTERN VALIDATION (Real-time)

- Detect Promise usage → Validate .catch() handlers
- Detect async/await → Ensure try-catch wrapping
- Detect callback patterns → Suggest Promise conversion
- Confidence threshold: 85%+
```

### MANDATORY Post-Code Quality Assurance

```markdown
**_BẮT BUỘC_** execute after EVERY code completion:

☐ 1. API INTEGRATION FINAL CHECK (20 seconds max)

- Verify all API calls have error handling
- Confirm timeout configurations present
- Validate response data handling
- Check network permission declarations

☐ 2. ASYNC PATTERN COMPLIANCE (15 seconds max)

- Scan for unhandled promise rejections
- Verify proper async error propagation
- Validate concurrent operation safety
- Check resource cleanup procedures

☐ 3. SECURITY VALIDATION (10 seconds max)

- Scan for API key exposure
- Verify HTTPS usage for external calls
- Check for sensitive data logging
- Validate input sanitization
```

## 🚨 Claude Critical Enforcement Rules

### Zero Tolerance Issues (BLOCK RESPONSE)

```markdown
**_NGHIÊM CẤM_** provide code with:

❌ Unhandled API calls (missing try-catch)
❌ Missing timeout configurations for HTTP requests
❌ Unsafe JSON parsing (no null checks)
❌ Exposed API keys or sensitive credentials
❌ HTTP usage for sensitive operations

ACTION: Must auto-fix with 90%+ confidence or REFUSE to generate code
```

### High Priority Issues (FIX BEFORE RESPONSE)

```markdown
**_BẮT BUỘC_** resolve before providing code:

⚠️ Incomplete error handling in async operations
⚠️ Missing data validation for API responses
⚠️ Inefficient API call patterns
⚠️ Insecure communication protocols
⚠️ Resource leak potential in async operations

ACTION: Auto-fix with 85%+ confidence or FLAG with explanation
```

## 📊 Claude Quality Metrics & Monitoring

### API Integration Metrics (MANDATORY)

```markdown
✅ API call error handling coverage: > 95%
✅ Timeout configuration compliance: > 90%
✅ JSON parsing safety rate: > 98%
✅ Async error handling coverage: > 85%
✅ Security vulnerability prevention: > 99%
```

### Performance Requirements

```markdown
📈 Response generation time: < 30 seconds
📈 Quality check execution time: < 45 seconds
📈 Auto-fix application rate: > 90%
📈 False positive rate: < 3%
📈 Security issue detection rate: > 95%
```

### Claude-Specific Compliance Tracking

```markdown
🤖 API safety pattern application: > 95%
🤖 JSON parsing robustness: > 98%
🤖 Async error handling: > 85%
🤖 Security best practices: > 99%
🤖 Code quality improvement rate: > 90%
```

## 🔄 Claude Quality Monitoring System

### Real-Time Response Quality Gates

```json
{
  "response_quality_gates": {
    "api_safety_check": {
      "enabled": true,
      "strictness": "high",
      "auto_fix": true,
      "confidence_threshold": 0.9
    },
    "json_safety_validation": {
      "enabled": true,
      "strictness": "very_high",
      "auto_fix": true,
      "confidence_threshold": 0.95
    },
    "async_pattern_enforcement": {
      "enabled": true,
      "strictness": "high",
      "auto_fix": true,
      "confidence_threshold": 0.85
    },
    "security_vulnerability_scan": {
      "enabled": true,
      "strictness": "maximum",
      "auto_fix": false,
      "block_on_detection": true
    }
  }
}
```

### Escalation Matrix

```yaml
claude_escalation_procedures:
  critical_security_issues:
    action: "block_response_generation"
    notification: "immediate_flag_to_user"
    manual_intervention: "required"

  api_safety_violations:
    action: "auto_fix_with_high_confidence"
    fallback: "refuse_unsafe_code_generation"
    user_education: "provide_safe_alternatives"

  pattern_compliance_issues:
    action: "auto_fix_with_explanation"
    documentation: "include_best_practice_guidance"
    follow_up: "suggest_improvements"
```

## 🎯 Claude Integration Templates

### API Safety Auto-Fix Templates

```javascript
// CLAUDE AUTO-FIX: API Call Safety
// BEFORE (UNSAFE):
fetch("/api/data").then((response) => response.json());

// AFTER (SAFE - Auto-applied):
try {
  const response = await fetch("/api/data", {
    timeout: 10000,
    headers: { "Content-Type": "application/json" },
  });

  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`);
  }

  const data = await response.json();
  return data;
} catch (error) {
  console.error("API call failed:", error);
  throw error;
}
```

### JSON Parsing Safety Templates

```javascript
// CLAUDE AUTO-FIX: JSON Parsing Safety
// BEFORE (UNSAFE):
const userData = JSON.parse(responseText);
const userName = userData.name;

// AFTER (SAFE - Auto-applied):
try {
  const userData = JSON.parse(responseText);
  const userName = userData?.name ?? "Unknown User";

  // Validate critical fields
  if (!userData || typeof userData !== "object") {
    throw new Error("Invalid user data format");
  }

  return { userName, userData };
} catch (error) {
  console.error("JSON parsing failed:", error);
  return { userName: "Unknown User", userData: null };
}
```

### Async Pattern Enhancement Templates

```javascript
// CLAUDE AUTO-FIX: Async Pattern Safety
// BEFORE (RISKY):
async function processData() {
  const result = await apiCall();
  return result.data;
}

// AFTER (SAFE - Auto-applied):
async function processData() {
  try {
    const result = await apiCall();

    // Validate response structure
    if (!result || !result.data) {
      throw new Error("Invalid API response structure");
    }

    return result.data;
  } catch (error) {
    console.error("Data processing failed:", error);

    // Implement graceful fallback
    return null; // or appropriate default value
  }
}
```

## 🚀 Claude Implementation Guidelines

### Response Generation Protocol

```markdown
1. **PRE-ANALYSIS** (MANDATORY)

   - Execute quality workflow analysis
   - Load appropriate safety templates
   - Configure confidence thresholds

2. **CODE GENERATION** (WITH REAL-TIME VALIDATION)

   - Apply safety patterns as code is generated
   - Ensure error handling for all risky operations
   - Include appropriate timeouts and validations

3. **POST-VALIDATION** (MANDATORY)

   - Final safety and security scan
   - Compliance verification
   - User education inclusion

4. **RESPONSE DELIVERY** (ENHANCED)
   - Include quality assurance summary
   - Highlight applied safety improvements
   - Provide additional best practice guidance
```

### User Education Integration

```markdown
Claude responses MUST include:

📚 **Quality Improvements Applied:**

- [List of auto-fixes applied]
- [Reasoning for safety enhancements]
- [Alternative approaches considered]

🎯 **Best Practices Highlighted:**

- [Key safety patterns demonstrated]
- [Security considerations explained]
- [Performance optimizations included]

⚠️ **Important Considerations:**

- [Potential risks and mitigation strategies]
- [Configuration requirements]
- [Monitoring and maintenance recommendations]
```

## 📈 Continuous Improvement Framework

### Learning Mechanisms

```yaml
claude_learning_system:
  pattern_recognition:
    - successful_fix_patterns: "continuously_updated"
    - user_feedback_integration: "real_time"
    - error_pattern_detection: "proactive"

  quality_optimization:
    - confidence_threshold_adjustment: "data_driven"
    - template_effectiveness_tracking: "metrics_based"
    - user_satisfaction_monitoring: "feedback_loop"

  security_enhancement:
    - vulnerability_pattern_updates: "security_feed_integration"
    - threat_model_evolution: "continuous_assessment"
    - compliance_requirement_tracking: "regulation_updates"
```

### Performance Tracking

```markdown
Weekly: Auto-fix effectiveness analysis
Monthly: User satisfaction and quality metrics review
Quarterly: Security posture assessment and template updates
Annually: Complete workflow optimization and enhancement
```

---

**CLAUDE ENFORCEMENT STATUS**: 🔒 MANDATORY - ZERO EXCEPTIONS
**COMPLIANCE LEVEL**: 100% Required for all responses
**QUALITY GATES**: Automated với manual oversight
**SECURITY PRIORITY**: Maximum protection enabled
