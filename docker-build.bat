@echo off
echo ========================================
echo Building EasyFin API Docker Image
echo ========================================
echo.

docker build -t easyfin-api:latest .

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Build successful!
    echo ========================================
    echo.
    echo To run the container:
    echo   docker run -d -p 8080:8080 --name easyfin-api easyfin-api:latest
    echo.
    echo Or use docker-compose:
    echo   docker-compose up -d
    echo.
) else (
    echo.
    echo ========================================
    echo Build failed! Check the error above.
    echo ========================================
)

pause

