# 🔧 ngrok CORS Fix - iOS Developer Guide

## ✅ UPDATE: Production API Now Available!

**Production URL:** `https://hackathon2-ibmt.onrender.com/api/v1`

**No special headers needed for production!** Just use:
```swift
let baseURL = "https://hackathon2-ibmt.onrender.com/api/v1"
```

---

## Problem (Only for ngrok testing)
When using ngrok free tier, you get a **"Network Failure"** or **CORS error** because ngrok shows a browser warning page before allowing API requests through.

## ✅ Solution (BACKEND - ALREADY FIXED)
The backend CORS configuration has been updated to allow the ngrok bypass header.

## 📱 iOS Developer Action Required

### Add Header to ALL API Requests

You need to add this header to bypass ngrok's browser warning page:

```swift
"ngrok-skip-browser-warning": "true"
```

### Implementation Examples

#### Option 1: URLSession (Basic)
```swift
var request = URLRequest(url: url)
request.setValue("true", forHTTPHeaderField: "ngrok-skip-browser-warning")
request.setValue("application/json", forHTTPHeaderField: "Content-Type")

let task = URLSession.shared.dataTask(with: request) { data, response, error in
    // Handle response
}
task.resume()
```

#### Option 2: Alamofire
```swift
let headers: HTTPHeaders = [
    "ngrok-skip-browser-warning": "true",
    "Content-Type": "application/json"
]

AF.request("https://stevie-superdivine-unpompously.ngrok-free.dev/api/v1/dashboard/summary",
           headers: headers)
    .responseDecodable(of: DashboardResponse.self) { response in
        // Handle response
    }
```

#### Option 3: Create a Base Network Manager
```swift
class APIManager {
    static let shared = APIManager()
    let baseURL = "https://stevie-superdivine-unpompously.ngrok-free.dev/api/v1"
    
    private var defaultHeaders: [String: String] {
        return [
            "ngrok-skip-browser-warning": "true",
            "Content-Type": "application/json",
            "Accept": "application/json"
        ]
    }
    
    func request<T: Decodable>(
        endpoint: String,
        method: String = "GET",
        body: Data? = nil,
        completion: @escaping (Result<T, Error>) -> Void
    ) {
        guard let url = URL(string: baseURL + endpoint) else { return }
        
        var request = URLRequest(url: url)
        request.httpMethod = method
        request.httpBody = body
        
        // Add default headers
        defaultHeaders.forEach { key, value in
            request.setValue(value, forHTTPHeaderField: key)
        }
        
        URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                completion(.failure(error))
                return
            }
            
            guard let data = data else {
                completion(.failure(NSError(domain: "", code: -1)))
                return
            }
            
            do {
                let decoder = JSONDecoder()
                let result = try decoder.decode(T.self, from: data)
                completion(.success(result))
            } catch {
                completion(.failure(error))
            }
        }.resume()
    }
}

// Usage:
APIManager.shared.request(endpoint: "/dashboard/summary") { (result: Result<DashboardResponse, Error>) in
    switch result {
    case .success(let dashboard):
        print("Dashboard loaded: \(dashboard.fullName)")
    case .failure(let error):
        print("Error: \(error)")
    }
}
```

## 🧪 Testing

### Test with curl (from terminal):
```bash
curl -X GET \
  'https://stevie-superdivine-unpompously.ngrok-free.dev/api/v1/dashboard/summary' \
  -H 'ngrok-skip-browser-warning: true' \
  -H 'Content-Type: application/json'
```

### Expected Response:
```json
{
  "firstName": "Rail",
  "lastName": "Nuriyev",
  "fullName": "Rail Nuriyev",
  "totalBalance": 45300.00,
  ...
}
```

## ⚠️ Important Notes

1. **This header is REQUIRED for all requests** when using ngrok free tier
2. **Add it to EVERY API call** in your app
3. **Test immediately** after adding the header
4. If still not working, check:
   - Is ngrok tunnel running? Check your ngrok URL is correct
   - Are you using the correct base URL?
   - Is the backend server running?

## 🔍 Debugging

### If you still get errors:

1. **Check ngrok status:**
   - Visit the ngrok URL in Safari browser
   - You should NOT see a warning page
   - If you see a warning page, the header isn't being sent

2. **Check response in Xcode:**
   ```swift
   if let httpResponse = response as? HTTPURLResponse {
       print("Status Code: \(httpResponse.statusCode)")
       print("Headers: \(httpResponse.allHeaderFields)")
   }
   ```

3. **Check network logs:**
   - Open Xcode -> Debug -> View Debug Area
   - Look for response status codes
   - 200 = Success ✅
   - 0/Failed = Network/CORS issue ❌

## 🎯 Quick Checklist

- [ ] Add `ngrok-skip-browser-warning: true` header to all requests
- [ ] Test with curl command
- [ ] Test from iOS app
- [ ] Verify you can fetch dashboard summary
- [ ] Update all API calls to include the header

## 📞 Support

If you still have issues after following this guide:
1. Share the exact error message
2. Share the request code
3. Share the response (if any)

**Happy Coding! 🚀**

