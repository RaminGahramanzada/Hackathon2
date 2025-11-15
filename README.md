# EasyFin Open Banking API

Financial management and tax optimization API for small business owners in Azerbaijan, specifically designed for restaurants and small businesses with employees.

## 🎯 Project Overview

**EasyFin** provides comprehensive financial management features including:
- Real-time transaction categorization
- Azerbaijan-specific tax calculations (sadələşdirilmiş vergi - 75% exemption)
- Employee & payroll management with SSF calculations
- Cash flow forecasting (30-60 days)
- Intelligent alerts for financial anomalies
- Actionable recommendations for cost optimization

**Target User:** Restaurant owner with 7 employees, annual income <200,000 AZN

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- (Optional) ngrok for remote Swagger access

### Run the Application

```bash
# Clone and navigate to project
cd Hackathon2

# Run with Maven
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access Swagger UI

Open your browser and navigate to:
```
http://localhost:8080/swagger-ui.html
```

### H2 Database Console (for debugging)

```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:easyfindb
Username: sa
Password: (leave empty)
```

## 📡 Share Swagger with iOS Developers

### Option 1: ngrok (Recommended - FREE & INSTANT)

1. Download ngrok: https://ngrok.com/download
2. Start the Spring Boot app: `mvn spring-boot:run`
3. In a new terminal, run: `ngrok http 8080`
4. Share the public URL with iOS developers:
   ```
   https://abc123.ngrok.io/swagger-ui.html
   ```

### Option 2: Railway.app (FREE - Permanent URL)

1. Push code to GitHub
2. Go to railway.app and connect your GitHub repo
3. Auto-deploys and provides permanent URL
4. Share: `https://yourapp.railway.app/swagger-ui.html`

## 📋 API Endpoints

### Dashboard
- `GET /api/v1/dashboard/summary` - Financial overview

### Transactions
- `GET /api/v1/transactions` - List all transactions (with filters)
- `GET /api/v1/transactions/{id}` - Get specific transaction
- `POST /api/v1/transactions/sync` - Sync from bank
- `PUT /api/v1/transactions/{id}/category` - Update category

### Categories
- `GET /api/v1/categories` - Get all categories
- `GET /api/v1/categories/spending` - Spending breakdown
- `GET /api/v1/categories/tax-ready` - Tax-ready expenses
- `POST /api/v1/categories/{id}/auto-categorize` - Auto-categorize

### Tax Management (Azerbaijan-specific)
- `GET /api/v1/tax/summary` - Tax summary (sadələşdirilmiş vergi)
- `GET /api/v1/tax/micro-entrepreneur-status` - Check 75% exemption eligibility
- `GET /api/v1/tax/deductions` - Tax deductions
- `GET /api/v1/tax/deadlines` - Upcoming deadlines
- `GET /api/v1/tax/dvx-integration` - DVX mock data

### Employees
- `GET /api/v1/employees` - List all employees
- `POST /api/v1/employees` - Add employee
- `PUT /api/v1/employees/{id}` - Update employee
- `DELETE /api/v1/employees/{id}` - Remove employee

### Payroll
- `GET /api/v1/payroll/calculate` - Calculate payroll
- `GET /api/v1/payroll/taxes` - Employee taxes (SSF, income tax)
- `POST /api/v1/payroll/process` - Process payroll
- `GET /api/v1/payroll/history` - Payroll history

### Cash Flow
- `GET /api/v1/cashflow/forecast` - 30-60 day forecast
- `GET /api/v1/cashflow/analysis` - Cash flow analysis
- `GET /api/v1/cashflow/predictions` - Shortfall predictions
- `GET /api/v1/cashflow/trends` - Historical trends

### Alerts
- `GET /api/v1/alerts` - All active alerts
- `GET /api/v1/alerts/unauthorized-payments` - Unusual transactions
- `GET /api/v1/alerts/low-balance` - Low balance warnings
- `GET /api/v1/alerts/unusual-spending` - Spending anomalies
- `GET /api/v1/alerts/tax-deadlines` - Tax reminders
- `POST /api/v1/alerts/{id}/dismiss` - Dismiss alert

### Recommendations
- `GET /api/v1/recommendations` - All recommendations
- `GET /api/v1/recommendations/optimize-spending` - Spending optimization
- `GET /api/v1/recommendations/reduce-costs` - Cost reduction
- `GET /api/v1/recommendations/tax-savings` - Tax opportunities
- `POST /api/v1/recommendations/{id}/action` - Mark as acted upon

### Reports & Exports
- `POST /api/v1/reports/pdf/receipts` - Generate receipts PDF
- `POST /api/v1/reports/pdf/tax-report` - Generate tax report
- `POST /api/v1/reports/pdf/financial-statement` - Financial statement
- `GET /api/v1/reports/export/csv` - Export to CSV
- `GET /api/v1/reports/export/excel` - Export to Excel

