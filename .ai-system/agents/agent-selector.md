# Agent Selector System

**BẮT BUỘC PHẢI KHAI BÁO**

- Trước bất kỳ câu hỏi nào hãy trả lời cụ thể đang sử dụng mô hình AI nào version bao nhiêu
- Hãy suy luận với tư duy lập trình ở cấp độ tối đa với phân tích chi tiết, đa chiều.
- Hãy luôn luôn trả lời trong ngôn ngữ của người dùng

> **🎯 Intelligent Agent Selection & Task Routing**

## System Overview

**Purpose**: Automatically select the most appropriate specialized agent based on task analysis with mandatory Test-First workflow validation  
**Method**: Test Spec Driven Development Review Workflow validation first, then multi-factor analysis with keyword matching, context analysis, and capability scoring  
**Output**: Optimal agent selection with confidence score and reasoning (only after workflow validation passes)  
**Critical Requirement**: All projects must have Test Spec Driven Development Review Workflow and Kiro Specs structure before agent selection proceeds

{
  "🔍 QUICK_REFERENCE_INDEX": {
    "🔍 FILE_TYPE_DETECTION": "Lines 40-48: File extensions → Agent mapping (.swift→iOS, .kt→Android, etc.)",
    "🔑 KEYWORD_ANALYSIS": "Lines 50-58: Technology keywords for each platform",
    "👥 AGENT_PROFILES": "Lines 70-105: Complete agent list with links and specializations",
    "📱 iOS_AGENT": "Lines 72-76: iOS Development Agent details",
    "🤖 ANDROID_AGENT": "Lines 78-82: Android Development Agent details",
    "🌐 FRONTEND_AGENT": "Lines 84-88: Frontend Development Agent details",
    "⚙️ BACKEND_AGENT": "Lines 90-94: Backend Development Agent details",
    "📱 CROSSPLATFORM_AGENT": "Lines 96-100: Mobile Cross-platform Agent details",
    "🔧 APK_MODIFIER_AGENT": "Lines 102-106: APK Modification Agent details",
    "🚀 DEVOPS_AGENT": "Lines 108-112: DevOps Infrastructure Agent details",
    "🔄 SELECTION_PROCESS": "Lines 114-170: Step-by-step agent selection workflow",
    "⚙️ OPERATION_MODES": "Lines 130-170: YOLO/Standard/Auto mode configurations",
    "📊 SCORING_ALGORITHM": "Lines 210-220: Agent scoring and confidence thresholds",
    "🛡️ QUALITY_ASSURANCE": "Lines 250-318: Validation rules and error handling",
    "🎯 OPTIMIZATION_NOTE": "AI chỉ cần đọc lines 1-112 để có đầy đủ thông tin agent selection + platform detection"
  }
}



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

- **Simple**: Single file changes, bug fixes, minor updates
- **Medium**: Feature additions, refactoring, multi-file changes
- **Complex**: Architecture changes, new modules, system integration

### Agent Capability Matching

**Scoring Factors**:

1. **Keyword Match** (35%): Direct technology/framework alignment
2. **File Type** (25%): Project structure and file extensions
3. **Complexity** (20%): Agent's capability to handle task complexity
4. **Performance** (15%): Historical success rate and efficiency
5. **User Preference** (5%): Previous selections and feedback

**Selection Logic**:

```
if (score >= 0.75) → High Confidence Selection
if (score >= 0.55) → Medium Confidence Selection
if (score >= 0.35) → Low Confidence Selection
if (score < 0.35) → Request Clarification
```

**Priority Hierarchy**:

1. **`.ai-system/agents/`** - **PRIMARY SOURCE** (Highest Priority)
2. **`.cursor/rules/`** - Secondary reference
3. Other rule sources - Lowest priority

## Agent Profiles

### iOS Development Agent

**Link**: [ios-specialist.md](engineering/ios-specialist.md)  
**Specialization**: Native iOS apps with Swift/SwiftUI  
**Strengths**: iOS frameworks, App Store guidelines, Apple ecosystem  
**Triggers**: iOS-specific keywords, .swift files, Xcode projects

### Android Development Agent

**Link**: [android-developer.md](engineering/android-developer.md)  
**Specialization**: Native Android apps with Kotlin/Java  
**Strengths**: Android SDK, Jetpack libraries, Google Play guidelines  
**Triggers**: Android-specific keywords, .kt/.java files, Gradle builds

