# 🎨 Hướng dẫn sử dụng AI Image Generation

## 🚀 Cách hoạt động

App SuperPhoto hiện đã tích hợp AI để tạo ảnh thông minh với 2 workflow chính:

### 1. 📸 **Image + Text Generation** (Ảnh + Mô tả)
**Khi bạn upload ảnh chân dung + nhập text mô tả:**

```
Ảnh chân dung của bạn + "đeo kính và đội mũ"
↓
Gemini AI phân tích ảnh → Tạo mô tả chi tiết về bạn
↓
Kết hợp với yêu cầu "đeo kính và đội mũ"
↓
Pollinations AI tạo ảnh mới: CÙNG NGƯỜI với kính và mũ
```

### 2. ✍️ **Text-Only Generation** (Chỉ mô tả)
**Khi bạn chỉ nhập text không upload ảnh:**

```
"Một người đàn ông đeo kính và đội mũ"
↓
Pollinations AI tạo ảnh trực tiếp từ mô tả
```

## 🎯 Để có kết quả tốt nhất

### ✅ **Khi upload ảnh chân dung:**
- **Ảnh rõ nét, ánh sáng tốt**
- **Khuôn mặt nhìn rõ, không bị che khuất**
- **Text mô tả cụ thể:** "đeo kính đen", "đội mũ lưỡi trai đỏ", "mặc áo sơ mi trắng"

### ✅ **Text prompts hiệu quả:**
```
❌ Không tốt: "thay đổi"
✅ Tốt: "đeo kính đen và đội mũ baseball"

❌ Không tốt: "khác"  
✅ Tốt: "mặc vest đen, đeo cà vạt đỏ"

❌ Không tốt: "đẹp hơn"
✅ Tốt: "tóc dài, đeo hoa tai, makeup tự nhiên"
```

## 🔧 **Workflow chi tiết:**

### Bước 1: Upload ảnh (tùy chọn)
- Nhấn vào khu vực upload
- Chọn ảnh chân dung từ thư viện
- Ảnh sẽ hiển thị trong preview

### Bước 2: Nhập mô tả
- Gõ text mô tả những gì bạn muốn thay đổi/thêm vào
- Ví dụ: "đeo kính râm, đội mũ snapback, mặc áo hoodie"

### Bước 3: Chọn style và tỷ lệ
- **Style:** Photo (thực tế), Anime, Illustration
- **Aspect Ratio:** 1:1, 16:9, 9:16, 3:4

### Bước 4: Generate
- Nhấn "Generate" 
- Đợi AI xử lý (15-30 giây)
- Ảnh kết quả sẽ hiển thị

## 🧠 **AI Logic:**

1. **Gemini AI** phân tích ảnh gốc:
   - Nhận diện đặc điểm khuôn mặt, tóc, da
   - Phân tích tư thế, góc chụp, ánh sáng
   - Tạo mô tả chi tiết để giữ nguyên identity

2. **Prompt Enhancement:**
   - Kết hợp mô tả chi tiết + yêu cầu người dùng
   - Thêm keywords đảm bảo chất lượng
   - Format: "Cùng người [mô tả chi tiết] [thay đổi yêu cầu]"

3. **Pollinations AI** tạo ảnh:
   - Sử dụng prompt đã enhance
   - Tạo ảnh với độ phân giải cao
   - Giữ nguyên identity + áp dụng thay đổi

## 📱 **Ví dụ thực tế:**

### Scenario 1: Thêm phụ kiện
```
Input: Ảnh selfie + "đeo kính đen và đội mũ lưỡi trai"
Output: Cùng khuôn mặt nhưng có kính đen + mũ lưỡi trai
```

### Scenario 2: Thay đổi trang phục
```
Input: Ảnh chân dung + "mặc vest đen, đeo cà vạt"
Output: Cùng người nhưng trong trang phục formal
```

### Scenario 3: Thay đổi kiểu tóc
```
Input: Ảnh chân dung + "tóc dài xoăn, đeo hoa tai"
Output: Cùng khuôn mặt với tóc dài xoăn + hoa tai
```

## ⚡ **Tips để có kết quả tốt:**

1. **Ảnh input chất lượng cao**
2. **Mô tả cụ thể, chi tiết**
3. **Sử dụng style "Photo" cho ảnh thực tế**
4. **Thử nhiều prompts khác nhau**
5. **Kiên nhẫn - AI cần thời gian xử lý**

## 🔑 **API Configuration:**
- **Gemini AI:** Phân tích và mô tả ảnh
- **Pollinations AI:** Tạo ảnh miễn phí
- **Rate Limits:** 15 requests/phút (Gemini Flash)

---

**🎉 Giờ bạn có thể tạo ảnh AI thông minh với chính khuôn mặt của mình!**