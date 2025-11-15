# 📱 iOS Developer - Required Endpoints

## 🏠 HOME SCREEN (Main Dashboard)

### ✅ Primary Endpoint:
```
GET /api/v1/dashboard/summary
```

**iOS Usage:**
```swift
struct DashboardResponse: Codable {
    // User Info (Top of screen)
    let firstName: String        // "Rail"
    let lastName: String         // "Nuriyev"
    let fullName: String         // "Rail Nuriyev"
    
    // Balance Card
    let totalBalance: Double     // $45,300
    let availableBalance: Double // $29,400 (Hazirda hesabda olan pul)
    let pendingBalance: Double   // $8,900 (Vergide qalan pul)
    
    // Summary Metrics
    let totalIncome: Double
    let totalExpenses: Double
    let activeEmployees: Int
    let unreadAlerts: Int
    
    // Recent Activity
    let recentTransactions: [RecentTransaction]?
    let spendingByCategory: [String: Double]?
}
```

---

## 🚨 ALERTS (Tax Deadline Notification)

### ✅ Get Alerts:
```
GET /api/v1/alerts
```

**Returns:**
```json
[
  {
    "id": 2,
    "alertType": "TAX_DEADLINE",
    "title": "Tax Deadline Approaching",
    "message": "VAT Payment in 5 days",
    "severity": "high",
    "isRead": false
  }
]
```

**iOS Model:**
```swift
struct Alert: Codable {
    let id: Int
    let alertType: String
    let title: String
    let message: String
    let severity: String    // "high", "medium", "low"
    let isRead: Bool
}
```

### ✅ Dismiss Alert:
```
POST /api/v1/alerts/{id}/dismiss
```

---

## 💸 TRANSACTIONS

### ✅ Get All Transactions:
```
GET /api/v1/transactions
```

**Optional Filters:**
- `?startDate=2024-11-01`
- `&endDate=2024-11-14`
- `&category=FOOD_SUPPLIES`

---

## 📊 CASH FLOW

### ✅ Get Cash Flow Forecast:
```
GET /api/v1/cashflow/forecast?days=30
```

**Response:**
```json
[
  {
    "forecastDate": "2024-11-15",
    "predictedIncome": 800.00,
    "predictedExpenses": 540.00,
    "predictedBalance": 45560.00,
    "confidence": 0.75
  }
]
```

---

## 💼 PAYROLL TAB

### ✅ Get All Employees:
```
GET /api/v1/employees
```

### ✅ Calculate Current Payroll:
```
GET /api/v1/payroll/calculate
```

### ✅ Get Employee Taxes:
```
GET /api/v1/payroll/taxes
```

**Returns:**
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

## 💡 SUGGESTIONS TAB

### ✅ Get All Recommendations:
```
GET /api/v1/recommendations
```

### ✅ Get Tax Savings Recommendations:
```
GET /api/v1/recommendations/tax-savings
```

**Response:**
```json
[
  {
    "id": 1,
    "title": "You qualify for 75% tax exemption!",
    "description": "As a micro-entrepreneur...",
    "category": "tax-savings",
    "potentialSavings": 27750.00,
    "priority": "high"
  }
]
```

---

## 📄 TAXES TAB

### ✅ Get Tax Summary:
```
GET /api/v1/tax/summary
```

**Response:**
```json
{
  "taxStatus": "Micro-Entrepreneur",
  "totalIncome": 185000.00,
  "taxableIncome": 46250.00,
  "totalDeductions": 28400.00,
  "estimatedTaxLiability": 9250.00,
  "estimatedTaxSavings": 27750.00,
  "period": "2024-08-14 to 2024-11-14"
}
```

### ✅ Check Micro-Entrepreneur Status:
```
GET /api/v1/tax/micro-entrepreneur-status
```

### ✅ Get Tax Deadlines:
```
GET /api/v1/tax/deadlines
```

---

## 📑 REPORTS

### ✅ Export to PDF:
```
POST /api/v1/reports/pdf/tax-report
```

### ✅ Export to CSV:
```
GET /api/v1/reports/export/csv
```

---

## 🏢 BUSINESS PROFILE

### ✅ Get Business Info:
```
GET /api/v1/business/profile
```

**Response:**
```json
{
  "id": 1,
  "businessName": "Nizami Restaurant",
  "businessType": "RESTAURANT",
  "employeeCount": 7,
  "annualIncome": 185000.00,
  "taxStatus": "MICRO_ENTREPRENEUR",
  "ownerName": "Murad Aliyev",
  "email": "murad@nizamirestaurant.az",
  "phone": "+994501234567"
}
```

---

## 🔗 INTEGRATIONS (Mock Data)

### ✅ DVX Integration:
```
GET /api/v1/integrations/dvx/declarations
GET /api/v1/integrations/dvx/status
```

### ✅ Bank Integration:
```
GET /api/v1/integrations/bank/accounts
GET /api/v1/integrations/bank/recent-transactions
```

### ✅ ASAN Imza Integration:
```
GET /api/v1/integrations/asan/verify
```

---

## 🌐 BASE URL

**Local Testing:**
```
http://localhost:8080/api/v1
```

**Production (ngrok):**
```
https://stevie-superdivine-unpompously.ngrok-free.dev/api/v1
```

---

## 📚 DOCUMENTATION

**Swagger UI (Interactive):**
```
http://localhost:8080/swagger-ui.html
```

Try all endpoints here and see real responses!

---

## 💡 QUICK START FOR iOS

1. **Test in Browser First:**
   ```
   http://localhost:8080/api/v1/dashboard/summary
   ```

2. **iOS URLSession Example:**
   ```swift
   let url = URL(string: "http://localhost:8080/api/v1/dashboard/summary")!
   
   URLSession.shared.dataTask(with: url) { data, response, error in
       guard let data = data else { return }
       let dashboard = try? JSONDecoder().decode(DashboardResponse.self, from: data)
       print(dashboard?.firstName) // "Rail"
       print(dashboard?.totalBalance) // 45300.00
   }.resume()
   ```

3. **Check Swagger for Full Details:**
   All endpoints are documented with request/response examples!

---

## ⚠️ IMPORTANT NOTES

1. **Date Format:** ISO 8601 (`2024-11-14T10:30:00`)
2. **Currency:** All amounts in AZN
3. **Error Handling:** All errors return JSON with status, message, timestamp
4. **Mock Data:** Currently using mock data for a restaurant with 7 employees

---

## ✅ COMPLETE ENDPOINT LIST

| Screen | Endpoint | Method |
|--------|----------|--------|
| Home | `/dashboard/summary` | GET |
| Alerts | `/alerts` | GET |
| Transactions | `/transactions` | GET |
| Cash Flow | `/cashflow/forecast` | GET |
| Payroll | `/employees` | GET |
| Payroll | `/payroll/calculate` | GET |
| Suggestions | `/recommendations` | GET |
| Taxes | `/tax/summary` | GET |
| Profile | `/business/profile` | GET |

**Total: 9 primary endpoints for full app functionality** ✅

