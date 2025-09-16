# 🤖 Agent Registry - Universal AI Agents Hub

> **🎯 Centralized registry of all AI agents across IDEs**  
> Standardized agent definitions for consistent behavior

---

## 📊 Agent Selection Algorithm

### Multi-Factor Scoring (100%)
1. **Context Analysis** (35%): Tech stack, file types, project structure
2. **Keyword Matching** (25%): Primary/secondary technology keywords
3. **Complexity Assessment** (20%): Task difficulty and scope
4. **Performance History** (15%): Success rates, quality metrics
5. **User Preferences** (5%): Historical selections, feedback

### Decision Thresholds
- **High Confidence** (>85%): Direct assignment
- **Medium Confidence** (70-85%): Assignment with monitoring
- **Low Confidence** (<70%): User confirmation required

---

## 🔧 Engineering Agents

### 📱 Mobile Development

#### iOS Development Agent
- **File**: [engineering/ios-specialist.md](engineering/ios-specialist.md)
- **Specialization**: Native iOS apps with Swift/SwiftUI
- **Keywords**: swift, swiftui, ios, xcode, uikit, core data
- **Capabilities**: Swift (10/10), SwiftUI (10/10), Xcode (10/10), App Store (9/10)
- **Success Rate**: 85% | Quality: 9.2/10
- **Workflows**: ios-workflow.md, ios-project-template.md

#### Android Development Agent
- **File**: [engineering/android-specialist.md](engineering/android-specialist.md)
- **Specialization**: Native Android with Kotlin/Jetpack Compose
- **Keywords**: kotlin, android, jetpack compose, gradle, room
- **Capabilities**: Kotlin (10/10), Jetpack Compose (9/10), Android Studio (10/10)
- **Success Rate**: 82% | Quality: 8.9/10
- **Workflows**: android-workflow.md, TSDDR-2.0-Guide.md

#### Cross-Platform Mobile Agent
- **File**: [engineering/cross-platform-specialist.md](engineering/cross-platform-specialist.md)
- **Specialization**: Flutter, React Native hybrid development
- **Keywords**: flutter, react native, dart, expo, hybrid
- **Capabilities**: Flutter (8/10), React Native (8/10), Dart (8/10)
- **Success Rate**: 80% | Quality: 8.4/10
- **Workflows**: TSDDR-2.0-Guide.md

### 🌐 Web Development

#### Frontend Development Agent
- **File**: [engineering/frontend-developer.md](engineering/frontend-developer.md)
- **Specialization**: Modern web frontends with React/Vue/Angular
- **Keywords**: react, vue, angular, typescript, tailwind, nextjs
- **Capabilities**: React (9/10), TypeScript (9/10), CSS (8/10), Responsive (9/10)
- **Success Rate**: 88% | Quality: 8.8/10
- **Workflows**: frontend-rules.md, shadcn-ui-rules.md

#### Backend Development Agent
- **File**: [engineering/backend-architect.md](engineering/backend-architect.md)
- **Specialization**: Server-side APIs, databases, microservices
- **Keywords**: nodejs, laravel, api, database, docker, microservices
- **Capabilities**: Node.js (9/10), Laravel (8/10), APIs (9/10), Databases (8/10)
- **Success Rate**: 86% | Quality: 8.7/10
- **Workflows**: development-rules.md, database-management.md

### 🏗️ Infrastructure & DevOps

#### DevOps Infrastructure Agent
- **File**: [engineering/devops-automator.md](engineering/devops-automator.md)
- **Specialization**: Docker, Kubernetes, CI/CD, cloud deployment
- **Keywords**: docker, kubernetes, cicd, aws, gcp, terraform
- **Capabilities**: Docker (9/10), K8s (8/10), CI/CD (9/10), Cloud (8/10)
- **Success Rate**: 83% | Quality: 8.6/10
- **Workflows**: infrastructure-rules.md, git-workflow.md

#### AI Engineer Agent
- **File**: [engineering/ai-engineer.md](engineering/ai-engineer.md)
- **Specialization**: ML/AI integration, model deployment, AI workflows
- **Keywords**: machine learning, ai, tensorflow, pytorch, llm
- **Capabilities**: ML (9/10), AI Integration (9/10), Model Deploy (8/10)
- **Success Rate**: 81% | Quality: 8.5/10
- **Workflows**: ai-integration-workflow.md

---

## 🎯 Specialized Agents

### 🔧 APK Modification Agent
- **File**: [specialized/apk-modifier.md](specialized/apk-modifier.md)
- **Specialization**: Reverse engineering, Firebase integration, SafeAds
- **Keywords**: apk, reverse engineering, firebase, safeads, smali, mod app, modification
- **Capabilities**: APK Analysis (9/10), Firebase (8/10), SafeAds (9/10)
- **Success Rate**: 78% | Quality: 8.5/10
- **Workflows**: android-workflow.md

