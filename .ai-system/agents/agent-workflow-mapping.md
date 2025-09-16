---
trae_context:
  format: "native"
  version: "1.0"
  migrated_from: "cursor"
  last_updated: "2025-08-18T07:00:32.126Z"
---

## #agent-workflow-mapping

description: Agent-Workflow Mapping for IDE Integration
globs:

- "\*_/_.md"
- "\*_/_.md"
  alwaysApply: false

---

# Agent-Workflow Mapping

> **🔄 Compact mapping between Claude Agents and Workflows**

## 🎯 Agent #selection-rules

### Engineering Agents

**ios-development-agent** → **[ios-specialist.md](engineering/ios-specialist.md)**

- **Primary**: `ios-workflow.md`, `ios-project-template.md`, `tdd-mobile-workflow.md`
- **Supporting**: `development-rules.md`, `git-workflow.md`, `resource-management.md`, `validate-workflow.md`
- **Keywords**: ios, swift, swiftui, xcode, cocoapods, spm, uikit, combine, core data
- **Specialization**: iOS native development, SwiftUI, UIKit, iOS architecture patterns

**android-development-agent** → **[android-specialist.md](engineering/android-specialist.md)**

- **Primary**: `android-workflow.md`, `tdd-mobile-workflow.md`
- **Supporting**: `development-rules.md`, `git-workflow.md`, `resource-management.md`, `validate-workflow.md`
- **Keywords**: android, kotlin, jetpack compose, gradle, room, hilt, dagger, coroutines
- **Specialization**: Android native development, Jetpack Compose, MVVM, Clean Architecture

**apk-modification-agent** → **[apk-modifier.md](engineering/apk-modifier.md)**

- **Primary**: `android-workflow.md`, `development-rules.md`
- **Supporting**: `git-workflow.md`, `validate-workflow.md`, `file-protection-rules.md`
- **Keywords**: apk, reverse engineering, firebase, safeads, smali, decompilation
- **Specialization**: APK reverse engineering, Firebase integration, SafeAds implementation

**frontend-development-agent** → **[frontend-developer.md](engineering/frontend-developer.md)**

- **Primary**: `frontend-rules.md`, `shadcn-ui-rules.md`, `development-rules.md`
- **Supporting**: `git-workflow.md`, `validate-workflow.md`, `i18n-rules.md`
- **Keywords**: react, nextjs, typescript, tailwind, zustand, vite, webpack, pwa
- **Specialization**: React 18+, Next.js 14+, modern frontend development

**backend-development-agent** → **[backend-architect.md](engineering/backend-architect.md)**

- **Primary**: `development-rules.md`, `database-management.md`, `api-integration-rules.md`
- **Supporting**: `git-workflow.md`, `validate-workflow.md`, `terminal-rules.md`
- **Keywords**: php, laravel, nodejs, express, fastify, mysql, postgresql, redis, api
- **Specialization**: PHP/Laravel, Node.js/TypeScript, RESTful APIs, microservices

**mobile-crossplatform-agent** → **[cross-platform-specialist.md](engineering/cross-platform-specialist.md)**

- **Primary**: `tdd-mobile-workflow.md`, `development-rules.md`
- **Supporting**: `ios-workflow.md`, `android-workflow.md`, `git-workflow.md`, `validate-workflow.md`
- **Keywords**: react native, flutter, dart, expo, cross platform, hybrid mobile
- **Specialization**: React Native, Flutter, cross-platform mobile development

**devops-infrastructure-agent** → **[devops-automator.md](engineering/devops-automator.md)**

- **Primary**: `infrastructure-rules.md`, `git-workflow.md`, `terminal-rules.md`
- **Supporting**: `development-rules.md`, `validate-workflow.md`, `file-protection-rules.md`
- **Keywords**: devops, ci/cd, docker, kubernetes, terraform, aws, gcp, azure, helm
- **Specialization**: Cloud infrastructure, containerization, CI/CD pipelines, monitoring

**ai-engineer** → `ai-execution-templates.md`, `ai-code-quality-automation.md`

- Keywords: ai, ml, machine learning, neural network, tensorflow, pytorch

**rapid-prototyper** → `development-rules.md`, `api-integration-rules.md`, `frontend-rules.md`

- Keywords: prototype, mvp, poc, quick, fast, demo, rapid

### Product Management Agents

**sprint-prioritizer** → `task-creation-workflow.md`, `planning-enforcement.md`

- Keywords: sprint, planning, prioritization, task, agile, scrum

**feedback-synthesizer** → `user-intent-analysis-workflow.md`, `kiro-dynamic-workflow.md`

- Keywords: feedback, user, requirements, analysis, user story

**trend-researcher** → `brainstorm-overview.md`, `expert-brainstorm-workflow.md`

- Keywords: research, market, trend, analysis, competitor

