# Agent Selector System

> **🤖 Hệ thống tự động lựa chọn agent tối ưu cho từng loại công việc**

Hệ thống này cung cấp logic và templates để tự động chọn agent phù hợp nhất dựa trên input của người dùng.

## 🧠 Agent Selection Logic

### Input Analysis Framework
```yaml
User Input Analysis:
  Keywords Detection:
    Mobile: ["android", "ios", "mobile", "app", "flutter", "react native"]
    Web: ["website", "web", "frontend", "backend", "api", "database"]
    AI: ["ai", "machine learning", "ml", "nlp", "computer vision"]
    DevOps: ["deploy", "ci/cd", "infrastructure", "docker", "kubernetes"]
    Planning: ["plan", "project", "requirements", "design", "brainstorm"]
    Marketing: ["marketing", "social media", "content", "seo", "growth"]
    Prototype: ["prototype", "mvp", "quick", "fast", "demo"]
  
  Complexity Assessment:
    Simple: Single feature, straightforward implementation
    Medium: Multiple features, some integration required
    Complex: Full system, multiple integrations, advanced features
  
  Timeline Detection:
    Urgent: ["asap", "urgent", "quick", "fast", "immediately"]
    Normal: ["soon", "this week", "normal priority"]
    Long-term: ["long term", "future", "roadmap", "planning"]
```

### Decision Matrix
```yaml
Agent Selection Matrix:
  
  # ENGINEERING AGENTS
  mobile-app-builder:
    Triggers:
      - Keywords: ["mobile", "android", "ios", "app"]
      - File Context: ["android/", "ios/", ".kt", ".swift", "flutter/"]
      - Complexity: ["medium", "complex"]
    Confidence: 0.9
    Workflows: ["mobile-utility-workflow", "android-workflow", "ios-workflow"]
  
  rapid-prototyper:
    Triggers:
      - Keywords: ["prototype", "mvp", "quick", "demo", "poc"]
      - Timeline: ["urgent"]
      - Complexity: ["simple", "medium"]
    Confidence: 0.95
    Workflows: ["development-rules", "api-integration-rules"]
  
  frontend-developer:
    Triggers:
      - Keywords: ["frontend", "ui", "react", "vue", "angular", "web"]
      - File Context: [".tsx", ".jsx", ".vue", "components/"]
      - Complexity: ["medium", "complex"]
    Confidence: 0.85
    Workflows: ["frontend-rules", "shadcn-ui-rules"]
  
  backend-architect:
    Triggers:
      - Keywords: ["backend", "api", "database", "server", "microservice"]
      - File Context: [".py", ".js", ".go", "api/", "models/"]
      - Complexity: ["medium", "complex"]
    Confidence: 0.85
    Workflows: ["backend-rules-optimized", "database-management"]
  
  ai-engineer:
    Triggers:
      - Keywords: ["ai", "ml", "machine learning", "nlp", "computer vision"]
      - File Context: ["ml/", "ai/", ".py", "models/"]
      - Complexity: ["complex"]
    Confidence: 0.9
    Workflows: ["ai-execution-templates", "ai-code-quality-automation"]
  
  devops-automator:
    Triggers:
      - Keywords: ["deploy", "ci/cd", "docker", "kubernetes", "infrastructure"]
      - File Context: ["Dockerfile", ".yml", ".yaml", "terraform/"]
      - Complexity: ["medium", "complex"]
    Confidence: 0.8
    Workflows: ["infrastructure-rules", "git-workflow"]
  
  # PRODUCT AGENTS
  sprint-prioritizer:
    Triggers:
      - Keywords: ["sprint", "task", "priority", "backlog", "planning"]
      - Context: ["project management", "task management"]
    Confidence: 0.85
    Workflows: ["task-creation-workflow", "planning-enforcement"]
  
  feedback-synthesizer:
    Triggers:
      - Keywords: ["feedback", "requirements", "user story", "specification"]
      - Context: ["requirements gathering", "user research"]
    Confidence: 0.8
    Workflows: ["user-intent-analysis-workflow", "kiro-dynamic-workflow"]
  
  trend-researcher:
    Triggers:
      - Keywords: ["research", "market", "trend", "analysis", "competitor"]
      - Context: ["market research", "competitive analysis"]
    Confidence: 0.75
    Workflows: ["brainstorm-overview", "expert-brainstorm-workflow"]
  
  # MARKETING AGENTS
  content-creator:
    Triggers:
      - Keywords: ["content", "blog", "article", "copy", "writing"]
      - Context: ["content marketing", "documentation"]
    Confidence: 0.8
    Workflows: []
  
  app-store-optimizer:
    Triggers:
      - Keywords: ["app store", "aso", "play store", "optimization"]
      - Context: ["mobile app", "publishing"]
    Confidence: 0.9
    Workflows: ["android-aso-package-workflow"]
  
  growth-hacker:
    Triggers:
      - Keywords: ["growth", "marketing", "acquisition", "retention"]
      - Context: ["growth strategy", "user acquisition"]
    Confidence: 0.75
    Workflows: ["feature-suggestion-engine"]
  
  # SOCIAL MEDIA AGENTS
  instagram-curator:
    Triggers:
      - Keywords: ["instagram", "ig", "social media", "visual content"]
      - Context: ["social media marketing"]
    Confidence: 0.85
    Workflows: []
  
  tiktok-strategist:
    Triggers:
      - Keywords: ["tiktok", "short video", "viral", "trending"]
      - Context: ["social media marketing"]
    Confidence: 0.85
    Workflows: []
  
  twitter-engager:
    Triggers:
      - Keywords: ["twitter", "tweet", "engagement", "community"]
      - Context: ["social media marketing"]
    Confidence: 0.8
    Workflows: []
  
  reddit-community-builder:
    Triggers:
      - Keywords: ["reddit", "community", "discussion", "forum"]
      - Context: ["community building"]
    Confidence: 0.8
    Workflows: []
  
  # META AGENTS
  sub-agent-creator:
    Triggers:
      - Keywords: ["new project", "initialize", "setup", "create"]
      - Context: ["project initialization"]
    Confidence: 0.9
    Workflows: ["project-creation-workflow", "project-identity-template"]
  
  sub-agent-optimizer:
    Triggers:
      - Keywords: ["optimize", "improve", "enhance", "refactor"]
      - Context: ["optimization", "improvement"]
    Confidence: 0.7
    Workflows: ["expert-brainstorm-guide", "four-role-development"]
```

