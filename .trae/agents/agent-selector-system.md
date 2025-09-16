# Agent Selector System - Fallback Router

> **🎯 Lightweight Agent Selection & Task Routing**

## 🔴 MANDATORY RULES

**Rule #0**: Khai báo mô hình AI và version trước mọi task

**Critical System Checks**:
1. **Project Identity**: Kiểm tra `.project-identity` và `projectStage`
2. **Working Status**: Cập nhật `currentWorkingStatus` để tránh conflict
3. **Operation Mode**: Xác định YOLO/Standard/Auto mode
4. **Stage Validation**: Đảm bảo progression rules được tuân thủ

## 📍 System Migration Notice

**Primary Location**: [`.ai-system/agents/selector/agent-selector.md`](../../.ai-system/agents/selector/agent-selector.md)

**Quick Links**: Main Selector, Workflow Mapping, Engineering Agents

## 🤖 Available Agents

| Agent | Keywords | Confidence |
|-------|----------|------------|
| **iOS** | swift, swiftui, ios, xcode | 85% |
| **Android** | kotlin, android, jetpack, gradle | 82% |
| **APK Mod** | apk, reverse, firebase, safeads | 78% |
| **Frontend** | react, vue, angular, typescript | 88% |
| **Backend** | nodejs, laravel, api, database | 86% |
| **Cross-platform** | flutter, react-native, dart | 80% |
| **DevOps** | docker, kubernetes, cicd, aws | 83% |

## ⚡ Selection Algorithm

### Scoring Factors
1. **Keyword Match** (40%): Technology alignment
2. **File Type** (25%): Project structure
3. **Complexity** (20%): Task difficulty
4. **Performance** (15%): Success rate

### Decision Thresholds
- **High Confidence** (>75%): Direct assignment
- **Medium Confidence** (55-75%): Assignment with monitoring
- **Low Confidence** (<55%): User confirmation

### Operation Modes
- **YOLO Mode**: Quick keyword matching, 60% threshold, <2s response
- **Standard Mode**: Full validation, 75% threshold, <5s response
- **Auto Mode**: Dynamic selection based on task complexity

## 🔄 Workflow Integration

**Triggers for .god workflows**: Keywords "spec", "requirements", "PRD", "design", complex features, `.kiro/specs/` presence

**Hybrid Approach**: .god for planning → .trae for execution

**Stage-Based Routing**:
- **stage1_brainstorm**: → Kiro spec-driven workflow
- **stage2_setup**: → Project structure setup
- **stage3_development**: → Specialized development agents

## 🚨 Error Handling

**Blocking Conditions**: Missing AI declaration, currentWorkingStatus conflicts, stage progression violations, missing .project-identity

**Fallback Mechanisms**: User selection for low confidence, default to last successful agent, emergency YOLO mode activation

## 📊 Performance Targets

- **Selection Accuracy**: >90%
- **Response Time**: <2s (YOLO), <5s (Standard)
- **Token Usage**: Optimized for routing efficiency
- **Success Rate**: >85% average across agents

---
**Fallback router for efficient agent selection with minimal token usage**
