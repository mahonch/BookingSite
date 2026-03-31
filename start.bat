@echo off
chcp 65001 >nul
echo ========================================
echo  Forum Booking System - Запуск
echo ========================================
echo.

REM Проверка Java
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ОШИБКА: Java не найдена!
    pause
    exit /b 1
)
echo [OK] Java найдена

REM Запуск backend
echo [1/2] Запуск backend...
cd /d "%~dp0backend"
start "Backend" cmd /k "title Backend & apache-maven-3.9.6\bin\mvn.cmd spring-boot:run"

REM Ожидание
echo Ожидание запуска backend (30 сек)...
timeout /t 30 /nobreak >nul

REM Проверка backend
curl -s http://localhost:8080/api/companies >nul 2>&1
if %errorlevel% neq 0 (
    echo ВНИМАНИЕ: Backend не отвечает. Проверьте логи.
) else (
    echo [OK] Backend запущен
)

REM Запуск frontend
echo [2/2] Запуск frontend...
cd /d "%~dp0frontend"
if exist "node_modules" (
    start "Frontend" cmd /k "title Frontend & npm run dev"
) else (
    echo Установка зависимостей...
    start "Frontend Install" cmd /k "npm install && npm run dev"
)

echo.
echo ========================================
echo  Запуск завершен!
echo.
echo  Backend:  http://localhost:8080
echo  Frontend: http://localhost:5173
echo  Админка:  http://localhost:5173/admin/register
echo ========================================
echo.
pause
