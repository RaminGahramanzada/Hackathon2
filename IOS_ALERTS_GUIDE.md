# 🚨 Alert System - iOS Developer Guide

## Overview
The alert system provides real-time notifications for important events like tax deadlines, low balance warnings, unusual spending, and unauthorized payments.

---

## 📱 iOS Home Screen Alert Banner

### Endpoint for Tax Deadline Banner:
```
GET /api/v1/alerts/tax-deadlines
```

This is specifically for the **"Tax Deadline Approaching - VAT Payment in 5 days"** banner on the home screen.

### Response:
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
    "createdAt": "2024-11-14T10:00:00",
    "deadlineDate": "2024-11-19",
    "daysRemaining": 5,
    "actionUrl": "/taxes/declarations"
  }
]
```

### iOS Model:
```swift
struct AlertResponse: Codable {
    let id: Int
    let alertType: String          // "TAX_DEADLINE", "LOW_BALANCE", etc.
    let title: String              // "Tax Deadline Approaching"
    let message: String            // "VAT Payment in 5 days"
    let severity: String           // "low", "medium", "high", "critical"
    let isRead: Bool
    let isDismissed: Bool
    let createdAt: String          // ISO 8601 format
    
    // Tax deadline specific fields
    let deadlineDate: String?      // "2024-11-19" (only for TAX_DEADLINE alerts)
    let daysRemaining: Int?        // 5 (only for TAX_DEADLINE alerts)
    let actionUrl: String?         // "/taxes/declarations" (deep link)
}
```

### iOS Usage Example:
```swift
// Fetch tax deadline alerts for banner
func fetchTaxDeadlineAlert() {
    let url = URL(string: "\(baseURL)/api/v1/alerts/tax-deadlines")!
    
    URLSession.shared.dataTask(with: url) { data, response, error in
        guard let data = data else { return }
        
        let alerts = try? JSONDecoder().decode([AlertResponse].self, from: data)
        
        // Show the first urgent alert in the banner
        if let urgentAlert = alerts?.first(where: { $0.severity == "high" || $0.severity == "critical" }) {
            DispatchQueue.main.async {
                self.showAlertBanner(
                    title: urgentAlert.title,
                    message: urgentAlert.message,
                    daysRemaining: urgentAlert.daysRemaining
                )
            }
        }
    }.resume()
}
```

---

## 📋 All Alerts Endpoint

### Get All Active Alerts:
```
GET /api/v1/alerts
```

Returns all active (non-dismissed) alerts including tax deadlines, low balance, unusual spending, etc.

### Response:
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
    "createdAt": "2024-11-14T09:00:00",
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
    "createdAt": "2024-11-14T10:00:00",
    "deadlineDate": "2024-11-19",
    "daysRemaining": 5,
    "actionUrl": "/taxes/declarations"
  },
  {
    "id": 3,
    "alertType": "UNUSUAL_SPENDING",
    "title": "Unusual Spending Detected",
    "message": "Food supply costs increased by 25% this month.",
    "severity": "medium",
    "isRead": false,
    "isDismissed": false,
    "createdAt": "2024-11-14T11:00:00",
    "deadlineDate": null,
    "daysRemaining": null,
    "actionUrl": null
  }
]
```

---

## 🔔 Alert Types

| Alert Type | Description | Has Deadline Info |
|------------|-------------|-------------------|
| `TAX_DEADLINE` | Tax payment or declaration due | ✅ Yes |
| `LOW_BALANCE` | Account balance running low | ❌ No |
| `UNUSUAL_SPENDING` | Spending pattern anomaly detected | ❌ No |
| `UNAUTHORIZED_PAYMENT` | Suspicious transaction detected | ❌ No |
| `RECOMMENDATION` | Actionable recommendation available | ❌ No |

---

## 🎨 Severity Levels

Use these to determine colors and urgency:

| Severity | Color Suggestion | Use Case |
|----------|------------------|----------|
| `low` | 🔵 Blue | Informational alerts |
| `medium` | 🟡 Yellow | Needs attention |
| `high` | 🟠 Orange | Urgent, act soon |
| `critical` | 🔴 Red | Immediate action required |

---

## ✅ Dismiss an Alert

### Endpoint:
```
POST /api/v1/alerts/{id}/dismiss
```

### Example:
```
POST /api/v1/alerts/2/dismiss
```

### Response:
```json
"Alert dismissed successfully"
```

