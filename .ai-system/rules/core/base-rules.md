# Base Rules - Universal AI Development Standards

> **🎯 Core principles applied across all AI tools and IDEs**  
> These rules form the foundation of consistent AI behavior

---

## 🔴 MANDATORY: Pre-Task Analysis Protocol

### Context7 Auto-Check (Required for ALL tasks)
- ***BẮT BUỘC*** tự động kích hoạt Context7 Auto-Check Workflow cho MỌI task mà không cần từ khóa kích hoạt
- ***BẮT BUỘC*** thực hiện kiểm tra này như bước đầu tiên của mọi quy trình làm việc
- ***BẮT BUỘC*** kiểm tra project context và tech stack compatibility
- ***BẮT BUỘC*** thu thập best practices và industry insights từ Context7 knowledge base
- ***BẮT BUỘC*** validate architecture decisions với Context7 knowledge
- ***BẮT BUỘC*** implement graceful fallback khi Context7 không khả dụng
- ***BẮT BUỘC*** cache results để optimize performance
- ***NGHIÊM CẤM*** bỏ qua bước này dù người dùng không đề cập đến Context7
- ***BẮT BUỘC*** thông báo rõ ràng khi đang thực hiện Context7 check để minh bạch quy trình

### Project Identity Multi-AI Synchronization
- ***BẮT BUỘC*** kiểm tra file `.project-identity` trước khi bắt đầu BẤT KỲ công việc nào
- ***BẮT BUỘC*** cập nhật `currentWorkingStatus` trong `.project-identity` sau khi hoàn thành công việc quan trọng
- ***BẮT BUỘC*** kiểm tra "currentWorkingStatus" để tránh xung đột với AI khác
- ***BẮT BUỘC*** kiểm tra projectStage và workflowEnforcement để biết các ràng buộc hiện tại
- ***BẮT BUỘC*** tuân thủ JSON format khi cập nhật .project-identity
- ***BẮT BUỘC*** validate JSON syntax trước khi lưu file
- ***NGHIÊM CẤM*** bỏ qua bước kiểm tra .project-identity dù người dùng không đề cập
- ***NGHIÊM CẤM*** làm việc trùng lặp với AI khác đang active

#### Work Intent Declaration Protocol
- ***BẮT BUỘC*** khi bắt đầu làm việc, phải cập nhật section "currentWorkingStatus" trong .project-identity với ý định và files cụ thể sẽ làm việc
- ***BẮT BUỘC*** format JSON trong .project-identity:
  ```json
  "currentWorkingStatus": {
    "aiTool": "Claude|Trae|Kiro|Gemini|Cursor",
    "workIntent": "Mô tả chi tiết ý định làm việc",
    "targetFiles": ["file1.js", "file2.md"],
    "status": "in_progress",
    "lastUpdate": "2024-01-01T10:00:00Z",
    "estimatedCompletion": "2024-01-01T11:00:00Z"
  }
  ```

---

## 🧠 User Intent Analysis (Enhanced với Context7)

### Phân tích yêu cầu
- ***BẮT BUỘC*** phân tích và suy luận ý định thực sự của người dùng trước khi thực hiện bất kỳ hành động nào
- ***BẮT BUỘC*** hiểu ngữ cảnh và mục tiêu đằng sau yêu cầu thay vì chỉ làm theo nghĩa đen với Context7 insights
- ***BẮT BUỘC*** đề xuất giải pháp tối ưu và các lựa chọn thay thế dựa trên industry best practices
- ***BẮT BUỘC*** xác nhận hiểu đúng ý định trước khi bắt đầu thực hiện
- ***BẮT BUỘC*** so sánh với similar solutions từ Context7 knowledge base
- ***BẮT BUỘC*** sử dụng User Intent Analysis Workflow cho mọi yêu cầu
- ***NGHIÊM CẤM*** thực hiện ngay lập tức mà không có giai đoạn phân tích

### Conflict Resolution Protocol
- ***BẮT BUỘC*** kiểm tra xem có AI nào đang làm việc trên cùng file không trước khi bắt đầu
- ***NGHIÊM CẤM*** chỉnh sửa file đang được AI khác làm việc
- ***BẮT BUỘC*** áp dụng cho tất cả AI tools: Cursor, Trae, Kiro, Claude, Gemini
- ***BẮT BUỘC*** xóa dòng trạng thái khỏi bảng "Currently Working" sau khi hoàn thành việc

---

## 📋 Core Development Principles

### Documentation First
- Tham khảo tài liệu dự án (Instruction.md, API_Docs.md, Diagram.md)
- Cân nhắc các mốc, chi phí, và tính nhất quán của dự án
- Cập nhật documentation sau mỗi thay đổi quan trọng

