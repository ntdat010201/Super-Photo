# Token Optimization Guidelines for .god System

> **🎯 Smart Token Management & SubAgent Usage Optimization**  
> Hướng dẫn tối ưu hóa token và sử dụng SubAgent hiệu quả trong hệ thống .god

## Core Principles

**Mission**: Minimize token waste while maximizing task efficiency  
**Philosophy**: Context-aware delegation with intelligent timing detection  
**Approach**: Strategic SubAgent usage with mandatory validation checkpoints

## 🔴 CRITICAL TOKEN OPTIMIZATION RULES

### 1. SubAgent Usage Decision Matrix

**✅ USE SubAgents WHEN**:
- **Parallel Tasks**: Multiple subtasks can run simultaneously
- **Complex Instructions**: Main agent has too many rules/tools/MCPs for different purposes
- **Context Reset Signals**: User frequently uses "/clear" or starts new instances
- **Specialized Expertise**: Task requires domain-specific knowledge
- **Large Codebase Analysis**: Need to analyze multiple files/modules separately

**❌ AVOID SubAgents WHEN**:
- **Simple Tasks**: Single-step operations or basic code changes
- **Sequential Dependencies**: Tasks must be done in strict order
- **Shared Context Critical**: All information needed in one place
- **Token Budget Limited**: Already approaching usage limits
- **Main Agent Performing Well**: No clear performance issues

### 2. Context Sharing Optimization

**Smart Context Transfer**:
```markdown
🎯 OPTIMAL: Structured summaries with key data points
⚠️ AVOID: Full context dumps or overly abstract reports
❌ NEVER: Redundant information sharing between agents
```

**Context Sharing Rules**:
- **Minimal Viable Context**: Share only essential information
- **Structured Reports**: Use consistent format for agent communication
- **Key Data Points**: Focus on actionable information
- **Avoid Redundancy**: Don't repeat information already available

### 3. Timing Detection System

**Auto-Detection Triggers**:
```yaml
High Priority SubAgent Usage:
  - User mentions: "parallel", "simultaneously", "multiple tasks"
  - Context size > 8000 tokens
  - Multiple file analysis required
  - Complex multi-step workflows

Medium Priority:
  - Specialized domain keywords detected
  - User requests "/clear" equivalent
  - Performance issues with main agent

Low Priority (Prefer Main Agent):
  - Simple code fixes
  - Single file operations
  - Direct implementation tasks
```

## Token Usage Monitoring

### 4. Mandatory Token Tracking

**Pre-Task Analysis**:
```markdown
☐ Estimate token cost for main agent approach
☐ Estimate token cost for SubAgent approach
☐ Calculate efficiency ratio (task_completion_speed / token_cost)
☐ Choose optimal approach based on ratio
```

**During Execution**:
```markdown
☐ Monitor context sharing overhead
☐ Track redundant information transfer
☐ Validate SubAgent reports for completeness
☐ Prevent main agent from re-doing SubAgent work
```

### 5. Context Optimization Patterns

**Efficient Context Patterns**:

1. **Layered Context Sharing**:
   ```
   Level 1: Essential task data only
   Level 2: Technical implementation details
   Level 3: Full context (only when necessary)
   ```

2. **Progressive Context Loading**:
   ```
   Initial: Minimal context for task understanding
   On-Demand: Additional context when specifically needed
   Final: Complete context for validation only
   ```

3. **Smart Context Caching**:
   ```
   Cache: Frequently used project patterns
   Reuse: Common code structures and conventions
   Avoid: Re-analyzing same files multiple times
   ```

## Integration with .god Workflows

### 6. Workflow-Specific Optimization

**Planning Phase** (`.ai-system/workflows/planning/`):
- **Use SubAgents**: For parallel requirement analysis
- **Avoid SubAgents**: For sequential planning steps

