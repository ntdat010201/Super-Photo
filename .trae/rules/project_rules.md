# Trae AI Project Rules - Smart Agent Selector

**BẮT BUỘC PHẢI KHAI BÁO**

- Trước bất kỳ câu hỏi nào hãy trả lời cụ thể đang sử dụng mô hình AI nào version bao nhiêu
- Hãy suy luận với tư duy lập trình ở cấp độ tối đa với phân tích chi tiết, đa chiều
- Hãy luôn luôn trả lời trong ngôn ngữ của người dùng
- Khi kết thúc 1 nhiệm vụ bạn sẽ vào history để bổ sung thông tin về nhiệm vụ và tiến độ sau đó bạn vào .project-identity cập nhật lại trạng thái project

**Project Rules**

- Khi người dùng đính kèm hình ảnh thì mặc định ưu tiên phân tích hình ảnh kết hợp với nhiệm vụ hiện tại
- Khi người dùng đính kèm hình ảnh nhưng không có context cụ thể hoặc yêu cầu cụ thể thì kích hoạt quy trình Design to Prompt

> **🎯 Smart Agent Selection & Task Management System**  
> Right agent, right task, right time through intelligent selection with .god integration

## 🔴 MANDATORY PROJECT IDENTITY CHECK

**BẮT BUỘC: Kiểm tra .project-identity trước mọi task**

### 📝 PROJECT IDENTITY OPTIMIZATION RULES

**🎯 Template vs Real Project Configuration**

**Template Rules (.project-identity as template)**:

- Là mô tả dự án có cấu trúc, thư viện, tính năng, màu sắc
- ✅ Có thể mô tả nhiều platform và ngôn ngữ
- ✅ Có thể dài hơn 300 dòng để làm reference

**Real Project Rules (khi setup dự án thực tế)**:

- 🔴 **MANDATORY**: Phải tối ưu xuống dưới 300 dòng
- 🔴 **MANDATORY**: Chỉ giữ lại thông tin liên quan đến platform cụ thể
- 🔴 **MANDATORY**: Compact format - tránh xuống dòng không cần thiết

**Compact Format Examples**:

```json
// ❌ Template format (dài dòng):
"backend": [
  "api",
  "server",
  "database",
  "microservices"
]

// ✅ Real project format (compact):
"backend": "api,server,database,microservices"

// ❌ Template format:
"mainLanguages": [
  "kotlin",
  "java"
]

// ✅ Real project format:
"mainLanguages": "kotlin,java"
```

**Platform-Specific Optimization**:

- **Android Project**: Chỉ giữ android-related configs, xóa ios/web/backend sections
- **iOS Project**: Chỉ giữ ios-related configs, xóa android/web/backend sections
- **Web Project**: Chỉ giữ frontend-related configs, xóa mobile sections
- **Backend Project**: Chỉ giữ backend-related configs, xóa frontend/mobile sections

**Mandatory Cleanup Checklist**:

```markdown
☐ Xóa unused platform sections
☐ Convert arrays thành comma-separated strings
☐ Xóa example/template comments
☐ Giữ chỉ essential workflow rules
☐ Compact JSON format (no unnecessary line breaks)
☐ Validate < 300 lines total
☐ Keep only active integrations
☐ Remove unused agent configurations
```

**Auto-Optimization Triggers**:

- Khi user mention "setup project" hoặc "configure project"
- Khi projectStage chuyển từ "template" sang "setup"
- Khi detect specific platform (android/ios/web/backend)
- Khi .project-identity > 300 lines trong real project

### Pre-Task Analysis (MANDATORY)

```markdown
☐ Đọc và phân tích .project-identity file
☐ Xác định projectType, projectStage, mainLanguages, mainFrameworks
☐ Load workflow rules phù hợp với giai đoạn hiện tại
☐ Áp dụng platformSpecificRules nếu có
☐ Kiểm tra integrations và features được bật
```

### Project Stage Workflow Loading

- **stage1_brainstorm**: Load `.ai-system/workflows/planning/kiro-spec-driven-workflow.md`
- **stage2_setup**: Load `.ai-system/workflows/development/task-management.md`
- **stage3_development**: Load platform-specific rules based on projectType

### New Project Detection

**Triggers**: Empty project folder, no .project-identity, user mentions "ý tưởng mới"
**Action**: Force `stage1_brainstorm` with blocking message

### Stage Progression Rules

- ❌ Không được phép bỏ qua giai đoạn
- ✅ Yêu cầu xác nhận hoàn thành trước khi chuyển giai đoạn
- 🔄 Tự động cập nhật projectStage trong .project-identity
- ❌ Trong file .md chỉ là cấu trúc và instruction, không có code

