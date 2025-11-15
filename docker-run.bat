@echo off
echo ========================================
echo Starting EasyFin API Docker Container
echo ========================================
echo.

docker run -d -p 8080:8080 --name easyfin-api easyfin-api:latest

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Container started successfully!
    echo ========================================
    echo.
    echo API is now running at: http://localhost:8080
    echo.
    echo View logs:     docker logs easyfin-api -f
    echo Stop:          docker stop easyfin-api
    echo Remove:        docker rm easyfin-api
    echo.
    echo Test the API:  curl http://localhost:8080/api/v1/dashboard/summary
    echo Health check:  curl http://localhost:8080/actuator/health
    echo.
) else (
    echo.
    echo ========================================
    echo Failed to start container!
    echo ========================================
    echo.
    echo Maybe container already exists? Try:
    echo   docker rm easyfin-api
    echo Then run this script again.
    echo.
)

pause

