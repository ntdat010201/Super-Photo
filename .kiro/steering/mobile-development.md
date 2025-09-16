# Kiro AI Mobile Development Workflow

> **🔗 MANDATORY RULES SYNCHRONIZATION**  
> **BẮT BUỘC** sử dụng các rules từ `.cursor/rules/` làm nguồn chính thức cho mobile development workflows.  
> File này chỉ là alias/link đến các rules chính thức trong `.cursor/rules/`

## 🎯 Mobile Development Foundation

### Core Mobile Workflows

#[[file:../../.cursor/rules/mobile-utility-workflow.mdc]]

#[[file:../../.cursor/rules/tdd-mobile-workflow.mdc]]

### Platform-Specific Workflows

#[[file:../../.cursor/rules/android-workflow.mdc]]

#[[file:../../.cursor/rules/ios-workflow.mdc]]

### Project Templates

#[[file:../../.cursor/rules/android-project-template.mdc]]

#[[file:../../.cursor/rules/ios-project-template.mdc]]

## 🔄 Mobile Development Process

### Blueprint-First Development

Based on the mobile utility workflow rules above:

- **BẮT BUỘC** tạo blueprint trước khi viết code cho mỗi tính năng
- **BẮT BUỘC** kiểm tra module registry để tránh trùng lặp
- **BẮT BUỘC** cập nhật module registry sau khi hoàn thành tính năng
- **BẮT BUỘC** tuân thủ cấu trúc package tiêu chuẩn
- **BẮT BUỘC** sử dụng các base classes đã có
- **NGHIÊM CẤM** tạo code trùng lặp chức năng đã có

### Standard Package Structure

Following the Android project template:

```
com.base.app/
├── base/                 # Base classes
│   ├── activity/         # Base Activities
│   ├── fragment/         # Base Fragments
│   ├── viewmodel/        # Base ViewModels
│   ├── adapter/          # Base Adapters
│   └── view/             # Base Custom Views
├── core/                 # Core modules
│   ├── di/               # Dependency Injection
│   ├── network/          # Network components
│   ├── storage/          # Local storage
│   ├── analytics/        # Analytics tracking
│   └── utils/            # Utility classes
├── data/                 # Data layer
│   ├── repository/       # Repositories implementation
│   ├── datasource/       # Data sources (remote, local)
│   ├── model/            # Data models (entities, DTOs)
│   └── mapper/           # Mappers
├── domain/               # Domain layer
│   ├── usecase/          # Use cases (business logic)
│   ├── model/            # Domain models
│   └── repository/       # Repository interfaces
└── ui/                   # UI layer
    ├── components/       # Shared UI components
    └── features/         # Feature packages
        ├── feature1/     # Tính năng 1
        ├── feature2/     # Tính năng 2
        └── ...
```

## 🎯 Code Quality Standards

### Architecture Principles

#[[file:../../.cursor/rules/development-rules.mdc]]

### Code Deduplication

#[[file:../../.cursor/rules/android-code-deduplication.mdc]]

#[[file:../../.cursor/rules/universal-code-deduplication.mdc]]

### Testing Guidelines

#[[file:../../.cursor/rules/tdd-guidelines.mdc]]

## 🔄 Integration with Core Workflows

### Planning Integration

#[[file:../../.cursor/rules/planning-workflow.mdc]]

#[[file:../../.cursor/rules/planning-enforcement.mdc]]

### Design Analysis

#[[file:../../.cursor/rules/design-to-prompt.mdc]]

### API Integration

#[[file:../../.cursor/rules/api-integration-rules.mdc]]

## ⚠️ CRITICAL MOBILE DEVELOPMENT RULES

### Mandatory Architecture Compliance

1. **BẮT BUỘC** phân chia rõ ràng các layer (presentation, business logic, data)
2. **BẮT BUỘC** sử dụng dependency injection để tách bạch các thành phần
3. **BẮT BUỘC** ưu tiên composition over inheritance
4. **BẮT BUỘC** thiết kế API dễ sử dụng và mở rộng
5. **BẮT BUỘC** áp dụng Domain-Driven Design cho dự án phức tạp

### Security Requirements

- **BẮT BUỘC** validate tất cả input từ người dùng
- **BẮT BUỘC** sử dụng parameterized queries để tránh SQL injection
- **BẮT BUỘC** mã hoá dữ liệu nhạy cảm (passwords, tokens, PII)
- **BẮT BUỘC** implement đúng cách các authentication và authorization
- **BẮT BUỘC** tuân thủ hướng dẫn OWASP top 10
- **BẮT BUỘC** sử dụng HTTPS cho mọi API endpoints

### Performance Standards

- **BẮT BUỘC** tối ưu database queries để tránh N+1 problems
- **BẮT BUỘC** implement caching cho dữ liệu tĩnh và truy vấn đắt
- **BẮT BUỘC** tránh blocking operations trong event loop
- **BẮT BUỘC** sử dụng pagination cho large data sets
- **BẮT BUỘC** lazy load components và modules khi có thể
- **KHUYẾN NGHỊ** profiling code để phát hiện bottlenecks

### Error Handling

- **BẮT BUỘC** xử lý tất cả exceptions và errors
- **BẮT BUỘC** cung cấp error messages hữu ích nhưng an toàn
- **BẮT BUỘC** log errors đúng cách với context đủ để debug
- **BẮT BUỘC** implement retry mechanisms cho unstable operations
- **BẮT BUỘC** sử dụng circuit breakers cho external services

### Testing Requirements

- **BẮT BUỘC** viết unit tests với test coverage cao
- **BẮT BUỘC** implement integration tests cho critical flows
- **BẮT BUỘC** sử dụng mocking để tách biệt dependencies
- **BẮT BUỘC** ưu tiên testing pyramids (nhiều unit tests, ít e2e tests)
- **KHUYẾN NGHỊ** viết tests dễ đọc và maintain

## 🔧 Mobile-Specific Workflows

### Platform Selection

#[[file:../../.cursor/rules/platform-workflow-selector.mdc]]

### Template Selection

#[[file:../../.cursor/rules/template-selection-workflow.mdc]]

### Resource Management

#[[file:../../.cursor/rules/resource-management.mdc]]

### Internationalization

#[[file:../../.cursor/rules/i18n-rules.mdc]]

## 📞 Mobile Development Support

### Git Workflow

#[[file:../../.cursor/rules/git-workflow.mdc]]

### File Protection

#[[file:../../.cursor/rules/file-protection-rules.mdc]]

### Terminal Operations

#[[file:../../.cursor/rules/terminal-rules.mdc]]

---

**Remember: This is an ALIAS LAYER. All actual mobile development rules live in `.cursor/rules/`**