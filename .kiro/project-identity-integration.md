# Project Identity Integration for Kiro System

## Mandatory Project Identity Check Protocol for Kiro

### Pre-Task Execution Requirements
- **BẮT BUỘC**: Kiểm tra `/.project-identity` trước khi thực thi BẤT KỲ Kiro task nào
- **BẮT BUỘC**: Validate `currentWorkingStatus` không có AI nào đang làm việc trên cùng specs/tasks
- **BẮT BUỘC**: Kiểm tra `knownIssues` array cho Kiro-specific problems
- **BẮT BUỘC**: Sync với `taskQueue` để avoid duplication

### Kiro Task Declaration Protocol

#### Khi Bắt Đầu Task Execution
1. Cập nhật `currentWorkingStatus` trong .project-identity:
   ```json
   {
     "aiTool": "Kiro",
     "workIntent": "[Task type và mô tả] - [Spec files và output files]",
     "status": "in_progress",
     "lastUpdate": "2024-12-20T11:00:00Z"
   }
   ```

2. Kiro-specific Intent Format:
   - **Task Type**: CREATE_NEW, UPDATE_EXISTING, SUPPLEMENT_DATA, RESTRUCTURE
   - **Scope**: Requirements, Design, Tasks, hoặc Full Workflow
   - **Target Project**: Tên project trong .kiro/specs/
   - **Expected Output**: Files sẽ được tạo/cập nhật

3. Target Files cho Kiro:
   - `.kiro/specs/{project}/requirements.md`
   - `.kiro/specs/{project}/design.md`
   - `.kiro/specs/{project}/tasks.md`
   - Các files liên quan khác

#### Kiro Dynamic Workflow Integration
- **BẮT BUỘC**: Declare intent cho từng stage của Dynamic Workflow
- **BẮT BUỘC**: Update Target Files khi chuyển stage
- **BẮT BUỘC**: Maintain consistency với Kiro Task Execution rules

#### Stage-Specific Declarations

##### Brainstorm Stage
```json
{
  "aiTool": "Kiro",
  "workIntent": "Brainstorm stage for [project] - analyzing requirements and generating ideas - brainstorms/[project].md",
  "status": "in_progress",
  "lastUpdate": "2024-12-20T11:00:00Z"
}
```

##### Requirements Stage
```json
{
  "aiTool": "Kiro",
  "workIntent": "Requirements stage for [project] - converting brainstorm to functional requirements - .kiro/specs/[project]/requirements.md",
  "status": "in_progress",
  "lastUpdate": "2024-12-20T11:15:00Z"
}
```

##### Design Stage
```json
{
  "aiTool": "Kiro",
  "workIntent": "Design stage for [project] - creating architecture and UI/UX design - .kiro/specs/[project]/design.md",
  "status": "in_progress",
  "lastUpdate": "2024-12-20T11:30:00Z"
}
```

##### Tasks Stage
```json
{
  "aiTool": "Kiro",
  "workIntent": "Tasks stage for [project] - generating actionable development tasks - .kiro/specs/[project]/tasks.md",
  "status": "in_progress",
  "lastUpdate": "2024-12-20T11:45:00Z"
}
```

### Kiro-Specific Conflict Resolution
- **NGHIÊM CẤM**: Thực thi task nếu AI khác đang làm việc trên cùng project specs (check `currentWorkingStatus.aiTool`)
- **BẮT BUỘC**: Coordinate với other AIs khi cần access shared resources
- **BẮT BUỘC**: Respect working status và wait for completion (`status: "completed"`)

### Quality Gates Integration
- **BẮT BUỘC**: Update `currentWorkingStatus.status` khi pass/fail quality gates
- **BẮT BUỘC**: Document quality issues trong `knownIssues` array
- **BẮT BUỘC**: Add entry to `recentChanges` về quality gate status

### Auto-Task Execution Coordination
- **BẮT BUỘC**: Declare intent trong `currentWorkingStatus` trước khi auto-execute tasks
- **BẮT BUỘC**: Update `lastUpdate` timestamp cho long-running auto-executions
- **BẮT BUỘC**: Handle interruptions gracefully với status "blocked" hoặc "completed"

### Kiro System Status Reporting
- **BẮT BUỘC**: Report Kiro system health trong `systemHealth` object
- **BẮT BUỘC**: Update `metrics` object với Kiro performance data
- **BẮT BUỘC**: Log Kiro-specific errors trong `knownIssues` và resolutions trong `recentChanges`

## Example Kiro Declarations

### Full Project Creation
```json
{
  "aiTool": "Kiro",
  "workIntent": "Creating complete specs for e-commerce mobile app - .kiro/specs/ecommerce-app/requirements.md, design.md, tasks.md",
  "status": "in_progress",
  "lastUpdate": "2024-12-20T11:00:00Z"
}
```

### Requirements Update
```json
{
  "aiTool": "Kiro",
  "workIntent": "Updating requirements for payment integration - .kiro/specs/ecommerce-app/requirements.md",
  "status": "in_progress",
  "lastUpdate": "2024-12-20T11:15:00Z"
}
```

### Task Generation
```json
{
  "aiTool": "Kiro",
  "workIntent": "Generating development tasks for user authentication - .kiro/specs/ecommerce-app/tasks.md",
  "status": "in_progress",
  "lastUpdate": "2024-12-20T11:30:00Z"
}
```

### Multi-Stage Workflow
```json
{
  "aiTool": "Kiro",
  "workIntent": "Dynamic workflow: Brainstorm → Requirements → Design → Tasks for CRM system - brainstorms/crm-system.md, .kiro/specs/crm-system/*",
  "status": "in_progress",
  "lastUpdate": "2024-12-20T11:45:00Z"
}
```

## Integration với Existing Kiro Rules
- Tuân thủ tất cả rules trong `.cursor/rules/kiro-*.mdc`
- Maintain compatibility với Kiro Task Execution System
- Enhance existing workflows với project-identity coordination
- Preserve Kiro's autonomous operation capabilities

## Monitoring và Metrics
- Track Kiro task completion rates trong `metrics.kiroTaskCompletionRate`
- Monitor conflict resolution effectiveness trong `metrics.conflictResolutionTime`
- Measure coordination overhead trong `metrics.coordinationOverhead`
- Report performance impact của project-identity integration trong `systemHealth.integrationPerformance`