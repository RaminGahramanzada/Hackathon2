# 📱 EasyFin API - iOS Developer Guide

## 🌐 Base URL
```
Production: https://hackathon2-ibmt.onrender.com/api/v1
Local: http://localhost:8080/api/v1
```

## 🔑 API Version
All endpoints start with: `/api/v1`

## ✅ Connection Requirements
- **CORS**: Fully configured ✅
- **HTTPS**: Production uses HTTPS ✅
- **No special headers required** for production deployment
- **Content-Type**: `application/json` (recommended)

## 🔧 If Using ngrok (Temporary Testing Only)
If the backend is temporarily using ngrok, add this header:
```swift
request.setValue("true", forHTTPHeaderField: "ngrok-skip-browser-warning")
```
See `IOS_NGROK_FIX.md` for details.

---

## 📊 DASHBOARD / HOME SCREEN

### Get Dashboard Summary
**Endpoint:** `GET /api/v1/dashboard/summary`

**Description:** Returns complete dashboard data for the home screen including user name, balance information, and summary metrics.

**Response:**
```json
{
  "firstName": "Rail",
  "lastName": "Nuriyev",
  "fullName": "Rail Nuriyev",
  
  "totalBalance": 45300.00,
  "availableBalance": 29400.00,
  "pendingBalance": 8900.00,
  
  "totalIncome": 48000.00,
  "totalExpenses": 32450.00,
  "netCashFlow": 15550.00,
  "taxDeductibleExpenses": 28400.00,
  "estimatedTaxSavings": 27750.00,
  
  "activeEmployees": 7,
  "unreadAlerts": 3,
  "pendingRecommendations": 3,
  
  "recentTransactions": [
    {
      "id": 1,
      "merchantName": "Taze Bazar",
      "amount": 250.00,
      "category": "Food Supplies",
      "date": "2024-11-14"
    }
  ],
  
  "spendingByCategory": {
    "Food Supplies": 12500.00,
    "Rent": 9000.00,
    "Utilities": 2400.00
  }
}
```

**iOS Model:**
```swift
struct DashboardResponse: Codable {
    let firstName: String
    let lastName: String
    let fullName: String
    
    let totalBalance: Double
    let availableBalance: Double      // Hazirda hesabda olan pul
    let pendingBalance: Double        // Vergide qalan pul (2 həftədən sonra gələcək)
    
    let totalIncome: Double
    let totalExpenses: Double
    let netCashFlow: Double
    let taxDeductibleExpenses: Double
    let estimatedTaxSavings: Double
    
    let activeEmployees: Int
    let unreadAlerts: Int
    let pendingRecommendations: Int
    
    let recentTransactions: [RecentTransaction]?
    let spendingByCategory: [String: Double]?
}
```

---

## 💰 BALANCE EXPLANATION

### Balance Fields:
1. **totalBalance**: Total money (income - expenses)
   - Display as main "Total Balance"

2. **availableBalance**: Hazirda hesabda olan pul
   - Current money you can use now
   - Display as "Available"

3. **pendingBalance**: Vergide qalan pul
   - Money stuck in tax processing
   - Will be available after ~2 weeks
   - Display as "Pending"

**Formula:**
```
Total Balance = Available + Pending
Total Balance = All Income - All Expenses
Available = Total Balance - Pending
Pending = Tax-related money in processing
```

---

## 💸 TRANSACTIONS

### Get All Transactions
**Endpoint:** `GET /api/v1/transactions`

**Query Parameters (Optional):**
- `startDate`: Filter by start date (ISO 8601 format)
- `endDate`: Filter by end date
- `category`: Filter by category

**Response:**
```json
[
  {
    "id": 1,
    "transactionDate": "2024-11-14T10:30:00",
    "amount": 250.00,
    "currency": "AZN",
    "merchantName": "Taze Bazar",
    "description": "Food supplies",
    "category": "FOOD_SUPPLIES",
    "isTaxDeductible": true,
    "isIncome": false,
    "categorizationConfidence": 0.85,
    "receiptUrl": null
  }
]
```

### Get Single Transaction
**Endpoint:** `GET /api/v1/transactions/{id}`

**Example:** `GET /api/v1/transactions/1`

---

