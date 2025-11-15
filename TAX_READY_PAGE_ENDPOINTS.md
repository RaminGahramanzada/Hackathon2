# 📊 Tax Ready Page - Endpoints

## ✅ Main Endpoint for Tax Ready Page

### 📍 Get Tax Ready Data (Complete Page Data)
```
GET https://hackathon2-ibmt.onrender.com/api/v1/tax/ready
```

**Description:** Returns all data for Tax Ready page - total deductible, category breakdown, transactions, and recommendations

---

## 📊 Response Format:

```json
{
  "year": 2024,
  "totalDeductible": 12847.50,
  "lastMonthChange": 2340.00,
  
  "categoryBreakdown": {
    "Business Expenses": 8425.00,
    "Office Supplies": 4422.50
  },
  
  "recommendation": {
    "type": "SIMPLIFIED_TAXATION",
    "title": "Simplified Taxation recommended",
    "message": "Based on your expense profile, simplified taxation (sadələşdirilmiş vergi) could save you more",
    "actionUrl": "/recommendations"
  },
  
  "recentTransactions": [
    {
      "id": 1,
      "merchantName": "Office Equipment Store",
      "description": "Laptop and accessories",
      "amount": 1249.99,
      "date": "2024-12-15",
      "category": "OFFICE_SUPPLIES",
      "isDeductible": true
    },
    {
      "id": 2,
      "merchantName": "Internet Provider",
      "description": "Business internet service",
      "amount": 89.99,
      "date": "2024-12-12",
      "category": "TELECOMMUNICATIONS",
      "isDeductible": true
    },
    // ... more transactions
  ]
}
```

---

## 📱 iOS Implementation:

### Swift Model:
```swift
struct TaxReadyData: Codable {
    let year: Int
    let totalDeductible: Double           // For main card: "$12,847.50"
    let lastMonthChange: Double           // For "+$2,340 from last month"
    let categoryBreakdown: [String: Double]  // For category cards
    let recommendation: TaxRecommendation
    let recentTransactions: [DeductibleTransaction]
}

struct TaxRecommendation: Codable {
    let type: String
    let title: String
    let message: String
    let actionUrl: String
}

struct DeductibleTransaction: Codable {
    let id: Int
    let merchantName: String
    let description: String
    let amount: Double
    let date: String
    let category: String
    let isDeductible: Bool
}
```

### Usage Example:
```swift
func loadTaxReadyData() {
    let url = URL(string: "https://hackathon2-ibmt.onrender.com/api/v1/tax/ready")!
    
    URLSession.shared.dataTask(with: url) { data, response, error in
        guard let data = data else { return }
        
        do {
            let taxData = try JSONDecoder().decode(TaxReadyData.self, from: data)
            
            DispatchQueue.main.async {
                // Update UI
                self.totalDeductible = taxData.totalDeductible
                self.lastMonthChange = taxData.lastMonthChange
                self.categories = taxData.categoryBreakdown
                self.transactions = taxData.recentTransactions
                self.recommendation = taxData.recommendation
            }
        } catch {
            print("Error: \(error)")
        }
    }.resume()
}
```

---

## 🎨 UI Mapping:

| UI Element | Data Field |
|------------|-----------|
| **Total Deductible Card** | `totalDeductible` |
| **"from last month" text** | `lastMonthChange` |
| **Business Expenses Card** | `categoryBreakdown["Business Expenses"]` |
| **Office Supplies Card** | `categoryBreakdown["Office Supplies"]` |
| **Recommendation Banner** | `recommendation` object |
| **Transaction List** | `recentTransactions` array |

---

## 🔎 Additional Endpoints (If Needed):

### Get All Transactions (with filters)
```
GET /api/v1/transactions?startDate=2024-01-01&endDate=2024-12-31
```

### Get Only Deductible Transactions
```swift
// Use the main /tax/ready endpoint - it already filters deductibles
```

### Get Tax Summary
```
GET /api/v1/tax/summary
```

### Get Recommendations
```
GET /api/v1/recommendations/tax-savings
```

---

## 📝 Filter Implementation (Tabs):

The UI shows 3 tabs: **All**, **Deductible**, **Taxable**

### For "All" Tab:
```
GET /api/v1/transactions
```

### For "Deductible" Tab:
```
GET /api/v1/tax/ready
```
Then use `recentTransactions` (already filtered)

### For "Taxable" Tab:
Filter client-side or use:
```
GET /api/v1/transactions
```
Then filter where `isDeductible == false`

---

## ⚠️ IMPORTANT: Deployment

**Manual deploy required on Render:**

1. **https://dashboard.render.com**
2. **"Hackathon2"** → **"Manual Deploy"**
3. **"Clear build cache & deploy"** ✅
4. **Wait 10 minutes**

---

## 🧪 Testing:

After deployment, test:
```bash
curl https://hackathon2-ibmt.onrender.com/api/v1/tax/ready
```

Expected response:
- ✅ `year`: 2024
- ✅ `totalDeductible`: number
- ✅ `categoryBreakdown`: object with categories
- ✅ `recentTransactions`: array of transactions
- ✅ `recommendation`: object

---

## 📊 Complete Endpoints Summary for Tax Ready Page:

### Primary (Use This):
```
GET /api/v1/tax/ready
```
**Returns:** Everything you need for the page!

### Secondary (Optional):
```
GET /api/v1/transactions          # For "All" tab
GET /api/v1/tax/summary           # For detailed tax info
GET /api/v1/recommendations       # For more recommendations
```

---

## 🎯 Summary:

**One endpoint for entire Tax Ready page:**
- ✅ Total Deductible
- ✅ Last Month Change
- ✅ Category Breakdown
- ✅ Recommendation Banner
- ✅ Recent Deductible Transactions List

**Endpoint:** `/api/v1/tax/ready` ✅

---

**Deploy on Render and test in ~10 minutes!** 🚀