### External Integrations (Mock)
- `POST /api/v1/integrations/dvx/sync` - DVX sync
- `GET /api/v1/integrations/dvx/declarations` - Tax declarations
- `POST /api/v1/integrations/egov/business-info` - eGov business info
- `GET /api/v1/integrations/egov/certificates` - Business certificates
- `POST /api/v1/integrations/asan-imza/sign` - ASAN İmza signing
- `POST /api/v1/integrations/bank/sync` - Bank sync
- `GET /api/v1/integrations/bank/accounts` - Connected accounts

### Business Profile
- `GET /api/v1/business/profile` - Get business profile
- `PUT /api/v1/business/profile` - Update profile
- `GET /api/v1/business/tax-status` - Tax status & eligibility
- `GET /api/v1/business/compliance` - Compliance status

## 🏪 Mock Data - Restaurant Use Case

The application automatically initializes with realistic restaurant data:

### Business: Nizami Restaurant
- **Type:** Restaurant
- **Employees:** 7 workers
- **Annual Income:** 185,000 AZN
- **Tax Status:** Micro-entrepreneur (75% exemption)
- **Location:** Baku, Azerbaijan

### Employees (7)
1. Rashad Mammadov - Head Chef (1,500 AZN/month)
2. Aysel Huseynova - Sous Chef (1,200 AZN/month)
3. Elvin Aliyev - Waiter (800 AZN/month)
4. Leyla Hasanova - Waiter (800 AZN/month)
5. Nigar Qasimova - Waiter (800 AZN/month)
6. Orkhan Ibrahimov - Dishwasher (600 AZN/month)
7. Sabina Mammadova - Manager (1,800 AZN/month)

### 100+ Transactions
- Daily sales revenue
- Food supplies from local markets
- Utilities (Azercell, Azersu, Azerishiq, Azergas)
- Equipment and maintenance
- Marketing (Instagram, Facebook ads)
- Transportation (Bolt)
- Monthly rent

### Payroll Records
- SSF contributions (22% employer, 3% employee)
- Income tax withholding (14%)
- Net salary calculations

### Active Alerts
- Low balance warning
- Tax deadline reminder
- Unusual spending detected

### Recommendations
- 75% tax exemption opportunity (save 27,750 AZN)
- Food supplier pricing review
- Cash flow optimization

## 🧪 Azerbaijan Tax Rules Implemented

### Micro-Entrepreneur (Sadələşdirilmiş Vergi)
- **Eligibility:** 1-10 employees, <200,000 AZN annual income
- **Benefits:**
  - 75% income tax exemption
  - 100% property tax exemption
  - 2x accelerated depreciation
  - Simplified reporting

### Employee Taxes
- **SSF (Social Security):**
  - Employer contribution: 22%
  - Employee contribution: 3%
- **Income Tax:** 14% (simplified rate)

### Tax-Deductible Categories
- Food supplies
- Rent
- Utilities
- Salaries & SSF contributions
- Equipment & maintenance
- Marketing & advertising
- Office supplies
- Professional services

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 In-Memory Database**
- **Springdoc OpenAPI (Swagger)**
- **Apache PDFBox** (for PDF generation)
- **Apache POI** (for Excel export)
- **Maven**
- **Lombok**

## 📊 API Documentation Features

- ✅ Complete Swagger UI with examples
- ✅ Grouped endpoints by functionality
- ✅ Request/response schemas
- ✅ Error response formats
- ✅ CORS enabled for iOS apps
- ✅ ISO 8601 date formats
- ✅ Consistent JSON structure

## 🔧 Configuration

All configuration is in `src/main/resources/application.properties`:

```properties
server.port=8080
spring.datasource.url=jdbc:h2:mem:easyfindb
spring.h2.console.enabled=true
springdoc.swagger-ui.path=/swagger-ui.html
```

## 📱 iOS Developer Integration

### Base URL
- Local: `http://localhost:8080`
- ngrok: `https://your-ngrok-url.ngrok.io`
- Railway: `https://yourapp.railway.app`

### API Versioning
All endpoints are versioned: `/api/v1/...`

### Date Format
ISO 8601: `2024-11-14T10:30:00`

### Currency
All amounts in AZN (Azerbaijan Manat)

### Error Response Format
```json
{
  "status": 404,
  "message": "Transaction not found",
  "timestamp": "2024-11-14T10:30:00",
  "path": "/api/v1/transactions/999"
}
```

## 🎉 Success Criteria

- [x] API responds < 200ms for most endpoints
- [x] Complete Swagger documentation
- [x] 100+ realistic restaurant transactions
- [x] 7 employees with complete payroll data
- [x] All 60+ endpoints functional
- [x] CORS configured for iOS
- [x] Mock integrations (DVX, eGov, ASAN İmza)
- [x] Azerbaijan-specific tax calculations

## 📞 Support

For questions or issues, contact the EasyFin team during the hackathon.

---

**Built for IDDA Hackathon 2024**
*Empowering small business owners in Azerbaijan with smart financial tools*