## 🧑‍💼 EMPLOYEES

### Get All Employees
**Endpoint:** `GET /api/v1/employees`

**Response:**
```json
[
  {
    "id": 1,
    "firstName": "Rashad",
    "lastName": "Mammadov",
    "fullName": "Rashad Mammadov",
    "position": "Head Chef",
    "monthlySalary": 1500.00,
    "hireDate": "2023-05-15",
    "email": "rashad@nizamirestaurant.az",
    "phone": "+994501234567",
    "isActive": true
  }
]
```

---

## 💼 PAYROLL

### Get Monthly Payroll Summary (⭐ NEW - For Payroll Calculator Page)
**Endpoint:** `GET /api/v1/payroll/summary`

**Description:** Get monthly payroll summary for ALL employees (total aggregated data)

**Response:**
```json
{
  "month": "2024-11-01",
  "employeeCount": 7,
  "totalGrossPayroll": 18450.00,
  "totalTaxes": 3691.00,
  "totalNetPayrollCost": 22141.00,
  
  "costBreakdown": {
    "grossWages": 18450.00,
    "employerSSF": 1650.00,
    "employeeSSF": 225.00,
    "incomeTax": 1200.00
  },
  
  "taxCalculations": {
    "socialSecurityTax": 1875.00,
    "employerSSF": 1650.00,
    "employeeSSF": 225.00,
    "incomeTax": 1200.00
  }
}
```

**iOS Model:**
```swift
struct PayrollSummary: Codable {
    let month: String
    let employeeCount: Int
    let totalGrossPayroll: Double          // For "Gross Payroll" card
    let totalTaxes: Double                 // For "Total Taxes" card
    let totalNetPayrollCost: Double        // For "Net Payroll Cost"
    let costBreakdown: CostBreakdown       // For Pie Chart
    let taxCalculations: TaxCalculations   // For "Tax Calculations" section
}

struct CostBreakdown: Codable {
    let grossWages: Double      // 83.3% in pie chart
    let employerSSF: Double     // State Tax
    let employeeSSF: Double     // FICA
    let incomeTax: Double       // Federal
}

struct TaxCalculations: Codable {
    let socialSecurityTax: Double  // Total SSF (employer + employee)
    let employerSSF: Double
    let employeeSSF: Double
    let incomeTax: Double
}
```

---

### Calculate Payroll (Individual Employees)
**Endpoint:** `GET /api/v1/payroll/calculate`

**Description:** Calculate current month payroll for each employee individually

**Response:**
```json
[
  {
    "id": null,
    "employeeId": 1,
    "employeeName": "Rashad Mammadov",
    "payrollMonth": "2024-11-01",
    "grossSalary": 1500.00,
    "employeeSsfContribution": 45.00,
    "employerSsfContribution": 330.00,
    "incomeTax": 203.70,
    "netSalary": 1251.30,
    "totalEmployerCost": 1830.00,
    "isPaid": false
  }
]
```

### Get Employee Taxes
**Endpoint:** `GET /api/v1/payroll/taxes`

**Description:** Get total employee-related taxes (SSF + income tax)

**Response:**
```json
{
  "totalEmployerSSF": 1650.00,
  "totalEmployeeSSF": 225.00,
  "totalIncomeTax": 1200.00,
  "totalTaxBurden": 3075.00,
  "employeeCount": 7
}
```

---

## 💵 TAX MANAGEMENT

### Get Tax Summary
**Endpoint:** `GET /api/v1/tax/summary`

**Response:**
```json
{
  "taxStatus": "Micro-Entrepreneur",
  "taxExemptionRate": 0.75,
  "totalIncome": 185000.00,
  "taxableIncome": 46250.00,
  "totalDeductions": 28400.00,
  "estimatedTaxLiability": 9250.00,
  "estimatedTaxSavings": 27750.00,
  "employeeTaxWithheld": 1200.00,
  "ssfContributions": 1875.00,
  "period": "2024-08-14 to 2024-11-14"
}
```

### Check Micro-Entrepreneur Status
**Endpoint:** `GET /api/v1/tax/micro-entrepreneur-status`

