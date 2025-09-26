# 🧪 Demo Mode Test Guide

## ✅ Tích hợp hoàn thành

### 🔧 Các thành phần đã được tích hợp:

1. **APIConfig.kt** - Cấu hình demo mode
   - `ENABLE_DEMO_MODE = true`
   - `AI_GENERATION_API_KEY = "DEMO_MODE"`
   - Helper methods: `isDemoMode()`, `getAIGenerationApiKey()`, `getAIGenerationBaseUrl()`

2. **AIGenerationFallbackRepository.kt** - Repository fallback sử dụng Gemini
   - Mô phỏng tất cả tính năng AI generation
   - Sử dụng Gemini để tạo concept/description
   - Trả về demo URLs và status "completed"

3. **AIGenerationManager.kt** - Manager tự động chọn repository
   - Tự động chọn giữa real API và fallback dựa trên demo mode
   - Cung cấp interface thống nhất cho tất cả Fragment

4. **NetworkModule.kt** - Dependency injection
   - Tự động inject đúng repository dựa trên demo mode
   - Hỗ trợ cả real API và fallback

5. **Fragments đã được cập nhật:**
   - `AIImagesFragment.kt`
   - `ImageToVideoFragment.kt` 
   - `TextToVideoFragment.kt`
   - `LipSyncFragment.kt`
   - `GenerationStatusManager.kt`

### 🎯 Cách test Demo Mode:

#### Test 1: AI Image Generation
1. Mở app SuperPhoto
2. Vào tab "AI Images"
3. Upload một ảnh
4. Nhập prompt: "Transform into anime style"
5. Nhấn Generate
6. **Kết quả mong đợi:** 
   - Hiển thị loading với Gemini concept
   - Sau 3 giây hiển thị demo image URL
   - Status: "completed"

#### Test 2: Text to Video
1. Vào tab "Text to Video"
2. Nhập prompt: "A cat playing in the garden"
3. Chọn duration và style
4. Nhấn Generate
5. **Kết quả mong đợi:**
   - Hiển thị Gemini-generated concept
   - Demo video URL được trả về
   - Thumbnail URL có sẵn

#### Test 3: Image to Video
1. Vào tab "Image to Video"
2. Upload 2-3 ảnh
3. Nhập prompt: "Create smooth transition"
4. Nhấn Generate
5. **Kết quả mong đợi:**
   - Phân tích ảnh bằng Gemini
   - Tạo concept cho video
   - Demo video URL

#### Test 4: Lip Sync
1. Vào tab "Lip Sync"
2. Upload video và audio file
3. Nhấn Generate
4. **Kết quả mong đợi:**
   - Gemini phân tích content
   - Demo lip-sync video URL

### 🔍 Debug Information:

Để kiểm tra demo mode đang hoạt động:
```kotlin
// Trong bất kỳ Fragment nào
Log.d("DemoMode", "Is Demo Mode: ${APIConfig.isDemoMode()}")
Log.d("DemoMode", "API Key: ${APIConfig.getAIGenerationApiKey()}")
Log.d("DemoMode", "Base URL: ${APIConfig.getAIGenerationBaseUrl()}")
```

### 🚀 Chuyển sang Production Mode:

Khi có API key thật:
1. Cập nhật `APIConfig.kt`:
   ```kotlin
   const val ENABLE_DEMO_MODE = false
   const val AI_GENERATION_API_KEY = "your_real_api_key"
   const val AI_GENERATION_BASE_URL = "https://api.runwayml.com" // hoặc API thật
   ```

2. App sẽ tự động chuyển sang sử dụng real API

### 📊 Demo URLs được tạo:

- **Images**: `https://demo.superphoto.ai/images/{taskId}.jpg`
- **Videos**: `https://demo.superphoto.ai/videos/{taskId}.mp4`
- **Thumbnails**: `https://demo.superphoto.ai/thumbnails/{taskId}.jpg`
- **Audio**: `https://demo.superphoto.ai/audio/{taskId}.mp3`

### ⚠️ Lưu ý:

1. **Demo URLs không thật** - chỉ để test UI flow
2. **Gemini API key cần được cấu hình** trong `APIConfig.GEMINI_API_KEY`
3. **Simulation delay** = 3 giây để mô phỏng processing time
4. **Tất cả status** trả về "completed" sau delay

### 🔧 Troubleshooting:

**Nếu gặp lỗi:**
1. Kiểm tra Gemini API key đã được cấu hình
2. Kiểm tra internet connection
3. Xem logs để debug
4. Đảm bảo `ENABLE_DEMO_MODE = true`

**Build issues:**
- Sử dụng `./gradlew assembleDebug -x lint` để skip lint
- Lint issues sẽ được sửa trong update tiếp theo