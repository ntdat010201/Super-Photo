# 🚀 Hướng dẫn cấu hình Google Gemini API cho SuperPhoto

## 📋 Tổng quan
SuperPhoto đã được tích hợp với **Google Gemini API miễn phí** để cung cấp các tính năng AI mạnh mẽ:
- ✨ AI Enhancement (Tăng cường ảnh bằng AI)
- 🎨 Style Transfer (Chuyển đổi phong cách)
- 🖼️ Smart Suggestions (Gợi ý thông minh)
- 🎭 Face Swap (Hoán đổi khuôn mặt)
- 🌈 AI Colorize (Tô màu bằng AI)
- 🗑️ Object Removal (Xóa đối tượng)

## 🔑 Bước 1: Tạo Google Gemini API Key (MIỄN PHÍ)

### 1. Truy cập Google AI Studio
- Mở trình duyệt và truy cập: https://aistudio.google.com/
- Đăng nhập bằng tài khoản Google của bạn

### 2. Tạo API Key
- Nhấp vào **"Get API Key"** hoặc **"Create API Key"**
- Chọn **"Create API key in new project"** (hoặc chọn project có sẵn)
- Copy API key được tạo (dạng: `AIzaSy...`)

### 3. Lưu ý quan trọng
- ⚠️ **KHÔNG chia sẻ API key với ai khác**
- 💰 **HOÀN TOÀN MIỄN PHÍ** - không cần thẻ tín dụng
- 📊 **Giới hạn**: 15 requests/phút với Gemini 2.5 Flash
- 🔄 **Đủ để test** tất cả tính năng của SuperPhoto

## 🔧 Bước 2: Cấu hình API Key trong SuperPhoto

### Cách 1: Thông qua ứng dụng (Khuyến nghị)
1. Mở ứng dụng SuperPhoto
2. Khi sử dụng tính năng AI lần đầu, app sẽ hiển thị dialog cấu hình
3. Dán API key vào ô **"Gemini API Key"**
4. Nhấn **"Lưu"**

### Cách 2: Chỉnh sửa trực tiếp code
1. Mở file: `app/src/main/java/com/example/superphoto/config/APIConfig.kt`
2. Tìm dòng:
   ```kotlin
   const val GEMINI_API_KEY = "YOUR_GEMINI_API_KEY_HERE"
   ```
3. Thay thế `YOUR_GEMINI_API_KEY_HERE` bằng API key của bạn:
   ```kotlin
   const val GEMINI_API_KEY = "AIzaSy_your_actual_api_key_here"
   ```
4. Build lại ứng dụng: `./gradlew assembleDebug`

## 📱 Bước 3: Test các tính năng

### 1. AI Enhancement
- Chọn một ảnh từ thư viện
- Nhấn **"AI Transform"** → **"AI Enhance"**
- Chờ AI phân tích và cải thiện ảnh

### 2. Style Transfer
- Chọn ảnh và nhấn **"Style Transfer"**
- Chọn phong cách mong muốn
- AI sẽ áp dụng phong cách mới cho ảnh

### 3. Smart Suggestions
- Mở ảnh bất kỳ
- AI sẽ tự động đưa ra gợi ý cải thiện
- Nhấn vào gợi ý để áp dụng

## 🔍 Kiểm tra trạng thái API

### Trong ứng dụng:
- **Màn hình chính**: Hiển thị trạng thái API ở góc trên
- **Khi sử dụng AI**: App sẽ kiểm tra rate limit tự động
- **Thông báo lỗi**: Nếu API key không hợp lệ hoặc hết quota

### Thông tin Rate Limit:
- **Requests còn lại**: Hiển thị số lượng requests có thể thực hiện
- **Thời gian reset**: Khi nào quota sẽ được làm mới
- **Auto retry**: App tự động thử lại khi gặp lỗi tạm thời

## 🛠️ Xử lý sự cố

### API Key không hoạt động:
1. Kiểm tra API key có đúng format không (bắt đầu bằng `AIzaSy`)
2. Đảm bảo đã enable Gemini API trong Google Cloud Console
3. Kiểm tra quota còn lại tại https://aistudio.google.com/

### Lỗi Rate Limit:
- **Tự động xử lý**: App sẽ tự động chờ và thử lại
- **Thông báo**: Hiển thị thời gian cần chờ
- **Giải pháp**: Chờ 1 phút hoặc sử dụng tính năng khác

### Lỗi kết nối:
1. Kiểm tra kết nối internet
2. Thử lại sau vài giây
3. Restart ứng dụng nếu cần

## 📊 Thông tin kỹ thuật

### Models được sử dụng:
- **Gemini 2.5 Flash**: Cho các tác vụ nhanh (15 RPM)
- **Gemini 2.5 Pro**: Cho các tác vụ phức tạp (2 RPM)

### Cấu hình tối ưu:
- **Timeout**: 45 giây cho request, 90 giây cho đọc dữ liệu
- **Retry**: Tối đa 3 lần thử lại với delay 2 giây
- **Rate limiting**: Tự động quản lý để tránh vượt quota

### Bảo mật:
- API key được mã hóa trong ứng dụng
- Không gửi dữ liệu nhạy cảm lên server
- Xử lý ảnh local trước khi gửi API

## 🎉 Hoàn thành!

Sau khi cấu hình xong, bạn có thể:
- ✅ Sử dụng tất cả tính năng AI của SuperPhoto
- ✅ Tận hưởng 15 requests miễn phí mỗi phút
- ✅ Trải nghiệm AI photography mạnh mẽ

**Chúc bạn có những trải nghiệm tuyệt vời với SuperPhoto! 📸✨**