**Response:**
```json
{
  "isEligible": true,
  "currentStatus": "Micro-Entrepreneur",
  "taxExemption": 0.75,
  "employeeCount": 7,
  "annualIncome": 185000.00,
  "description": "Micro-entrepreneurs (1-10 employees, <200K AZN) get 75% income tax exemption"
}
```

### Get Tax Deadlines
**Endpoint:** `GET /api/v1/tax/deadlines`

**Response:**
```json
{
  "nextDeadline": "2024-12-20",
  "type": "Quarterly Declaration",
  "daysRemaining": 15,
  "description": "Quarterly tax declaration to DVX (Dövlət Vergi Xidməti)"
}
```

---

## 📈 CASH FLOW

### Get Cash Flow Forecast
**Endpoint:** `GET /api/v1/cashflow/forecast?days=60`

**Query Parameters:**
- `days`: Number of days to forecast (default: 60)

**Response:**
```json
[
  {
    "id": 1,
    "forecastDate": "2024-11-15",
    "predictedIncome": 800.00,
    "predictedExpenses": 540.00,
    "predictedBalance": 45560.00,
    "confidence": 0.75
  }
]
```

### Get Cash Flow Analysis
**Endpoint:** `GET /api/v1/cashflow/analysis`

**Response:**
```json
{
  "periodStart": "2024-10-15",
  "periodEnd": "2024-11-14",
  "totalIncome": 48000.00,
  "totalExpenses": 32450.00,
  "netCashFlow": 15550.00,
  "currentBalance": 15550.00,
  "incomeByCategory": {
    "RESTAURANT_SALES": 45000.00,
    "CATERING_SERVICES": 3000.00
  },
  "expensesByCategory": {
    "FOOD_SUPPLIES": 12500.00,
    "RENT": 9000.00,
    "UTILITIES": 2400.00,
    "SALARIES": 8550.00
  },
  "forecast": [...]
}
```

**iOS Model:**
```swift
struct CashFlowAnalysis: Codable {
    let periodStart: String
    let periodEnd: String
    let totalIncome: Double
    let totalExpenses: Double
    let netCashFlow: Double
    let currentBalance: Double
    let incomeByCategory: [String: Double]     // Pul hansı kategoriyadan gəlir
    let expensesByCategory: [String: Double]   // Pul hara gedir
    let forecast: [ForecastItem]?
}
```

---

## 🚨 ALERTS

### Get All Active Alerts
**Endpoint:** `GET /api/v1/alerts`

**Description:** Returns all active (non-dismissed) alerts including tax deadlines with complete deadline information.

**Response:**
```json
[
  {
    "id": 1,
    "alertType": "LOW_BALANCE",
    "title": "Low Balance Warning",
    "message": "Your account balance is running low. Current projected balance: 2,500 AZN.",
    "severity": "high",
    "isRead": false,
    "isDismissed": false,
    "createdAt": "2024-11-14T10:00:00",
    "deadlineDate": null,
    "daysRemaining": null,
    "actionUrl": null
  },
  {
    "id": 2,
    "alertType": "TAX_DEADLINE",
    "title": "Tax Deadline Approaching",
    "message": "VAT Payment in 5 days",
    "severity": "high",
    "isRead": false,
    "isDismissed": false,
    "createdAt": "2024-11-14T09:00:00",
    "deadlineDate": "2024-11-19",
    "daysRemaining": 5,
    "actionUrl": "/taxes/declarations"
  }
]
```

**iOS Model:**
```swift
struct AlertResponse: Codable {
    let id: Int
    let alertType: String
    let title: String
    let message: String
    let severity: String
    let isRead: Bool
    let isDismissed: Bool
    let createdAt: String
    let deadlineDate: String?      // For TAX_DEADLINE alerts
    let daysRemaining: Int?        // For TAX_DEADLINE alerts
    let actionUrl: String?         // Deep link
}
```

### Get Tax Deadline Alerts (For Home Banner)
**Endpoint:** `GET /api/v1/alerts/tax-deadlines`

**Description:** Returns only TAX_DEADLINE alerts with deadline date and days remaining. Use this for the "Tax Deadline Approaching" banner on home screen.

