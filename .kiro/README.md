# Kiro Task Management System

## Overview

Kiro là hệ thống quản lý task tự động cho Base-AI-Project, hỗ trợ tạo và thực thi các task chi tiết từ feature requests.

## Structure

```
.kiro/
├── specs/
│   └── {project}/
│       ├── requirements.md    # Yêu cầu nghiệp vụ
│       ├── design.md          # Thiết kế hệ thống
│       └── tasks.md           # Danh sách tasks chi tiết
└── steering/
    ├── rules/                 # Quy tắc và guidelines
    └── workflows/             # Quy trình làm việc
```

## Task Creation Workflow

### Automatic Task Generation

Sử dụng **Task Creation Workflow** để tự động tạo comprehensive tasks:

1. **Feature Analysis**: Phân tích feature request
2. **Auto Expansion**: Tự động mở rộng thành sub-features
3. **Task Generation**: Tạo tasks chi tiết với acceptance criteria
4. **Technical Specs**: Bổ sung technical implementation details
5. **Quality Gates**: Thêm testing và validation requirements

### Supported IDEs

- **Claude**: AI-powered task creation và analysis
- **Cursor**: Integrated development với task execution
- **Trae**: Visual task management và tracking
- **Kiro**: Core task management system

### Usage Commands

```bash
# Tạo task mới từ feature request
/create-tasks "User Authentication System"

# Mở rộng task hiện có
/expand-task TASK-001

# Validate task structure
/validate-tasks

# Execute task workflow
/execute-task TASK-001
```

## Key Features

- **Auto-Expansion**: Tự động mở rộng feature thành hệ thống task hoàn chỉnh
- **Smart Context**: Nhận biết context dự án và technology stack
- **Quality Assurance**: Built-in quality gates và validation
- **Cross-IDE Support**: Hoạt động trên tất cả IDE được hỗ trợ
- **Dependency Management**: Tự động quản lý dependencies giữa tasks

## Integration

Task Creation Workflow được tích hợp với:

- **Base Rules**: `.cursor/rules/base-rules.mdc`
- **Project Rules**: `.trae/rules/project_rules.md`
- **Kiro Execution**: `.cursor/rules/kiro-task-execution.mdc`

Xem chi tiết tại: `.cursor/rules/task-creation-workflow.mdc`