### Frontend Development Agent

**Link**: [frontend-developer.md](engineering/frontend-developer.md)  
**Specialization**: Web frontend with React/Vue/Angular  
**Strengths**: Modern JavaScript, CSS frameworks, build tools  
**Triggers**: Frontend keywords, .js/.ts files, package.json

### Backend Development Agent

**Link**: [backend-architect.md](engineering/backend-architect.md)  
**Specialization**: Server-side APIs and databases  
**Strengths**: RESTful APIs, database design, server architecture  
**Triggers**: Backend keywords, API endpoints, database schemas

### Mobile Cross-platform Agent

**Link**: [cross-platform-specialist.md](engineering/cross-platform-specialist.md)  
**Specialization**: Flutter and React Native development  
**Strengths**: Cross-platform frameworks, shared codebase  
**Triggers**: Flutter/RN keywords, .dart files, pubspec.yaml

### APK Modification Agent

**Link**: [apk-modifier.md](specialized/apk-modifier.md)  
**Specialization**: APK reverse engineering, Google Services integration, Firebase SDK updates  
**Strengths**: Smali analysis, SDK integration, method limit management, SafeAds implementation  
**Triggers**: APK modification keywords, mod app, modification, .smali files, Firebase integration, Google Services

### DevOps Infrastructure Agent

**Link**: [devops-automator.md](engineering/devops-automator.md)  
**Specialization**: Deployment, CI/CD, infrastructure  
**Strengths**: Docker, Kubernetes, cloud platforms, automation  
**Triggers**: DevOps keywords, Docker files, YAML configs

## Selection Process

### Step 0: Project Identity & Operation Mode Check

**🔴 MANDATORY FIRST CHECK: .project-identity Analysis**

1. **Project Identity Validation**:

   - Read and analyze `.project-identity` file
   - Extract `projectType`, `projectStage`, `mainLanguages`, `mainFrameworks`
   - Load appropriate workflow rules based on current stage
   - Apply `platformSpecificRules` if available
   - Check `integrations` and enabled `features`

2. **Operation Mode Detection**:

   - Check for `operationMode` setting in `.project-identity`
   - Available modes:
     - `"yolo"`: Fast-track selection with minimal validation
     - `"standard"`: Full workflow with mandatory validations
     - `"auto"`: Intelligent mode selection based on task complexity

3. **Operation Mode Configuration**:

   ```json
   "operationMode": {
     "mode": "yolo|standard",
     "description": "Agent selection behavior preference"
   }
   ```

4. **User Prompt for Missing Operation Mode**:

   ```
   🤖 CHỌN CHỂ ĐỘ HOẠT ĐỘNG:

   1️⃣ **YOLO Mode** - Nhanh gọn, bỏ qua một số kiểm tra
      ✅ Phù hợp: Task đơn giản, cần tốc độ
      ⚠️  Ít validation, có thể thiếu sót

   2️⃣ **Standard Mode** - Đầy đủ quy trình, kiểm tra kỹ lưỡng
      ✅ Phù hợp: Dự án quan trọng, cần chất lượng cao
      ⏱️  Chậm hơn nhưng an toàn và chính xác

   3️⃣ **Auto Mode** - Tự động chọn dựa trên độ phức tạp
      ✅ Phù hợp: Muốn cân bằng tốc độ và chất lượng

   Bạn muốn sử dụng chế độ nào? (1/2/3)
   ```

### Step 1: Mode-Specific Analysis

#### YOLO Mode Process:

1. **Fast Context Analysis**: Quick keyword and file type detection
2. **Simplified Scoring**: Focus on primary factors (keyword + file type)
3. **Rapid Selection**: Select if confidence > 0.6, otherwise ask user
4. **Skip Validations**: Bypass workflow and dependency checks

#### Standard Mode Process:

1. **🔴 PRIORITY 1: Test Spec Driven Development Review Workflow Validation** ([TSDDR 2.0 Guide](../../docs/TSDDR-2.0-Guide.md))

   - **MANDATORY FIRST CHECK**: Verify existence of Test Spec Driven Development Review Workflow documentation
   - **Kiro Specs Validation**: Check for workflow implementation in `.kiro/specs/` directory
     - Scan for `requirements.md`, `design.md`, `tasks.md` files in project specs
     - Validate workflow structure and completeness
     - Ensure Test-First approach is documented and ready
   - **Critical Dependencies Check**:
     - Verify Test Phase implementation (AI-powered error pattern detection)
     - Confirm Review Phase setup (Multi-AI code review system)
     - Validate Quality Gates configuration
   - **Action Required if Missing**:
     - **STOP** agent selection process immediately
     - Prompt user to create or brainstorm this critical workflow
     - Guide user to setup `.kiro/specs/{project}/` structure
     - Emphasize: "This workflow is essential for high-efficiency project development"
     - **NO BYPASS**: Agent selection cannot proceed without this validation

