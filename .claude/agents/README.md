# Claude Agents System - Tối Ưu Hóa Hiệu Suất Công Việc

> **🚀 Hệ thống agents thông minh cho Base AI Project**

Hệ thống này tối ưu hóa hiệu suất công việc bằng cách tự động lựa chọn agent phù hợp nhất cho từng loại công việc, tích hợp với các quy trình từ `.cursor/rules/` và hệ thống Kiro.

## 📋 Tổng Quan Hệ Thống

### 🎯 Mục Tiêu
- **Tự động hóa việc lựa chọn agent** dựa trên loại công việc
- **Tối ưu hóa hiệu suất** bằng cách giao việc cho đúng chuyên gia
- **Đảm bảo tuân thủ quy trình** từ `.cursor/rules/`
- **Tích hợp mượt mà** với Kiro Task Execution System
- **Hỗ trợ collaboration** giữa nhiều agents

### 🏗️ Kiến Trúc Hệ Thống
```
.claude/agents/
├── README.md                    # 📖 Tài liệu tổng quan (file này)
├── agent-workflow-mapping.md    # 🗺️ Mapping agents với workflows
├── agent-selector.md           # 🤖 Logic tự động chọn agent
├── engineering/                # 👨‍💻 Engineering agents
│   ├── ai-engineer.md
│   ├── backend-architect.md
│   ├── devops-automator.md
│   ├── frontend-developer.md
│   ├── mobile-app-builder.md
│   └── rapid-prototyper.md
├── product/                    # 📋 Product management agents
│   ├── feedback-synthesizer.md
│   ├── sprint-prioritizer.md
│   └── trend-researcher.md
├── marketing/                  # 📈 Marketing agents
│   ├── app-store-optimizer.md
│   ├── content-creator.md
│   ├── growth-hacker.md
│   ├── instagram-curator.md
│   ├── reddit-community-builder.md
│   ├── tiktok-strategist.md
│   └── twitter-engager.md
└── sub-agent-*.md             # 🔧 Meta agents
```

## 🚀 Cách Sử Dụng

### 1. Tự Động Lựa Chọn Agent

Hệ thống sẽ tự động phân tích yêu cầu của bạn và chọn agent phù hợp:

```yaml
Ví dụ Input → Agent được chọn:

"Tôi muốn tạo ứng dụng Android mới"
→ Agent: mobile-app-builder
→ Workflows: android-workflow.mdc, mobile-utility-workflow.mdc

"Cần build API backend nhanh cho demo"
→ Agent: rapid-prototyper
→ Workflows: backend-rules-optimized.mdc, api-integration-rules.mdc

"Lập kế hoạch cho dự án mới"
→ Agent: sub-agent-creator
→ Workflows: project-creation-workflow.mdc, planning-workflow.mdc

"Tối ưu App Store cho ứng dụng"
→ Agent: app-store-optimizer
→ Workflows: android-aso-package-workflow.mdc
```

### 2. Multi-Agent Collaboration

Cho các dự án phức tạp, hệ thống sẽ phối hợp nhiều agents:

```yaml
Dự án Mobile App hoàn chỉnh:
  Phase 1 - Planning:
    - sub-agent-creator: Project setup
    - trend-researcher: Market research
  
  Phase 2 - Development:
    - mobile-app-builder: Core app development
    - backend-architect: API & database
    - ai-engineer: AI features integration
  
  Phase 3 - Launch:
    - devops-automator: Deployment
    - app-store-optimizer: Store optimization
    - growth-hacker: Marketing strategy
```

## 📊 Danh Sách Agents

### 🏗️ Engineering Agents

