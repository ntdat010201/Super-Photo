# Project Memory System - Hướng dẫn sử dụng

## Tổng quan

Project Memory System là một hệ thống bộ nhớ tạm thời thông minh được thiết kế để giúp AI agents hiểu và làm việc hiệu quả với codebase. Hệ thống này tự động quét, phân tích và lưu trữ thông tin về dự án trong một định dạng tối ưu hóa.

## Kiến trúc hệ thống

### Thành phần chính

1. **Project Memory Scanner** (`project-memory-scanner.js`)
   - Quét toàn bộ codebase và thu thập metadata
   - Phân tích cấu trúc dự án, dependencies, và patterns
   - Tối ưu hóa dữ liệu để giữ dưới 100k tokens

2. **Project Memory API** (`project-memory-api.js`)
   - Cung cấp interface để truy vấn thông tin dự án
   - Phân tích task và gợi ý files/directories liên quan
   - Đánh giá độ phức tạp và workflow phù hợp

3. **Memory Optimizer** (`memory-optimizer.js`)
   - Tối ưu hóa kích thước bộ nhớ
   - Loại bỏ thông tin trùng lặp và không cần thiết
   - Cân bằng giữa độ chi tiết và hiệu quả

### Cấu trúc dữ liệu

```json
{
  "metadata": {
    "version": "1.0.0",
    "lastUpdated": "timestamp",
    "totalTokens": 12000,
    "scanDepth": 3,
    "excludedPaths": [...],
    "performance": {
      "tokenUsage": "12.9%",
      "compressionRatio": 0.85
    }
  },
  "projectStructure": {
    "directories": [...],
    "features": [...],
    "taskPatterns": [...]
  },
  "fileIndex": {
    "byPurpose": {
      "configuration": [...],
      "source": [...],
      "documentation": [...]
    },
    "byTechnology": {...},
    "byComplexity": {...}
  }
}
```

## Cách sử dụng

### 1. Quét dự án (Full Scan)

```bash
node .ai-system/scripts/project-memory-scanner.js --full
```

**Khi nào sử dụng:**
- Lần đầu thiết lập project memory
- Sau khi có thay đổi lớn về cấu trúc dự án
- Khi cần refresh toàn bộ bộ nhớ

### 2. Quét tăng cường (Incremental Scan)

```bash
node .ai-system/scripts/project-memory-scanner.js --incremental
```

**Khi nào sử dụng:**
- Cập nhật hàng ngày
- Sau khi thêm/sửa files
- Để duy trì bộ nhớ luôn cập nhật

### 3. Truy vấn thông tin dự án

```bash
# Tìm files liên quan đến task
node .ai-system/scripts/project-memory-api.js "android development"

# Xem thống kê bộ nhớ
node .ai-system/scripts/project-memory-api.js stats
```

### 4. Tối ưu hóa bộ nhớ

```bash
node .ai-system/scripts/memory-optimizer.js
```

## API Reference

### Project Memory API

#### Truy vấn task
```javascript
const result = await analyzeTaskAndFindFiles(taskDescription);
```

**Response format:**
```json
{
  "suggestedFiles": [
    {
      "path": "path/to/file",
      "relevance": 0.85,
      "reason": "Contains Android-specific code"
    }
  ],
  "relevantDirectories": [
    {
      "path": ".ai-system/rules/platforms",
      "purpose": "Platform-specific rules",
      "confidence": 0.9
    }
  ],
  "taskPattern": "mobile_development",
  "complexity": "medium",
  "suggestedWorkflow": "android_development",
  "confidence": 0.75
}
```

#### Lấy thống kê
```javascript
const stats = await getMemoryStats();
```

## Tích hợp với .god System

### Workflow Integration

Project Memory tự động tích hợp với .ai-system workflows:

1. **Kiro Spec-Driven Workflow**: Phân tích specs và gợi ý files liên quan
2. **Development Workflows**: Gợi ý workflow phù hợp dựa trên task
3. **Platform-Specific Rules**: Áp dụng rules phù hợp với platform

### Agent Selection Support

Hỗ trợ agent selection thông qua:
- Phân tích technology stack
- Đánh giá độ phức tạp task
- Gợi ý workflow và agent phù hợp

## Best Practices

### 1. Maintenance Schedule

- **Hàng ngày**: Chạy incremental scan
- **Hàng tuần**: Kiểm tra memory stats và optimize nếu cần
- **Sau major changes**: Chạy full scan

### 2. Performance Optimization

- Giữ token usage dưới 15% (hiện tại: 12.9%)
- Loại trừ các thư mục không cần thiết trong `.ai-systemignore`
- Định kỳ chạy memory optimizer

### 3. Quality Assurance

- Kiểm tra confidence scores trong API responses
- Validate suggested files có thực sự liên quan
- Monitor performance metrics

## Troubleshooting

### Lỗi thường gặp

1. **TypeError: Cannot read property 'toLowerCase'**
   - Nguyên nhân: Dữ liệu null/undefined trong fileIndex
   - Giải pháp: Chạy full scan để rebuild index

2. **Memory usage quá cao**
   - Nguyên nhân: Quá nhiều files được index
   - Giải pháp: Cập nhật excludedPaths và chạy optimizer

3. **Confidence scores thấp**
   - Nguyên nhân: Thiếu training data hoặc patterns
   - Giải pháp: Cập nhật taskPatterns trong scanner

### Debug Commands

```bash
# Kiểm tra cấu trúc memory
node .ai-system/scripts/project-memory-api.js stats

# Test specific query
node .ai-system/scripts/project-memory-api.js "your query here"

# Rebuild memory từ đầu
node .ai-system/scripts/project-memory-scanner.js --full --verbose
```

## Configuration

### Scanner Configuration

Cấu hình trong `project-memory-scanner.js`:

```javascript
const CONFIG = {
  MAX_TOKENS: 100000,
  SCAN_DEPTH: 3,
  EXCLUDED_PATTERNS: [
    'node_modules',
    '.git',
    'dist',
    'build'
  ],
  COMPRESSION_RATIO: 0.8
};
```

### API Configuration

Cấu hình trong `project-memory-api.js`:

```javascript
const ANALYSIS_CONFIG = {
  MIN_CONFIDENCE: 0.1,
  MAX_SUGGESTIONS: 10,
  RELEVANCE_THRESHOLD: 0.3
};
```

## Roadmap

### Version 1.1 (Planned)
- [ ] Machine learning-based relevance scoring
- [ ] Integration với external knowledge bases
- [ ] Real-time memory updates
- [ ] Advanced caching mechanisms

### Version 1.2 (Future)
- [ ] Multi-project memory management
- [ ] Collaborative memory sharing
- [ ] Advanced analytics và insights
- [ ] Performance benchmarking tools

## Support

Nếu gặp vấn đề hoặc cần hỗ trợ:
1. Kiểm tra troubleshooting section
2. Chạy debug commands
3. Xem logs trong terminal output
4. Tạo issue với detailed error information

---

*Project Memory System v1.0.0 - Intelligent codebase understanding for AI agents*