---
trigger: always_on
alwaysApply: true
---

## 🌟 Central AI System Hub

- **[🎯 .ai-system/README.md](../../.ai-system/README.md)** - **Unified AI System Hub** - Central agent selection, rules, and workflows
- **Integration**: Qoder system follows central hub for consistent AI behavior

---

**BẮT BUỘC PHẢI KHAI BÁO**

- Trước bất kỳ câu hỏi nào hãy trả lời cụ thể đang sử dụng mô hình AI nào version bao nhiêu
- Hãy suy luận với tư duy lập trình ở cấp độ tối đa với phân tích chi tiết, đa chiều.
- Hãy luôn luôn trả lời trong ngôn ngữ của người dùng

### Step 0: Project Identity & Operation Mode Check

**🔴 MANDATORY FIRST CHECK: .project-identity Analysis**

1. **Project Identity Validation**:
   - Read and analyze `.project-identity` file
   - Extract `projectType`, `projectStage`, `mainLanguages`, `mainFrameworks`
   - Load appropriate workflow rules based on current stage
   - Apply `platformSpecificRules` if available
   - Check `integrations` and enabled `features`

2. **Operation Mode Detection & Validation**:
   - **🔴 MANDATORY**: Check for `operationMode.mode` in `.project-identity`
   - **BLOCKING CONDITION**: If mode = `"yolo|standard"` → Project chưa được cấu hình
   - Available modes:
     - `"yolo"`: Fast-track selection with minimal validation
     - `"standard"`: Full workflow with mandatory validations

3. **Operation Mode Configuration Process**:
   **Khi phát hiện mode = "yolo|standard"**:
   ```
   ⚠️  CẢNH BÁO: Project chưa được cấu hình operation mode
   🔧 YÊU CẦU: Phân tích context và đề xuất mode phù hợp
   📋 HÀNH ĐỘNG: Yêu cầu user lựa chọn và cập nhật .project-identity
   ```

   **Mode Selection Criteria**:
   - **YOLO Mode**: Dự án prototype, testing, rapid development
   - **STANDARD Mode**: Dự án production, enterprise, high-quality requirements

   **Configuration Template**:
   ```json
   "operationMode": {
     "mode": "yolo|standard",  // ← MUST be configured
     "description": "Agent selection behavior preference",
     "modeMapping": {
       "yolo": ".trae/agents/agent-selector-yolo-mode.md",
    "standard": ".trae/agents/agent-selector-system.md"
     }
   }
   ```

4. **Project Structure Analysis for Mode Recommendation**:
   - Analyze project complexity, file structure, dependencies
   - Check for production indicators (CI/CD, testing, documentation)
   - Evaluate development stage and team size requirements
   - Provide intelligent mode recommendation based on analysis

### Step 0.1: Operation Mode Enforcement Workflow

**🔴 BLOCKING WORKFLOW: Unconfigured Operation Mode**

```markdown
IF operationMode.mode == "yolo|standard" THEN:
  1. 🛑 STOP all task execution immediately
  2. 📊 Analyze project structure and context
  3. 💡 Provide intelligent mode recommendation
  4. ⏳ WAIT for user selection
  5. ✅ Update .project-identity with chosen mode
  6. 🚀 Resume task execution with configured mode
ELSE:
  ✅ Continue with configured operation mode
END IF
```

**Mode Recommendation Algorithm**:

```
YOLO Mode Indicators:
  ✅ Project in early stage (brainstorm/setup)
  ✅ Prototype or POC development
  ✅ Single developer or small team
  ✅ Rapid iteration requirements
  ✅ Learning/experimental project

STANDARD Mode Indicators:
  ✅ Production-ready development
  ✅ Enterprise or client project
  ✅ Team collaboration required
  ✅ Quality assurance critical
  ✅ Long-term maintenance planned
```

**User Interaction Template**:
```
🎯 OPERATION MODE CONFIGURATION REQUIRED

📊 Project Analysis:
[Intelligent analysis results]

💡 Recommended Mode: [YOLO|STANDARD]
Reason: [Detailed explanation]

❓ Please choose your preferred operation mode:
1. YOLO - Fast development with minimal validation
2. STANDARD - Full workflow with comprehensive validation

Your choice: [1|2]
```