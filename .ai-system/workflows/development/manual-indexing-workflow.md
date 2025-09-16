# Manual Project Indexing Workflow

> **Systematic AI-Driven Manual Project Indexing**  
> Step-by-step workflow for creating comprehensive project indexes with function-level documentation

## Workflow Overview

**Purpose**: Guide AI assistants through systematic manual project indexing  
**Duration**: 30-60 minutes depending on project size  
**Output**: Comprehensive project index with function-level documentation  
**Integration**: Works with .god system and project memory

## Pre-Workflow Checklist

```markdown
☐ Project has .project-identity file
☐ Project is in stage2_setup or stage3_development
☐ Manual indexing template is available
☐ Project memory directory exists
☐ AI has read access to all source files
```

## Phase 1: Project Discovery & Setup

### Step 1.1: Load Project Context
```markdown
☐ Read .project-identity file
☐ Load projectMemorySystem configuration section
☐ Identify projectType, projectStage, mainLanguages
☐ Note mainFrameworks and key technologies
☐ Review indexing structure requirements from projectMemorySystem.indexStructure
☐ Check quality standards from projectMemorySystem.qualityStandards
☐ Check for existing automated index
☐ Determine indexing scope based on project size
```

**Commands**:
```bash
# Check project structure
find . -type f -name "*.js" -o -name "*.ts" -o -name "*.py" | head -20

# Count total files to index
find . -type f \( -name "*.js" -o -name "*.ts" -o -name "*.py" \) | wc -l
```

### Step 1.2: Initialize Index Structure
```markdown
☐ Create project-memory directory if not exists
☐ Load manual indexing template
☐ Initialize index.json with project overview
☐ Set up metadata tracking
☐ Create backup of any existing index
```

**Template Initialization**:
```json
{
  "projectName": "[from .project-identity]",
  "projectType": "[from .project-identity]",
  "mainLanguages": "[from .project-identity]",
  "frameworks": "[from .project-identity]",
  "lastIndexed": "[current timestamp]",
  "indexVersion": "1.0",
  "totalFiles": 0,
  "indexedFiles": 0,
  "coreFiles": {},
  "featureModules": {},
  "utilities": {},
  "configurations": {},
  "tests": {},
  "directoryStructure": {},
  "apiEndpoints": {}
}
```

## Phase 2: Core File Analysis

### Step 2.1: Identify Entry Points
```markdown
☐ Find main application entry points
☐ Locate server setup files
☐ Identify routing configuration
☐ Find database connection files
☐ Locate authentication setup
```

**Common Entry Point Patterns**:
- `app.js`, `main.js`, `index.js`, `server.js`
- `src/app.js`, `src/main.ts`, `src/index.ts`
- `package.json` scripts section for entry points

### Step 2.2: Document Core Files
```markdown
☐ Read each core file completely
☐ Identify main functions and their purposes
☐ Document function parameters and return types
☐ Note dependencies (imports/requires)
☐ Map exports and public interfaces
☐ Assess complexity and importance
```

**Core File Documentation Template**:
```json
{
  "[file_path]": {
    "type": "entry|config|main|core",
    "purpose": "Brief description of file's main purpose",
    "responsibilities": [
      "Primary responsibility 1",
      "Primary responsibility 2"
    ],
    "functions": {
      "[function_name]": {
        "purpose": "What this function does",
        "parameters": ["param1: type", "param2: type"],
        "returns": "return type and description",
        "complexity": "low|medium|high"
      }
    },
    "dependencies": {
      "imports": ["imported_module1", "imported_module2"],
      "exports": ["exported_item1", "exported_item2"]
    },
    "importance": "critical|high|medium|low"
  }
}
```

## Phase 3: Feature Module Discovery

### Step 3.1: Identify Feature Modules
```markdown
☐ Group related files by functionality
☐ Identify controllers, services, models
☐ Find feature-specific routes
☐ Locate business logic implementations
☐ Map feature dependencies
```

**Module Identification Patterns**:
- Directory-based: `src/users/`, `src/products/`, `src/orders/`
- File-based: `userController.js`, `userService.js`, `userModel.js`
- Feature-based: `authentication/`, `payment/`, `notification/`

### Step 3.2: Document Feature Modules
```markdown
☐ Define module purpose and scope
☐ List all files in the module
☐ Document each file's role within the module
☐ Map API endpoints to implementations
☐ Note inter-module dependencies
```

**Feature Module Template**:
```json
{
  "[module_name]": {
    "description": "What this module/feature does",
    "files": {
      "[file_path]": {
        "role": "controller|service|model|view|component|utility",
        "purpose": "Specific purpose within the module",
        "keyFunctions": [
          {
            "name": "function_name",
            "purpose": "What it does",
            "isPublic": true
          }
        ],
        "apiEndpoints": [
          {
            "method": "GET|POST|PUT|DELETE",
            "path": "/api/endpoint",
            "purpose": "Endpoint description"
          }
        ]
      }
    },
    "dependencies": ["other_module1", "other_module2"],
    "status": "active|deprecated|experimental"
  }
}
```

## Phase 4: Function-Level Documentation

### Step 4.1: Function Analysis Strategy
```markdown
☐ Prioritize public functions and exports
☐ Include complex private functions
☐ Document business logic functions
☐ Note utility and helper functions
☐ Skip trivial getters/setters unless complex
```

### Step 4.2: Function Documentation Process
```markdown
☐ Read function implementation completely
☐ Understand function's purpose and context
☐ Document parameters with types
☐ Note return values and types
☐ Assess complexity (low/medium/high)
☐ Note any side effects or state changes
☐ Document error handling approach
```