## 🏗️ CLEAN ARCHITECTURE CODE BASE STAGE - CRITICAL RULES

**🔴 GIAI ĐOẠN BẮT BUỘC: Clean Architecture Code Base Creation**

### Workflow Sequence (KHÔNG ĐƯỢC BỎ QUA)

```
Brainstorm → Kiro Tasks → Clean Architecture Code Base → Detailed Implementation
```

### Clean Architecture Code Base Rules (NGHIÊM NGẶT)

**🚫 TUYỆT ĐỐI KHÔNG ĐƯỢC PHÉP:**

- ❌ Code chi tiết tính năng trong giai đoạn code base
- ❌ Implement business logic cụ thể
- ❌ Tạo UI components với nội dung thực tế
- ❌ Viết API endpoints với logic xử lý
- ❌ Implement database operations chi tiết
- ❌ Tạo test cases cho tính năng cụ thể
- ❌ Bỏ qua việc tạo interfaces/abstractions

**✅ CHỈ ĐƯỢC PHÉP TẠO:**

- ✅ Folder structure theo Clean Architecture
- ✅ Empty classes/interfaces với comments mô tả
- ✅ Abstract base classes và contracts
- ✅ Dependency injection setup (empty)
- ✅ Navigation structure (empty screens)
- ✅ Model classes với properties (no logic)
- ✅ Repository interfaces (method signatures only)
- ✅ Use case classes (empty methods với TODO comments)
- ✅ ViewModel/Presenter base classes (empty)
- ✅ Configuration files và constants

### Validation Checklist (MANDATORY)

**Trước khi chuyển sang detailed implementation:**

```markdown
☐ Tất cả classes chỉ có structure, không có implementation
☐ Tất cả methods chỉ có signature, body là TODO hoặc throw NotImplementedException
☐ Folder structure tuân thủ Clean Architecture layers
☐ Dependency injection container được setup (empty)
☐ Navigation routes được định nghĩa (empty destinations)
☐ Model classes chỉ có properties, không có business logic
☐ Repository interfaces được tạo với method signatures
☐ Use cases được tạo với empty methods
☐ ViewModels/Presenters chỉ có structure
☐ Configuration và constants được định nghĩa
```

### AI Violation Prevention

**🔴 CRITICAL BLOCKING RULES:**

- **Auto-Stop Trigger**: Nếu AI bắt đầu implement chi tiết → DỪNG NGAY LẬP TỨC
- **Code Review Check**: Mọi code phải pass qua validation "Is this just structure?"
- **Method Body Rule**: Tất cả method bodies phải là TODO, empty, hoặc throw exception
- **Business Logic Detection**: Scan code để phát hiện business logic → REJECT
- **UI Content Check**: UI components chỉ được có placeholder content

### Stage Transition Control

**Từ Clean Architecture Code Base → Detailed Implementation:**

```markdown
🔴 MANDATORY CONFIRMATION:
"Xác nhận code base đã hoàn thành và chỉ chứa structure, không có implementation chi tiết?"

✅ CHỈ KHI USER XÁC NHẬN: "Có, code base chỉ có structure"
❌ KHÔNG ĐƯỢC TỰ Ý chuyển giai đoạn
```

### Error Recovery Protocol

**Nếu AI vi phạm và code chi tiết:**

1. **Immediate Stop**: Dừng ngay khi phát hiện
2. **Rollback**: Xóa code chi tiết đã tạo
3. **Reset**: Quay lại chế độ "structure only"
4. **Warning**: Thông báo vi phạm cho user
5. **Restart**: Bắt đầu lại từ structure creation

## General Rules

- **Clean Code**: Established conventions, readable structure
- **Testing**: Unit, integration, end-to-end coverage
- **Security**: Input validation, data protection, secure practices
- **Performance**: Optimized algorithms, efficient resource usage
- **Modular Design**: Loosely coupled, highly cohesive
- **Version Control**: Git workflow, meaningful commits

## Hybrid Workflow Selection System

**Automatic .god Workflow Triggers**:

- User mentions "spec", "specification", "PRD", "requirements", "design document"
- User requests "task creation", "break down", "planning", "architecture"
- Complex feature implementation requiring structured approach

**Prefer .trae Workflow**:

- Simple code fixes, debugging, optimization
- Direct implementation with clear requirements
- Platform-specific technical issues
- Quick questions or clarifications

**Hybrid Approach**: .god for planning/specs → .trae for execution

## Agent Selection System

**Multi-Factor Scoring**:

1. **Workflow Context Analysis** (40%): .god vs .trae workflow suitability
2. **Tech Stack & Complexity** (30%): Technology keywords, task difficulty
3. **Kiro Priority Scoring** (15%): Spec/planning keyword detection
4. **Performance History** (10%): Success rates, quality metrics
5. **User Preferences** (5%): Historical selections, feedback

