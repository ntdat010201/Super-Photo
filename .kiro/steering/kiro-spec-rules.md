---
inclusion: manual
---

# Kiro Spec-Driven Development Rules

> **🎯 KIRO-SPECIFIC SPEC WORKFLOW RULES**  
> These rules are specifically designed for Kiro's spec-driven development workflow

## 🔄 Spec Workflow Overview

Kiro follows a structured 3-phase approach to feature development:

1. **Requirements Phase** - EARS format requirements with user stories
2. **Design Phase** - Comprehensive technical design with research
3. **Tasks Phase** - Actionable implementation checklist

## ⚠️ CRITICAL WORKFLOW ENFORCEMENT

### Sequential Phase Requirements

- **BẮT BUỘC** complete Requirements before Design
- **BẮT BUỘC** complete Design before Tasks  
- **BẮT BUỘC** get explicit user approval after each phase
- **BẮT BUỘC** use userInput tool with specific reasons for each review
- **NGHIÊM CẤM** skipping any phase or proceeding without approval

### User Approval Protocol

- Requirements review: `reason: 'spec-requirements-review'`
- Design review: `reason: 'spec-design-review'`
- Tasks review: `reason: 'spec-tasks-review'`
- **BẮT BUỘC** wait for explicit approval ("yes", "approved", "looks good")
- **BẮT BUỘC** continue feedback-revision cycle until approval received

## 📁 Spec File Structure

```
.kiro/specs/{feature_name}/
├── requirements.md    # EARS format with user stories
├── design.md         # Technical design with research
└── tasks.md          # Implementation task checklist
```

### Feature Naming Convention

- Use kebab-case format (e.g., "user-authentication")
- Based on the core feature functionality
- Keep concise but descriptive

## 📋 Requirements Phase Rules

### Document Structure Requirements

- **BẮT BUỘC** create `.kiro/specs/{feature_name}/requirements.md`
- **BẮT BUỘC** include Introduction section summarizing the feature
- **BẮT BUỘC** use hierarchical numbered list of requirements
- **BẮT BUỘC** each requirement must contain:
  - User story: "As a [role], I want [feature], so that [benefit]"
  - Numbered acceptance criteria in EARS format

### EARS Format Requirements

- Use WHEN/THEN structure: "WHEN [event] THEN [system] SHALL [response]"
- Use IF/THEN structure: "IF [precondition] THEN [system] SHALL [response]"
- Include AND conditions when needed: "WHEN [event] AND [condition] THEN [system] SHALL [response]"

### Requirements Review Process

- **BẮT BUỘC** generate initial requirements WITHOUT asking sequential questions first
- **BẮT BUỘC** consider edge cases, UX, technical constraints, success criteria
- **BẮT BUỘC** ask "Do the requirements look good? If so, we can move on to the design."
- **BẮT BUỘC** make modifications if user requests changes
- **BẮT BUỘC** continue revision cycle until explicit approval

## 🎨 Design Phase Rules

### Document Structure Requirements

- **BẮT BUỘC** create `.kiro/specs/{feature_name}/design.md`
- **BẮT BUỘC** base design on approved requirements document
- **BẮT BUỘC** include required sections:
  - Overview
  - Architecture  
  - Components and Interfaces
  - Data Models
  - Error Handling
  - Testing Strategy

### Research Integration

- **BẮT BUỘC** identify areas needing research based on requirements
- **BẮT BUỘC** conduct research and build context in conversation
- **BẮT BUỘC** summarize key findings that inform design
- **KHUYẾN NGHỊ** cite sources and include relevant links
- **BẮT BUỘC** incorporate research findings directly into design

### Design Review Process

- **BẮT BUỘC** ask "Does the design look good? If so, we can move on to the implementation plan."
- **BẮT BUỘC** make modifications if user requests changes
- **BẮT BUỘC** offer to return to requirements if gaps identified
- **BẮT BUỘC** continue revision cycle until explicit approval

## ✅ Tasks Phase Rules

### Document Structure Requirements

- **BẮT BUỘC** create `.kiro/specs/{feature_name}/tasks.md`
- **BẮT BUỘC** base tasks on approved design document
- **BẮT BUỘC** format as numbered checkbox list with max 2 hierarchy levels
- **BẮT BUỘC** use decimal notation for sub-tasks (1.1, 1.2, 2.1)

### Task Content Requirements

- **BẮT BUỘC** each task must involve writing, modifying, or testing code
- **BắT BUỘC** include clear objective as task description
- **BẮT BUỘC** add specific requirements references as sub-bullets
- **BẮT BUỘC** ensure tasks build incrementally on previous steps
- **BẮT BUỘC** cover all aspects of design through code implementation

### Prohibited Task Types

- **NGHIÊM CẤM** user acceptance testing or feedback gathering
- **NGHIÊM CẤM** deployment to production/staging environments
- **NGHIÊM CẤM** performance metrics gathering/analysis
- **NGHIÊM CẤM** running application for end-to-end testing
- **NGHIÊM CẤM** user training or documentation creation
- **NGHIÊM CẤM** business process or organizational changes
- **NGHIÊM CẤM** marketing or communication activities

### Tasks Review Process

- **BẮT BUỘC** ask "Do the tasks look good?" 
- **BẮT BUỘC** make modifications if user requests changes
- **BẮT BUỘC** continue revision cycle until explicit approval
- **BẮT BUỘC** stop workflow once tasks document approved

## 🔧 Task Execution Rules

### Pre-Execution Requirements

- **BẮT BUỘC** read requirements.md, design.md, and tasks.md before executing
- **BẮT BUỘC** understand task details and context
- **BẮT BUỘC** start with sub-tasks if they exist
- **BẮT BUỘC** focus on ONE task at a time

### Task Status Management

- **BẮT BUỘC** update task status to 'in_progress' before starting
- **BẮT BUỘC** update task status to 'completed' when finished
- **BẮT BUỘC** update parent task to 'completed' when all sub-tasks done
- **BẮT BUỘC** use taskStatus tool with exact task text from tasks.md

### Execution Constraints

- **BẮT BUỘC** implement only functionality for current task
- **BẮT BUỘC** verify implementation against task requirements
- **BẮT BUỘC** stop after completing task for user review
- **NGHIÊM CẤM** automatically proceeding to next task
- **NGHIÊM CẤM** implementing functionality for other tasks

### Task Questions vs Execution

- Distinguish between task questions and execution requests
- For questions about tasks: provide information without executing
- For execution requests: follow full execution protocol
- Examples of questions: "What's the next task?", "How many tasks are left?"

## 🎯 Integration with Core Rules

### Development Standards

#[[file:../../.cursor/rules/development-rules.mdc]]

### Code Quality

#[[file:../../.cursor/rules/universal-code-deduplication.mdc]]

### File Protection

#[[file:../../.cursor/rules/file-protection-rules.mdc]]

### Planning Integration

#[[file:../../.cursor/rules/planning-workflow.mdc]]

## 📞 Workflow Completion

### End State Requirements

- **BẮT BUỘC** inform user that workflow is complete after tasks approval
- **BẮT BUỘC** explain that implementation should be done separately
- **BẮT BUỘC** direct user to tasks.md file for execution
- **BẮT BUỘC** mention "Start task" functionality for task execution

### Post-Completion Actions

- User can execute tasks by opening tasks.md
- User can click "Start task" next to task items
- Implementation happens through separate workflow
- Spec workflow is ONLY for creating planning artifacts

---

**This workflow creates design and planning artifacts only. Actual feature implementation requires separate execution workflow.**