### 🎨 UI/UX Specialist Agent
- **File**: [specialized/ui-ux-specialist.md](specialized/ui-ux-specialist.md)
- **Specialization**: Design systems, user experience, accessibility
- **Keywords**: design, ui, ux, figma, accessibility, design system
- **Capabilities**: Design (9/10), UX (9/10), Accessibility (8/10)
- **Success Rate**: 84% | Quality: 8.7/10
- **Workflows**: design-to-prompt.md

### 🧪 QA Testing Agent
- **File**: [specialized/qa-testing-specialist.md](specialized/qa-testing-specialist.md)
- **Specialization**: Test automation, quality assurance, performance testing
- **Keywords**: testing, qa, automation, selenium, cypress, jest
- **Capabilities**: Test Automation (9/10), QA (9/10), Performance (8/10)
- **Success Rate**: 86% | Quality: 8.8/10
- **Workflows**: tdd-guidelines.md

---

## 📊 Product Agents

### 📋 Sprint Prioritizer
- **File**: [product/sprint-prioritizer.md](product/sprint-prioritizer.md)
- **Specialization**: Agile planning, task prioritization, sprint management
- **Keywords**: agile, scrum, sprint, prioritization, planning
- **Capabilities**: Planning (9/10), Prioritization (9/10), Agile (8/10)
- **Success Rate**: 87% | Quality: 8.6/10
- **Workflows**: planning-workflow.md

### 📈 Feedback Synthesizer
- **File**: [product/feedback-synthesizer.md](product/feedback-synthesizer.md)
- **Specialization**: User feedback analysis, requirement extraction
- **Keywords**: feedback, analysis, requirements, user research
- **Capabilities**: Analysis (9/10), Synthesis (8/10), Requirements (8/10)
- **Success Rate**: 83% | Quality: 8.4/10
- **Workflows**: requirement-analysis.md

### 🔍 Trend Researcher
- **File**: [product/trend-researcher.md](product/trend-researcher.md)
- **Specialization**: Market research, technology trends, competitive analysis
- **Keywords**: research, trends, market, competition, analysis
- **Capabilities**: Research (9/10), Analysis (8/10), Trends (9/10)
- **Success Rate**: 81% | Quality: 8.3/10
- **Workflows**: market-research-workflow.md

---

## 🎯 Agent Selection Workflow

### Automatic Selection Process
```
Project Identity Check → Input Analysis → Context Extraction → Agent Scoring → Decision → Assignment
```

### Enhanced Process Flow
1. **Project Identity Analysis**: Load .project-identity, determine stage & type
2. **Workflow Rules Loading**: Apply appropriate rules based on project configuration
3. **Context Extraction**: Analyze task requirements with project context
4. **Agent Scoring**: Multi-factor scoring with project-specific weights
5. **Decision & Assignment**: Select optimal agent with workflow integration

### YOLO Mode Selection
- **Triggers**: "yolo", "quick", "fast", "asap"
- **Process**: Simplified analysis, immediate assignment
- **Threshold**: Minimum 60% confidence
- **Monitoring**: Enhanced real-time tracking

---

## 📈 Performance Metrics

### System Targets
- **Selection Accuracy**: >90%
- **Response Time**: <2s
- **Uptime**: >99.5%

### Quality Targets
- **Code Quality**: >8.5/10
- **User Satisfaction**: >4.5/5
- **Error Rate**: <2%

---

## 🔄 Agent Management

### Adding New Agents
1. Create agent definition file in appropriate category
2. Update this registry with agent details
3. Add to agent selection algorithm
4. Test selection workflow
5. Monitor performance metrics

### Agent Updates
1. Update agent definition file
2. Update performance metrics
3. Retrain selection algorithm if needed
4. Validate selection accuracy

### Agent Retirement
1. Mark agent as deprecated
2. Redirect tasks to alternative agents
3. Update selection algorithm
4. Archive agent definition

---

## 🔗 Integration Points

### Project Identity Integration
- Automatic configuration loading based on projectType and projectStage
- Platform-specific rules from platformSpecificRules
- Feature enablement based on features configuration
- Integration loading (Telegram, MCP tools, AI APIs)

### Context7 Memory Integration
- Automatic project memory checks
- Project-specific knowledge and best practices loading
- Project constraints and requirements synchronization

### Dynamic Workflow Adaptation
- Real-time adjustment based on project progress
- Automatic stage progression with validation
- Context-aware agent selection with project history

---

**🎯 Smart agent selection with workflow integration for optimal development efficiency across all IDEs and AI tools.**