## 🔍 Context Analysis Engine

### File Context Detection
```yaml
File Pattern Analysis:
  Mobile Development:
    Android: [".kt", ".java", "android/", "app/src/main/"]
    iOS: [".swift", ".m", ".h", "ios/", ".xcodeproj"]
    Flutter: ["lib/", ".dart", "pubspec.yaml"]
    React Native: ["android/", "ios/", ".tsx", "metro.config.js"]
  
  Web Development:
    Frontend: [".tsx", ".jsx", ".vue", "components/", "pages/"]
    Backend: [".py", ".js", ".go", ".php", "api/", "routes/"]
    Database: [".sql", "migrations/", "models/", "schema/"]
  
  DevOps:
    Containers: ["Dockerfile", "docker-compose.yml"]
    CI/CD: [".github/", ".gitlab-ci.yml", "Jenkinsfile"]
    Infrastructure: ["terraform/", ".tf", "ansible/"]
  
  AI/ML:
    Python ML: [".py", "requirements.txt", "models/", "notebooks/"]
    Data: [".csv", ".json", "data/", "datasets/"]
    Models: [".pkl", ".h5", ".onnx", "checkpoints/"]
```

### Project Context Detection
```yaml
Project Type Detection:
  Mobile App:
    Indicators: ["android/", "ios/", "mobile", "app"]
    Confidence Boost: +0.2 for mobile agents
  
  Web Application:
    Indicators: ["frontend/", "backend/", "web", "api"]
    Confidence Boost: +0.15 for web agents
  
  AI/ML Project:
    Indicators: ["ml/", "ai/", "models/", "data/"]
    Confidence Boost: +0.25 for ai-engineer
  
  Marketing Project:
    Indicators: ["marketing/", "content/", "social/"]
    Confidence Boost: +0.2 for marketing agents
```

## 🎯 Selection Algorithm

### Step-by-Step Process
```python
def select_agent(user_input, file_context, project_context):
    """
    Agent selection algorithm
    """
    scores = {}
    
    # Step 1: Keyword Analysis
    for agent, config in AGENT_MATRIX.items():
        score = 0
        for keyword in config['triggers']['keywords']:
            if keyword.lower() in user_input.lower():
                score += config['confidence']
        scores[agent] = score
    
    # Step 2: File Context Boost
    for agent, config in AGENT_MATRIX.items():
        if 'file_context' in config['triggers']:
            for pattern in config['triggers']['file_context']:
                if any(pattern in f for f in file_context):
                    scores[agent] += 0.1
    
    # Step 3: Project Context Boost
    project_type = detect_project_type(project_context)
    if project_type:
        boost_agents = PROJECT_BOOSTS.get(project_type, [])
        for agent in boost_agents:
            if agent in scores:
                scores[agent] += 0.2
    
    # Step 4: Complexity & Timeline Adjustment
    complexity = detect_complexity(user_input)
    timeline = detect_timeline(user_input)
    
    for agent, config in AGENT_MATRIX.items():
        if complexity in config['triggers'].get('complexity', []):
            scores[agent] += 0.1
        if timeline in config['triggers'].get('timeline', []):
            scores[agent] += 0.1
    
    # Step 5: Select Top Agent
    if not scores:
        return 'rapid-prototyper'  # Default fallback
    
    top_agent = max(scores.items(), key=lambda x: x[1])
    
    # Step 6: Confidence Threshold
    if top_agent[1] < 0.3:
        return 'rapid-prototyper'  # Low confidence fallback
    
    return top_agent[0]
```