### Marketing Agents

**content-creator** → `markdown-unified-rules.md`

- Keywords: content, blog, article, documentation, writing, marketing

**app-store-optimizer** → `android-aso-package-workflow.md`

- Keywords: aso, app store, play store, optimization, keywords

**growth-hacker** → `feature-suggestion-engine.md`

- Keywords: growth, acquisition, viral, retention, conversion

### Design & Testing Agents

**ui-designer** → `frontend-rules.md`, `shadcn-ui-rules.md`

- Keywords: ui, design, interface, component, figma

**api-tester** → `api-integration-rules.md`, `backend-rules-optimized.md`

- Keywords: api, testing, integration, endpoint, postman

**performance-benchmarker** → `development-rules.md`

- Keywords: performance, benchmark, optimization, speed, load test

## 🔄 Selection Algorithm

```yaml
Selection Process:
  1. Context Analysis Engine analyzes user request
  2. Extract keywords, project type, and technical requirements
  3. Calculate confidence scores using Agent Registry
  4. Apply Selection Decision Matrix with priority rules
  5. Select highest scoring agent (threshold: 0.75)
  6. Load agent-specific rules and workflows
  7. Execute with specialized context and knowledge base

Agent Priority Rules:
  - Exact keyword match: +0.3 confidence
  - Technology stack alignment: +0.2 confidence
  - Project context match: +0.15 confidence
  - Specialization relevance: +0.1 confidence

Fallback Chain:
  1. If no agent scores > 0.75 → Use agent-selector-system
  2. If multiple agents tie → Select most specialized
  3. If context unclear → Prompt for clarification
  4. Ultimate fallback → Use general development rules

Performance Monitoring:
  - Track agent selection accuracy
  - Monitor task completion rates per agent
  - Collect user satisfaction feedback
  - Continuous learning algorithm for improvement
```

## 📋 Workflow Index Reference

### Complete Workflow Mapping

**📖 [Agent Workflow Index](./agent-workflow-index.md)** - Comprehensive workflow mapping for all agents

### Workflow Categories

- **Primary Workflows**: Core workflows that define agent functionality
- **Supporting Workflows**: Common workflows used across scenarios
- **Specialized Workflows**: Advanced workflows for specific use cases
- **Core Workflows**: Universal workflows applicable to all agents

### Quick Access Links

- **📱 iOS**: [iOS Workflows](./agent-workflow-index.md#📱-ios-development-agent)
- **🤖 Android**: [Android Workflows](./agent-workflow-index.md#🤖-android-development-agent)
- **🔧 APK Mod**: [APK Modification Workflows](./agent-workflow-index.md#🔧-apk-modification-agent)
- **🌐 Frontend**: [Frontend Workflows](./agent-workflow-index.md#🌐-frontend-development-agent)
- **⚙️ Backend**: [Backend Workflows](./agent-workflow-index.md#⚙️-backend-development-agent)
- **📱 Cross-platform**: [Cross-platform Workflows](./agent-workflow-index.md#📱-mobile-cross-platform-agent)
- **🚀 DevOps**: [DevOps Workflows](./agent-workflow-index.md#🚀-devops-infrastructure-agent)

## 🎯 IDE Integration

### Trae AI Native Integration

- **Agent Activation**: Automatic agent selection based on context analysis
- **Rule Loading**: Dynamic loading of agent-specific rules from `.ai-system/`
- **Workflow Integration**: Seamless integration with indexed workflows
- **Performance Tracking**: Built-in metrics and feedback collection

### Cross-IDE Compatibility

- **Cursor**: Uses this mapping via `.ai-system/` with fallback to `.cursor/rules/`
- **Trae**: Native agent system with specialized rule loading
- **Gemini**: Aliases via `GEMINI.md` with agent context
- **Claude**: Direct agent selection with context awareness

### Agent System Features

- **Specialized Knowledge**: Each agent has domain-specific expertise
- **Workflow Index**: Organized workflow access with priority levels
- **Best Practices**: Industry-standard practices and conventions
- **Integration Points**: Seamless connection with external services and tools
- **Quality Assurance**: Built-in checklists and validation rules

### Usage Examples

```yaml
# iOS Development
User Input: "Create a SwiftUI login screen"
Selected Agent: ios-development-agent
Loaded Workflows: ios-workflow.md, ios-project-template.md, tdd-mobile-workflow.md

# Backend API
User Input: "Build a Laravel REST API"
Selected Agent: backend-development-agent
Loaded Workflows: development-rules.md, database-management.md, api-integration-rules.md

# APK Modification
User Input: "Integrate Firebase into APK"
Selected Agent: apk-modification-agent
Loaded Workflows: android-workflow.md, development-rules.md, file-protection-rules.md
```

---

**Compact Design**: Keeps context usage minimal while maintaining functionality.
