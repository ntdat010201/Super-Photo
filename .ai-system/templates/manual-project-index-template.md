# Manual Project Index Template

> **Template for AI-driven manual project indexing**  
> Provides comprehensive file analysis with functions, purposes, and relationships

## Index Structure

### Project Overview
```json
{
  "projectName": "[Project Name]",
  "projectType": "[web|mobile|desktop|api|library]",
  "mainLanguages": ["language1", "language2"],
  "frameworks": ["framework1", "framework2"],
  "lastIndexed": "YYYY-MM-DD HH:mm:ss",
  "indexVersion": "1.0",
  "totalFiles": 0,
  "indexedFiles": 0
}
```

### File Index Format

#### Core Files
```json
{
  "coreFiles": {
    "[file_path]": {
      "type": "[config|entry|main|core]",
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
          "complexity": "[low|medium|high]"
        }
      },
      "classes": {
        "[class_name]": {
          "purpose": "What this class represents",
          "methods": {
            "[method_name]": {
              "purpose": "Method description",
              "visibility": "[public|private|protected]",
              "static": false
            }
          },
          "properties": {
            "[property_name]": {
              "type": "property type",
              "purpose": "Property description"
            }
          }
        }
      },
      "dependencies": {
        "imports": ["imported_module1", "imported_module2"],
        "exports": ["exported_item1", "exported_item2"]
      },
      "relationships": {
        "usedBy": ["file1.js", "file2.js"],
        "uses": ["dependency1.js", "dependency2.js"]
      },
      "notes": "Additional important information",
      "lastModified": "YYYY-MM-DD",
      "complexity": "[low|medium|high]",
      "importance": "[low|medium|high|critical]"
    }
  }
}
```

#### Feature Modules
```json
{
  "featureModules": {
    "[module_name]": {
      "description": "What this module/feature does",
      "files": {
        "[file_path]": {
          "role": "[controller|service|model|view|component|utility]",
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
      "status": "[active|deprecated|experimental]"
    }
  }
}
```

#### Utility Files
```json
{
  "utilities": {
    "[file_path]": {
      "category": "[helper|validator|formatter|converter|constants]",
      "purpose": "What utilities this file provides",
      "functions": {
        "[function_name]": {
          "purpose": "Utility function description",
          "usage": "How to use this function",
          "example": "Code example if complex"
        }
      },
      "constants": {
        "[constant_name]": {
          "value": "constant value",
          "purpose": "Why this constant exists"
        }
      }
    }
  }
}
```

#### Configuration Files
```json
{
  "configurations": {
    "[file_path]": {
      "type": "[build|env|app|database|server]",
      "purpose": "What this config controls",
      "keySettings": {
        "[setting_name]": {
          "value": "current value",
          "purpose": "What this setting does",
          "impact": "What happens if changed"
        }
      },
      "environment": "[development|production|test|all]"
    }
  }
}
```

#### Test Files
```json
{
  "tests": {
    "[test_file_path]": {
      "testType": "[unit|integration|e2e]",
      "targetFile": "file_being_tested.js",
      "coverage": {
        "functions": ["tested_function1", "tested_function2"],
        "scenarios": ["scenario1", "scenario2"]
      },
      "framework": "[jest|mocha|cypress|etc]"
    }
  }
}
```

### Directory Structure
```json
{
  "directoryStructure": {
    "[directory_path]": {
      "purpose": "What this directory contains",
      "fileTypes": ["js", "css", "html"],
      "organization": "How files are organized",
      "importance": "[low|medium|high|critical]"
    }
  }
}
```

### API Documentation
```json
{
  "apiEndpoints": {
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
      "authentication": "[required|optional|none]"
    }
  }
}
```

