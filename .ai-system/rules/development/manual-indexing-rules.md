# Manual Project Indexing Rules

> **AI-Driven Manual Project Indexing System**  
> Comprehensive rules for creating detailed project indexes with function-level documentation

## Core Principles

**Mission**: Create comprehensive, AI-readable project indexes that provide deep understanding of codebase functionality  
**Philosophy**: Quality over automation - manual indexing provides richer context than automated scanning  
**Approach**: Systematic, template-driven indexing with function-level detail  
**Integration**: Seamless integration with .god workflow system and project memory

## When to Use Manual Indexing

### Triggers for Manual Indexing

- **New Project Setup**: When `.project-identity` indicates `stage2_setup` or `stage3_development`
- **Major Refactoring**: When significant code structure changes occur
- **Feature Completion**: After implementing major features or modules
- **Code Review Preparation**: Before important code reviews or releases
- **Knowledge Transfer**: When new team members need to understand the codebase
- **Documentation Updates**: When automated index lacks sufficient detail

### User Request Patterns

```
✅ "index this project manually"
✅ "create detailed project documentation"
✅ "I need AI to understand my codebase better"
✅ "map out all functions and their purposes"
✅ "create comprehensive project memory"
✅ "tạo index chi tiết với thông tin functions"
✅ "cần file index có mô tả chức năng từng file"
```

## Indexing Workflow

### Phase 1: Project Analysis

```markdown
☐ Load .project-identity for project context
☐ Identify project type, stage, and main technologies
☐ Scan directory structure for organization patterns
☐ Identify core files, feature modules, and utilities
☐ Determine indexing scope and priorities
```

### Phase 2: Core File Indexing

```markdown
☐ Start with entry points (main.js, app.js, index.js)
☐ Document primary application setup and configuration
☐ Map core routing and middleware files
☐ Index database connection and model files
☐ Document authentication and security implementations
```

### Phase 3: Feature Module Analysis

```markdown
☐ Group related files by feature/functionality
☐ Document each module's purpose and responsibilities
☐ Map API endpoints to their implementation files
☐ Index business logic and data processing functions
☐ Document inter-module dependencies and relationships
```

### Phase 4: Function-Level Documentation

```markdown
☐ Document all public functions with purpose and parameters
☐ Include complex private functions that contain business logic
☐ Note function complexity and performance considerations
☐ Map function relationships and call hierarchies
☐ Document error handling and edge cases
```

### Phase 5: Integration and Validation

```markdown
☐ Create comprehensive relationship mappings
☐ Validate index completeness against project structure
☐ Generate summary statistics and coverage metrics
☐ Save index to project memory system
☐ Update .project-identity with indexing metadata
```

### Phase 6: DETAILED-PROJECT-INDEX.md Creation

```markdown
☐ Create DETAILED-PROJECT-INDEX.md to replace PROJECT-INDEX.md
☐ Include comprehensive file-by-file analysis with functions
☐ Document each function's purpose, parameters, and return values
☐ Map API endpoints to their implementation functions
☐ Show dependency relationships between files
☐ Include performance and complexity notes
☐ Add code examples for complex functions
☐ Generate table of contents with direct links to sections
```

## Indexing Standards

### File Classification

**Core Files** (Priority: Critical)

- Entry points and main application files
- Server setup and configuration
- Database connections and core models
- Authentication and security middleware
- Main routing and API setup

**Feature Modules** (Priority: High)

- Business logic implementations
- API controllers and services
- Data models and repositories
- Feature-specific utilities
- Module configuration files

**Utilities** (Priority: Medium)

- Helper functions and shared utilities
- Validation and formatting functions
- Constants and configuration helpers
- Third-party integrations
- Development tools and scripts

**Configuration** (Priority: Medium)

- Environment configuration
- Build and deployment scripts
- Package management files
- CI/CD configuration
- Development tooling setup

**Tests** (Priority: Low)

- Unit test files
- Integration test suites
- End-to-end test scenarios
- Test utilities and fixtures
- Mock data and test helpers

### Function Documentation Requirements

**Mandatory Fields**:

- `purpose`: Clear description of what the function does
- `parameters`: List of parameters with types
- `returns`: Return type and description
- `complexity`: Assessment of function complexity

**Optional Fields**:

- `usage`: How to use the function (for complex utilities)
- `example`: Code example for complex functions
- `sideEffects`: Any side effects or state changes
- `performance`: Performance considerations
- `errorHandling`: How errors are handled

### Relationship Mapping

**Import/Export Tracking**:

```json
{
  "dependencies": {
    "imports": ["module1", "module2"],
    "exports": ["function1", "class1"]
  },
  "relationships": {
    "usedBy": ["file1.js", "file2.js"],
    "uses": ["dependency1.js", "dependency2.js"]
  }
}
```

