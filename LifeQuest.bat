@echo off
title LifeQuest - The Backend Journey
color 0B

echo =========================================
echo       WAKING UP MENTOR ASTRA...
echo =========================================
echo.
echo Starting local server...

cd /d "D:\Gamification System\lifequest"

timeout /t 2 /nobreak >nul

start msedge --app=http://localhost:5174/ 2>nul || start chrome --app=http://localhost:5174/ 2>nul || start http://localhost:5174/

npm run dev