## 📋 Agent Selection Templates

### Quick Selection Prompts
```yaml
Development Tasks:
  "Tôi muốn tạo ứng dụng mobile Android":
    Agent: mobile-app-builder
    Workflows: [android-workflow, mobile-utility-workflow]
    Confidence: 0.95
  
  "Cần build API backend nhanh":
    Agent: rapid-prototyper
    Workflows: [backend-rules-optimized, api-integration-rules]
    Confidence: 0.9
  
  "Tạo prototype web app":
    Agent: rapid-prototyper
    Workflows: [development-rules, frontend-rules]
    Confidence: 0.95
  
  "Tích hợp AI vào ứng dụng":
    Agent: ai-engineer
    Workflows: [ai-execution-templates]
    Confidence: 0.9

Planning Tasks:
  "Lập kế hoạch dự án mới":
    Agent: sub-agent-creator
    Workflows: [project-creation-workflow, planning-workflow]
    Confidence: 0.9
  
  "Phân tích requirements":
    Agent: feedback-synthesizer
    Workflows: [user-intent-analysis-workflow, kiro-dynamic-workflow]
    Confidence: 0.85
  
  "Brainstorm tính năng mới":
    Agent: trend-researcher
    Workflows: [brainstorm-overview, expert-brainstorm-workflow]
    Confidence: 0.8

Marketing Tasks:
  "Tối ưu App Store":
    Agent: app-store-optimizer
    Workflows: [android-aso-package-workflow]
    Confidence: 0.95
  
  "Tạo content marketing":
    Agent: content-creator
    Workflows: []
    Confidence: 0.85
  
  "Chiến lược growth hacking":
    Agent: growth-hacker
    Workflows: [feature-suggestion-engine]
    Confidence: 0.8
```

## 🔄 Multi-Agent Workflows

### Collaboration Patterns
```yaml
Sequential Collaboration:
  Mobile App Development:
    1. sub-agent-creator: Project setup
    2. mobile-app-builder: Core development
    3. ai-engineer: AI features (if needed)
    4. devops-automator: Deployment
    5. app-store-optimizer: Store optimization
  
  Web Application:
    1. sub-agent-creator: Project initialization
    2. rapid-prototyper: MVP development
    3. frontend-developer: UI enhancement
    4. backend-architect: API optimization
    5. devops-automator: Production deployment

Parallel Collaboration:
  Full Product Launch:
    Development Track:
      - mobile-app-builder: App development
      - backend-architect: API & database
    
    Marketing Track:
      - content-creator: Marketing materials
      - app-store-optimizer: Store preparation
      - growth-hacker: Launch strategy
    
    Management Track:
      - sprint-prioritizer: Task coordination
      - feedback-synthesizer: User feedback integration
```

## 🚀 Implementation Guide

### Integration với Trae AI
```yaml
Trae AI Integration:
  1. Input Analysis:
     - Parse user request
     - Analyze current file context
     - Check project structure
  
  2. Agent Selection:
     - Run selection algorithm
     - Calculate confidence scores
     - Select primary + supporting agents
  
  3. Workflow Activation:
     - Load relevant workflows from .cursor/rules/
     - Initialize agent with proper context
     - Set up collaboration if needed
  
  4. Execution Monitoring:
     - Track progress
     - Handle agent switching if needed
     - Ensure workflow compliance
```

### Quality Assurance
```yaml
QA Checklist:
  ✅ Agent selection confidence > 0.3
  ✅ Relevant workflows identified
  ✅ Project context considered
  ✅ File patterns analyzed
  ✅ Complexity assessment done
  ✅ Timeline requirements checked
  ✅ Fallback agent available
  ✅ Multi-agent coordination planned
```

## 📊 Performance Metrics

### Success Indicators
```yaml
Metrics to Track:
  Selection Accuracy: % of correct agent selections
  Task Completion Rate: % of successfully completed tasks
  User Satisfaction: Rating of agent performance
  Workflow Compliance: % adherence to defined workflows
  Response Time: Average time to complete tasks
  Agent Utilization: Distribution of agent usage
```

### Optimization Strategies
```yaml
Continuous Improvement:
  - Monitor agent performance metrics
  - Adjust confidence thresholds based on results
  - Update keyword patterns from user feedback
  - Refine collaboration patterns
  - Add new agents as needed
```

---

**🎯 Usage**: Hệ thống này được thiết kế để tự động hóa việc lựa chọn agent, đảm bảo mỗi công việc được giao cho agent có khả năng xử lý tốt nhất, tối ưu hóa hiệu suất và chất lượng công việc.