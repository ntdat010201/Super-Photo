# Test Script cho AI Images Feature
# Su dung ADB de test tu dong

Write-Host "Bat dau test chuc nang AI Images..." -ForegroundColor Green

# 1. Khoi dong app
Write-Host "Khoi dong app SuperPhoto..." -ForegroundColor Yellow
adb shell am start -n com.example.superphoto/.ui.activities.MainActivity
Start-Sleep -Seconds 3

# 2. Kiem tra app co chay khong
Write-Host "Kiem tra app co dang chay..." -ForegroundColor Yellow
$appRunning = adb shell "ps | grep superphoto"
if ($appRunning) {
    Write-Host "App dang chay!" -ForegroundColor Green
} else {
    Write-Host "App khong chay!" -ForegroundColor Red
    exit 1
}

# 3. Chup screenshot de xem giao dien
Write-Host "Chup screenshot giao dien hien tai..." -ForegroundColor Yellow
adb shell screencap -p /sdcard/screenshot_main.png
adb pull /sdcard/screenshot_main.png ./screenshot_main.png
Write-Host "Screenshot da luu: screenshot_main.png" -ForegroundColor Green

# 4. Thu tap vao tab AI Images (gia su o vi tri bottom navigation)
Write-Host "Thu tap vao tab AI Images..." -ForegroundColor Yellow
# Tap vao vi tri co the la tab AI Images (can dieu chinh toa do)
adb shell input tap 400 1800  # Toa do co the can dieu chinh
Start-Sleep -Seconds 2

# 5. Chup screenshot sau khi tap
Write-Host "Chup screenshot sau khi tap..." -ForegroundColor Yellow
adb shell screencap -p /sdcard/screenshot_ai.png
adb pull /sdcard/screenshot_ai.png ./screenshot_ai.png
Write-Host "Screenshot AI tab da luu: screenshot_ai.png" -ForegroundColor Green

# 6. Test nhap text (gia su co EditText)
Write-Host "Test nhap text prompt..." -ForegroundColor Yellow
adb shell input tap 400 800  # Tap vao text input
Start-Sleep -Seconds 1
adb shell input text "a beautiful sunset over mountains"
Start-Sleep -Seconds 1

# 7. Chup screenshot sau khi nhap text
Write-Host "Chup screenshot sau khi nhap text..." -ForegroundColor Yellow
adb shell screencap -p /sdcard/screenshot_text.png
adb pull /sdcard/screenshot_text.png ./screenshot_text.png
Write-Host "Screenshot voi text da luu: screenshot_text.png" -ForegroundColor Green

# 8. Thu tap vao nut Generate (gia su o cuoi man hinh)
Write-Host "Thu tap vao nut Generate..." -ForegroundColor Yellow
adb shell input tap 400 1600  # Toa do nut Generate
Start-Sleep -Seconds 2

# 9. Chup screenshot sau khi tap Generate
Write-Host "Chup screenshot sau khi tap Generate..." -ForegroundColor Yellow
adb shell screencap -p /sdcard/screenshot_generate.png
adb pull /sdcard/screenshot_generate.png ./screenshot_generate.png
Write-Host "Screenshot Generate da luu: screenshot_generate.png" -ForegroundColor Green

# 10. Doi va kiem tra ket qua (30 giay)
Write-Host "Doi AI generate anh (30 giay)..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

# 11. Chup screenshot cuoi cung
Write-Host "Chup screenshot ket qua cuoi cung..." -ForegroundColor Yellow
adb shell screencap -p /sdcard/screenshot_result.png
adb pull /sdcard/screenshot_result.png ./screenshot_result.png
Write-Host "Screenshot ket qua da luu: screenshot_result.png" -ForegroundColor Green

# 12. Kiem tra log de xem co loi gi
Write-Host "Kiem tra log..." -ForegroundColor Yellow
adb logcat -d | Select-String "superphoto|SuperPhoto|AI|ERROR" | Select-Object -Last 20 | Out-File -FilePath "./test_log.txt"
Write-Host "Log da luu: test_log.txt" -ForegroundColor Green

Write-Host "Test hoan thanh! Kiem tra cac file screenshot va log de xem ket qua." -ForegroundColor Green
Write-Host "Files tao ra:" -ForegroundColor Cyan
Write-Host "   - screenshot_main.png (Giao dien chinh)" -ForegroundColor White
Write-Host "   - screenshot_ai.png (Tab AI Images)" -ForegroundColor White
Write-Host "   - screenshot_text.png (Sau khi nhap text)" -ForegroundColor White
Write-Host "   - screenshot_generate.png (Sau khi tap Generate)" -ForegroundColor White
Write-Host "   - screenshot_result.png (Ket qua cuoi cung)" -ForegroundColor White
Write-Host "   - test_log.txt (Log chi tiet)" -ForegroundColor White