**Selection Process**: Project Identity Check → Workflow Selection → Context Analysis → Agent Scoring → Decision → Assignment

**Decision Thresholds**:

- **High Confidence** (>85%): Direct assignment
- **Medium Confidence** (70-85%): Assignment with monitoring
- **Low Confidence** (<70%): User confirmation

**YOLO Mode**: Fast-track selection with 60% confidence threshold

## Available Agents

### 📱 iOS Development Agent

**Keywords**: swift, swiftui, ios, xcode, uikit, core data  
**Workflows**: → [.ai-system/rules/platforms/ios-workflow.md](../../.ai-system/rules/platforms/ios-workflow.md)

### 🤖 Android Development Agent

**Keywords**: kotlin, android, jetpack compose, gradle, room  
**Workflows**: → [.ai-system/rules/platforms/android-workflow.md](../../.ai-system/rules/platforms/android-workflow.md) | [TSDDR 2.0 Guide](../../docs/TSDDR-2.0-Guide.md)

### 🔧 APK Modification Agent

**Keywords**: apk, reverse engineering, firebase, safeads, smali, mod app, modification  
**Workflows**: → [.ai-system/rules/platforms/apk-modification-workflow.md](../../.ai-system/rules/platforms/apk-modification-workflow.md)
**Nguyên tắc tối quan trọng**: Không bao giờ được sao chép hàng loạt resource hay code từ APK Decompiled

### 🌐 Frontend Development Agent

**Keywords**: react, vue, angular, typescript, tailwind, nextjs  
**Workflows**: → [.ai-system/rules/platforms/frontend-rules.md](../../.ai-system/rules/platforms/frontend-rules.md)

### ⚙️ Backend Development Agent

**Keywords**: nodejs, laravel, api, database, docker, microservices  
**Workflows**: → [.ai-system/rules/platforms/backend-rules.md](../../.ai-system/rules/platforms/backend-rules.md)

### 📱 Mobile Cross-platform Agent

**Keywords**: flutter, react native, dart, expo, hybrid  
**Workflows**: → [TSDDR 2.0 Guide](../../docs/TSDDR-2.0-Guide.md)

### 🚀 DevOps Infrastructure Agent

**Keywords**: docker, kubernetes, cicd, aws, gcp, terraform  
**Workflows**: → [.ai-system/workflows/development/deployment-automation.md](../../.ai-system/workflows/development/deployment-automation.md)

## Workflow Index

### Core Workflows

**📋 Task Creation** → [.ai-system/workflows/development/task-management.md](../../.ai-system/workflows/development/task-management.md)  
**📋 Planning** → [.ai-system/workflows/planning/kiro-spec-driven-workflow.md](../../.ai-system/workflows/planning/kiro-spec-driven-workflow.md)  
**⚡ YOLO Mode** → [.ai-system/workflows/development/kiro-task-execution-workflow.md](../../.ai-system/workflows/development/kiro-task-execution-workflow.md)

### Development Workflows

**📱 iOS** → [.ai-system/rules/platforms/ios-workflow.md](../../.ai-system/rules/platforms/ios-workflow.md)  
**🤖 Android** → [.ai-system/rules/platforms/android-workflow.md](../../.ai-system/rules/platforms/android-workflow.md)  
**🌐 Frontend** → [.ai-system/rules/platforms/frontend-rules.md](../../.ai-system/rules/platforms/frontend-rules.md)  
**⚙️ Backend** → [.ai-system/rules/platforms/backend-rules.md](../../.ai-system/rules/platforms/backend-rules.md)  
**🧪 TSDDR 2.0** → [../../docs/TSDDR-2.0-Guide.md](../../docs/TSDDR-2.0-Guide.md)

## Performance Targets

**System**: Selection accuracy >90%, Response time <2s  
**Quality**: Code quality >8.5/10, User satisfaction >4.5/5

## .ai-system Integration

**Automatic AI System Detection**:

- Check for `.ai-system/` directory structure
- Load `.ai-system/README.md` for available workflows
- Import `.ai-system/rules/core/` for system-wide rules
- Detect Kiro integration via `.kiro/specs/` presence

**Workflow Coordination**:

- **Planning Phase**: Route to `.ai-system/workflows/planning/kiro-spec-driven-workflow.md`
- **Task Creation**: Leverage Kiro for structured task breakdown
- **Cross-IDE Execution**: Use `.ai-system/workflows/development/kiro-task-execution-workflow.md`
- **Agent Selection**: Apply `.ai-system/rules/core/kiro-priority-agent-selection.md`

**Hybrid Execution Patterns**:

