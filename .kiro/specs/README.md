# Kiro Specs Directory - Project Templates

## 📁 Directory Structure

This directory contains project-specific specifications following the Kiro AI Spec-Driven Development Workflow. Each project should have its own subdirectory with standardized specification files.

```
.kiro/specs/
├── README.md                    # This file - explains the structure
├── {project-name}/              # Project-specific specs directory
│   ├── requirements.md          # Detailed requirements specification
│   ├── design.md               # Technical design document
│   ├── tasks.md                # Implementation tasks breakdown
│   ├── user-flows.md           # User journey and screen navigation mapping
│   ├── code-review.md          # AI code review and quality assessment
│   └── {feature}-design.md     # Additional feature-specific designs
└── templates/                   # Template files for new projects
    ├── requirements-template.md
    ├── design-template.md
    ├── tasks-template.md
    ├── user-flows-template.md
    ├── code-review-template.md
    └── feature-design-template.md
```

## 🎯 Template Usage

When creating a new project specification:

1. **Create Project Directory**: `mkdir .kiro/specs/{your-project-name}`
2. **Copy Templates**: Copy files from `templates/` directory
3. **Customize Content**: Replace template placeholders with project-specific information
4. **Follow Workflow**: Use Kiro AI Spec-Driven Development Workflow

## 📋 File Descriptions

### requirements.md

- **Purpose**: Comprehensive requirements specification
- **Content**: User stories, functional/non-functional requirements, acceptance criteria
- **Format**: EARS format for acceptance criteria (Given/When/Then)
- **Sections**: Project overview, user stories, requirements, constraints, success metrics

### design.md

- **Purpose**: Technical design and architecture documentation
- **Content**: System architecture, data models, module structure, API design
- **Format**: Technical diagrams, code examples, architectural decisions
- **Sections**: Architecture overview, data models, module structure, technical decisions

### tasks.md

- **Purpose**: Detailed implementation tasks breakdown
- **Content**: Task list with priorities, dependencies, acceptance criteria
- **Format**: Hierarchical task structure with status tracking
- **Sections**: Task modules, implementation details, dependencies, status tracking

### user-flows.md

- **Purpose**: User journey and interaction flow examples with screen navigation mapping
- **Content**: Real-world usage scenarios, step-by-step user interactions, screen connection diagrams
- **Format**: Flow diagrams, user personas, example scenarios, navigation maps
- **Sections**: User personas, flow examples, interaction patterns, screen navigation map, connection matrix

### {feature}-design.md

- **Purpose**: Feature-specific design documents (optional)
- **Content**: Specialized designs for complex features (e.g., gamification, AI features)
- **Format**: Feature-focused technical and UX design
- **Sections**: Feature overview, specialized requirements, implementation details

### code-review.md

- **Purpose**: AI-powered code review and quality assessment based on git diff analysis
- **Content**: Code duplication detection, synchronization issues, feature integration validation
- **Format**: Git diff analysis, quality metrics, refactoring recommendations
- **Sections**: Git diff analysis, code quality assessment, integration validation, recommendations

## ⚠️ MANDATORY KIRO WORKFLOW REQUIREMENTS

**BẮT BUỘC**: Mọi project phải tuân thủ cấu trúc path sau:

```
.kiro/specs/{project-name}/
├── requirements.md    # BẮT BUỘC - Specification requirements
├── design.md         # BẮT BUỘC - Technical design document
└── tasks.md          # BẮT BUỘC - Implementation tasks breakdown
```

### Quy Định Bắt Buộc:

1. **Path Structure**: Tất cả specs phải được tạo trong `.kiro/specs/{project-name}/`
2. **Core Files**: Ba file `requirements.md`, `design.md`, `tasks.md` là BẮT BUỘC
3. **File Order**: Phải tạo theo thứ tự: requirements → design → tasks
4. **Naming Convention**: Tên project phải sử dụng kebab-case (ví dụ: `blood-pressure-tracking`)
5. **Content Standards**: Mỗi file phải tuân thủ template structure đã định nghĩa

### Kiro Task Execution System:

- **Auto-Detection**: Hệ thống tự động phát hiện thiếu file nào
- **Dynamic Workflow**: Kích hoạt quy trình tạo/cập nhật file thiếu
- **Quality Gates**: Kiểm tra chất lượng tại mỗi giai đoạn
- **Status Tracking**: Theo dõi tiến độ hoàn thành

## 🔄 Workflow Integration

These specifications integrate with the Kiro AI Spec-Driven Development Workflow:

1. **Brainstorm Phase** → Generate initial ideas and concepts
2. **Requirements Phase** → Create `requirements.md` with detailed specifications
3. **Design Phase** → Develop `design.md` with technical architecture
4. **User Flow Phase** → Create `user-flows.md` with screen navigation and connection mapping
5. **Tasks Phase** → Break down into `tasks.md` with implementation plan
6. **Implementation Phase** → Execute tasks with continuous validation
7. **Code Review Phase** → Generate `code-review.md` with AI-powered git diff analysis

## 📝 Template Variables

When using templates, replace these placeholders:

- `{PROJECT_NAME}` - Your project name
- `{FEATURE_NAME}` - Specific feature name
- `{PLATFORM}` - Target platform (Android, iOS, Web, etc.)
- `{PRIORITY}` - Feature priority (High, Medium, Low)
- `{ESTIMATED_TIME}` - Development time estimate
- `{DEPENDENCIES}` - Project dependencies
- `{TARGET_USERS}` - Target user personas

## 🎨 Example Projects

### blood-pressure-tracking/

Complete example of a health tracking feature specification including:

- Comprehensive requirements with EARS format
- Technical design with MVVM + Clean Architecture
- Detailed task breakdown with AI integration
- Real-world user flow examples
- Gamification design specifications

## 🚀 Getting Started

### Mandatory Steps (BẮT BUỘC):

1. **Create Project Directory**: `mkdir .kiro/specs/{your-project-name}`
2. **Copy Core Templates**: Copy `requirements-template.md`, `design-template.md`, `tasks-template.md`
3. **Rename Files**: Remove `-template` suffix from filenames
4. **Follow Order**: Complete requirements → design → tasks
5. **Validate Structure**: Ensure all mandatory files exist

### Optional Steps:

6. Review the `blood-pressure-tracking/` example
7. Add additional feature-specific design files if needed
8. Iterate and refine based on implementation feedback

## 🔒 Enforcement Rules

### Kiro System Validation:

- **Path Validation**: System checks for correct `.kiro/specs/{project}/` structure
- **File Existence**: Validates presence of required files
- **Content Validation**: Ensures templates are properly filled
- **Dependency Checking**: Verifies logical flow between files

### Error Handling:

- **Missing Files**: Auto-triggers template creation workflow
- **Invalid Structure**: Provides correction guidance
- **Incomplete Content**: Highlights missing sections
- **Dependency Issues**: Shows resolution steps

## 📚 Best Practices

- **Be Specific**: Include detailed acceptance criteria and technical requirements
- **Use Examples**: Provide real-world scenarios and user personas
- **Stay Updated**: Keep specifications current with implementation changes
- **Cross-Reference**: Link between requirements, design, and tasks
- **Version Control**: Track changes and decisions in specifications

---

**Note**: This is a base AI project template. Adapt the structure and content to match your specific project needs and development workflow.