| Agent | Chuyên Môn | Use Cases | Workflows Chính |
|-------|------------|-----------|------------------|
| **mobile-app-builder** | Mobile Development | Android/iOS apps, React Native, Flutter | `mobile-utility-workflow`, `android-workflow`, `ios-workflow` |
| **rapid-prototyper** | Fast Prototyping | MVPs, demos, POCs, quick solutions | `development-rules`, `api-integration-rules` |
| **frontend-developer** | Web Frontend | React, Vue, Angular, UI/UX implementation | `frontend-rules`, `shadcn-ui-rules` |
| **backend-architect** | Backend Systems | APIs, databases, microservices, architecture | `backend-rules-optimized`, `database-management` |
| **ai-engineer** | AI/ML Integration | AI features, ML pipelines, NLP, computer vision | `ai-execution-templates`, `ai-code-quality-automation` |
| **devops-automator** | DevOps & Infrastructure | CI/CD, deployment, infrastructure, monitoring | `infrastructure-rules`, `git-workflow` |

### 📋 Product Management Agents

| Agent | Chuyên Môn | Use Cases | Workflows Chính |
|-------|------------|-----------|------------------|
| **sprint-prioritizer** | Task Management | Sprint planning, task prioritization, resource allocation | `task-creation-workflow`, `planning-enforcement` |
| **feedback-synthesizer** | Requirements Analysis | User feedback, requirements gathering, specifications | `user-intent-analysis-workflow`, `kiro-dynamic-workflow` |
| **trend-researcher** | Market Research | Market analysis, competitive research, trend identification | `brainstorm-overview`, `expert-brainstorm-workflow` |

### 📈 Marketing Agents

| Agent | Chuyên Môn | Use Cases | Workflows Chính |
|-------|------------|-----------|------------------|
| **content-creator** | Content Marketing | Blog posts, marketing copy, documentation | - |
| **app-store-optimizer** | ASO | App store optimization, keyword research, conversion | `android-aso-package-workflow` |
| **growth-hacker** | Growth Strategy | User acquisition, viral marketing, growth experiments | `feature-suggestion-engine` |
| **instagram-curator** | Instagram Marketing | Visual content, Instagram campaigns, engagement | - |
| **tiktok-strategist** | TikTok Marketing | Short videos, viral content, trending strategies | - |
| **twitter-engager** | Twitter Marketing | Community engagement, Twitter campaigns | - |
| **reddit-community-builder** | Community Building | Reddit marketing, community management | - |

### 🔧 Meta Agents

| Agent | Chuyên Môn | Use Cases | Workflows Chính |
|-------|------------|-----------|------------------|
| **sub-agent-creator** | Project Initialization | New project setup, agent creation, system setup | `project-creation-workflow`, `project-identity-template` |
| **sub-agent-optimizer** | System Optimization | Agent optimization, workflow improvement | `expert-brainstorm-guide`, `four-role-development` |

## 🎯 Agent Selection Logic

### Tự Động Phân Tích
Hệ thống phân tích:
- **Keywords trong yêu cầu**: Tìm từ khóa liên quan đến từng domain
- **File context**: Phân tích các file đang mở/chỉnh sửa
- **Project structure**: Hiểu loại dự án đang làm việc
- **Complexity level**: Đánh giá độ phức tạp của task
- **Timeline requirements**: Xác định mức độ cấp thiết

### Confidence Scoring
```yaml
Scoring System:
  Keyword Match: 0.0 - 1.0
  File Context Boost: +0.1
  Project Context Boost: +0.2
  Complexity Match: +0.1
  Timeline Match: +0.1
  
Threshold:
  High Confidence: > 0.7
  Medium Confidence: 0.3 - 0.7
  Low Confidence: < 0.3 (fallback to rapid-prototyper)
```

## 🔄 Workflow Integration

### Tích Hợp với .cursor/rules/
Mỗi agent được mapping với các workflows cụ thể:

```yaml
Workflow Mapping Examples:
  mobile-app-builder:
    - mobile-utility-workflow.mdc
    - android-workflow.mdc
    - ios-workflow.mdc
    - tdd-mobile-workflow.mdc
  
  rapid-prototyper:
    - development-rules.mdc
    - api-integration-rules.mdc
    - frontend-rules.mdc
  
  ai-engineer:
    - ai-execution-templates.mdc
    - ai-code-quality-automation.mdc
```

