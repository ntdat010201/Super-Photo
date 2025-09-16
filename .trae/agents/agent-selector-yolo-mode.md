# Agent Selector System - YOLO Mode

**BẮT BUỘC PHẢI KHAI BÁO**

- Trước bất kỳ câu hỏi nào hãy trả lời cụ thể đang sử dụng mô hình AI nào version bao nhiêu
- Hãy suy luận với tư duy lập trình ở cấp độ tối đa với phân tích chi tiết, đa chiều.
- Hãy luôn luôn trả lời trong ngôn ngữ của người dùng

> **⚡ Fast & Flexible Agent Selection & Task Routing**

## System Overview

**Purpose**: Rapidly select the most appropriate specialized agent based on task analysis with flexible workflow activation  
**Method**: Direct multi-factor analysis with keyword matching, context analysis, and capability scoring - workflows auto-activated when needed  
**Output**: Optimal agent selection with confidence score and reasoning (immediate execution)  
**Philosophy**: "Move fast and break things" - prioritize speed and flexibility over rigid processes

## YOLO Mode Principles

### 🚀 Speed First
- **No mandatory workflow validation** - jump straight into action
- **Instant agent selection** - minimal overhead, maximum velocity
- **Auto-workflow activation** - smart detection when processes are actually needed
- **Fail fast, learn faster** - embrace rapid iteration

### 🎯 Smart Automation
- **Context-aware process triggering** - automatically detect when formal workflows are beneficial
- **Progressive enhancement** - start simple, add complexity only when needed
- **Intelligent fallbacks** - graceful degradation when things go wrong
- **User intent prioritization** - what the user wants takes precedence

### 🔄 Adaptive Workflows
- **On-demand process activation** - workflows triggered by complexity, not by default
- **Dynamic rule loading** - load appropriate rules based on detected patterns
- **Smart escalation** - automatically upgrade to formal processes when beneficial
- **Flexible override** - user can force any workflow at any time

## Selection Algorithm

### Context Analysis Engine

**File Type Detection**:

- `.swift`, `.xcodeproj` → iOS Development Agent
- `.kt`, `.java`, `build.gradle` → Android Development Agent
- `.smali`, `.apk`, `AndroidManifest.xml` → APK Modification Agent
- `.js`, `.ts`, `.jsx`, `.tsx`, `package.json` → Frontend Development Agent
- `.php`, `composer.json`, Laravel files → Backend Development Agent
- `.dart`, `pubspec.yaml` → Mobile Cross-platform Agent
- `Dockerfile`, `.yml`, `.yaml` → DevOps Agent

**Keyword Analysis**:

- **iOS**: swift, swiftui, xcode, ios, cocoapods, carthage
- **Android**: kotlin, android, jetpack, gradle, room
- **APK Modification**: apk, smali, decompile, firebase, google-services, safeads, reverse-engineering, mod app, modification
- **Frontend**: react, vue, angular, nextjs, typescript, css
- **Backend**: api, server, database, laravel, express, nodejs
- **Cross-platform**: flutter, react-native, dart, expo
- **DevOps**: docker, kubernetes, ci/cd, deployment, aws

**Complexity Assessment**:

- **Simple**: Single file changes, bug fixes, minor updates → Direct execution
- **Medium**: Feature additions, refactoring, multi-file changes → Consider workflow activation
- **Complex**: Architecture changes, new modules, system integration → Auto-trigger appropriate workflows

### Agent Capability Matching

**YOLO Mode Scoring Factors**:

1. **Keyword Match** (40%): Direct technology/framework alignment (increased weight for speed)
2. **File Type** (30%): Project structure and file extensions (increased weight for accuracy)
3. **Complexity** (15%): Agent's capability to handle task complexity (reduced weight for speed)
4. **Performance** (10%): Historical success rate and efficiency (reduced weight for speed)
5. **User Preference** (5%): Previous selections and feedback

**YOLO Selection Logic**:

```
if (score >= 0.60) → Immediate High Confidence Selection
if (score >= 0.40) → Fast Medium Confidence Selection
if (score >= 0.25) → Quick Low Confidence Selection with user notification
if (score < 0.25) → Rapid fallback to general agent with context notes
```

**Priority Hierarchy**:

1. **`.trae/agents/`** - **PRIMARY SOURCE** (Highest Priority)
2. **User intent keywords** - Direct user signals
3. **`.cursor/rules/`** - Secondary reference
4. Other rule sources - Lowest priority

