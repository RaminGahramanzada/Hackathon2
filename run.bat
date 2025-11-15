@echo off
echo ================================================
echo Starting EasyFin Open Banking API
echo ================================================
echo.
echo Swagger UI will be available at:
echo http://localhost:8080/swagger-ui.html
echo.
echo H2 Console available at:
echo http://localhost:8080/h2-console
echo.
echo Press Ctrl+C to stop the server
echo ================================================
echo.

mvn spring-boot:run