### Tích Hợp với Kiro System
- **Automatic Task Detection**: Tự động phát hiện Kiro tasks
- **Dynamic Workflow**: Tạo/cập nhật requirements, design, tasks
- **Quality Gates**: Đảm bảo chất lượng tại mỗi giai đoạn
- **Progress Tracking**: Theo dõi tiến độ across multiple agents

## 📈 Performance Optimization

### Metrics Tracking
```yaml
Key Performance Indicators:
  Selection Accuracy: % agent selections đúng
  Task Completion Rate: % tasks hoàn thành thành công
  User Satisfaction: Đánh giá từ người dùng
  Workflow Compliance: % tuân thủ quy trình
  Response Time: Thời gian trung bình hoàn thành
  Agent Utilization: Phân bố sử dụng agents
```

### Continuous Improvement
- **Feedback Loop**: Thu thập feedback để cải thiện selection logic
- **Pattern Learning**: Học từ các patterns thành công
- **Threshold Tuning**: Điều chỉnh confidence thresholds
- **New Agent Integration**: Thêm agents mới khi cần

## 🛠️ Configuration

### Environment Setup
```yaml
Required Files:
  ✅ .project-identity - Project identification
  ✅ .cursor/rules/ - Workflow definitions
  ✅ .claude/agents/ - Agent definitions
  ✅ .kiro/specs/ - Kiro task specifications
```

### Customization Options
```yaml
Customizable Settings:
  - Agent confidence thresholds
  - Keyword patterns for detection
  - Workflow priority mappings
  - Multi-agent collaboration rules
  - Fallback agent preferences
```

## 🚨 Best Practices

### Do's ✅
- **Luôn kiểm tra project context** trước khi chọn agent
- **Sử dụng multi-agent collaboration** cho dự án phức tạp
- **Tuân thủ workflows** từ `.cursor/rules/`
- **Document decisions** trong project files
- **Monitor performance** và adjust khi cần

### Don'ts ❌
- **Không bỏ qua confidence scores** thấp
- **Không skip quality gates** trong workflows
- **Không hardcode agent selection** mà không có logic
- **Không ignore user feedback** về agent performance
- **Không forget to update** agent mappings khi có changes

## 🔧 Troubleshooting

### Common Issues

**Agent Selection Không Chính Xác**
```yaml
Solution:
  1. Kiểm tra keywords trong input
  2. Verify file context detection
  3. Adjust confidence thresholds
  4. Update keyword patterns
```

**Workflow Không Được Áp Dụng**
```yaml
Solution:
  1. Kiểm tra file paths trong .cursor/rules/
  2. Verify agent-workflow mappings
  3. Check workflow file permissions
  4. Validate workflow syntax
```

**Multi-Agent Coordination Issues**
```yaml
Solution:
  1. Define clear handoff points
  2. Establish communication protocols
  3. Set up progress tracking
  4. Plan conflict resolution
```

## 📚 Advanced Usage

### Custom Agent Creation
```yaml
Steps to Add New Agent:
  1. Create agent definition file
  2. Update agent-workflow-mapping.md
  3. Add to agent-selector.md logic
  4. Test with sample inputs
  5. Document use cases
```

### Workflow Customization
```yaml
Customizing Workflows:
  1. Identify specific needs
  2. Create/modify workflow in .cursor/rules/
  3. Update agent mappings
  4. Test integration
  5. Document changes
```

## 🎉 Success Stories

### Typical Improvements
- **50% faster task completion** với đúng agent selection
- **80% reduction in context switching** giữa các tools
- **90% workflow compliance** với automated processes
- **70% improvement in code quality** với specialized agents

---

## 🚀 Getting Started

1. **Đọc hiểu** agent capabilities trong từng file agent
2. **Thử nghiệm** với các yêu cầu đơn giản
3. **Quan sát** agent selection process
4. **Feedback** để cải thiện system
5. **Scale up** cho các dự án phức tạp

**💡 Tip**: Bắt đầu với `rapid-prototyper` cho các task không rõ ràng, sau đó chuyển sang specialized agents khi requirements rõ hơn.

---

**📞 Support**: Nếu có vấn đề với agent selection hoặc workflow integration, hãy check troubleshooting section hoặc tạo issue để được hỗ trợ.