## Agent Profiles

### iOS Development Agent

**Link**: [ios-specialist.md](../../.ai-system/agents/profiles/ios-specialist.md)  
**Specialization**: Native iOS apps with Swift/SwiftUI  
**Strengths**: iOS frameworks, App Store guidelines, Apple ecosystem  
**Triggers**: iOS-specific keywords, .swift files, Xcode projects  
**YOLO Activation**: Instant for iOS-related tasks

### Android Development Agent

**Link**: [android-specialist.md](../../.ai-system/agents/profiles/android-specialist.md)  
**Specialization**: Native Android apps with Kotlin/Java  
**Strengths**: Android SDK, Jetpack libraries, Google Play guidelines  
**Triggers**: Android-specific keywords, .kt/.java files, Gradle builds  
**YOLO Activation**: Instant for Android-related tasks

### Frontend Development Agent

**Link**: [frontend-developer.md](../../.ai-system/agents/profiles/frontend-developer.md)  
**Specialization**: Web frontend with React/Vue/Angular  
**Strengths**: Modern JavaScript, CSS frameworks, build tools  
**Triggers**: Frontend keywords, .js/.ts files, package.json  
**YOLO Activation**: Instant for web frontend tasks

### Backend Development Agent

**Link**: [backend-architect.md](../../.ai-system/agents/profiles/backend-architect.md)  
**Specialization**: Server-side APIs and databases  
**Strengths**: RESTful APIs, database design, server architecture  
**Triggers**: Backend keywords, API endpoints, database schemas  
**YOLO Activation**: Instant for server-side tasks

### Mobile Cross-platform Agent

**Link**: [cross-platform-specialist.md](../../.ai-system/agents/profiles/cross-platform-specialist.md)  
**Specialization**: Flutter and React Native development  
**Strengths**: Cross-platform frameworks, shared codebase  
**Triggers**: Flutter/RN keywords, .dart files, pubspec.yaml  
**YOLO Activation**: Instant for cross-platform mobile tasks

### APK Modification Agent

**Link**: [apk-modifier.md](../../.ai-system/agents/profiles/apk-modifier.md)  
**Specialization**: APK reverse engineering, Google Services integration, Firebase SDK updates  
**Strengths**: Smali analysis, SDK integration, method limit management, SafeAds implementation  
**Triggers**: APK modification keywords, mod app, modification, .smali files, Firebase integration, Google Services  
**YOLO Activation**: Instant for APK modification tasks

### DevOps Infrastructure Agent

**Link**: [devops-automator.md](../../.ai-system/agents/profiles/devops-automator.md)  
**Specialization**: Deployment, CI/CD, infrastructure  
**Strengths**: Docker, Kubernetes, cloud platforms, automation  
**Triggers**: DevOps keywords, Docker files, YAML configs  
**YOLO Activation**: Instant for infrastructure tasks

## YOLO Selection Process

### Step 1: Rapid Analysis (< 1 second)

1. **🚀 INSTANT START**: No mandatory workflow validation - begin immediately
2. **Quick keyword scan**: Parse user request for technology indicators
3. **Fast file type detection**: Analyze project structure rapidly
4. **Complexity estimation**: Quick assessment of task scope
5. **Technology stack identification**: Primary framework/language detection

### Step 2: Lightning Agent Scoring (< 0.5 seconds)

1. **Rapid keyword matching**: Fast scoring based on technology alignment
2. **File type bonuses**: Quick context-based scoring adjustments
3. **Simplified complexity weighting**: Streamlined capability assessment
4. **Performance shortcuts**: Use cached performance data
5. **Instant confidence calculation**: Generate final scores rapidly

### Step 3: Immediate Decision (< 0.5 seconds)

1. **Fast selection**: Choose highest scoring agent if confidence > 0.4
2. **Quick fallback**: Default to general agent if all scores < 0.25
3. **Rapid notification**: Brief confidence level communication
4. **Instant activation**: Begin task execution immediately

## Smart Workflow Auto-Activation

### Complexity-Based Triggers

**Simple Tasks** (Direct Execution):
- Single file modifications
- Bug fixes
- Minor feature additions
- Configuration changes

**Medium Tasks** (Optional Workflow Suggestion):
- Multi-file refactoring
- New feature implementation
- API integration
- Database schema changes