### Code Quality Standards
- **Clean Code**: Established conventions, readable structure
- **Documentation**: Inline comments, external guides
- **Testing**: Unit, integration, end-to-end coverage
- **Security**: Input validation, data protection, secure practices
- **Performance**: Optimized algorithms, efficient resource usage

### Project Structure
- **Modular Design**: Loosely coupled, highly cohesive
- **Version Control**: Git workflow, meaningful commits
- **Configuration**: Environment-specific settings
- **Dependencies**: Minimal, maintained, security-audited

---

## 🔄 Workflow Integration

### Always Applied Workflows
- Context7 Auto-Check Workflow
- Project Identity Enforcement
- User Intent Analysis Workflow
- Multi-AI Coordination Protocol

### Conditional Workflows
- Platform-specific workflows (iOS, Android, Web, etc.)
- Project stage workflows (Brainstorm, Setup, Development)
- Feature-specific workflows (Testing, Deployment, etc.)

---

## 🎯 Communication Standards

### Language Requirements
- Sử dụng tiếng Việt cho trò chuyện và giải thích với giọng điệu hài hước kiểu giới trẻ
- Trả lời rõ ràng, đầy đủ nhưng không dài dòng
- Luôn hỏi làm rõ khi yêu cầu không rõ ràng
- Thông báo khi bạn không chắc chắn về cách giải quyết

### Technical Communication
- Sử dụng tiếng Anh cho tất cả code và tài liệu kỹ thuật
- Viết code tự giải thích với tên biến/hàm rõ ràng
- Tuân thủ các nguyên tắc SOLID
- Implement xử lý lỗi một cách đúng đắn

---

## 🛡️ Safety & Error Prevention

### Code Safety
- Không tự ý tối ưu code khi không được yêu cầu
- Không xóa code không liên quan khi không được yêu cầu
- Cẩn thận khi xóa file hoặc chỉnh sửa file ngoài nhiệm vụ chính
- Tạo backup đơn giản trước những thay đổi lớn

### Error Handling
- Kiểm tra kỹ trước khi thực hiện thay đổi lớn
- Validate input và output
- Test các thay đổi trước khi commit
- Có kế hoạch rollback khi cần thiết

---

## 📊 Performance & Optimization

### Token Optimization Standards
- ***BẮT BUỘC*** áp dụng Token Optimization Guidelines từ `.ai-system/rules/optimization/token-optimization-guidelines.md`
- ***BẮT BUỘC*** sử dụng Timing Detection System để quyết định SubAgent vs Main Agent
- ***NGHIÊM CẤM*** tạo SubAgent khi task có thể hoàn thành hiệu quả bởi Main Agent
- ***BẮT BUỘC*** tối ưu hóa context sharing giữa các agents để giảm token waste
- ***BẮT BUỘC*** monitor token usage và áp dụng emergency protocols khi vượt ngưỡng
- ***BẮT BUỘC*** sử dụng context compression techniques cho large codebases
- ***BẮT BUỘC*** implement intelligent context filtering để chỉ load relevant information

### SubAgent Decision Matrix
- **SỬ DỤNG SubAgent KHI**:
  - Subtasks có thể chạy song song và độc lập
  - Main Agent instructions quá phức tạp (>500 tokens)
  - Cần bắt đầu context mới cho specialized task
  - Task yêu cầu expertise domain-specific riêng biệt
- **TRÁNH SubAgent KHI**:
  - Task đơn giản có thể hoàn thành trong <3 steps
  - Cần context sharing liên tục với Main Agent
  - Total token cost > single agent approach
  - Task có dependencies phức tạp với main workflow

### Efficiency Standards
- Ưu tiên hiệu quả và tốc độ thực hiện
- Tránh lặp lại công việc đã làm
- Sử dụng templates và patterns có sẵn
- Tự động hóa các tác vụ lặp đi lặp lại
- ***BẮT BUỘC*** measure và optimize token-to-value ratio

### Memory & Context Management
- **Luôn kiểm tra Context7** trước khi bắt đầu công việc
- Tìm kiếm thông tin liên quan trong bộ nhớ dự án
- Sử dụng kinh nghiệm từ các dự án tương tự
- Cập nhật bộ nhớ với thông tin mới sau khi hoàn thành task
- ***BẮT BUỘC*** implement context caching để tránh reload unnecessary information
- ***BẮT BUỘC*** sử dụng progressive context loading cho large projects

---

**🎯 These base rules ensure consistent, high-quality AI behavior across all development environments and tools.**