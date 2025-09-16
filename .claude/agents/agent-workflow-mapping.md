# Agent-Workflow Mapping

> **🔗 MANDATORY RULES SYNCHRONIZATION**  
> **BẮT BUỘC** sử dụng mapping từ `.cursor/rules/agent-workflow-mapping.mdc` làm nguồn chính thức.

## 📍 Primary Source

**[Agent-Workflow Mapping Rule](./../.cursor/rules/agent-workflow-mapping.mdc)** - Compact mapping between Claude Agents and Workflows

## 🎯 Quick Reference

### Agent Categories

- **Engineering**: mobile-app-builder, rapid-prototyper, frontend-developer, backend-architect, ai-engineer, devops-automator
- **Product**: sprint-prioritizer, feedback-synthesizer, trend-researcher  
- **Marketing**: content-creator, app-store-optimizer, growth-hacker
- **Design & Testing**: ui-designer, api-tester, performance-benchmarker

### Selection Process

1. Analyze keywords in user request
2. Calculate confidence scores for each agent
3. Select highest scoring agent (threshold: 0.7)
4. Load corresponding workflows from `.cursor/rules/`
5. Execute with agent-specific context

## ⚠️ CRITICAL ENFORCEMENT

- **BẮT BUỘC** tuân thủ 100% mapping trong `.cursor/rules/agent-workflow-mapping.mdc`
- **NGHIÊM CẤM** tạo mapping độc lập trong file này
- **BẮT BUỘC** sync với primary source khi có thay đổi

---

**Compact Design**: Minimizes context usage while maintaining full functionality.