**Function Documentation Checklist**:
- ✅ Purpose is clear and accurate
- ✅ All parameters are documented with types
- ✅ Return value is documented
- ✅ Complexity assessment is appropriate
- ✅ Side effects are noted if any
- ✅ Error handling is documented

## Phase 5: API and Integration Mapping

### Step 5.1: API Endpoint Discovery
```markdown
☐ Find route definition files
☐ Identify all HTTP endpoints
☐ Map endpoints to controller functions
☐ Document request/response formats
☐ Note authentication requirements
```

### Step 5.2: Integration Documentation
```markdown
☐ Document database connections
☐ Map external API integrations
☐ Note third-party service dependencies
☐ Document configuration requirements
☐ Map environment variables
```

**API Endpoint Template**:
```json
{
  "[endpoint_path]": {
    "method": "GET|POST|PUT|DELETE",
    "purpose": "What this endpoint does",
    "parameters": {
      "[param_name]": {
        "type": "string|number|object",
        "required": true,
        "description": "Parameter description"
      }
    },
    "response": {
      "format": "json|xml|text",
      "structure": "Response structure description"
    },
    "implementedIn": "file_path.js",
    "function": "function_name",
    "authentication": "required|optional|none"
  }
}
```

## Phase 6: Utility and Configuration Analysis

### Step 6.1: Utility File Documentation
```markdown
☐ Identify helper and utility files
☐ Categorize utilities by purpose
☐ Document utility functions
☐ Note usage patterns
☐ Map utility dependencies
```

### Step 6.2: Configuration Documentation
```markdown
☐ Document environment configurations
☐ Map build and deployment configs
☐ Note package management files
☐ Document development tooling
☐ Map CI/CD configurations
```

## Phase 7: Relationship Mapping

### Step 7.1: Dependency Analysis
```markdown
☐ Map import/export relationships
☐ Identify circular dependencies
☐ Document data flow patterns
☐ Note shared utilities usage
☐ Map configuration dependencies
```

### Step 7.2: Architecture Documentation
```markdown
☐ Document overall architecture patterns
☐ Map layer relationships (MVC, etc.)
☐ Note design patterns used
☐ Document data persistence patterns
☐ Map authentication/authorization flow
```

## Phase 8: Quality Assurance & Validation

### Step 8.1: Completeness Check
```markdown
☐ Verify all core files are indexed
☐ Check feature module completeness
☐ Validate function documentation
☐ Confirm API endpoint mapping
☐ Review relationship accuracy
```

### Step 8.2: Quality Validation
```markdown
☐ Check description clarity
☐ Verify technical accuracy
☐ Ensure consistent terminology
☐ Validate JSON structure
☐ Test index usability
```

**Quality Metrics**:
```json
{
  "completeness": {
    "filesCovered": "[number]",
    "totalFiles": "[number]",
    "coveragePercentage": "[percentage]"
  },
  "qualityMetrics": {
    "functionsDocumented": "[number]",
    "apiEndpointsMapped": "[number]",
    "modulesCovered": "[number]",
    "relationshipsMapped": "[number]"
  }
}
```

## Phase 9: Finalization & Storage

### Step 9.1: Index Finalization
```markdown
☐ Complete final JSON structure
☐ Generate metadata file
☐ Create backup of previous index
☐ Validate JSON syntax
☐ Calculate final statistics
```

### Step 9.2: Storage & Integration
```markdown
☐ Save index to project-memory/manual-index.json
☐ Save metadata to project-memory/index-metadata.json
☐ Update .project-identity with indexing info
☐ Create index history entry
☐ Generate summary report
```

**Final File Structure**:
```
project-memory/
├── manual-index.json          # Main comprehensive index
├── index-metadata.json        # Indexing metadata and metrics
├── manual-index-backup.json   # Backup of previous version
└── index-history/             # Historical versions
    └── 2024-01-15-index.json
```

## Success Criteria

### Immediate Success Indicators
- ✅ Complete index file following template structure
- ✅ All core files documented with functions
- ✅ Feature modules properly organized and documented
- ✅ API endpoints mapped to implementations
- ✅ Utility functions categorized and documented
- ✅ Configuration files documented
- ✅ Relationships and dependencies mapped
- ✅ Quality metrics meet standards (>90% file coverage)

### Long-term Success Indicators
- ✅ AI assistants can quickly understand codebase
- ✅ Developers can navigate code efficiently
- ✅ Code reviews are more effective
- ✅ New team member onboarding is faster
- ✅ Technical debt is reduced

## Troubleshooting

### Common Issues

**Large Codebase (>100 files)**:
- Focus on core files and main features first
- Use sampling for utility files
- Prioritize business logic over boilerplate

**Complex Functions**:
- Break down into sub-purposes
- Document algorithm approach
- Note performance considerations

**Missing Documentation**:
- Infer purpose from code context
- Use function/variable names as hints
- Note uncertainty in documentation

**Circular Dependencies**:
- Document but flag as architectural concern
- Note in relationship mapping
- Suggest refactoring if severe

## Workflow Completion

### Final Checklist
```markdown
☐ Index file is complete and valid JSON
☐ Metadata file contains accurate metrics
☐ Backup of previous index is created
☐ .project-identity is updated
☐ Summary report is generated
☐ Index is accessible to other AI assistants
```

### Handoff Documentation
```markdown
☐ Document any limitations or gaps
☐ Note areas needing future attention
☐ Provide recommendations for maintenance
☐ List any architectural concerns discovered
☐ Suggest next steps for project development
```

This workflow ensures systematic, comprehensive manual indexing that provides AI assistants with deep understanding of project structure, functionality, and architecture.