**API Endpoint Mapping**:

```json
{
  "apiEndpoints": [
    {
      "method": "POST",
      "path": "/api/users",
      "purpose": "Create new user account",
      "implementedIn": "controllers/userController.js",
      "function": "createUser"
    }
  ]
}
```

## Quality Standards

### Completeness Metrics

- **File Coverage**: >90% of source files indexed
- **Function Coverage**: >80% of public functions documented
- **API Coverage**: 100% of endpoints mapped to implementations
- **Module Coverage**: All feature modules identified and documented

### Documentation Quality

- **Clarity**: Descriptions are clear and unambiguous
- **Accuracy**: Information matches actual code implementation
- **Completeness**: All required fields are populated
- **Consistency**: Consistent terminology and formatting

### Validation Checklist

```markdown
☐ All core files are indexed with complete information
☐ Feature modules are properly grouped and documented
☐ Function purposes are clear and accurate
☐ API endpoints are mapped to their implementations
☐ Dependencies and relationships are correctly identified
☐ Configuration files are documented with their purposes
☐ Index follows the standard template structure
☐ No critical files or functions are missing
```

## Integration with Project Memory

### Project Identity Integration

- Read `.project-identity` → `projectMemorySystem` section for configuration
- Understand project type, stage, and technologies from project metadata
- Apply indexing structure defined in `projectMemorySystem.indexStructure`
- Follow quality standards from `projectMemorySystem.qualityStandards`

### Memory System Coordination

- Use configuration from `.project-identity.projectMemorySystem.manualIndexing`
- Coordinate with automated scanning system (`.ai-system/scripts/project-memory-scanner.js`)
- Supplement automated index with manual insights following defined structure
- Maintain consistency with project memory format and token optimization

### Cross-Reference Validation

- Verify against existing project documentation
- Check consistency with `.ai-system` system rules defined in `projectMemorySystem.integrationPoints.aiSystem`
- Validate against project structure definitions in `.project-identity.projectStructure`
- Follow memory optimization guidelines from `projectMemorySystem.integrationPoints.memoryOptimization`

### Storage Location

- **Primary Index**: `project-memory/manual-index.json`
- **Backup**: `project-memory/manual-index-backup.json`
- **Metadata**: `project-memory/index-metadata.json`

### Memory Integration

```json
{
  "indexType": "manual",
  "lastUpdated": "YYYY-MM-DD HH:mm:ss",
  "indexedBy": "AI Assistant",
  "version": "1.0",
  "completeness": {
    "filesCovered": 45,
    "totalFiles": 50,
    "coveragePercentage": 90
  },
  "qualityMetrics": {
    "functionsDocumented": 120,
    "apiEndpointsMapped": 25,
    "modulesCovered": 8
  }
}
```

### Update Triggers

- **Code Changes**: When significant code modifications occur
- **New Features**: After implementing new functionality
- **Refactoring**: After major code restructuring
- **Scheduled**: Weekly for active projects, monthly for stable projects

## AI Assistant Guidelines

### Indexing Process

1. **Analyze Project Structure**: Understand the codebase organization
2. **Follow Template**: Use the manual indexing template strictly
3. **Prioritize Core Files**: Start with most important files first
4. **Document Functions**: Include purpose, parameters, and complexity
5. **Map Relationships**: Track dependencies and data flow
6. **Validate Completeness**: Ensure all critical components are covered
7. **Generate Metadata**: Create indexing statistics and quality metrics

### Best Practices

- **Read Code Carefully**: Understand what each function actually does
- **Use Consistent Terminology**: Maintain consistent naming and descriptions
- **Focus on Business Logic**: Prioritize functions that contain business rules
- **Document Complex Logic**: Provide extra detail for complex algorithms
- **Map Data Flow**: Track how data moves through the application
- **Note Performance Considerations**: Identify potentially expensive operations

### Error Prevention

- **Verify Function Signatures**: Ensure parameter lists are accurate
- **Check Return Types**: Confirm what functions actually return
- **Validate Relationships**: Ensure import/export mappings are correct
- **Cross-Reference APIs**: Verify endpoint mappings to implementations
- **Review for Completeness**: Check that no critical files are missed

## Output Format

### File Structure

```
project-memory/
├── manual-index.json          # Main index file (JSON format)
├── index-metadata.json        # Indexing metadata
├── manual-index-backup.json   # Backup of previous index
└── index-history/             # Historical index versions
    ├── 2024-01-15-index.json
    └── 2024-01-10-index.json
```

### Primary Output Files

1. **DETAILED-PROJECT-INDEX.md** - Human-readable detailed index with functions
2. **project-memory/manual-index.json** - Machine-readable structured data