2. **Comprehensive Analysis**: Parse user request for keywords and context
3. **Project Structure Analysis**: Analyze project structure and file types
4. **Complexity Assessment**: Assess task complexity and scope
5. **Technology Stack Identification**: Identify primary technology stack

#### Auto Mode Process:

1. **Task Complexity Detection**:
   - **Simple** (keywords: "fix", "update", "change"): → YOLO Mode
   - **Medium** (keywords: "add", "implement", "create"): → Standard Mode
   - **Complex** (keywords: "architecture", "system", "integration"): → Standard Mode
2. **Apply selected mode process**

### Step 2: Agent Scoring

1. Calculate keyword match scores for each agent
2. Apply file type and context bonuses
3. Factor in complexity handling capabilities
4. Include performance and preference weights
5. Generate final confidence scores

### Step 3: Selection Decision

1. Select highest scoring agent if confidence > 0.6
2. Request clarification if all scores < 0.4
3. Provide alternative suggestions for medium confidence
4. Log selection reasoning and confidence level

## Fallback Mechanisms

**Multi-Agent Tasks**: Route to primary agent with collaboration notes  
**Unclear Context**: Request additional information before selection  
**Low Confidence**: Present top 2-3 options for user choice  
**New Technologies**: Default to most relevant existing agent with notes

## Performance Optimization

**Caching**: Store recent selections and patterns  
**Learning**: Update weights based on success/failure feedback  
**Monitoring**: Track selection accuracy and user satisfaction  
**Adaptation**: Adjust algorithm based on usage patterns

## Integration Points

**Input Sources**:

- User natural language requests
- Project file structure analysis
- Previous conversation context
- User preference history

**Output Targets**:

- Selected agent activation
- Confidence score reporting
- Selection reasoning explanation
- Alternative agent suggestions

## Quality Assurance

**Validation Rules**:

- **🔴 PRIORITY 0**: .project-identity must exist and be readable
- **🔴 PRIORITY 1**: Operation mode must be configured or user prompted
- **🔴 PRIORITY 2**: Test Spec Driven Development Review Workflow (Standard Mode only)
- **🔴 PRIORITY 3**: Kiro Specs structure (Standard Mode only)
- Mode-specific confidence thresholds:
  - YOLO Mode: minimum 0.6
  - Standard Mode: minimum 0.75
  - Auto Mode: dynamic based on complexity
- Maximum response time (< 2 seconds for YOLO, < 5 seconds for Standard)
- Fallback agent availability
- Selection reasoning clarity

**Error Handling**:

- **Missing .project-identity**: Create template and prompt user configuration
- **Missing Operation Mode**: Show mode selection prompt and update .project-identity
- **Workflow Missing** (Standard Mode): Immediate stop with guided setup instructions
- **Kiro Specs Missing** (Standard Mode): Auto-prompt for project structure creation
- **YOLO Mode Failures**: Graceful fallback to user selection
- **Auto Mode Conflicts**: Default to Standard Mode with user notification
- **Timeout Handling**: Default to last successful agent with warning
- User override capabilities (available in all modes)
- Selection history logging with mode tracking

**Mode-Specific Quality Gates**:

**YOLO Mode**:

- Skip workflow validations
- Fast keyword matching
- Confidence threshold: 0.6
- Maximum selection time: 2 seconds
- Fallback: Direct user choice

**Standard Mode**:

- Full workflow validation required
- Comprehensive analysis
- Confidence threshold: 0.75
- Maximum selection time: 5 seconds
- Fallback: Guided troubleshooting

**Auto Mode**:

- Dynamic validation based on task complexity
- Adaptive confidence thresholds
- Smart mode switching
- Context-aware fallbacks

---

**Activation**: Automatically triggered for all development requests  
**Integration**: Works with all specialized development agents  
**Maintenance**: Self-learning system with manual oversight capability
