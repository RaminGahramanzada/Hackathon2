@echo off
echo ========================================
echo Testing EasyFin API Docker Setup
echo ========================================
echo.
echo Step 1: Building Docker image...
docker build -t easyfin-api:latest .

if %ERRORLEVEL% NEQ 0 (
    echo Build failed! Exiting...
    pause
    exit /b 1
)

echo.
echo Step 2: Starting container...
docker run -d -p 8080:8080 --name easyfin-api-test easyfin-api:latest

if %ERRORLEVEL% NEQ 0 (
    echo Failed to start container! Exiting...
    pause
    exit /b 1
)

echo.
echo Step 3: Waiting for application to start (30 seconds)...
timeout /t 30 /nobreak

echo.
echo Step 4: Testing health endpoint...
curl http://localhost:8080/actuator/health

echo.
echo.
echo Step 5: Testing API endpoint...
curl http://localhost:8080/api/v1/dashboard/summary

echo.
echo.
echo ========================================
echo Test complete!
echo ========================================
echo.
echo To stop and remove test container:
echo   docker stop easyfin-api-test
echo   docker rm easyfin-api-test
echo.

pause

