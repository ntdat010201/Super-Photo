# Mandatory Project Identity Update Hooks

> **🔴 CRITICAL: Mandatory Project State Management**  
> Hệ thống hooks bắt buộc để đảm bảo AI agents luôn cập nhật trạng thái project-identity

## System Overview

**Purpose**: Enforce mandatory project-identity updates at critical workflow checkpoints  
**Method**: Automated hooks with validation checkpoints and blocking mechanisms  
**Enforcement**: Hard stops and validation gates that prevent workflow continuation  
**Integration**: Embedded in all .god workflows and agent selection processes

## 🔴 MANDATORY HOOK SYSTEM

### Core Enforcement Principles

```yaml
Enforcement Level: BLOCKING
Bypass Allowed: NEVER
Validation Required: ALWAYS
Failure Action: STOP_WORKFLOW
```

**Non-Negotiable Rules**:
- ❌ **NO workflow can proceed without project-identity validation**
- ❌ **NO task completion without status update**
- ❌ **NO agent switching without state synchronization**
- ❌ **NO session end without final state commit**

## Hook Implementation Points

### 1. Pre-Task Validation Hook (MANDATORY)

**Trigger**: Before any task execution  
**Action**: Validate and load current project state

```markdown
🔴 PRE-TASK VALIDATION CHECKLIST (BLOCKING)
☐ .project-identity file exists and is readable
☐ Current projectStage is valid and up-to-date
☐ Project metadata matches current session context
☐ Required workflow files are accessible
☐ Previous session state is properly loaded

❌ BLOCKING CONDITIONS:
- Missing .project-identity file
- Invalid or corrupted project metadata
- Stage progression violations
- Workflow file access failures
```

**Implementation**:
```python
def pre_task_validation_hook():
    """
    MANDATORY: Execute before any task processing
    BLOCKING: Workflow cannot continue if validation fails
    """
    try:
        # Load and validate project identity
        project_identity = load_project_identity()
        validate_project_identity_structure(project_identity)
        
        # Validate stage progression
        validate_stage_progression(project_identity.projectStage)
        
        # Load appropriate workflow rules
        load_stage_specific_workflows(project_identity)
        
        # Validate session continuity
        validate_session_continuity(project_identity)
        
        return ValidationResult.SUCCESS
        
    except ValidationError as e:
        # BLOCKING: Stop all workflow execution
        raise BlockingValidationError(
            f"MANDATORY VALIDATION FAILED: {e}"
            f"\n🚫 WORKFLOW BLOCKED UNTIL RESOLVED"
            f"\n✅ Required Action: {e.resolution_steps}"
        )
```

### 2. Task Progress Update Hook (MANDATORY)

**Trigger**: During task execution at key milestones  
**Action**: Update project state with current progress

```markdown
🔴 PROGRESS UPDATE CHECKPOINTS (MANDATORY)
☐ Task initiation (status: started)
☐ 25% completion milestone
☐ 50% completion milestone  
☐ 75% completion milestone
☐ Task completion (status: completed)
☐ Error/failure states (status: failed)

📊 REQUIRED UPDATES:
- currentTask: Task description and ID
- taskProgress: Percentage completion
- lastUpdated: ISO timestamp
- sessionId: Current session identifier
- agentType: Current executing agent
```

**Implementation**:
```python
def task_progress_update_hook(task_id, progress_percentage, status):
    """
    MANDATORY: Update project state during task execution
    FREQUENCY: At every 25% milestone + start/end
    """
    try:
        project_identity = load_project_identity()
        
        # Update task progress
        project_identity.currentTask = {
            'id': task_id,
            'progress': progress_percentage,
            'status': status,
            'lastUpdated': get_iso_timestamp(),
            'sessionId': get_current_session_id(),
            'agentType': get_current_agent_type()
        }
        
        # Update session metadata
        project_identity.lastUpdated = get_iso_timestamp()
        project_identity.sessionHistory.append({
            'sessionId': get_current_session_id(),
            'timestamp': get_iso_timestamp(),
            'action': f'task_progress_update_{progress_percentage}%',
            'taskId': task_id
        })
        
        # Atomic write with backup
        atomic_write_project_identity(project_identity)
        
        return UpdateResult.SUCCESS
        
    except Exception as e:
        # Log error but don't block workflow for progress updates
        log_critical_error(f"Progress update failed: {e}")
        schedule_retry_update(task_id, progress_percentage, status)
```

