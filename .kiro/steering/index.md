# Kiro AI Steering System Index

> **🎯 COMPREHENSIVE RULE NAVIGATION FOR KIRO AI**  
> This index provides quick access to all rules and workflows available in the Kiro steering system

## 🌟 Central AI System Hub

- **[🎯 .god/index.md](../../.god/index.md)** - **Unified AI System Hub** - Central agent selection, rules, and workflows
- **Integration**: Kiro system syncs with central hub for consistent AI behavior across all IDEs

## 📋 Quick Navigation

### 🔧 Core System Files

- **[README.md](README.md)** - System overview and synchronization protocol
- **[project-rules.md](project-rules.md)** - Complete alias to all .cursor/rules
- **[index.md](index.md)** - This navigation file

### 🎯 Specialized Workflows

- **[spec-workflow.md](spec-workflow.md)** - Spec-driven development workflow
- **[mobile-development.md](mobile-development.md)** - Mobile development workflows
- **[kiro-spec-rules.md](kiro-spec-rules.md)** - Kiro-specific spec rules (manual inclusion)

## 🔄 Rule Categories Overview

### Core Development Foundation

| Category | Primary Rules | Purpose |
|----------|---------------|---------|
| **Base Rules** | #[[file:../../.cursor/rules/base-rules.mdc]] | Fundamental development principles |
| **Development** | #[[file:../../.cursor/rules/development-rules.mdc]] | General development standards |
| **Control** | #[[file:../../.cursor/rules/development-control-rules.mdc]] | Development process control |
| **Protection** | #[[file:../../.cursor/rules/file-protection-rules.mdc]] | File safety and backup |

### Planning & Architecture

| Category | Primary Rules | Purpose |
|----------|---------------|---------|
| **Planning** | #[[file:../../.cursor/rules/planning-workflow.mdc]] | Project planning methodology |
| **Enforcement** | #[[file:../../.cursor/rules/planning-enforcement.mdc]] | Planning compliance |
| **Validation** | #[[file:../../.cursor/rules/planning-validation-rules.mdc]] | Plan validation |
| **Brainstorm** | #[[file:../../.cursor/rules/brainstorm-detailed-workflow.mdc]] | Enhanced brainstorming |

### Mobile Development

| Category | Primary Rules | Purpose |
|----------|---------------|---------|
| **Mobile Core** | #[[file:../../.cursor/rules/mobile-utility-workflow.mdc]] | Core mobile workflows |
| **Android** | #[[file:../../.cursor/rules/android-workflow.mdc]] | Android development |
| **iOS** | #[[file:../../.cursor/rules/ios-workflow.mdc]] | iOS development |
| **TDD Mobile** | #[[file:../../.cursor/rules/tdd-mobile-workflow.mdc]] | Mobile test-driven development |

### Code Quality & Architecture

| Category | Primary Rules | Purpose |
|----------|---------------|---------|
| **Deduplication** | #[[file:../../.cursor/rules/universal-code-deduplication.mdc]] | Prevent code duplication |
| **Android Dedup** | #[[file:../../.cursor/rules/android-code-deduplication.mdc]] | Android-specific deduplication |
| **TDD Guidelines** | #[[file:../../.cursor/rules/tdd-guidelines.mdc]] | Test-driven development |
| **Templates** | #[[file:../../.cursor/rules/template-selection-workflow.mdc]] | Template selection |

### Integration & API

| Category | Primary Rules | Purpose |
|----------|---------------|---------|
| **API Integration** | #[[file:../../.cursor/rules/api-integration-rules.mdc]] | API integration standards |
| **Backend** | #[[file:../../.cursor/rules/backend-rules-optimized.mdc]] | Backend development |
| **Frontend** | #[[file:../../.cursor/rules/frontend-rules.mdc]] | Frontend development |
| **Database** | #[[file:../../.cursor/rules/database-management.mdc]] | Database management |

### Project Management

| Category | Primary Rules | Purpose |
|----------|---------------|---------|
| **Project Creation** | #[[file:../../.cursor/rules/project-creation-workflow.mdc]] | New project setup |
| **Identity** | #[[file:../../.cursor/rules/project-identity-template.mdc]] | Project identity management |
| **Upgrade** | #[[file:../../.cursor/rules/project-upgrade-workflow.mdc]] | Project upgrade process |
| **Git Workflow** | #[[file:../../.cursor/rules/git-workflow.mdc]] | Version control |

### Advanced Features

| Category | Primary Rules | Purpose |
|----------|---------------|---------|
| **Memory Bank** | #[[file:../../.cursor/rules/memory-bank-workflow.mdc]] | Knowledge management |
| **Experience** | #[[file:../../.cursor/rules/experience-system-workflow.mdc]] | Experience tracking |
| **Context7** | #[[file:../../.cursor/rules/context7-auto-workflow.mdc]] | Context management |
| **Four Roles** | #[[file:../../.cursor/rules/four-role-development.mdc]] | Role-based development |

## 🎯 Kiro-Specific Features

### Spec-Driven Development

The Kiro spec workflow follows a structured 3-phase approach:

1. **Requirements Phase** - EARS format requirements with user stories
2. **Design Phase** - Comprehensive technical design with research  
3. **Tasks Phase** - Actionable implementation checklist

**Key Files:**
- [kiro-spec-rules.md](kiro-spec-rules.md) - Detailed spec workflow rules
- [spec-workflow.md](spec-workflow.md) - Integration with core workflows

### Mobile Development Integration

Kiro provides specialized mobile development support:

- Blueprint-first development approach
- Platform-specific workflows (Android/iOS)
- Code deduplication enforcement
- TDD integration for mobile

**Key Files:**
- [mobile-development.md](mobile-development.md) - Complete mobile workflow

## ⚠️ Critical Usage Rules

### Synchronization Protocol

1. **BẮT BUỘC** - All rules originate from `.cursor/rules/` as PRIMARY SOURCE
2. **NGHIÊM CẤM** - Creating independent rules in `.kiro/steering/`
3. **BẮT BUỘC** - Using file references `#[[file:...]]` for rule inclusion
4. **BẮT BUỘC** - Updating aliases when `.cursor/rules/` changes

### Rule Hierarchy Priority

```
1. .cursor/rules/     ← PRIMARY SOURCE (Highest Priority)
4. .kiro/steering/    ← Alias Layer (Lowest Priority)
```

### Manual Inclusion Rules

Some rules require manual inclusion via context keys:

- `kiro-spec-rules.md` - Use `#kiro-spec-rules` in chat
- Conditional rules based on file patterns
- Always-included rules (default behavior)

## 🔧 Quick Access Commands

### For Spec Development
```
#kiro-spec-rules - Include Kiro-specific spec workflow rules
```

### For Mobile Development
```
#mobile-development - Include mobile development workflows
```

### For Complete Rule Set
```
#project-rules - Include all available rules via aliases
```

## 📞 Support & Maintenance

### Rule Modifications
- Edit in `.cursor/rules/` first
- Update aliases in `.kiro/steering/`
- Test all file references
- Verify rule accessibility

### Broken Links
- Check file reference syntax: `#[[file:../../.cursor/rules/filename.mdc]]`
- Verify relative paths are correct
- Ensure target files exist in `.cursor/rules/`

### New Rules
- Create in `.cursor/rules/` first
- Add alias in appropriate `.kiro/steering/` file
- Update this index if needed
- Test inclusion works properly

---

**Remember: Kiro steering system is an ALIAS LAYER. All actual rules live in `.cursor/rules/`**