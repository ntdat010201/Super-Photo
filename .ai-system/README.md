# .ai-system/ - Universal AI Rules & Configuration Hub

> **🎯 Central Command Center for Multi-IDE AI Development**  
> Unified rules, agents, and workflows for consistent AI behavior across all IDEs

## 🎯 The .ai-system System

The `.ai-system` directory contains the complete AI agent system for cross-IDE development. This system provides:

## 📁 Structure Overview

```
.ai-system/
├── index.md              # Main navigation hub (ENTRY POINT)
├── README.md             # This file
├── agents/               # Standardized AI agents
│   ├── index.md         # Agent registry
│   ├── engineering/     # Development agents
│   ├── product/         # Product & planning agents
│   └── specialized/     # Domain-specific agents
├── rules/               # Core rules library
│   ├── index.md         # Rules registry
│   ├── core/            # Base rules (always applied)
│   ├── development/     # Development-specific rules
│   ├── platforms/       # Platform-specific rules
│   └── workflows/       # Workflow rules
└── workflows/           # Standardized workflows
    ├── index.md         # Workflow registry
    ├── development/     # Development workflows
    ├── planning/        # Planning & brainstorm workflows
    └── integration/     # Integration workflows
```

## 🎯 Purpose

**Problem Solved**: Eliminates rule duplication across `.trae/`, `.cursor/`, `.kiro/`, `.qoder/`, and other IDE configurations.

**Benefits**:
- ✅ **Single Source of Truth**: All rules maintained in one place
- ✅ **Consistency**: Same behavior across all AI tools
- ✅ **Maintainability**: Update once, apply everywhere
- ✅ **Scalability**: Easy to add new IDEs or AI tools
- ✅ **Version Control**: Centralized rule versioning

## 🔗 IDE Integration

Each IDE configuration should link to `.ai-system/index.md`:

```markdown
# IDE-Specific Config

<!-- Import universal rules -->
@import ".ai-system/index.md"

<!-- IDE-specific overrides only -->
[IDE-specific rules here]
```

## 📋 Migration Status

- [ ] **Analysis**: Identify duplicate rules across IDEs
- [ ] **Structure**: Create .ai-system/ directory structure
- [ ] **Migration**: Move common rules to .ai-system/
- [ ] **Integration**: Update IDE configs to reference .ai-system/
- [ ] **Validation**: Test consistency across all IDEs
- [ ] **Cleanup**: Remove duplicated rules from IDE folders

## 🚀 Quick Start with .ai-system

1. **Import the system**: Add `.ai-system/index.md` to your IDE configuration
2. **For New Projects**: Start with `.ai-system/index.md`
3. **For Existing Projects**: Follow migration guide
4. **For New IDEs**: Reference `.ai-system/index.md` in your config

---

**🎯 One source, infinite possibilities. Consistent AI behavior across all development environments.**