### 3. Task Completion Hook (BLOCKING)

**Trigger**: Upon task completion or failure  
**Action**: Mandatory final state update and validation

```markdown
🔴 TASK COMPLETION VALIDATION (BLOCKING)
☐ Task marked as completed in project-identity
☐ Final deliverables documented
☐ Quality metrics recorded
☐ Next steps identified and documented
☐ Session summary generated
☐ Workflow state properly transitioned

❌ BLOCKING CONDITIONS:
- Task not marked as completed
- Missing deliverables documentation
- Quality validation failures
- State transition violations
```

**Implementation**:
```python
def task_completion_hook(task_id, completion_status, deliverables, quality_metrics):
    """
    MANDATORY: Execute upon task completion
    BLOCKING: Session cannot end without successful completion
    """
    try:
        project_identity = load_project_identity()
        
        # Validate task completion
        validate_task_completion(task_id, completion_status)
        
        # Update completion state
        project_identity.completedTasks.append({
            'id': task_id,
            'completedAt': get_iso_timestamp(),
            'status': completion_status,
            'deliverables': deliverables,
            'qualityMetrics': quality_metrics,
            'sessionId': get_current_session_id(),
            'agentType': get_current_agent_type()
        })
        
        # Clear current task
        project_identity.currentTask = None
        
        # Update project statistics
        update_project_statistics(project_identity, completion_status)
        
        # Validate stage progression requirements
        validate_stage_completion_requirements(project_identity)
        
        # Atomic write with validation
        atomic_write_project_identity(project_identity)
        
        return CompletionResult.SUCCESS
        
    except ValidationError as e:
        # BLOCKING: Prevent session end
        raise BlockingCompletionError(
            f"TASK COMPLETION VALIDATION FAILED: {e}"
            f"\n🚫 SESSION CANNOT END UNTIL RESOLVED"
            f"\n✅ Required Action: {e.resolution_steps}"
        )
```

### 4. Agent Switch Hook (MANDATORY)

**Trigger**: When switching between agents or workflows  
**Action**: Synchronize state between agents

```markdown
🔴 AGENT SWITCH SYNCHRONIZATION (MANDATORY)
☐ Current agent state exported
☐ Context transfer package created
☐ New agent state initialized
☐ Handoff validation completed
☐ Continuity verification passed

📦 CONTEXT TRANSFER PACKAGE:
- Current task state and progress
- Session history and decisions
- Active workflow state
- Quality metrics and validations
- User preferences and feedback
```

**Implementation**:
```python
def agent_switch_hook(from_agent, to_agent, context_transfer_data):
    """
    MANDATORY: Execute during agent transitions
    BLOCKING: New agent cannot start without proper handoff
    """
    try:
        project_identity = load_project_identity()
        
        # Export current agent state
        current_state = export_agent_state(from_agent)
        
        # Create context transfer package
        transfer_package = create_context_transfer_package(
            current_state, context_transfer_data
        )
        
        # Validate transfer package completeness
        validate_context_transfer_package(transfer_package)
        
        # Update agent transition history
        project_identity.agentTransitions.append({
            'fromAgent': from_agent,
            'toAgent': to_agent,
            'timestamp': get_iso_timestamp(),
            'sessionId': get_current_session_id(),
            'transferPackageId': transfer_package.id
        })
        
        # Initialize new agent with context
        initialize_agent_with_context(to_agent, transfer_package)
        
        # Validate handoff completion
        validate_agent_handoff(from_agent, to_agent, transfer_package)
        
        # Atomic write
        atomic_write_project_identity(project_identity)
        
        return HandoffResult.SUCCESS
        
    except HandoffError as e:
        # BLOCKING: Prevent agent switch
        raise BlockingHandoffError(
            f"AGENT HANDOFF FAILED: {e}"
            f"\n🚫 AGENT SWITCH BLOCKED UNTIL RESOLVED"
            f"\n✅ Required Action: {e.resolution_steps}"
        )
```