**Response:**
```json
[
  {
    "id": 2,
    "alertType": "TAX_DEADLINE",
    "title": "Tax Deadline Approaching",
    "message": "VAT Payment in 5 days",
    "severity": "high",
    "isRead": false,
    "isDismissed": false,
    "createdAt": "2024-11-14T09:00:00",
    "deadlineDate": "2024-11-19",
    "daysRemaining": 5,
    "actionUrl": "/taxes/declarations"
  }
]
```

### Dismiss Alert
**Endpoint:** `POST /api/v1/alerts/{id}/dismiss`

**Example:** `POST /api/v1/alerts/1/dismiss`

**Response:** `"Alert dismissed successfully"`

---

## 💡 RECOMMENDATIONS

### Get All Recommendations
**Endpoint:** `GET /api/v1/recommendations`

**Response:**
```json
[
  {
    "id": 1,
    "title": "You qualify for 75% tax exemption!",
    "description": "As a micro-entrepreneur with 7 employees and income under 200,000 AZN, you qualify for 75% income tax exemption...",
    "category": "tax-savings",
    "potentialSavings": 27750.00,
    "priority": "high",
    "isActedUpon": false,
    "createdAt": "2024-11-14T08:00:00"
  }
]
```

### Get Tax Savings Recommendations
**Endpoint:** `GET /api/v1/recommendations/tax-savings`

---

## 📄 REPORTS

### Generate Tax Report PDF
**Endpoint:** `POST /api/v1/reports/pdf/tax-report`

**Response:**
```json
{
  "status": "success",
  "downloadUrl": "/reports/tax-report-Q4-2024.pdf",
  "message": "Tax report PDF generated successfully"
}
```

### Export Transactions to CSV
**Endpoint:** `GET /api/v1/reports/export/csv`

**Response:** CSV file download

---

## 🔗 INTEGRATIONS (Mock)

### DVX Integration
**Endpoint:** `GET /api/v1/integrations/dvx/declarations`

**Response:**
```json
[
  {
    "declarationId": "DVX-2024-Q3-001",
    "quarter": "Q3 2024",
    "submissionDate": "2024-09-20",
    "status": "Submitted",
    "taxAmount": "3,250.00 AZN"
  }
]
```

### Bank Account Info
**Endpoint:** `GET /api/v1/integrations/bank/accounts`

**Response:**
```json
[
  {
    "bankName": "Kapital Bank",
    "accountNumber": "****5678",
    "accountType": "Business Current Account",
    "balance": "45,230.50 AZN",
    "currency": "AZN",
    "status": "Connected"
  }
]
```

---

## 🏢 BUSINESS PROFILE

### Get Business Profile
**Endpoint:** `GET /api/v1/business/profile`

**Response:**
```json
{
  "id": 1,
  "businessName": "Nizami Restaurant",
  "businessType": "RESTAURANT",
  "employeeCount": 7,
  "annualIncome": 185000.00,
  "currency": "AZN",
  "taxStatus": "MICRO_ENTREPRENEUR",
  "taxExemption": 0.75,
  "ownerName": "Murad Aliyev",
  "email": "murad@nizamirestaurant.az",
  "phone": "+994501234567",
  "address": "28 May küç., Baku, Azerbaijan"
}
```

---

## ⚠️ ERROR HANDLING

All errors follow this format:

```json
{
  "status": 404,
  "message": "Transaction not found",
  "timestamp": "2024-11-14T10:30:00",
  "path": "/api/v1/transactions/999"
}
```

**Common HTTP Status Codes:**
- `200` - Success
- `201` - Created
- `400` - Bad Request
- `404` - Not Found
- `500` - Server Error

---

## 📅 DATE FORMAT

All dates use ISO 8601 format:
```
2024-11-14T10:30:00
```

Swift parsing:
```swift
let formatter = ISO8601DateFormatter()
let date = formatter.date(from: dateString)
```

---

## 💱 CURRENCY

All amounts are in **AZN** (Azerbaijan Manat)

---

## 🧪 TESTING

Use Swagger UI for testing:
```
Production: https://hackathon2-ibmt.onrender.com/swagger-ui.html
Local: http://localhost:8080/swagger-ui.html
```

---

## 📞 SUPPORT

For questions during development, check the Swagger documentation or contact the backend team.

**Happy Coding! 🚀**

