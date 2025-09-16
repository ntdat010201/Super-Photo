# Kiro AI Spec-Driven Development Workflow

> **🔗 MANDATORY RULES SYNCHRONIZATION**  
> **BẮT BUỘC** sử dụng các rules từ `.cursor/rules/` làm nguồn chính thức cho spec workflows.  
> File này chỉ là alias/link đến các rules chính thức trong `.cursor/rules/`

## 🎯 Spec Workflow Foundation

### Core Planning Rules

#[[file:../../.cursor/rules/planning-workflow.mdc]]

#[[file:../../.cursor/rules/planning-enforcement.mdc]]

#[[file:../../.cursor/rules/planning-validation-rules.mdc]]

### Brainstorm Integration

#[[file:../../.cursor/rules/brainstorm-overview.mdc]]

#[[file:../../.cursor/rules/brainstorm-detailed-workflow.mdc]]

#[[file:../../.cursor/rules/expert-brainstorm-workflow.mdc]]

#[[file:../../.cursor/rules/brainstorm-expert-integration.mdc]]

## 🔄 Spec Development Process

### Phase 1: Requirements Gathering

Based on the planning workflow rules above, the requirements phase follows these principles:

- **BẮT BUỘC** tạo file `.kiro/specs/{feature_name}/requirements.md`
- **BẮT BUỘC** sử dụng EARS format cho acceptance criteria
- **BẮT BUỘC** bao gồm user stories cho mỗi requirement
- **BẮT BUỘC** xin phê duyệt từ user trước khi chuyển sang design

### Phase 2: Design Document

Following the architecture principles from the core rules:

- **BẮT BUỘC** tạo file `.kiro/specs/{feature_name}/design.md`
- **BẮT BUỘC** bao gồm các sections: Overview, Architecture, Components, Data Models, Error Handling, Testing Strategy
- **BẮT BUỘC** thực hiện research và tích hợp findings vào design
- **BẮT BUỘC** xin phê duyệt từ user trước khi chuyển sang tasks

### Phase 3: User Flow & Screen Navigation

Based on user experience and navigation principles:

- **BẮT BUỘC** tạo file `.kiro/specs/{feature_name}/user-flows.md`
- **BẮT BUỘC** bao gồm screen navigation map và connection matrix
- **BẮT BUỘC** định nghĩa navigation rules và data flow giữa các màn hình
- **BẮT BUỘC** đảm bảo tất cả màn hình có liên kết rõ ràng với nhau
- **BẮT BUỘC** xin phê duyệt từ user trước khi chuyển sang tasks

### Phase 4: Implementation Tasks

Based on the development control rules:

- **BẮT BUỘC** tạo file `.kiro/specs/{feature_name}/tasks.md`
- **BẮT BUỘC** chia nhỏ thành các coding tasks cụ thể
- **BẮT BUỘC** mỗi task phải reference requirements tương ứng
- **BẮT BUỘC** sử dụng checkbox format với hierarchy tối đa 2 levels
- **BẮT BUỘC** xin phê duyệt từ user trước khi bắt đầu implementation

### Phase 5: Code Review & Quality Assessment

Based on AI-powered code analysis principles:

- **BẮT BUỘC** tạo file `.kiro/specs/{feature_name}/code-review.md` sau khi hoàn thành implementation
- **BẮT BUỘC** phân tích git diff để phát hiện code duplication và inconsistency
- **BẮT BUỘC** kiểm tra feature integration và screen navigation connections
- **BẮT BUỘC** đánh giá code quality, performance và security issues
- **BẮT BUỘC** đưa ra recommendations để refactor và optimize code
- **BẮT BUỘC** xin phê duyệt từ user về kết quả review và action items

## 🎯 Task Execution Rules

### Core Development Standards

#[[file:../../.cursor/rules/development-rules.mdc]]

#[[file:../../.cursor/rules/development-control-rules.mdc]]

### Code Quality Enforcement

#[[file:../../.cursor/rules/universal-code-deduplication.mdc]]

#[[file:../../.cursor/rules/tdd-guidelines.mdc]]

### File Protection During Implementation

#[[file:../../.cursor/rules/file-protection-rules.mdc]]

## 🔄 Integration with Existing Workflows

### Mobile Development

#[[file:../../.cursor/rules/mobile-utility-workflow.mdc]]

#[[file:../../.cursor/rules/android-workflow.mdc]]

#[[file:../../.cursor/rules/ios-workflow.mdc]]

### Frontend/Backend Development

#[[file:../../.cursor/rules/frontend-rules.mdc]]

#[[file:../../.cursor/rules/backend-rules-optimized.mdc]]

### API Integration

#[[file:../../.cursor/rules/api-integration-rules.mdc]]

## ⚠️ CRITICAL SPEC WORKFLOW RULES

### Mandatory Sequential Process

1. **BẮT BUỘC** hoàn thành Requirements trước khi Design
2. **BẮT BUỘC** hoàn thành Design trước khi User Flows
3. **BẮT BUỘC** hoàn thành User Flows trước khi Tasks
4. **BẮT BUỘC** hoàn thành Tasks trước khi Implementation
5. **BẮT BUỘC** thực hiện Code Review sau Implementation
6. **BẮT BUỘC** xin phê duyệt user sau mỗi phase
7. **BẮT BUỘC** chỉ execute một task tại một thời điểm
8. **NGHIÊM CẤM** bỏ qua bất kỳ phase nào

### User Approval Requirements

- **BẮT BUỘC** sử dụng userInput tool với reason 'spec-requirements-review'
- **BẮT BUỘC** sử dụng userInput tool với reason 'spec-design-review'
- **BẮT BUỘC** sử dụng userInput tool với reason 'spec-userflows-review'
- **BẮT BUỘC** sử dụng userInput tool với reason 'spec-tasks-review'
- **BẮT BUỘC** sử dụng userInput tool với reason 'spec-codereview-review'
- **BẮT BUỘC** chờ explicit approval trước khi proceed

### Task Status Management

- **BẮT BUỘC** cập nhật task status thành 'in_progress' trước khi bắt đầu
- **BẮT BUỘC** cập nhật task status thành 'completed' khi hoàn thành
- **BẮT BUỘC** dừng lại sau mỗi task để user review
- **NGHIÊM CẤM** tự động chuyển sang task tiếp theo

## 🔧 Spec File Structure

```
.kiro/specs/{feature_name}/
├── requirements.md    # EARS format requirements
├── design.md         # Comprehensive design document
├── user-flows.md     # Screen navigation and user flow mapping
├── tasks.md          # Implementation task checklist
└── code-review.md    # AI-powered git diff analysis and quality assessment
```

## 📞 Workflow Support

### Template Selection

#[[file:../../.cursor/rules/template-selection-workflow.mdc]]

### Project Identity

#[[file:../../.cursor/rules/project-identity-template.mdc]]

#[[file:../../.cursor/rules/project-identification-rules.mdc]]

### Git Integration

#[[file:../../.cursor/rules/git-workflow.mdc]]

---

**Remember: This is an ALIAS LAYER. All actual workflow rules live in `.cursor/rules/`**