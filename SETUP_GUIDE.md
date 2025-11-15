# EasyFin Setup Guide for iOS Developers

## Quick Start (Windows)

### 1. Start the API Server

Simply double-click `run.bat` or run in PowerShell:
```bash
.\run.bat
```

The API will start on `http://localhost:8080`

**Wait for this message:**
```
=================================
EasyFin Open Banking API Started
Swagger UI: http://localhost:8080/swagger-ui.html
H2 Console: http://localhost:8080/h2-console
=================================
```

### 2. Access Swagger UI

Open your browser and go to:
```
http://localhost:8080/swagger-ui.html
```

You'll see all 60+ API endpoints organized by category.

### 3. Share with Remote iOS Developers

#### Using ngrok (Recommended)

1. **Download ngrok:** https://ngrok.com/download (free)

2. **Start the API** (run.bat)

3. **In a new terminal, run:**
   ```bash
   ngrok http 8080
   ```

4. **Copy the public URL** (something like `https://abc123.ngrok.io`)

5. **Share with iOS developers:**
   ```
   Swagger UI: https://abc123.ngrok.io/swagger-ui.html
   API Base URL: https://abc123.ngrok.io
   ```

#### Using localtunnel (No Account Required)

1. **Install localtunnel:**
   ```bash
   npm install -g localtunnel
   ```

2. **Start the API** (run.bat)

3. **In a new terminal:**
   ```bash
   lt --port 8080 --subdomain easyfin
   ```

4. **Share:** `https://easyfin.loca.lt/swagger-ui.html`

## Testing the API

### Via Swagger UI

1. Go to `http://localhost:8080/swagger-ui.html`
2. Click on any endpoint (e.g., `GET /api/v1/dashboard/summary`)
3. Click "Try it out"
4. Click "Execute"
5. See the response with mock restaurant data

### Via curl

```bash
# Get dashboard summary
curl http://localhost:8080/api/v1/dashboard/summary

# Get all transactions
curl http://localhost:8080/api/v1/transactions

# Get all employees
curl http://localhost:8080/api/v1/employees

# Get tax summary
curl http://localhost:8080/api/v1/tax/summary
```

### Via Postman

1. Import the base URL: `http://localhost:8080`
2. Add prefix to all requests: `/api/v1`
3. Test any endpoint from the list in README.md

## Mock Data Available

The API automatically loads realistic restaurant data on startup:

✅ **Business:** Nizami Restaurant (7 employees, <200K AZN)
✅ **Employees:** 7 staff members with salaries
✅ **Transactions:** 100+ realistic transactions (food, utilities, rent, etc.)
✅ **Payroll:** Complete payroll records with SSF and tax calculations
✅ **Alerts:** 3 active alerts (low balance, tax deadline, unusual spending)
✅ **Recommendations:** 3 smart recommendations

## API Endpoints by Category

### Core Endpoints for iOS App

```
Dashboard:
GET /api/v1/dashboard/summary

Transactions:
GET /api/v1/transactions
GET /api/v1/transactions/{id}

Employees:
GET /api/v1/employees
POST /api/v1/employees

Payroll:
GET /api/v1/payroll/calculate
GET /api/v1/payroll/taxes

Tax:
GET /api/v1/tax/summary
GET /api/v1/tax/micro-entrepreneur-status
GET /api/v1/tax/deductions

Cash Flow:
GET /api/v1/cashflow/forecast
GET /api/v1/cashflow/analysis

Alerts:
GET /api/v1/alerts
POST /api/v1/alerts/{id}/dismiss

Recommendations:
GET /api/v1/recommendations
GET /api/v1/recommendations/tax-savings
```

## Response Format

All responses are in JSON format:

```json
{
  "totalIncome": 48000.00,
  "totalExpenses": 32450.00,
  "netCashFlow": 15550.00,
  "taxDeductibleExpenses": 28400.00,
  "estimatedTaxSavings": 27750.00,
  "activeEmployees": 7,
  "unreadAlerts": 3,
  "pendingRecommendations": 3
}
```

## Error Handling

The API returns standardized error responses:

```json
{
  "status": 404,
  "message": "Transaction not found",
  "timestamp": "2024-11-14T22:30:00",
  "path": "/api/v1/transactions/999"
}
```

## Date Format

All dates use ISO 8601 format: `2024-11-14T10:30:00`

## Currency

All amounts are in AZN (Azerbaijan Manat)

## Troubleshooting

### Port 8080 already in use

```bash
# Find process using port 8080
netstat -ano | findstr :8080

# Kill the process (replace PID with actual process ID)
taskkill /F /PID <PID>
```

### Maven not found

Download Maven from: https://maven.apache.org/download.cgi

### Java not found

Download Java 17 from: https://adoptium.net/

## Support

For questions during the hackathon, contact the backend team or check:
- README.md for full API documentation
- Swagger UI for interactive API exploration
- H2 Console at http://localhost:8080/h2-console for database inspection

---

**Happy Coding! 🚀**

