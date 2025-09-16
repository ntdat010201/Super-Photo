# Agent Selector System Hierarchy

> **📋 Quick Reference Guide for Agent Selector System**

## System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Agent Selector Hierarchy                 │
├─────────────────────────────────────────────────────────────┤
│  1. .trae/rules/project_rules.md        [PRIMARY - HIGH]    │
│     ↳ Mức độ ưu tiên cao nhất, hoạt động mọi lúc           │
│                                                             │
│  2. .ai-system/agents/selector/agent-selector.md [MAIN SYSTEM] │
│     ↳ Hệ thống chính với đầy đủ tính năng                  │
│                                                             │
│  3. .trae/agents/agent-selector-system.md [FALLBACK]       │
│     ↳ Fallback router, tối ưu token usage                  │
└─────────────────────────────────────────────────────────────┘
```

## Role & Responsibility

### 1. Project Rules (Primary)
**File**: `.trae/rules/project_rules.md`
**Priority**: 🔴 HIGHEST
**Role**: Core system rules và mandatory checks
**Usage**: Hoạt động gần như mọi lúc
**Token Impact**: ⚠️ HIGH - Đã được tối ưu hóa

**Key Functions**:
- Project identity enforcement
- Stage-based workflow loading
- Multi-AI coordination protocol
- .god system integration

### 2. Main Agent Selector (Primary System)
**File**: `.ai-system/agents/selector/agent-selector.md`
**Priority**: 🟡 HIGH
**Role**: Comprehensive agent selection với full features
**Usage**: Standard và complex tasks
**Token Impact**: 🟡 MEDIUM - Full-featured system

**Key Functions**:
- Intelligent agent selection với multi-factor analysis
- Test Spec Driven Development Review Workflow validation
- Operation modes (YOLO/Standard/Auto)
- Comprehensive error handling

### 3. Fallback Router (Backup System)
**File**: `.trae/agents/agent-selector-system.md`
**Priority**: 🟢 MEDIUM
**Role**: Lightweight fallback router
**Usage**: Khi main system không khả dụng
**Token Impact**: 🟢 LOW - Đã được tối ưu hóa

**Key Functions**:
- Quick agent selection
- Basic error handling
- Minimal token usage
- Emergency routing

## Operation Flow

### Normal Operation
```
User Request → project_rules.md (validation) → .ai-system/agents/selector/agent-selector.md (selection) → Specialized Agent
```

### Fallback Operation
```
User Request → project_rules.md (validation) → .trae/agent-selector-system.md (fallback) → Specialized Agent
```

### Emergency Mode
```
User Request → .trae/agent-selector-system.md (direct routing) → Specialized Agent
```

## Optimization Results

### Before Optimization
- **project_rules.md**: 495 lines
- **agent-selector-system.md**: 443 lines
- **Total**: 938 lines

### After Optimization
- **project_rules.md**: ~200 lines (-60%)
- **agent-selector-system.md**: 74 lines (-83%)
- **Total**: ~274 lines (-71%)

### Token Savings
- **Estimated Token Reduction**: ~70%
- **Performance Impact**: Minimal
- **Functionality**: Preserved core features

## Usage Guidelines

### When to Use Each System

**Use project_rules.md when**:
- Every session (mandatory)
- Project identity validation needed
- Stage progression required

**Use .ai-system/agents/selector/agent-selector.md when**:
- Complex task analysis needed
- Full workflow validation required
- Standard/Auto operation modes

**Use .trae/agent-selector-system.md when**:
- Quick routing needed
- Main system unavailable
- YOLO mode operation
- Token usage is critical

## Maintenance Notes

- **project_rules.md**: Core rules, update carefully
- **.ai-system/agents/selector/agent-selector.md**: Full-featured, can be extended
- **.trae/agent-selector-system.md**: Keep lightweight, avoid bloat

---
**Last Updated**: January 2025  
**Optimization Status**: ✅ Completed  
**Token Efficiency**: 🟢 Optimized