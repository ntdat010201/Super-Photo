---
trae_context:
  format: "native"
  version: "1.0"
  agent_type: "documentation"
  last_updated: "2025-01-18T07:30:00.000Z"
---

# Trae AI Agent System Documentation

> **🤖 Comprehensive guide to using specialized development agents in Trae AI**

## 📋 Table of Contents

1. [Overview](#overview)
2. [Available Agents](#available-agents)
3. [Agent Selection Process](#agent-selection-process)
4. [Usage Examples](#usage-examples)
5. [Integration Guide](#integration-guide)
6. [Best Practices](#best-practices)
7. [Troubleshooting](#troubleshooting)

## 🎯 Overview

The Trae AI Agent System provides specialized development agents that automatically activate based on your project context and requirements. Each agent brings domain-specific expertise, code templates, and best practices to accelerate your development workflow.

### Key Features

- **🎯 Automatic Agent Selection**: Context-aware agent selection based on keywords and project analysis
- **📚 Specialized Knowledge**: Each agent has deep expertise in specific technologies and frameworks
- **🔧 Code Templates**: Pre-built patterns and examples for rapid development
- **✅ Quality Assurance**: Built-in checklists and validation rules
- **🔄 Workflow Integration**: Seamless integration with existing Trae AI workflows
- **📊 Performance Tracking**: Metrics and feedback collection for continuous improvement
- **🏗️ Clean Architecture Code Base**: Mandatory structure-only phase before detailed implementation

## 🏗️ NEW: Clean Architecture Code Base Stage

**🔴 CRITICAL WORKFLOW ADDITION**

### Workflow Sequence
```
Brainstorm → Kiro Tasks → Clean Architecture Code Base → Detailed Implementation
```

### Purpose
This mandatory stage ensures AI creates **ONLY architectural structure** without detailed implementation, preventing common issues where AI jumps directly to feature implementation and breaks project architecture.

### Key Rules
- ❌ **NO business logic implementation**
- ❌ **NO detailed UI components**
- ❌ **NO API implementation**
- ✅ **ONLY folder structure and empty classes**
- ✅ **ONLY method signatures with TODO comments**
- ✅ **ONLY interfaces and abstract classes**

### Validation Required
User must confirm "Code base chỉ có structure" before proceeding to detailed implementation.

**Workflow File**: `.ai-system/workflows/development/clean-architecture-codebase-workflow.md`

## 🤖 Available Agents

### Mobile Development

#### iOS Development Agent
- **File**: `ios-development-agent.md`
- **Keywords**: ios, swift, swiftui, xcode, cocoapods, spm, uikit, combine, core data
- **Specialization**: iOS native development, SwiftUI, UIKit, iOS architecture patterns
- **Use Cases**: iOS apps, SwiftUI interfaces, Core Data integration, iOS-specific features

#### Android Development Agent
- **File**: `android-development-agent.md`
- **Keywords**: android, kotlin, jetpack compose, gradle, room, hilt, dagger, coroutines
- **Specialization**: Android native development, Jetpack Compose, MVVM, Clean Architecture
- **Use Cases**: Android apps, Jetpack Compose UI, Room database, dependency injection

#### Mobile Cross-platform Agent
- **File**: `mobile-crossplatform-agent.md`
- **Keywords**: react native, flutter, dart, expo, cross platform, hybrid mobile
- **Specialization**: React Native, Flutter, cross-platform mobile development
- **Use Cases**: Cross-platform apps, React Native projects, Flutter applications

### Web Development

#### Frontend Development Agent
- **File**: `frontend-development-agent.md`
- **Keywords**: react, nextjs, typescript, tailwind, zustand, vite, webpack, pwa
- **Specialization**: React 18+, Next.js 14+, modern frontend development
- **Use Cases**: React applications, Next.js projects, TypeScript development, PWAs

#### Backend Development Agent
- **File**: `backend-development-agent.md`
- **Keywords**: php, laravel, nodejs, express, fastify, mysql, postgresql, redis, api
- **Specialization**: PHP/Laravel, Node.js/TypeScript, RESTful APIs, microservices
- **Use Cases**: REST APIs, Laravel applications, Node.js services, database design

### Infrastructure

#### DevOps/Infrastructure Agent
- **File**: `devops-infrastructure-agent.md`
- **Keywords**: devops, ci/cd, docker, kubernetes, terraform, aws, gcp, azure, helm
- **Specialization**: Cloud infrastructure, containerization, CI/CD pipelines, monitoring
- **Use Cases**: Infrastructure as Code, containerization, deployment pipelines, monitoring

### System Components

#### Agent Selector System
- **File**: `agent-selector-system.md`
- **Purpose**: Automatic agent selection and fallback management
- **Features**: Context analysis, confidence scoring, performance monitoring

#### Agent Workflow Mapping
- **File**: `agent-workflow-mapping.md`
- **Purpose**: Maps agents to workflows and defines selection rules
- **Features**: Keyword mapping, priority rules, IDE integration

## 🔄 Agent Selection Process

### Automatic Selection

1. **Context Analysis**: Trae AI analyzes your input for keywords, project type, and technical requirements
2. **Agent Scoring**: Each agent receives a confidence score based on relevance
3. **Selection**: The highest scoring agent (threshold: 0.75) is automatically selected
4. **Rule Loading**: Agent-specific rules and workflows are loaded
5. **Execution**: Your request is processed with specialized context and knowledge

### Selection Criteria

```yaml
Priority Rules:
  - Exact keyword match: +0.3 confidence
  - Technology stack alignment: +0.2 confidence
  - Project context match: +0.15 confidence
  - Specialization relevance: +0.1 confidence

Fallback Chain:
  1. No agent scores > 0.75 → Use agent-selector-system
  2. Multiple agents tie → Select most specialized
  3. Context unclear → Prompt for clarification
  4. Ultimate fallback → Use general development rules
```

## 💡 Usage Examples

### iOS Development
```
User Input: "Create a SwiftUI login screen with biometric authentication"

Selected Agent: ios-development-agent
Loaded Rules: ios-development-agent.md, ios-workflow.md
Result: SwiftUI code with Face ID/Touch ID integration
```

### Android Development
```
User Input: "Build an Android app with Jetpack Compose and Room database"

Selected Agent: android-development-agent
Loaded Rules: android-development-agent.md, android-workflow.md
Result: Kotlin code with Compose UI and Room integration
```

### Frontend Development
```
User Input: "Create a Next.js dashboard with TypeScript and Tailwind CSS"

Selected Agent: frontend-development-agent
Loaded Rules: frontend-development-agent.md, frontend-rules.md
Result: Next.js 14+ project with TypeScript and Tailwind setup
```

### Backend Development
```
User Input: "Develop a Laravel API with authentication and CRUD operations"

Selected Agent: backend-development-agent
Loaded Rules: backend-development-agent.md, backend-rules-optimized.md
Result: Laravel API with Sanctum auth and resource controllers
```

### DevOps/Infrastructure
```
User Input: "Set up Kubernetes deployment with Terraform on AWS"

Selected Agent: devops-infrastructure-agent
Loaded Rules: devops-infrastructure-agent.md, infrastructure-rules.md
Result: Terraform configurations and Kubernetes manifests
```

## 🔧 Integration Guide

### Trae AI Native Integration

The agent system is natively integrated into Trae AI:

1. **Automatic Activation**: Agents activate automatically based on your input
2. **Rule Loading**: Agent-specific rules are loaded from `.ai-system/rules/` and `.trae/agents/`
3. **Context Awareness**: Agents understand your project context and requirements
4. **Performance Tracking**: Built-in metrics track agent effectiveness

### Manual Agent Selection

You can manually specify an agent by including keywords:

```
"Use iOS agent to create a SwiftUI view"
"With Android agent, build a Compose screen"
"Frontend agent: create a React component"
```

### Cross-IDE Compatibility

- **Cursor**: Uses agent mapping via `.ai-system/rules/` with fallback to `.cursor/rules/`
- **Trae**: Native agent system with specialized rule loading
- **Other IDEs**: Agent rules can be referenced as standard markdown files

## ✅ Best Practices

### For Users

1. **Be Specific**: Include technology names and frameworks in your requests
2. **Provide Context**: Mention your project type and requirements
3. **Use Keywords**: Include relevant keywords to trigger the right agent
4. **Review Results**: Check that the selected agent matches your needs

### For Developers

1. **Maintain Agents**: Keep agent rules updated with latest best practices
2. **Monitor Performance**: Track agent selection accuracy and user satisfaction
3. **Update Keywords**: Add new keywords as technologies evolve
4. **Test Integration**: Ensure agents work correctly across different IDEs

### Agent Development Guidelines

1. **Specialized Focus**: Each agent should have a clear, specific domain
2. **Comprehensive Examples**: Include practical code examples and templates
3. **Best Practices**: Incorporate industry-standard practices and conventions
4. **Quality Checklists**: Provide validation rules and quality gates
5. **Integration Points**: Define clear integration with external tools and services

## 🔍 Troubleshooting

### Common Issues

#### Wrong Agent Selected
- **Cause**: Ambiguous keywords or context
- **Solution**: Be more specific with technology names and requirements
- **Example**: Instead of "create an app", use "create an iOS app with SwiftUI"

#### No Agent Selected
- **Cause**: No agent confidence score above threshold (0.75)
- **Solution**: Include more specific keywords or manually specify agent
- **Fallback**: System will use general development rules

#### Agent Rules Not Loading
- **Cause**: File path issues or missing agent files
- **Solution**: Check that agent files exist in `.ai-system/rules/` or `.trae/agents/`
- **Verification**: Ensure file names match the agent-workflow-mapping.md

### Performance Issues

#### Slow Agent Selection
- **Cause**: Complex context analysis or large rule files
- **Solution**: Optimize agent rules and reduce file sizes
- **Monitoring**: Check agent selection metrics

#### Inconsistent Results
- **Cause**: Conflicting rules or outdated agent knowledge
- **Solution**: Update agent rules and resolve conflicts
- **Testing**: Validate agent behavior with test cases

### Getting Help

1. **Check Logs**: Review Trae AI logs for agent selection details
2. **Validate Rules**: Ensure agent rule files are properly formatted
3. **Test Keywords**: Verify that keywords trigger the expected agent
4. **Update System**: Keep Trae AI and agent rules up to date

## 📊 Performance Metrics

The agent system tracks several metrics:

- **Selection Accuracy**: Percentage of correct agent selections
- **Task Completion Rate**: Success rate per agent
- **User Satisfaction**: Feedback scores for agent performance
- **Response Time**: Time taken for agent selection and rule loading
- **Fallback Rate**: Frequency of fallback to general rules

## 🚀 Future Enhancements

- **Machine Learning**: Improve agent selection using ML algorithms
- **Custom Agents**: Allow users to create custom specialized agents
- **Agent Collaboration**: Enable multiple agents to work together on complex tasks
- **Real-time Updates**: Dynamic agent rule updates without restart
- **Advanced Analytics**: Detailed performance analytics and insights

---

**📝 Note**: This documentation is automatically updated as the agent system evolves. For the latest information, refer to the individual agent files in `.ai-system/rules/` and `.trae/agents/`.

**🔗 Related Files**:
- [Agent Selector System](./agents/agent-selector-system.md)
- [Agent Workflow Mapping](./agents/agent-workflow-mapping.md)
- [iOS Development Agent](./agents/ios-development-agent.md)
- [Android Development Agent](./agents/android-development-agent.md)
- [Frontend Development Agent](./agents/frontend-development-agent.md)
- [Backend Development Agent](./agents/backend-development-agent.md)
- [Mobile Cross-platform Agent](./agents/mobile-crossplatform-agent.md)
- [DevOps Infrastructure Agent](./agents/devops-infrastructure-agent.md)