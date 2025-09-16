# Kiro AI Project Rules

> **🔗 MANDATORY RULES SYNCHRONIZATION**  
> **BẮT BUỘC** sử dụng các rules từ `.cursor/rules/` làm nguồn chính thức cho tất cả workflows.  
> File này chỉ là alias/link đến các rules chính thức trong `.cursor/rules/`

## 🎯 Primary Rules Sources (MANDATORY)

### Core Development Rules

#[[file:../../.cursor/rules/base-rules.mdc]]

#[[file:../../.cursor/rules/development-rules.mdc]]

#[[file:../../.cursor/rules/development-control-rules.mdc]]

#[[file:../../.cursor/rules/file-protection-rules.mdc]]

### Mobile Development Workflows

#[[file:../../.cursor/rules/mobile-utility-workflow.mdc]]

#[[file:../../.cursor/rules/android-workflow.mdc]]

#[[file:../../.cursor/rules/ios-workflow.mdc]]

#[[file:../../.cursor/rules/tdd-mobile-workflow.mdc]]

### Project Management

#[[file:../../.cursor/rules/planning-workflow.mdc]]

#[[file:../../.cursor/rules/planning-enforcement.mdc]]

#[[file:../../.cursor/rules/planning-validation-rules.mdc]]

#[[file:../../.cursor/rules/brainstorm-overview.mdc]]

#[[file:../../.cursor/rules/brainstorm-detailed-workflow.mdc]]

#[[file:../../.cursor/rules/expert-brainstorm-workflow.mdc]]

#[[file:../../.cursor/rules/brainstorm-expert-integration.mdc]]

#[[file:../../.cursor/rules/expert-brainstorm-guide.mdc]]

### Code Quality & Architecture

#[[file:../../.cursor/rules/android-code-deduplication.mdc]]

#[[file:../../.cursor/rules/universal-code-deduplication.mdc]]

#[[file:../../.cursor/rules/android-project-template.mdc]]

#[[file:../../.cursor/rules/ios-project-template.mdc]]

### Integration & API

#[[file:../../.cursor/rules/api-integration-rules.mdc]]

#[[file:../../.cursor/rules/backend-rules-optimized.mdc]]

#[[file:../../.cursor/rules/frontend-rules.mdc]]

### Specialized Workflows

#[[file:../../.cursor/rules/git-workflow.mdc]]

#[[file:../../.cursor/rules/i18n-rules.mdc]]

#[[file:../../.cursor/rules/resource-management.mdc]]

#[[file:../../.cursor/rules/terminal-rules.mdc]]

#[[file:../../.cursor/rules/design-to-prompt.mdc]]

### Project Setup & Identity

#[[file:../../.cursor/rules/project-creation-workflow.mdc]]

#[[file:../../.cursor/rules/project-identity-template.mdc]]

#[[file:../../.cursor/rules/project-identification-rules.mdc]]

#[[file:../../.cursor/rules/tech-stack-selection.mdc]]

#[[file:../../.cursor/rules/nodejs-project-creation.mdc]]

### Advanced Features

#[[file:../../.cursor/rules/memory-bank-workflow.mdc]]

#[[file:../../.cursor/rules/experience-system-workflow.mdc]]

#[[file:../../.cursor/rules/context7-auto-workflow.mdc]]

#[[file:../../.cursor/rules/cross-ide-compatibility.mdc]]

#[[file:../../.cursor/rules/four-role-development.mdc]]

## ⚠️ CRITICAL ENFORCEMENT RULES

### Mandatory Compliance

1. **BẮT BUỘC** tuân thủ 100% các rules trong `.cursor/rules/`
2. **NGHIÊM CẤM** tạo rules mới trong `.kiro/steering/` mà không sync với `.cursor/rules/`
3. **BẮT BUỘC** cập nhật alias links khi có thay đổi trong `.cursor/rules/`
4. **BẮT BUỘC** sử dụng file references để đảm bảo tính di động

### Synchronization Protocol

- Mọi thay đổi rules phải được thực hiện trong `.cursor/rules/` trước
- File này chỉ được cập nhật để sync alias links
- Không được override hoặc modify nội dung rules gốc

## 🔄 Rules Hierarchy Priority

1. `.cursor/rules/` - **PRIMARY SOURCE** (Highest Priority)
2. `.kiro/steering/` - Alias/Link layer only (Lowest Priority)

## Kiro AI Specific Configuration

- **BẮT BUỘC** sử dụng rules từ `.cursor/rules/` làm nguồn chính thức
- File này chỉ chứa alias links và không được chứa rules độc lập
- Mọi customization phải được thực hiện trong `.cursor/rules/`
