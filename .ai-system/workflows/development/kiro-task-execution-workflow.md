---
trae_context:
  format: "native"
  version: "1.0"
  migrated_from: "cursor"
  last_updated: "2025-08-18T07:00:32.181Z"
---

#kiro-task-execution
# Kiro Task Execution Workflow

## Overview

Hệ thống Kiro Task Execution tự động xử lý và thực thi các task được định nghĩa trong `.kiro/specs/{project}/tasks.md`. Workflow này đảm bảo rằng mọi task đều được thực hiện theo chuẩn Kiro specification mà không cần yêu cầu thủ công.

## Core Principles

### Automatic Task Detection
- **BẮT BUỘC** kiểm tra `.kiro/specs/{project}/tasks.md` trước khi bắt đầu bất kỳ task nào
- **BẮT BUỘC** xác định project context từ cấu trúc thư mục hiện tại
- **BẮT BUỘC** validate task format theo Kiro specification
- **BẮT BUỘC** ưu tiên task từ Kiro specs hơn task thủ công

### Task #execution-rules
- **BẮT BUỘC** thực hiện task theo thứ tự ưu tiên trong tasks.md
- **BẮT BUỘC** cập nhật trạng thái task sau khi hoàn thành
- **BẮT BUỘC** validate dependencies trước khi thực hiện task
- **NGHIÊM CẤM** thực hiện task không có trong Kiro specs
- **NGHIÊM CẤM** bỏ qua validation process

## Project Detection Algorithm

### Step 1: Identify Project Context
```
1. Kiểm tra current working directory
2. Tìm kiếm `.kiro/specs/` trong project root
3. Xác định project name từ folder structure
4. Validate existence của required files (requirements.md, design.md, tasks.md)
5. Nếu thiếu files → trigger Kiro Dynamic Workflow
```

### Step 2: Dynamic Workflow Trigger
```
1. Kiểm tra sự tồn tại của `.kiro/specs/{project}/requirements.md`
2. Kiểm tra sự tồn tại của `.kiro/specs/{project}/design.md`
3. Kiểm tra sự tồn tại của `.kiro/specs/{project}/tasks.md`
4. Nếu thiếu bất kỳ file nào → kích hoạt [Kiro Dynamic Workflow](kiro-dynamic-workflow.md)
5. Ưu tiên đề xuất sử dụng Kiro tools trước khi dynamic workflow
6. Thực hiện quy trình: Brainstorm → Requirements → Design → Tasks
```

### Step 3: Task File Validation
```
1. Kiểm tra `.kiro/specs/{project}/tasks.md` exists (sau khi dynamic workflow nếu cần)
2. Parse task structure theo Kiro format
3. Validate task dependencies
4. Kiểm tra task status (Not Started, In Progress, Completed)
5. Validate compatibility với Kiro execution system
```

## Task Structure Requirements

### Standard Task Format
```markdown
## Task ID: [UNIQUE_ID]
**Status**: [Not Started|In Progress|Completed]
**Priority**: [High|Medium|Low]
**Dependencies**: [List of dependent tasks]
**Estimated Time**: [Time estimate]

### Description
[Detailed task description]

### Acceptance Criteria
- [ ] Criterion 1
- [ ] Criterion 2
- [ ] Criterion 3

### Implementation Notes
[Technical notes and considerations]
```

### Task Status Management
- **Not Started**: ❌ - Task chưa được bắt đầu
- **In Progress**: ⏳ - Task đang được thực hiện
- **Completed**: ✅ - Task đã hoàn thành
- **Blocked**: 🚫 - Task bị chặn bởi dependencies

## Context Loading Requirements

### Mandatory Context Files
**BẮT BUỘC kiểm tra và load các file context trước khi thực hiện bất kỳ task nào:**

#### Requirements Context
- **File**: `.kiro/specs/{project}/requirements.md`
- **Purpose**: Hiểu rõ yêu cầu chức năng và phi chức năng của tính năng
- **Usage**: Reference trong quá trình validation và implementation
- **Validation**: Đảm bảo task đáp ứng đầy đủ requirements

#### Design Context
- **File**: `.kiro/specs/{project}/design.md`
- **Purpose**: Hiểu rõ architecture, UI/UX design, và technical approach
- **Usage**: Đảm bảo implementation tuân theo thiết kế đã định
- **Validation**: Verify code structure và UI matches design specifications

#### Task Context
- **File**: `.kiro/specs/{project}/tasks.md`
- **Purpose**: Danh sách chi tiết các task cần thực hiện
- **Usage**: Task execution và progress tracking
- **Validation**: Ensure task completion meets acceptance criteria

