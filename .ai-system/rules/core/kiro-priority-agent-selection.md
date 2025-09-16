# 🎯 Kiro Priority Agent Selection Rules

> **🚀 Intelligent agent selection with Kiro task optimization**  
> Prioritize Kiro for spec creation, optimize execution across IDEs

---

## 🌟 Core Philosophy

**Mission**: Leverage Kiro's superior task creation capabilities while optimizing execution across the IDE ecosystem  
**Strategy**: Kiro-first for planning and specification, best-fit for execution  
**Outcome**: Maximum efficiency through intelligent agent selection

---

## 🎯 Kiro Priority Triggers

### Automatic Kiro Selection

**High Priority Triggers** (Auto-select Kiro):
- User mentions "spec", "specification", "PRD", "requirements"
- User requests "task creation", "break down", "planning"
- User asks for "design document", "architecture", "technical design"
- Project needs feature planning or requirement analysis
- Complex feature implementation requiring structured approach

**Medium Priority Triggers** (Prefer Kiro):
- User mentions "tasks", "todo", "implementation plan"
- Project has existing `.kiro/specs/` structure
- User requests "organize", "structure", "plan"
- Feature requests that benefit from systematic breakdown

**Context Indicators**:
- Presence of `.kiro/` directory in project
- Existing spec files in `.kiro/specs/`
- User history of using Kiro for planning
- Project complexity requiring structured approach

### Kiro Exclusion Criteria

**Never Route to Kiro**:
- Simple code fixes or debugging
- Direct implementation without planning needs
- Platform-specific technical issues
- Quick questions or clarifications
- Emergency bug fixes requiring immediate action

**Prefer Other IDEs**:
- Pure implementation tasks with clear requirements
- Platform-specific development (iOS native, Android native)
- Performance optimization and code analysis
- Testing and quality assurance tasks
- Infrastructure and deployment tasks

---

## 🔄 Hybrid Workflow Integration

### Kiro → Other IDE Handoff

**Optimal Handoff Scenarios**:
1. **Kiro creates specs** → **Trae/Cursor executes implementation**
2. **Kiro designs architecture** → **Specialized IDE builds components**
3. **Kiro breaks down features** → **Platform IDE implements native features**

**Handoff Protocol**:
- Kiro completes specification phase
- Generate comprehensive context package
- Route implementation to optimal execution IDE
- Maintain traceability between spec and implementation

### Cross-IDE Collaboration Patterns

**Pattern 1: Spec-Driven Development**
```
User Request → Kiro (Specs) → Execution IDE (Implementation) → Validation
```

**Pattern 2: Complex Feature Development**
```
Kiro (Requirements) → Kiro (Design) → Kiro (Tasks) → Multiple IDEs (Execution)
```

**Pattern 3: Iterative Enhancement**
```
Kiro (Analysis) → Execution IDE (Changes) → Kiro (Validation) → Repeat
```

---

## 🎛️ Selection Algorithm

### Multi-Factor Scoring

**Kiro Preference Score (0-100)**:
- **Spec Keywords** (+30): "spec", "PRD", "requirements", "design"
- **Planning Keywords** (+25): "plan", "break down", "organize", "structure"
- **Project Context** (+20): Existing `.kiro/` structure, complex features
- **Task Complexity** (+15): Multi-step features, architectural decisions
- **User History** (+10): Previous successful Kiro interactions

**Execution IDE Score (0-100)**:
- **Implementation Keywords** (+30): "implement", "code", "build", "fix"
- **Platform Specificity** (+25): iOS, Android, React, specific frameworks
- **Technical Depth** (+20): Performance, debugging, optimization
- **Urgency Indicators** (+15): "urgent", "quick", "emergency", "asap"
- **Direct Action** (+10): Clear implementation path without planning

### Decision Matrix

| Kiro Score | Execution Score | Decision | Reasoning |
|------------|-----------------|----------|----------|
| 80+ | <60 | **Kiro** | Clear specification/planning need |
| 60-79 | 60-79 | **Kiro** | Prefer structured approach |
| 40-59 | 80+ | **Execution IDE** | Clear implementation focus |
| <40 | 80+ | **Execution IDE** | Direct implementation preferred |
| 60+ | 60+ | **Hybrid** | Kiro for specs, then handoff |

---

## 🚀 Advanced Selection Strategies

### Context-Aware Selection

**Project Stage Consideration**:
- **Early Stage**: Prefer Kiro for foundation and planning
- **Development Stage**: Balance between Kiro planning and execution IDEs
- **Maintenance Stage**: Prefer execution IDEs for direct changes
- **Refactoring Stage**: Kiro for analysis, execution IDEs for implementation

**Feature Complexity Assessment**:
- **Simple Features**: Direct to execution IDE
- **Medium Features**: Kiro for breakdown, execution IDE for implementation
- **Complex Features**: Full Kiro spec-driven workflow
- **Cross-Platform Features**: Kiro for coordination, multiple IDEs for implementation

### Dynamic Adaptation

**Learning from Outcomes**:
- Track success rates by selection decision
- Adjust scoring weights based on performance
- Learn user preferences and project patterns
- Optimize for both quality and efficiency

**Real-Time Optimization**:
- Consider current IDE availability and workload
- Factor in recent performance and quality metrics
- Adapt to changing project needs and priorities
- Balance immediate needs with long-term project health

---

## 📊 Quality Assurance

### Selection Validation

**Pre-Selection Checks**:
- Verify agent availability and capability
- Confirm context and requirements understanding
- Validate selection against project constraints
- Ensure optimal resource utilization

**Post-Selection Monitoring**:
- Track execution quality and efficiency
- Monitor user satisfaction with selection
- Identify opportunities for improvement
- Adjust selection criteria based on outcomes

### Performance Metrics

**Selection Accuracy**: Percentage of optimal agent selections  
**User Satisfaction**: Rating of agent selection appropriateness  
**Project Velocity**: Impact on overall development speed  
**Quality Outcomes**: Code quality and requirement fulfillment  
**Resource Efficiency**: Optimal utilization of IDE capabilities

---

## 🎯 Implementation Guidelines

### For System Integrators

**Selection Logic Implementation**:
- Implement scoring algorithm with configurable weights
- Create decision tree for complex scenarios
- Build learning mechanism for continuous improvement
- Ensure fallback strategies for edge cases

**Context Management**:
- Maintain comprehensive project context
- Track user preferences and patterns
- Monitor IDE performance and capabilities
- Facilitate smooth handoffs between agents

### For Users

**Optimization Tips**:
- Use clear keywords to indicate intent ("spec" vs "implement")
- Provide context about project stage and complexity
- Specify preferences when multiple approaches are valid
- Give feedback on agent selection effectiveness

**Best Practices**:
- Start complex features with Kiro for planning
- Use execution IDEs for direct implementation tasks
- Leverage hybrid workflows for optimal results
- Trust the system but provide guidance when needed

---

## 🌟 Success Indicators

**Optimal Utilization**: Kiro used for planning, execution IDEs for implementation  
**Seamless Handoffs**: Smooth transitions between specification and execution  
**Quality Consistency**: High standards maintained across all agents  
**User Satisfaction**: Positive experience with agent selection decisions  
**Project Success**: Improved delivery outcomes through intelligent routing

---

*This rule set ensures that Kiro's exceptional task creation capabilities are leveraged optimally while maintaining efficient execution across the entire IDE ecosystem.*