### 5. Session End Hook (BLOCKING)

**Trigger**: Before session termination  
**Action**: Final state validation and commit

```markdown
🔴 SESSION END VALIDATION (BLOCKING)
☐ All active tasks properly closed
☐ Project state fully synchronized
☐ Session summary generated
☐ Quality metrics finalized
☐ Next session preparation completed
☐ Backup and recovery points created

❌ SESSION END BLOCKERS:
- Active tasks not properly closed
- Unsaved state changes
- Quality validation failures
- Backup creation failures
```

**Implementation**:
```python
def session_end_hook(session_id, session_summary):
    """
    MANDATORY: Execute before session termination
    BLOCKING: Session cannot end without successful validation
    """
    try:
        project_identity = load_project_identity()
        
        # Validate all tasks are properly closed
        validate_all_tasks_closed(project_identity)
        
        # Generate final session summary
        final_summary = generate_session_summary(
            session_id, session_summary, project_identity
        )
        
        # Update session history
        project_identity.sessionHistory.append({
            'sessionId': session_id,
            'endTime': get_iso_timestamp(),
            'summary': final_summary,
            'tasksCompleted': count_completed_tasks(session_id),
            'qualityScore': calculate_session_quality_score(session_id)
        })
        
        # Create backup and recovery points
        create_session_backup(project_identity, session_id)
        
        # Prepare for next session
        prepare_next_session_state(project_identity)
        
        # Final atomic write
        atomic_write_project_identity(project_identity)
        
        return SessionEndResult.SUCCESS
        
    except ValidationError as e:
        # BLOCKING: Prevent session end
        raise BlockingSessionEndError(
            f"SESSION END VALIDATION FAILED: {e}"
            f"\n🚫 SESSION CANNOT END UNTIL RESOLVED"
            f"\n✅ Required Action: {e.resolution_steps}"
        )
```

## Auto-Reminder System

### 6. Periodic Reminder Hook (NON-BLOCKING)

**Trigger**: Every 10 minutes during active session  
**Action**: Remind AI to update project state

```python
def periodic_reminder_hook():
    """
    NON-BLOCKING: Periodic reminders for state updates
    FREQUENCY: Every 10 minutes during active session
    """
    try:
        project_identity = load_project_identity()
        last_update = parse_timestamp(project_identity.lastUpdated)
        
        if time_since_last_update(last_update) > 10_minutes:
            send_reminder_notification(
                "⏰ REMINDER: Update project-identity status"
                "\n📊 Current task progress needs documentation"
                "\n✅ Action: Update currentTask and progress"
            )
            
        if has_active_task_without_progress_update(project_identity):
            send_urgent_reminder(
                "🚨 URGENT: Active task missing progress updates"
                "\n📋 Task: {project_identity.currentTask.id}"
                "\n⏱️ Last update: {last_update}"
                "\n✅ Action: Update task progress immediately"
            )
            
    except Exception as e:
        log_reminder_error(f"Reminder hook failed: {e}")
```

## Validation and Error Handling

### 7. Validation Rules Engine

