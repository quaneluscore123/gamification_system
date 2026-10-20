@echo off
title LifeQuest Android - Emulator
color 0B

set SDK=%LOCALAPPDATA%\Android\Sdk
set ADB=%SDK%\platform-tools\adb.exe
set AVD=LifeQuest_Pixel

echo =========================================
echo    KHOI DONG DIEN THOAI GIA LAP...
echo =========================================

rem Mo cua so emulator neu chua co thiet bi nao dang chay
"%ADB%" get-state >nul 2>&1
if errorlevel 1 (
    start "" "%SDK%\emulator\emulator.exe" -avd %AVD% -gpu host -no-snapshot-save -no-audio
)

echo Dang cho emulator boot xong (lan dau co the mat 1-3 phut)...
"%ADB%" wait-for-device
:waitboot
for /f "delims=" %%b in ('"%ADB%" shell getprop sys.boot_completed 2^>nul') do set BOOTED=%%b
if not "%BOOTED%"=="1" (
    timeout /t 3 /nobreak >nul
    goto waitboot
)

echo Dang build va cai LifeQuest...
cd /d "%~dp0"
call gradlew.bat installDebug
if errorlevel 1 (
    echo Build that bai!
    pause
    exit /b 1
)

"%ADB%" shell am start -n com.lifequest.app/.MainActivity
echo Xong! LifeQuest dang chay tren emulator.
timeout /t 5 >nul