**Development Phase** (`.ai-system/workflows/development/`):
- **Use SubAgents**: For multi-file refactoring, parallel testing
- **Avoid SubAgents**: For single feature implementation

**Integration Phase** (`.ai-system/workflows/integration/`):
- **Use SubAgents**: For parallel deployment to multiple environments
- **Avoid SubAgents**: For sequential CI/CD steps

### 7. Agent Selection Optimization

**Enhanced Selection Algorithm**:
```python
def optimize_agent_selection(task, context_size, complexity):
    token_efficiency_score = calculate_token_efficiency(task)
    parallel_potential = detect_parallel_opportunities(task)
    context_overhead = estimate_context_sharing_cost(task)
    
    if (parallel_potential > 0.7 and token_efficiency_score > 0.6):
        return "use_subagents"
    elif (context_size > 8000 and complexity > 0.8):
        return "use_subagents_with_context_optimization"
    else:
        return "use_main_agent"
```

## Performance Metrics

### 8. Success Indicators

**Token Efficiency Metrics**:
- **Token/Task Ratio**: < 2000 tokens per completed task
- **Context Reuse Rate**: > 60% of context should be reusable
- **SubAgent Overhead**: < 30% additional tokens vs main agent
- **Task Completion Speed**: SubAgent approach should be 20%+ faster

**Quality Metrics**:
- **Information Loss Rate**: < 5% critical information lost in context transfer
- **Redundant Work Rate**: < 10% of SubAgent work redone by main agent
- **Context Accuracy**: > 95% of shared context should be relevant

## Implementation Guidelines

### 9. Mandatory Checkpoints

**Before SubAgent Creation**:
```markdown
✅ Validate parallel task potential
✅ Estimate token cost comparison
✅ Confirm specialized expertise needed
✅ Check context sharing requirements
```

**During SubAgent Execution**:
```markdown
✅ Monitor context sharing efficiency
✅ Validate report completeness
✅ Prevent redundant work
✅ Track token usage vs estimates
```

**After SubAgent Completion**:
```markdown
✅ Validate task completion quality
✅ Measure actual vs estimated token usage
✅ Update optimization patterns
✅ Document lessons learned
```

### 10. Emergency Protocols

**Token Budget Exceeded**:
1. Immediately switch to main agent only
2. Implement aggressive context pruning
3. Focus on essential tasks only
4. Defer non-critical optimizations

**SubAgent Performance Issues**:
1. Fallback to main agent approach
2. Analyze context sharing problems
3. Adjust SubAgent specialization
4. Update selection criteria

## Architecture Considerations

### 11. Code Architecture Impact

**AI-Friendly Architecture Principles**:
- **Modular Design**: Clear separation of concerns for easier AI analysis
- **Consistent Patterns**: Standardized code structures across project
- **Clear Dependencies**: Explicit relationships between components
- **Documentation**: Comprehensive inline and external documentation

**File Organization for AI Efficiency**:
```
project/
├── core/           # Essential business logic (main agent focus)
├── features/       # Individual features (SubAgent candidates)
├── shared/         # Common utilities (context sharing optimization)
├── tests/          # Testing (parallel SubAgent execution)
└── docs/           # Documentation (context reference)
```

## Continuous Improvement

### 12. Learning and Adaptation

**Performance Tracking**:
- Log all SubAgent vs main agent decisions
- Track token usage patterns
- Monitor task completion efficiency
- Analyze context sharing effectiveness

**Pattern Recognition**:
- Identify optimal SubAgent usage scenarios
- Recognize token waste patterns
- Detect context sharing inefficiencies
- Update selection algorithms based on data

**System Evolution**:
- Regular review of optimization guidelines
- Update based on new AI capabilities
- Adapt to changing project requirements
- Incorporate user feedback and preferences

---

**Last Updated**: 2024-01-16  
**Version**: 1.0  
**Integration**: Core .god system workflows  
**Maintenance**: Monthly review and updates based on usage data