### iOS Usage:
```swift
func dismissAlert(alertId: Int) {
    let url = URL(string: "\(baseURL)/api/v1/alerts/\(alertId)/dismiss")!
    var request = URLRequest(url: url)
    request.httpMethod = "POST"
    
    URLSession.shared.dataTask(with: request) { data, response, error in
        if let httpResponse = response as? HTTPURLResponse,
           httpResponse.statusCode == 200 {
            print("Alert dismissed")
            // Refresh alerts list
            self.fetchAlerts()
        }
    }.resume()
}
```

---

## 🔍 Filtered Alert Endpoints

### Get Only Tax Deadlines:
```
GET /api/v1/alerts/tax-deadlines
```

### Get Only Low Balance Alerts:
```
GET /api/v1/alerts/low-balance
```

### Get Only Unusual Spending:
```
GET /api/v1/alerts/unusual-spending
```

### Get Only Unauthorized Payments:
```
GET /api/v1/alerts/unauthorized-payments
```

---

## 🏠 Displaying Alert Banner on Home Screen

### Step 1: Fetch Alert
Call `/api/v1/alerts/tax-deadlines` when home screen loads.

### Step 2: Check for Urgent Alerts
Filter alerts with `severity == "high"` or `"critical"` and `daysRemaining <= 7`.

### Step 3: Display Banner
Show the banner with:
- **Title**: `alert.title` → "Tax Deadline Approaching"
- **Message**: `alert.message` → "VAT Payment in 5 days"
- **Icon**: Mail/envelope icon (as shown in your design)
- **Action**: Tap to navigate to `alert.actionUrl` → `/taxes/declarations`

### Step 4: Handle Dismissal
When user dismisses, call `POST /api/v1/alerts/{id}/dismiss`.

---

## 📅 Example SwiftUI View

```swift
struct AlertBannerView: View {
    let alert: AlertResponse
    let onDismiss: () -> Void
    
    var body: some View {
        HStack {
            Image(systemName: "envelope.fill")
                .font(.title2)
                .foregroundColor(.white)
            
            VStack(alignment: .leading, spacing: 4) {
                Text(alert.title)
                    .font(.headline)
                    .foregroundColor(.white)
                
                Text(alert.message)
                    .font(.subheadline)
                    .foregroundColor(.white.opacity(0.9))
            }
            
            Spacer()
            
            Image(systemName: "chevron.right")
                .foregroundColor(.white)
        }
        .padding()
        .background(Color.black)
        .cornerRadius(16)
        .padding(.horizontal)
        .onTapGesture {
            // Navigate to alert.actionUrl
            navigateToTaxes()
        }
    }
}
```

---

## 🧪 Test the API

### Using Swagger UI:
```
http://localhost:8080/swagger-ui.html
```

Navigate to **Alerts** section and try:
1. `GET /api/v1/alerts` - Get all alerts
2. `GET /api/v1/alerts/tax-deadlines` - Get tax deadline alerts
3. `POST /api/v1/alerts/{id}/dismiss` - Dismiss an alert

---

## 🔗 Related Endpoints

| Feature | Endpoint |
|---------|----------|
| Dashboard (shows alert count) | `GET /api/v1/dashboard/summary` |
| Recommendations | `GET /api/v1/recommendations` |
| Tax Summary | `GET /api/v1/tax/summary` |
| Tax Deadlines Detail | `GET /api/v1/tax/deadlines` |

---

## ⚠️ Important Notes

1. **Polling Frequency**: 
   - Poll alerts every 5-10 minutes when app is active
   - Use push notifications for real-time updates (requires backend setup)

2. **Caching**:
   - Cache alerts locally to show even when offline
   - Refresh on app launch and periodically

3. **Error Handling**:
   - Handle network errors gracefully
   - Show cached alerts if API fails

4. **Localization**:
   - Alert messages are in English/Azerbaijani
   - Consider translating on client side if needed

---

## 📱 Integration Checklist

- [ ] Fetch tax deadline alerts on home screen load
- [ ] Display banner for urgent alerts (severity: high/critical)
- [ ] Show days remaining in the banner
- [ ] Handle tap to navigate to taxes section
- [ ] Implement dismiss functionality
- [ ] Show all alerts in a dedicated alerts/notifications screen
- [ ] Use severity colors for visual distinction
- [ ] Cache alerts for offline viewing
- [ ] Implement pull-to-refresh for alerts
- [ ] Show badge count on alerts tab (use `unreadAlerts` from dashboard)

---

**Ready to integrate! 🚀**

For questions, check Swagger UI or test endpoints directly in the browser.