### Database Schema (if applicable)
```json
{
  "database": {
    "tables": {
      "[table_name]": {
        "purpose": "What data this table stores",
        "columns": {
          "[column_name]": {
            "type": "varchar|int|datetime|etc",
            "purpose": "What this column represents"
          }
        },
        "relationships": {
          "[related_table]": "relationship_type"
        }
      }
    },
    "migrations": {
      "[migration_file]": {
        "purpose": "What this migration does",
        "changes": ["change1", "change2"]
      }
    }
  }
}
```

### Build and Deployment
```json
{
  "buildSystem": {
    "buildFiles": {
      "[build_file]": {
        "purpose": "What this build file does",
        "commands": {
          "[command_name]": "What this command does"
        }
      }
    },
    "deploymentFiles": {
      "[deploy_file]": {
        "purpose": "Deployment configuration purpose",
        "environment": "target environment"
      }
    }
  }
}
```

## Indexing Guidelines

### Priority Levels
1. **Critical**: Core application files, main entry points
2. **High**: Feature modules, important utilities, API endpoints
3. **Medium**: Configuration files, middleware, helpers
4. **Low**: Documentation, examples, non-essential utilities

### Complexity Assessment
- **Low**: Simple functions, basic configurations
- **Medium**: Business logic, data processing
- **High**: Complex algorithms, architectural components

### Function Documentation Standards
- Include purpose, parameters, return values
- Note any side effects or dependencies
- Mention performance considerations for complex functions
- Document error handling approach

### Relationship Mapping
- Track which files import/export from each other
- Note circular dependencies
- Document data flow between components
- Map API endpoints to their implementation files

### Update Frequency
- **Daily**: For active development files
- **Weekly**: For stable feature modules
- **Monthly**: For configuration and utility files
- **As needed**: For documentation and test files

## Usage Instructions

1. **Start with project overview** - Define basic project information
2. **Identify core files** - Index main application entry points first
3. **Map feature modules** - Group related files by functionality
4. **Document utilities** - Index helper and utility functions
5. **Configuration files** - Document build and environment configs
6. **Test coverage** - Map test files to their targets
7. **API documentation** - Document all endpoints and their implementations
8. **Relationships** - Map dependencies and data flow
9. **Regular updates** - Keep index current with code changes

## Example Usage

```json
{
  "projectName": "E-commerce API",
  "projectType": "api",
  "coreFiles": {
    "src/app.js": {
      "type": "entry",
      "purpose": "Main application entry point, sets up Express server",
      "functions": {
        "startServer": {
          "purpose": "Initialize and start the Express server",
          "parameters": ["port: number"],
          "returns": "Promise<void>"
        }
      },
      "dependencies": {
        "imports": ["express", "./routes/index", "./middleware/auth"]
      },
      "importance": "critical"
    }
  },
  "featureModules": {
    "userManagement": {
      "description": "Handles user registration, authentication, and profile management",
      "files": {
        "src/controllers/userController.js": {
          "role": "controller",
          "purpose": "Handle HTTP requests for user operations",
          "apiEndpoints": [
            {
              "method": "POST",
              "path": "/api/users/register",
              "purpose": "Register new user account"
            }
          ]
        }
      }
    }
  }
}
```

## Configuration and Integration

### Project Identity Integration
- **Source**: `.project-identity` → `projectMemorySystem` section
- **Configuration Path**: `projectMemorySystem.manualIndexing`
- **Structure Requirements**: `projectMemorySystem.indexStructure`
- **Quality Standards**: `projectMemorySystem.qualityStandards`
- **Key Fields**: `projectType`, `mainLanguages`, `mainFrameworks`, `projectStage`
- **Workflow Rules**: Load appropriate rules based on project configuration

### Memory System Coordination
- **Automated Scanner**: `projectMemorySystem.autoIndexing.scanner`
- **Output Coordination**: Supplement automated index with manual insights
- **Token Optimization**: Follow `projectMemorySystem.integrationPoints.memoryOptimization`
- **Integration Points**: Coordinate with `.god` system via `projectMemorySystem.integrationPoints.godSystem`

This template provides a comprehensive structure for manual project indexing that gives AI assistants detailed understanding of codebase functionality and organization.