### Index File Format

Follow the template structure exactly as defined in:
`/Users/trungkientn/Dev2/Base-AI-Project/.ai-system/templates/manual-project-index-template.md`

### DETAILED-PROJECT-INDEX.md Requirements

- **Replace PROJECT-INDEX.md**: Create comprehensive replacement
- **Function-Level Detail**: Document all public functions with purpose, parameters, returns
- **Code Structure Analysis**: Map actual code organization and relationships
- **API Endpoint Mapping**: Link endpoints to implementation functions
- **Business Logic Documentation**: Explain what each module/file actually does
- **Dependency Tracking**: Show how files depend on each other
- **Performance Notes**: Identify complex or performance-critical functions

### DETAILED-PROJECT-INDEX.md Format Structure

```markdown
# [Project Name] - Detailed Code Index

## 📋 Project Overview

[Brief project description and tech stack]

## 📁 File Structure Analysis

### 🔧 Core Application Files

#### `src/app.js` - Main Application Entry

**Purpose**: Application initialization and server setup
**Functions**:

- `initializeApp()` - Sets up Express server and middleware
  - Parameters: `config: Object`
  - Returns: `Express.Application`
  - Complexity: Medium
- `setupRoutes()` - Configures API routes
  - Parameters: `app: Express.Application`
  - Returns: `void`
  - Dependencies: `./routes/userRoutes.js`, `./routes/apiRoutes.js`

#### `src/database/connection.js` - Database Setup

**Purpose**: Database connection and configuration
**Functions**:

- `connectDB()` - Establishes database connection
  - Parameters: `connectionString: string`
  - Returns: `Promise<Connection>`
  - Performance: Critical - connection pooling implemented

### 🎯 Feature Modules

#### `src/modules/user/` - User Management

**Files**: `userController.js`, `userService.js`, `userModel.js`
**API Endpoints**:

- `POST /api/users` → `userController.createUser()`
- `GET /api/users/:id` → `userController.getUserById()`

#### `userController.js`

**Functions**:

- `createUser(req, res)` - Creates new user account
  - Parameters: `req.body: {name, email, password}`
  - Returns: `{success: boolean, user: Object}`
  - Validation: Email format, password strength
  - Error Handling: Duplicate email, validation errors

### 🛠️ Utilities and Helpers

#### `src/utils/validation.js`

**Functions**:

- `validateEmail(email)` - Email format validation
- `validatePassword(password)` - Password strength check
- `sanitizeInput(input)` - Input sanitization

## 🔗 Dependency Map
```

app.js
├── routes/userRoutes.js
│ └── controllers/userController.js
│ └── services/userService.js
│ └── models/userModel.js
└── database/connection.js

```

## 📊 Performance Critical Functions
- `database/connection.js:connectDB()` - Database connection pooling
- `middleware/auth.js:verifyToken()` - JWT verification on every request
- `services/userService.js:hashPassword()` - CPU intensive bcrypt operation

## 🔍 API Endpoint Reference
| Method | Endpoint | Controller | Function | Purpose |
|--------|----------|------------|----------|----------|
| POST | /api/users | userController | createUser | Create user account |
| GET | /api/users/:id | userController | getUserById | Retrieve user data |

```

## Success Criteria

### Immediate Success

- ✅ Complete index file generated following template
- ✅ DETAILED-PROJECT-INDEX.md created to replace PROJECT-INDEX.md
- ✅ All core files documented with functions and their purposes
- ✅ Function-level documentation with parameters and return values
- ✅ API endpoints mapped to implementations
- ✅ Feature modules properly organized with business logic explanation
- ✅ Dependency relationships clearly mapped
- ✅ Performance critical functions identified
- ✅ Metadata file created with quality metrics

### Long-term Success

- ✅ AI assistants can quickly understand codebase structure
- ✅ New team members can navigate code using the index
- ✅ Code reviews are more efficient with comprehensive documentation
- ✅ Technical debt is reduced through better code understanding
- ✅ Development velocity increases due to better code navigation

## Maintenance

### Regular Updates

- **After Major Features**: Update index when new features are added
- **During Refactoring**: Refresh index when code structure changes
- **Weekly Reviews**: Check index accuracy for active development
- **Monthly Audits**: Comprehensive review of index completeness

### Quality Assurance

- **Accuracy Checks**: Verify that documented functions match actual code
- **Completeness Reviews**: Ensure no critical files or functions are missing
- **Consistency Audits**: Check for consistent terminology and formatting
- **Performance Monitoring**: Track how well the index serves its purpose

This manual indexing system provides AI assistants with comprehensive, detailed understanding of project codebases that goes far beyond simple file listings to include functional understanding and architectural insights.