### Context Loading Workflow
```
1. **BẮT BUỘC** Kiểm tra sự tồn tại của 3 files: requirements.md, design.md, tasks.md
2. **BẮT BUỘC** Load và parse nội dung của từng file
3. **BẮT BUỘC** Cross-reference giữa requirements, design, và tasks
4. **BẮT BUỘC** Validate consistency giữa các specifications
5. **BẮT BUỘC** Identify potential conflicts hoặc missing information
6. **KHUYẾN NGHỊ** Cache context để tránh reload không cần thiết
```

## Execution Workflow

### Phase 1: Pre-Execution Validation
```
1. Load .kiro/specs/{project}/tasks.md
2. **BẮT BUỘC** Load .kiro/specs/{project}/requirements.md để hiểu yêu cầu tính năng
3. **BẮT BUỘC** Load .kiro/specs/{project}/design.md để hiểu thiết kế và architecture
4. Parse all tasks and their metadata
5. Build dependency graph
6. Cross-reference tasks với requirements và design specifications
7. Identify next executable task
8. Validate prerequisites và compatibility với requirements/design
```

### Phase 2: Task Execution
```
1. Update task status to "In Progress"
2. **BẮT BUỘC** Reference requirements.md để đảm bảo task đáp ứng yêu cầu
3. **BẮT BUỘC** Reference design.md để đảm bảo implementation tuân theo thiết kế
4. Execute task according to specifications
5. Follow implementation notes và guidelines từ requirements/design
6. Validate acceptance criteria against requirements
7. Verify implementation matches design specifications
8. Update task status to "Completed"
```

### Phase 3: Post-Execution Updates
```
1. Update tasks.md with new status
2. Log execution details
3. Update project progress tracking
4. Identify next available tasks
5. Notify about completion
```

## Integration with Existing Workflows

### Planning Workflow Integration
- Planning workflow sẽ tạo tasks trong `.kiro/specs/{project}/tasks.md`
- Không tạo tasks trong các file khác
- Sync với existing planning documents

### Brainstorm Workflow Integration
- Brainstorm results sẽ được convert thành Kiro tasks
- Maintain traceability từ brainstorm đến tasks
- Update task priorities based on brainstorm insights

### Development Workflow Integration
- Tất cả development tasks phải có trong Kiro specs
- Code changes phải reference task IDs
- Commit messages phải include task references

## Task Types and Categories

### Development Tasks
- **Feature Implementation**: Triển khai tính năng mới
- **Bug Fixes**: Sửa lỗi và issues
- **Refactoring**: Cải thiện code structure
- **Testing**: Viết và chạy tests
- **Documentation**: Tạo và cập nhật docs

### Project Management Tasks
- **Planning**: Lập kế hoạch và estimation
- **Review**: Code review và quality assurance
- **Deployment**: Deploy và release management
- **Monitoring**: Performance và error monitoring

### Research Tasks
- **Investigation**: Nghiên cứu technical solutions
- **Prototyping**: Tạo proof of concepts
- **Analysis**: Phân tích requirements và constraints

## Error Handling and Recovery

### Task Execution Failures
```
1. Log error details với context
2. Revert task status về "Not Started"
3. Add failure notes to task
4. Identify root cause
5. Update task với additional requirements
```

### Dependency Resolution
```
1. Identify missing dependencies
2. Create dependency tasks if needed
3. Update task order based on dependencies
4. Notify about dependency issues
```

### File System Issues
```
1. Validate .kiro directory structure
2. Create missing directories/files
3. Backup existing tasks before updates
4. Provide recovery mechanisms
```

### Missing Context Files
```
1. **Thiếu requirements.md**:
   - STOP task execution immediately
   - Trigger Kiro Dynamic Workflow để tạo requirements
   - Validate generated requirements before proceeding
   - Resume task execution với updated context

2. **Thiếu design.md**:
   - STOP task execution immediately
   - Trigger design generation process
   - Ensure design addresses all requirements
   - Validate design consistency trước khi continue

3. **Thiếu cả requirements và design**:
   - MANDATORY: Complete Brainstorm → Requirements → Design workflow
   - Không được phép thực hiện task without proper context
   - Validate toàn bộ specification chain
```

## Quality Assurance

### Task #validation-rules
- **BẮT BUỘC** validate task format before execution
- **BẮT BUỘC** load và validate requirements.md context
- **BẮT BUỘC** load và validate design.md context
- **BẮT BUỘC** check acceptance criteria completeness against requirements
- **BẮT BUỘC** verify implementation matches design specifications
- **BẮT BUỘC** cross-reference task với requirements và design
- **BẮT BUỘC** test functionality before marking complete

### Progress Tracking
- Track time spent on each task
- Monitor task completion rates
- Identify bottlenecks and blockers
- Generate progress reports

## Command Interface

