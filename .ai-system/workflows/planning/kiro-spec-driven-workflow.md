# 🎯 Kiro Spec-Driven Development Workflow

> **⚡ Integrated Kiro task creation and execution workflow**  
> Leverages Kiro's powerful spec-driven development for structured feature implementation

---

## 🌟 Workflow Overview

**Purpose**: Utilize Kiro's superior task creation capabilities while enabling execution across all IDEs  
**Philosophy**: Kiro creates, everyone executes  
**Integration**: Seamless handoff from Kiro specs to IDE-specific implementation

### 🔄 Three-Phase Process

1. **📋 Requirements Phase** - EARS format requirements with user stories
2. **🎨 Design Phase** - Comprehensive technical design with research
3. **✅ Tasks Phase** - Actionable implementation checklist

---

## 🎯 Phase 1: Requirements Creation (Kiro)

### Trigger Conditions
- New feature development request
- Complex functionality requiring structured planning
- User mentions "spec", "requirements", or "detailed planning"
- Project stage requires formal documentation

### Kiro Requirements Process

**File Creation**: `.kiro/specs/{feature_name}/requirements.md`

**Structure Requirements**:
- Introduction section summarizing the feature
- Hierarchical numbered list of requirements
- User stories: "As a [role], I want [feature], so that [benefit]"
- EARS format acceptance criteria:
  - WHEN/THEN structure: "WHEN [event] THEN [system] SHALL [response]"
  - IF/THEN structure: "IF [precondition] THEN [system] SHALL [response]"
  - AND conditions when needed

**Quality Standards**:
- Consider edge cases, UX, technical constraints
- Include success criteria and validation points
- Reference existing system components
- Align with project architecture

**Approval Process**:
- Generate initial requirements without sequential questions
- Present complete requirements document
- Request user approval: "Do the requirements look good?"
- Iterate based on feedback until explicit approval

---

## 🎨 Phase 2: Design Creation (Kiro)

### Design Document Structure

**File Creation**: `.kiro/specs/{feature_name}/design.md`

**Required Sections**:
- **Overview**: High-level feature description and goals
- **Architecture**: System integration and component relationships
- **Components and Interfaces**: Detailed component specifications
- **Data Models**: Database schema and data structures
- **Error Handling**: Exception scenarios and recovery strategies
- **Testing Strategy**: Unit, integration, and acceptance testing approach

### Research Integration

**Research Requirements**:
- Identify knowledge gaps from requirements
- Conduct technical research during design phase
- Build context through conversation and documentation
- Summarize key findings that inform design decisions
- Cite sources and include relevant links
- Incorporate research directly into design document

**Design Quality Standards**:
- Base design on approved requirements
- Ensure technical feasibility
- Consider scalability and maintainability
- Align with existing system architecture
- Include security and performance considerations

**Approval Process**:
- Present complete design document
- Request user approval: "Does the design look good?"
- Offer to return to requirements if gaps identified
- Iterate until explicit approval received

---

## ✅ Phase 3: Tasks Creation (Kiro)

### Task Document Structure

**File Creation**: `.kiro/specs/{feature_name}/tasks.md`

**Format Requirements**:
- Numbered checkbox list with max 2 hierarchy levels
- Decimal notation for sub-tasks (1.1, 1.2, 2.1)
- Each task involves writing, modifying, or testing code
- Clear objective as task description
- Specific requirements references as sub-bullets
- Incremental build on previous steps

### Task Content Standards

**Allowed Task Types**:
- Code implementation and modification
- Unit and integration testing
- Configuration and setup
- Database schema creation
- API endpoint development
- UI component implementation

**Prohibited Task Types**:
- User acceptance testing or feedback gathering
- Production/staging deployment
- Performance metrics analysis
- End-to-end application testing
- User training or documentation
- Business process changes
- Marketing activities

**Quality Requirements**:
- Cover all design aspects through code
- Ensure logical task progression
- Include validation and testing steps
- Reference specific requirements
- Maintain appropriate granularity

**Approval Process**:
- Present complete tasks document
- Request user approval: "Do the tasks look good?"
- Iterate based on feedback
- Stop workflow once tasks approved

---

## 🔄 Phase 4: Cross-IDE Task Execution

### Task Distribution Strategy

**Kiro Advantage**: Superior task creation and project planning  
**Other IDEs**: Optimized for specific development tasks

### Execution Workflow

1. **Task Selection**: Choose appropriate IDE based on task type
2. **Context Loading**: Load requirements, design, and task context
3. **Implementation**: Execute task using IDE-specific strengths
4. **Status Update**: Update task status in Kiro specs
5. **Quality Check**: Verify against requirements and design

### IDE-Specific Task Routing

**Trae AI**: Complex multi-step tasks, system integration  
**Cursor**: Rapid prototyping, code generation  
**Claude**: Code analysis, optimization, documentation  
**Qoder**: Quick fixes, simple implementations

### Task Status Management

**Status Updates**:
- Update task to 'in_progress' before starting
- Update to 'completed' when finished
- Update parent task when all sub-tasks complete
- Use exact task text from tasks.md

**Execution Constraints**:
- Implement only current task functionality
- Verify against task requirements
- Stop after completion for review
- No automatic progression to next task

---

## 🎯 Integration Points

### Agent Selection Integration

**Kiro Priority Triggers**:
- Presence of `.kiro/specs/` directory
- User mentions "spec", "requirements", "design", "tasks"
- Complex feature development requests
- Formal planning requirements

**Task Execution Routing**:
- Analyze task type and complexity
- Route to optimal IDE for execution
- Maintain context across IDE switches
- Ensure consistent quality standards

### Project Identity Integration

**Spec-Driven Projects**:
- Set `useKiroSpecs: true` in project identity
- Enable automatic Kiro routing for planning
- Configure task execution preferences
- Maintain spec-to-implementation traceability

### Quality Assurance

**Validation Points**:
- Requirements completeness and clarity
- Design technical feasibility
- Task implementation accuracy
- Cross-IDE consistency

**Success Metrics**:
- Spec completion rate
- Implementation accuracy
- Time to delivery
- Quality scores

---

## 🚀 Best Practices

### For Kiro Spec Creation
- Generate complete documents before requesting approval
- Include comprehensive research and context
- Ensure traceability from requirements to tasks
- Maintain consistent naming and structure

### For Cross-IDE Execution
- Load full context before task execution
- Verify task completion against specifications
- Update status consistently across systems
- Maintain quality standards regardless of IDE

### For Project Management
- Use Kiro for all formal planning activities
- Route implementation to optimal IDEs
- Maintain central task tracking
- Regular quality and progress reviews

---

## 📊 Success Indicators

**Planning Quality**: Complete, clear, actionable specifications  
**Execution Efficiency**: Optimal IDE utilization for each task type  
**Delivery Speed**: Reduced planning overhead, faster implementation  
**Quality Consistency**: Uniform standards across all IDEs  
**Team Productivity**: Leveraged strengths of each development tool

---

*This workflow maximizes Kiro's planning strengths while enabling flexible execution across the entire IDE ecosystem.*