- **Pattern 1**: User Request → Kiro (Spec + Tasks) → Trae (Implementation) → Validation
- **Pattern 2**: Complex Request → .ai-system Workflow Selection → Kiro Planning → Specialized Execution
- **Pattern 3**: Large Feature → .ai-system Task Distribution → Multiple IDE Execution → Unified Integration

## Integration Points

**Automatic Configuration Loading**:

- Load workflow rules dựa trên `projectType` và `projectStage`
- Apply platform-specific rules từ `platformSpecificRules`
- Enable/disable features dựa trên `features` configuration
- Load integrations (Telegram, MCP tools, AI APIs)
- Auto-detect and load .god workflows when available

**External Integrations**:

- **Trae AI**: Dynamic agent loading, intelligent workload balancing
- **Kiro System**: Automated requirement extraction, AI-enhanced task creation
- **.ai-system Workflows**: Centralized workflow management, cross-IDE coordination
- **Telegram Notifications**: Project milestone alerts, completion notifications
- **MCP Tools**: Browser debugging, enhanced development capabilities

## Project Identity Enforcement

### Mandatory Checks (EVERY SESSION)

```markdown
🔴 CRITICAL: Đọc .project-identity trước mọi task
🔴 CRITICAL: Kiểm tra currentWorkingStatus trong .project-identity để tránh xung đột
🔴 CRITICAL: Validate projectStage và load appropriate workflows
🔴 CRITICAL: Apply always_applied rules + stage-specific rules
🔴 CRITICAL: Check for new project detection triggers
```

### Multi-AI Coordination Protocol

**BẮT BUỘC: Kiểm tra .project-identity trước mọi công việc**

**Work Status Declaration**:

1. **Khi bắt đầu làm việc**: Cập nhật section "currentWorkingStatus" trong .project-identity
2. **Trong quá trình làm việc**: Cập nhật "targetFiles" và "lastUpdate" timestamp định kỳ
3. **Sau khi hoàn thành**: Xóa section "currentWorkingStatus" hoặc set status = "completed"

**Conflict Resolution**:

- **Timeout Rules**: Nếu AI không cập nhật "lastUpdate" > 30 phút: Coi như idle
- **User Override**: User có thể force override bằng cách xóa "currentWorkingStatus"

### Error Handling

- **Missing .project-identity**: Tự động tạo template và yêu cầu user cấu hình
- **Invalid projectStage**: Reset về stage1_brainstorm và yêu cầu xác nhận
- **Missing Required Files**: Validate requiredOutputs của giai đoạn hiện tại

## 🗂️ CODE_BASE TEMPLATE SYSTEM

**BẮT BUỘC: Tham khảo code_base/ khi thiết kế cấu trúc dự án**

**Mandatory Code Base Check**:

- Đọc code_base/project-map.md - project overview
- Tham khảo code_base/quick-reference.md - navigation nhanh
- Sử dụng code_base/ai-navigation-guide.md - hướng dẫn chi tiết
- Áp dụng template patterns phù hợp với projectType

## 📋 PROJECT IDENTITY INDEX MAINTENANCE

**🔴 CRITICAL: Tự động cập nhật Quick Reference Index khi chỉnh sửa .project-identity**

### Auto-Update Rules (MANDATORY)

**Khi nào phải cập nhật:**

- Mỗi khi thêm/xóa/sửa section trong .project-identity
- Sau khi thêm nội dung mới làm thay đổi số dòng
- Khi cấu trúc file .project-identity bị thay đổi

**Quy trình cập nhật tự động:**

```markdown
1. ☐ Phát hiện thay đổi trong .project-identity
2. ☐ Đếm lại số dòng của các section quan trọng:
   - mandatoryAIConfiguration
   - projectType, projectStage
   - mainLanguages, mainFrameworks
   - projectLifecycle
   - currentWorkingStatus
   - developmentMode
   - codebaseDocumentation
3. ☐ Cập nhật QUICK_REFERENCE_INDEX với số dòng chính xác
4. ☐ Kiểm tra OPTIMIZATION_NOTE vẫn đúng (60 dòng đầu)
```

**Validation Rules:**

- ✅ Tất cả số dòng trong index phải chính xác
- ✅ Optimization note phải phản ánh đúng vị trí thông tin cơ bản
- ✅ AI phải có thể tìm thấy thông tin trong 1-2 lần đọc
- ❌ Không được để index lỗi thời hoặc sai số dòng

**Error Prevention:**

- Luôn kiểm tra lại số dòng sau mỗi thay đổi
- Cập nhật ngay lập tức, không trì hoãn
- Test bằng cách đọc theo index để đảm bảo chính xác
- projectLifecycle chỉ giữ lại stage hiện tại theo hiện trạng dự án