**Complex Tasks** (Auto-Workflow Activation):
- Architecture redesign
- New module creation
- System integration
- Performance optimization
- Security implementation

### Auto-Triggered Workflows

**When to Auto-Activate**:

```
if (task_complexity > 7/10) → Auto-trigger planning workflow
if (multiple_agents_needed) → Auto-trigger coordination workflow
if (testing_required) → Auto-suggest TDD workflow
if (deployment_involved) → Auto-trigger DevOps workflow
if (user_requests_quality) → Auto-trigger review workflow
```

**Smart Workflow Detection**:

- **Planning needed**: Multiple interconnected changes detected
- **Testing beneficial**: Critical functionality modifications
- **Review valuable**: Security or performance sensitive code
- **Documentation required**: Public API or complex logic changes

### Workflow Activation Messages

**Gentle Suggestions**:
```
💡 Tip: This looks complex - want me to create a quick plan first?
🧪 Suggestion: Testing might be valuable here - shall I set that up?
👥 Note: This might benefit from a review - want me to prepare that?
```

**Auto-Activations**:
```
🚀 Auto-activating planning workflow for this complex task
🔧 Setting up testing environment for critical changes
📋 Preparing review checklist for security-sensitive code
```

## Fallback Mechanisms

**Multi-Agent Tasks**: Route to primary agent with auto-collaboration setup  
**Unclear Context**: Quick clarification request, then proceed with best guess  
**Low Confidence**: Proceed with general agent + context notes  
**New Technologies**: Rapid research + closest match agent assignment

## Performance Optimization

**Speed Targets**:
- **Selection Time**: < 2 seconds total
- **Analysis Phase**: < 1 second
- **Scoring Phase**: < 0.5 seconds
- **Decision Phase**: < 0.5 seconds

**Optimization Techniques**:
- **Cached Patterns**: Store common selection patterns
- **Parallel Processing**: Simultaneous analysis and scoring
- **Smart Shortcuts**: Skip unnecessary validations
- **Predictive Loading**: Pre-load likely agents

## Integration Points

**Input Sources**:

- User natural language requests (primary)
- Project file structure analysis (secondary)
- Previous conversation context (tertiary)
- User preference history (optional)

**Output Targets**:

- Immediate agent activation
- Quick confidence score reporting
- Brief selection reasoning
- Optional workflow suggestions

## Quality Assurance

**YOLO Validation Rules**:

- **Minimum confidence threshold**: 0.25 (lowered for speed)
- **Maximum response time**: < 2 seconds (enforced)
- **Fallback agent availability**: Always ready
- **Selection reasoning**: Brief but clear

**Error Handling**:

- **Fast failure**: Quick error detection and recovery
- **Graceful degradation**: Always have a working fallback
- **User notification**: Brief error communication
- **Auto-retry**: Attempt alternative approaches
- **Learning integration**: Capture failures for improvement

## YOLO Mode Activation

**Trigger Keywords**:
- "yolo", "quick", "fast", "rapid", "asap", "now"
- "skip process", "no workflow", "direct", "immediate"
- "just do it", "move fast", "prototype", "experiment"

**Auto-Detection**:
- Simple task patterns
- Urgent user language
- Prototype/experiment context
- Time-sensitive requests

**Manual Override**:
- User can force YOLO mode: `--yolo` or `--fast`
- User can force formal workflow: `--formal` or `--process`
- User can request specific workflow: `--tdd`, `--review`, etc.

## Success Metrics

**Speed Metrics**:
- Selection time < 2 seconds (target: 95%)
- Task completion velocity increase (target: 40%)
- User satisfaction with speed (target: 4.5/5)

**Quality Metrics**:
- Task success rate (target: 80%+)
- Code quality maintenance (target: 8.0/10+)
- User satisfaction with results (target: 4.0/5+)

**Balance Metrics**:
- Appropriate workflow activation rate (target: 70%)
- False positive workflow triggers (target: <10%)
- User override frequency (target: <15%)

---

**⚡ YOLO Mode: Move fast, stay smart, deliver results**  
**🎯 When speed matters but quality still counts**  
**🚀 Activation**: Triggered by speed keywords or simple task patterns  
**🔄 Integration**: Works with all specialized development agents  
**📈 Maintenance**: Self-optimizing system with continuous learning**