```python
class ProjectIdentityValidator:
    """
    Comprehensive validation engine for project-identity integrity
    """
    
    def validate_structure(self, project_identity):
        """Validate basic structure and required fields"""
        required_fields = [
            'projectName', 'projectType', 'projectStage',
            'lastUpdated', 'sessionHistory', 'currentTask'
        ]
        
        for field in required_fields:
            if not hasattr(project_identity, field):
                raise StructureValidationError(f"Missing required field: {field}")
    
    def validate_stage_progression(self, current_stage, requested_stage):
        """Validate stage progression rules"""
        valid_progressions = {
            'stage1_brainstorm': ['stage2_setup'],
            'stage2_setup': ['stage3_development'],
            'stage3_development': ['stage4_testing', 'stage5_deployment'],
            'stage4_testing': ['stage3_development', 'stage5_deployment'],
            'stage5_deployment': ['stage6_maintenance']
        }
        
        if requested_stage not in valid_progressions.get(current_stage, []):
            raise StageProgressionError(
                f"Invalid stage progression: {current_stage} -> {requested_stage}"
            )
    
    def validate_task_completion(self, task_id, completion_status):
        """Validate task completion requirements"""
        if completion_status == 'completed':
            # Validate deliverables exist
            # Validate quality metrics
            # Validate acceptance criteria met
            pass
        elif completion_status == 'failed':
            # Validate failure reason documented
            # Validate recovery plan exists
            pass
```

### 8. Error Recovery Mechanisms

```python
class ErrorRecoveryManager:
    """
    Automated error recovery for project-identity issues
    """
    
    def recover_from_corruption(self, corrupted_file_path):
        """Recover from corrupted project-identity file"""
        # Attempt backup restoration
        backup_restored = self.restore_from_backup()
        if backup_restored:
            return RecoveryResult.BACKUP_RESTORED
        
        # Attempt reconstruction from session history
        reconstructed = self.reconstruct_from_history()
        if reconstructed:
            return RecoveryResult.RECONSTRUCTED
        
        # Create new project-identity with user input
        return self.create_new_with_user_input()
    
    def recover_from_missing_file(self):
        """Recover from missing project-identity file"""
        # Check for project indicators
        project_indicators = self.detect_project_indicators()
        
        if project_indicators:
            # Create project-identity from detected indicators
            return self.create_from_indicators(project_indicators)
        else:
            # Trigger new project workflow
            return self.trigger_new_project_workflow()
```

## Integration with .god Workflows

### 9. Workflow Integration Points

```yaml
Integration Mapping:
  Planning Workflows:
    - Pre-planning: project-identity validation
    - During planning: progress updates
    - Post-planning: completion validation
    
  Development Workflows:
    - Pre-development: environment validation
    - During development: milestone updates
    - Post-development: deliverable validation
    
  Integration Workflows:
    - Pre-integration: readiness validation
    - During integration: status monitoring
    - Post-integration: success validation
```

### 10. Performance Monitoring

```python
class HookPerformanceMonitor:
    """
    Monitor hook performance and system health
    """
    
    def monitor_hook_execution_time(self, hook_name, execution_time):
        """Monitor hook performance"""
        if execution_time > self.get_threshold(hook_name):
            self.log_performance_warning(hook_name, execution_time)
            self.optimize_hook_execution(hook_name)
    
    def monitor_validation_failure_rate(self):
        """Monitor validation failure patterns"""
        failure_rate = self.calculate_failure_rate()
        if failure_rate > 0.05:  # 5% threshold
            self.trigger_system_health_check()
            self.notify_maintenance_required()
    
    def monitor_state_consistency(self):
        """Monitor project-identity consistency"""
        consistency_score = self.calculate_consistency_score()
        if consistency_score < 0.95:  # 95% threshold
            self.trigger_consistency_repair()
```

---

**Implementation Priority**: CRITICAL - Immediate implementation required  
**Testing Requirements**: Comprehensive testing with all workflow scenarios  
**Rollout Strategy**: Gradual deployment with extensive monitoring  
**Maintenance**: Continuous monitoring and optimization