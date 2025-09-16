#!/bin/bash

# Cursor Base Control Manager - Bash Version
# Script để set và check cursor base control bằng curl

# Load environment variables from .env file
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
if [ -f "$SCRIPT_DIR/.env" ]; then
    source "$SCRIPT_DIR/.env"
else
    echo "❌ Không tìm thấy file .env trong thư mục scripts!"
    echo "💡 Vui lòng tạo file .env với các biến môi trường cần thiết."
    exit 1
fi

echo "🚀 Bắt đầu quản lý Cursor Base Control..."
echo "================================================"

# Kiểm tra xem COOKIE có được load từ .env không
if [ -z "$COOKIE" ]; then
    echo "❌ Biến COOKIE không được định nghĩa trong file .env!"
    echo "💡 Vui lòng cập nhật COOKIE trong file .env"
    exit 1
fi

echo "📤 Bước 1: Thiết lập base control..."
echo "------------------------------"

# Set hard limit
SET_RESPONSE=$(curl --silent --location 'https://cursor.com/api/dashboard/set-hard-limit' \
  --header "User-Agent: $USER_AGENT" \
  --header "Accept: $ACCEPT" \
  --header "Accept-Language: $ACCEPT_LANGUAGE" \
  --header "Accept-Encoding: $ACCEPT_ENCODING" \
  --header "Referer: $REFERER" \
  --header "Content-Type: $CONTENT_TYPE" \
  --header "Origin: $ORIGIN" \
  --header "Connection: $CONNECTION" \
  --header "Cookie: $COOKIE" \
  --header "Sec-Fetch-Dest: $SEC_FETCH_DEST" \
  --header "Sec-Fetch-Mode: $SEC_FETCH_MODE" \
  --header "Sec-Fetch-Site: $SEC_FETCH_SITE" \
  --header "Priority: u=0" \
  --header "TE: $TE" \
  --data '{"hardLimit":0,"noUsageBasedAllowed":true,"hardLimitPerUser":0}' \
  --write-out "HTTPSTATUS:%{http_code}")

# Parse response
SET_BODY=$(echo $SET_RESPONSE | sed -E 's/HTTPSTATUS:[0-9]{3}$//')
SET_STATUS=$(echo $SET_RESPONSE | tr -d '\n' | sed -E 's/.*HTTPSTATUS:([0-9]{3})$/\1/')

echo "📊 Status Code: $SET_STATUS"
echo "📝 Response: $SET_BODY"

if [ "$SET_STATUS" -eq 200 ]; then
    echo "✅ Thiết lập base control thành công!"
else
    echo "❌ Lỗi khi thiết lập base control: $SET_STATUS"
    exit 1
fi

echo ""
echo "📥 Bước 2: Kiểm tra base control..."
echo "------------------------------"

# Get hard limit
GET_RESPONSE=$(curl --silent --location 'https://cursor.com/api/dashboard/get-hard-limit' \
  --header "User-Agent: $USER_AGENT" \
  --header "Accept: $ACCEPT" \
  --header "Accept-Language: $ACCEPT_LANGUAGE" \
  --header "Accept-Encoding: $ACCEPT_ENCODING" \
  --header "Referer: $REFERER" \
  --header "Content-Type: $CONTENT_TYPE" \
  --header "Origin: $ORIGIN" \
  --header "Connection: $CONNECTION" \
  --header "Cookie: $COOKIE" \
  --header "Sec-Fetch-Dest: $SEC_FETCH_DEST" \
  --header "Sec-Fetch-Mode: $SEC_FETCH_MODE" \
  --header "Sec-Fetch-Site: $SEC_FETCH_SITE" \
  --header "Priority: u=4" \
  --header "TE: $TE" \
  --data '{}' \
  --write-out "HTTPSTATUS:%{http_code}")

# Parse response
GET_BODY=$(echo $GET_RESPONSE | sed -E 's/HTTPSTATUS:[0-9]{3}$//')
GET_STATUS=$(echo $GET_RESPONSE | tr -d '\n' | sed -E 's/.*HTTPSTATUS:([0-9]{3})$/\1/')

echo "📊 Status Code: $GET_STATUS"
echo "📝 Response: $GET_BODY"

if [ "$GET_STATUS" -eq 200 ]; then
    echo "✅ Lấy thông tin base control thành công!"
else
    echo "❌ Lỗi khi lấy base control: $GET_STATUS"
    exit 1
fi

echo ""
echo "🔍 Bước 3: Kiểm tra kết quả..."
echo "------------------------------"

# Check if noUsageBasedAllowed is true
if echo "$GET_BODY" | grep -q '"noUsageBasedAllowed":true'; then
    echo "🎉 Đã set thành công base control"
    echo "================================================"
    echo "🎊 HOÀN THÀNH THÀNH CÔNG!"
    exit 0
else
    echo "⚠️  Kết quả không như mong đợi"
    echo "📋 Dữ liệu nhận được: $GET_BODY"
    echo "================================================"
    echo "💥 QUY TRÌNH THẤT BẠI!"
    exit 1
fi