### Automatic Commands
```
- kiro:scan - Scan for available tasks
- kiro:next - Get next executable task
- kiro:status - Show current task status
- kiro:progress - Show project progress
```

### Manual Override Commands
```
- kiro:force-task [ID] - Force execute specific task
- kiro:skip-task [ID] - Skip task with reason
- kiro:reset-task [ID] - Reset task status
- kiro:validate - Validate all tasks
```

## Reporting and Analytics

### Task Metrics
- Task completion time vs estimates
- Task failure rates and reasons
- Dependency chain analysis
- Resource utilization tracking

### Project Health
- Overall progress percentage
- Blocked tasks count
- Critical path analysis
- Risk assessment

## Security and Compliance

### Access Control
- Validate user permissions for task execution
- Audit trail for all task modifications
- Secure handling of sensitive tasks

### Data Protection
- Backup task data before modifications
- Version control for task specifications
- Recovery procedures for data loss

## Best Practices

### Task Design
- Keep tasks atomic and focused
- Define clear acceptance criteria
- Estimate time realistically
- Document implementation approaches

### Workflow Optimization
- Parallelize independent tasks
- Minimize context switching
- Batch similar tasks together
- Optimize for developer productivity

### Maintenance
- Regular cleanup of completed tasks
- Archive old project specifications
- Update task templates based on learnings
- Continuous improvement of processes

## Integration Points

### File System Integration
```
.kiro/
├── specs/
│   └── {project}/
│       ├── requirements.md
│       ├── design.md
│       └── tasks.md          # Primary task source
└── steering/
    └── kiro-spec-rules.md
```

### Workflow Integration
```
.trae/rules/
├── planning-workflow.md     # Updated to use Kiro tasks
├── brainstorm-detailed-workflow.md  # Updated to generate Kiro tasks
├── development-rules.md     # Updated to reference Kiro tasks
└── kiro-task-execution.md   # This file
```

## Migration Strategy

### Existing Projects
1. Scan existing task files and documents
2. Convert to Kiro task format
3. Migrate to `.kiro/specs/{project}/tasks.md`
4. Update workflow references
5. Validate migration completeness

### New Projects
1. Initialize `.kiro/specs/{project}/` structure
2. Create initial tasks from requirements
3. Set up task dependencies
4. Configure workflow integration
5. Begin task execution

## Monitoring and Alerts

### Real-time Monitoring
- Task execution progress
- Dependency resolution status
- Error rates and patterns
- Performance metrics

### Alert Conditions
- Task execution failures
- Dependency deadlocks
- Missing task specifications
- Performance degradation

## Dynamic Workflow Integration

### Automatic Dynamic Detection
- **BẮT BUỘC** kiểm tra sự tồn tại của required Kiro files trước khi execution:
  - `.kiro/specs/{project}/requirements.md` - Yêu cầu tính năng
  - `.kiro/specs/{project}/design.md` - Thiết kế và architecture
  - `.kiro/specs/{project}/tasks.md` - Danh sách task
- **BẮT BUỘC** trigger [Kiro Dynamic Workflow](kiro-dynamic-workflow.md) khi thiếu bất kỳ file nào
- **KHUYẾN NGHỊ** đề xuất sử dụng Kiro tools trước khi dynamic workflow
- **BẮT BUỘC** validate generated files sau khi dynamic workflow hoàn thành
- **BẮT BUỘC** ensure context consistency giữa requirements, design, và tasks

### Dynamic Execution Flow
```
1. Detect missing files (requirements.md, design.md, tasks.md)
2. Notify user về missing files và options
3. Offer choice: Use Kiro tools OR Dynamic workflow
4. If dynamic chosen:
   a. Execute Brainstorm → Requirements → Design → Tasks
   b. Validate generated files format
   c. Test compatibility với Kiro execution
   d. Resume normal task execution
```

### Post-Dynamic Validation
- **BẮT BUỘC** validate task format compatibility
- **BẮT BUỘC** check task dependencies structure
- **BẮT BUỘC** verify acceptance criteria completeness
- **BẮT BUỘC** test task execution với generated files
- **BẮT BUỘC** ensure seamless transition to normal workflow

### Dynamic Quality Gates
- Requirements must satisfy project objectives
- Design must address all requirements
- Tasks must cover all design components
- All files must follow Kiro specifications
- Generated content must be executable

## Future Enhancements

### Planned Features
- AI-powered task estimation
- Automatic dependency detection
- Smart task prioritization
- Integration with external tools
- Enhanced dynamic workflow automation

### Extensibility
- Plugin system for custom task types
- API for external integrations
- Custom workflow definitions
- Advanced analytics and reporting
- Dynamic workflow customization

This workflow ensures that all task execution follows Kiro specifications while maintaining compatibility with existing development processes and providing robust dynamic mechanisms when Kiro files are not available.