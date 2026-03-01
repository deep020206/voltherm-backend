# API Documentation for Voltherm Backend

Here's a comprehensive list of all available APIs with sample requests and responses:

---

## 1. Product APIs

### 1.1 Get All Products
**Endpoint:** `GET /api/products`  
**Authentication:** Not required  
**Request Type:** Simple GET request

**Sample Request:**
```bash
curl -X GET http://localhost:8080/api/products
```

**Sample Response:**
```json
{
  "success": true,
  "data": [
    {
      "productId": "prod-123",
      "productName": "Industrial Heater XL",
      "price": 15000.0,
      "featured": true,
      "available": true,
      "category": "Heaters",
      "subCategory": "Industrial",
      "specificationFields": ["Power", "Voltage", "Temperature Range"],
      "specificationValues": ["5kW", "380V", "0-500°C"],
      "quickSpecs": ["High efficiency", "Durable construction"],
      "imageUrl": "/images/prod-123-uuid.jpg",
      "pdfDownloadUrl": "/api/products/prod-123/pdf"
    }
  ]
}
```

---

### 1.2 Get Product by ID
**Endpoint:** `GET /api/products/{productId}`  
**Authentication:** Not required  
**Request Type:** Simple GET request

**Sample Request:**
```bash
curl -X GET http://localhost:8080/api/products/prod-123
```

**Sample Response:**
```json
{
  "success": true,
  "data": {
    "productId": "prod-123",
    "productName": "Industrial Heater XL",
    "price": 15000.0,
    "featured": true,
    "available": true,
    "category": "Heaters",
    "subCategory": "Industrial",
    "specificationFields": ["Power", "Voltage", "Temperature Range"],
    "specificationValues": ["5kW", "380V", "0-500°C"],
    "quickSpecs": ["High efficiency", "Durable construction"],
    "imageUrl": "/images/prod-123-uuid.jpg",
    "pdfDownloadUrl": "/api/products/prod-123/pdf",
    "productDescription": "High-performance industrial heater designed for heavy-duty applications."
  }
}
```

---

### 1.3 Get Featured Products
**Endpoint:** `GET /api/products/featured`  
**Authentication:** Not required  
**Request Type:** Simple GET request

**Sample Request:**
```bash
curl -X GET http://localhost:8080/api/products/featured
```

**Sample Response:**
```json
{
  "success": true,
  "data": [
    {
      "productId": "prod-123",
      "productName": "Industrial Heater XL",
      "featured": true,
      "available": true,
      "category": "Heaters",
      "imageUrl": "/images/prod-123-uuid.jpg",
      "productDescription": "High-performance industrial heater designed for heavy-duty applications."
    }
  ]
}
```

---

### 1.4 Download Product PDF
**Endpoint:** `GET /api/products/{productId}/pdf`  
**Authentication:** Not required  
**Request Type:** Simple GET request  
**Response Type:** Binary file (application/pdf)

**Sample Request:**
```bash
curl -X GET http://localhost:8080/api/products/prod-123/pdf --output product.pdf
```

**JavaScript Fetch Example:**
```javascript
fetch('http://localhost:8080/api/products/prod-123/pdf')
  .then(response => response.blob())
  .then(blob => {
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'product.pdf';
    a.click();
  });
```

---

### 1.5 Create Product (Admin Only)
**Endpoint:** `POST /api/products`  
**Authentication:** Required (Admin session)  
**Request Type:** Multipart form-data  
**Content-Type:** `multipart/form-data`

**Sample Request (using cURL):**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Cookie: JSESSIONID=your-session-id" \
  -F "product={\"productId\":\"prod-456\",\"productName\":\"New Heater\",\"price\":12000,\"featured\":true,\"available\":true,\"category\":\"Heaters\",\"subCategory\":\"Commercial\",\"specificationFields\":[\"Power\",\"Voltage\"],\"specificationValues\":[\"3kW\",\"220V\"],\"quickSpecs\":[\"Compact design\"],\"productDescription\":\"Compact commercial heater suitable for office and retail spaces.\"};type=application/json" \
  -F "image=@/path/to/image.jpg" \
  -F "pdf=@/path/to/datasheet.pdf"
```

**JavaScript Fetch Example:**
```javascript
const formData = new FormData();

const productData = {
  productId: "prod-456",
  productName: "New Heater",
  price: 12000,
  featured: true,
  available: true,
  category: "Heaters",
  subCategory: "Commercial",
  specificationFields: ["Power", "Voltage"],
  specificationValues: ["3kW", "220V"],
  quickSpecs: ["Compact design"],
  productDescription: "Compact commercial heater suitable for office and retail spaces."
};

formData.append('product', new Blob([JSON.stringify(productData)], { type: 'application/json' }));
formData.append('image', imageFile); // File object from input[type=file]
formData.append('pdf', pdfFile); // Optional

fetch('http://localhost:8080/api/products', {
  method: 'POST',
  credentials: 'include', // Important for session cookies
  body: formData
})
.then(response => response.json())
.then(data => console.log(data));
```

**Sample Response:**
```json
{
  "success": true,
  "data": {
    "productId": "prod-456",
    "productName": "New Heater",
    "price": 12000.0,
    "featured": true,
    "available": true,
    "imageUrl": "/images/prod-456-uuid.jpg",
    "pdfDownloadUrl": "/api/products/prod-456/pdf",
    "productDescription": "Compact commercial heater suitable for office and retail spaces."
  }
}
```

---

### 1.6 Update Product (Admin Only)
**Endpoint:** `PUT /api/products/{productId}`  
**Authentication:** Required (Admin session)  
**Request Type:** Multipart form-data

**Sample Request:**
```bash
curl -X PUT http://localhost:8080/api/products/prod-456 \
  -H "Cookie: JSESSIONID=your-session-id" \
  -F "product={\"productName\":\"Updated Heater\",\"price\":13000,\"productDescription\":\"Updated description for the heater.\"};type=application/json" \
  -F "image=@/path/to/new-image.jpg"
```

**JavaScript Example:**
```javascript
const formData = new FormData();
formData.append('product', new Blob([JSON.stringify({ productName: "Updated Heater", price: 13000, productDescription: "Updated description for the heater." })], { type: 'application/json' }));
formData.append('image', newImageFile); // Optional

fetch('http://localhost:8080/api/products/prod-456', {
  method: 'PUT',
  credentials: 'include',
  body: formData
});
```

**Sample Response:**
```json
{
  "success": true,
  "data": {
    "productId": "prod-456",
    "productName": "Updated Heater",
    "price": 13000.0,
    "imageUrl": "/images/prod-456-new-uuid.jpg",
    "productDescription": "Updated description for the heater."
  }
}
```

---

### 1.7 Delete Product (Admin Only)
**Endpoint:** `DELETE /api/products/{productId}`  
**Authentication:** Required (Admin session)  
**Request Type:** Simple DELETE request

**Sample Request:**
```bash
curl -X DELETE http://localhost:8080/api/products/prod-456 \
  -H "Cookie: JSESSIONID=your-session-id"
```

**Sample Response:**
```json
{
  "success": true,
  "data": "Product deleted"
}
```

---

## 2. Inquiry APIs

### 2.1 Create Inquiry (Public)
**Endpoint:** `POST /api/inquiries`  
**Authentication:** Not required  
**Request Type:** JSON in body  
**Content-Type:** `application/json`

**Sample Request:**
```bash
curl -X POST http://localhost:8080/api/inquiries \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "phoneNumber": "+91-9876543210",
    "company": "ABC Industries",
    "requirements": "Need industrial heaters for manufacturing unit",
    "interestedProducts": ["prod-123", "prod-456"]
  }'
```

**JavaScript Example:**
```javascript
fetch('http://localhost:8080/api/inquiries', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    name: "John Doe",
    email: "john@example.com",
    phoneNumber: "+91-9876543210",
    company: "ABC Industries",
    requirements: "Need industrial heaters for manufacturing unit",
    interestedProducts: ["prod-123", "prod-456"]
  })
})
.then(response => response.json())
.then(data => console.log(data));
```

**Sample Response:**
```json
{
  "success": true,
  "data": {
    "id": "inq-789",
    "createdAt": "2024-01-15T10:30:00Z",
    "name": "John Doe",
    "email": "john@example.com",
    "phoneNumber": "+91-9876543210",
    "company": "ABC Industries",
    "requirements": "Need industrial heaters for manufacturing unit",
    "interestedProducts": ["prod-123", "prod-456"],
    "status": "new"
  }
}
```

---

### 2.2 List All Inquiries (Admin Only)
**Endpoint:** `GET /api/inquiries`  
**Authentication:** Required (Admin session)  
**Request Type:** Simple GET request

**Sample Request:**
```bash
curl -X GET http://localhost:8080/api/inquiries \
  -H "Cookie: JSESSIONID=your-session-id"
```

**Sample Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": "inq-789",
      "createdAt": "2024-01-15T10:30:00Z",
      "name": "John Doe",
      "email": "john@example.com",
      "phoneNumber": "+91-9876543210",
      "company": "ABC Industries",
      "requirements": "Need industrial heaters",
      "status": "new"
    }
  ]
}
```

---

### 2.3 Update Inquiry Status (Admin Only)
**Endpoint:** `PATCH /api/inquiries/{id}/status`  
**Authentication:** Required (Admin session)  
**Request Type:** JSON in body  
**Content-Type:** `application/json`

**Sample Request:**
```bash
curl -X PATCH http://localhost:8080/api/inquiries/inq-789/status \
  -H "Cookie: JSESSIONID=your-session-id" \
  -H "Content-Type: application/json" \
  -d '{"status": "contacted"}'
```

**JavaScript Example:**
```javascript
fetch('http://localhost:8080/api/inquiries/inq-789/status', {
  method: 'PATCH',
  credentials: 'include',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({ status: "contacted" })
});
```

**Sample Response:**
```json
{
  "success": true,
  "data": {
    "id": "inq-789",
    "status": "contacted"
  }
}
```

---

### 2.4 Bulk Delete Inquiries (Admin Only)
**Endpoint:** `DELETE /api/inquiries`  
**Authentication:** Required (Admin session)  
**Request Type:** JSON in body  
**Content-Type:** `application/json`

**Sample Request:**
```bash
curl -X DELETE http://localhost:8080/api/inquiries \
  -H "Cookie: JSESSIONID=your-session-id" \
  -H "Content-Type: application/json" \
  -d '{"ids": ["inq-789", "inq-790", "inq-791"]}'
```

**JavaScript Example:**
```javascript
fetch('http://localhost:8080/api/inquiries', {
  method: 'DELETE',
  credentials: 'include',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({ ids: ["inq-789", "inq-790", "inq-791"] })
})
.then(response => response.json())
.then(data => console.log(data));
```

**Sample Response:**
```json
{
  "success": true,
  "data": "Inquiries deleted successfully"
}
```

---

## 3. Contact Info APIs

### 3.1 Get Contact Info
**Endpoint:** `GET /api/contact-info`  
**Authentication:** Not required  
**Request Type:** Simple GET request

**Sample Request:**
```bash
curl -X GET http://localhost:8080/api/contact-info
```

**Sample Response:**
```json
{
  "success": true,
  "data": {
    "id": "contact-1",
    "salesEmail": "sales@voltherm.com",
    "salesPhoneNumber": "+91-9876543210",
    "businessEmail": "business@voltherm.com",
    "supportPhoneNumber": "+91-9876543211",
    "mainAddress": "123 Industrial Area, City, State - 123456",
    "branches": [
      {
        "branchId": "branch-1",
        "branchName": "Mumbai Office",
        "addressLine1": "456 Business Park",
        "addressLine2": "Andheri East",
        "city": "Mumbai",
        "state": "Maharashtra",
        "pincode": 400069,
        "phoneNumber": "+91-9876543212",
        "mapUrl": "https://maps.google.com/..."
      }
    ],
    "facebookUrl": "https://facebook.com/voltherm",
    "xUrl": "https://x.com/voltherm",
    "instagramUrl": "https://instagram.com/voltherm",
    "linkedinUrl": "https://linkedin.com/company/voltherm",
    "indiamartUrl": "https://indiamart.com/voltherm"
  }
}
```

---

### 3.2 Update Contact Info (Admin Only)
**Endpoint:** `PUT /api/contact-info`  
**Authentication:** Required (Admin session)  
**Request Type:** JSON in body  
**Content-Type:** `application/json`

**Sample Request:**
```bash
curl -X PUT http://localhost:8080/api/contact-info \
  -H "Cookie: JSESSIONID=your-session-id" \
  -H "Content-Type: application/json" \
  -d '{
    "salesEmail": "newsales@voltherm.com",
    "salesPhoneNumber": "+91-9999999999"
  }'
```

**Sample Response:**
```json
{
  "success": true,
  "data": {
    "id": "contact-1",
    "salesEmail": "newsales@voltherm.com",
    "salesPhoneNumber": "+91-9999999999"
  }
}
```

---

### 3.3 Add Office/Branch (Admin Only)
**Endpoint:** `POST /api/contact-info/offices`  
**Authentication:** Required (Admin session)  
**Request Type:** JSON in body

**Sample Request:**
```bash
curl -X POST http://localhost:8080/api/contact-info/offices \
  -H "Cookie: JSESSIONID=your-session-id" \
  -H "Content-Type: application/json" \
  -d '{
    "branchName": "Delhi Office",
    "addressLine1": "789 Industrial Complex",
    "addressLine2": "Sector 18",
    "city": "Delhi",
    "state": "Delhi",
    "pincode": 110001,
    "phoneNumber": "+91-9876543213",
    "mapUrl": "https://maps.google.com/..."
  }'
```

**Sample Response:**
```json
{
  "success": true,
  "data": {
    "branchId": "branch-2",
    "branchName": "Delhi Office",
    "city": "Delhi",
    "state": "Delhi"
  }
}
```

---

### 3.4 Update Office/Branch (Admin Only)
**Endpoint:** `PUT /api/contact-info/offices/{branchId}`  
**Authentication:** Required (Admin session)  
**Request Type:** JSON in body

**Sample Request:**
```bash
curl -X PUT http://localhost:8080/api/contact-info/offices/branch-2 \
  -H "Cookie: JSESSIONID=your-session-id" \
  -H "Content-Type: application/json" \
  -d '{"phoneNumber": "+91-1111111111"}'
```

**Sample Response:**
```json
{
  "success": true,
  "data": {
    "branchId": "branch-2",
    "phoneNumber": "+91-1111111111"
  }
}
```

---

### 3.5 Delete Office/Branch (Admin Only)
**Endpoint:** `DELETE /api/contact-info/offices/{branchId}`  
**Authentication:** Required (Admin session)  
**Request Type:** Simple DELETE request

**Sample Request:**
```bash
curl -X DELETE http://localhost:8080/api/contact-info/offices/branch-2 \
  -H "Cookie: JSESSIONID=your-session-id"
```

**Sample Response:**
```json
{
  "success": true,
  "data": "Office deleted successfully"
}
```

---

## 4. Certificate APIs

### 4.1 List All Certificates
**Endpoint:** `GET /api/certificates`  
**Authentication:** Not required  
**Request Type:** Simple GET request

**Sample Request:**
```bash
curl -X GET http://localhost:8080/api/certificates
```

**Sample Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": "cert-1",
      "name": "ISO 9001:2015",
      "imageUrl": "/images/cert-1-uuid.jpg"
    }
  ]
}
```

---

### 4.2 Create Certificate (Admin Only)
**Endpoint:** `POST /api/certificates`  
**Authentication:** Required (Admin session)  
**Request Type:** Multipart form-data  
**Content-Type:** `multipart/form-data`

**Sample Request:**
```bash
curl -X POST http://localhost:8080/api/certificates \
  -H "Cookie: JSESSIONID=your-session-id" \
  -F "name=ISO 14001:2015" \
  -F "image=@/path/to/certificate-image.jpg"
```

**JavaScript Example:**
```javascript
const formData = new FormData();
formData.append('name', 'ISO 14001:2015');
formData.append('image', imageFile); // File object from input[type=file]

fetch('http://localhost:8080/api/certificates', {
  method: 'POST',
  credentials: 'include',
  body: formData
})
.then(response => response.json())
.then(data => console.log(data));
```

**Sample Response:**
```json
{
  "success": true,
  "data": {
    "id": "cert-2",
    "name": "ISO 14001:2015",
    "imageUrl": "/images/cert-2-uuid.jpg"
  }
}
```

---

### 4.3 Delete Certificate (Admin Only)
**Endpoint:** `DELETE /api/certificates/{id}`  
**Authentication:** Required (Admin session)

**Sample Request:**
```bash
curl -X DELETE http://localhost:8080/api/certificates/cert-2 \
  -H "Cookie: JSESSIONID=your-session-id"
```

**Sample Response:**
```json
{
  "success": true,
  "data": "Certificate deleted successfully"
}
```

---

## 5. Admin Authentication APIs

### 5.1 Admin Login
**Endpoint:** `POST /api/admin/login`  
**Authentication:** Not required  
**Request Type:** JSON in body  
**Content-Type:** `application/json`

**Sample Request:**
```bash
curl -X POST http://localhost:8080/api/admin/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }' \
  -c cookies.txt
```

**JavaScript Example:**
```javascript
fetch('http://localhost:8080/api/admin/login', {
  method: 'POST',
  credentials: 'include', // Important!
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    username: "admin",
    password: "admin123"
  })
})
.then(response => response.json())
.then(data => console.log(data));
```

**Sample Response:**
```json
{
  "success": true,
  "data": {
    "username": "admin",
    "message": "Login successful"
  }
}
```

**Note:** Session cookie (JSESSIONID) is set in response headers. Store this for subsequent requests.

---

### 5.2 Get Admin Profile
**Endpoint:** `GET /api/admin/profile`  
**Authentication:** Required (Admin session)  
**Request Type:** Simple GET request

**Sample Request:**
```bash
curl -X GET http://localhost:8080/api/admin/profile \
  -H "Cookie: JSESSIONID=your-session-id"
```

**Sample Response:**
```json
{
  "success": true,
  "data": {
    "username": "admin"
  }
}
```

---

### 5.3 Admin Logout
**Endpoint:** `POST /api/admin/logout`  
**Authentication:** Required (Admin session)  
**Request Type:** Simple POST request

**Sample Request:**
```bash
curl -X POST http://localhost:8080/api/admin/logout \
  -H "Cookie: JSESSIONID=your-session-id"
```

**Sample Response:**
```json
{
  "success": true,
  "data": "Logged out successfully"
}
```

---

## 6. Admin Settings APIs

### 6.1 Initiate Password Change
**Endpoint:** `POST /api/settings/password/initiate`  
**Authentication:** Required (Admin session)  
**Request Type:** JSON in body

**Sample Request:**
```bash
curl -X POST http://localhost:8080/api/settings/password/initiate \
  -H "Cookie: JSESSIONID=your-session-id" \
  -H "Content-Type: application/json" \
  -d '{
    "currentPassword": "admin123",
    "newPassword": "NewSecure@Pass123"
  }'
```

**Sample Response:**
```json
{
  "success": true,
  "data": "OTP has been sent to your email. It will expire in 5 minutes."
}
```

---

### 6.2 Verify OTP and Change Password
**Endpoint:** `POST /api/settings/password/verify`  
**Authentication:** Required (Admin session)  
**Request Type:** JSON in body

**Sample Request:**
```bash
curl -X POST http://localhost:8080/api/settings/password/verify \
  -H "Cookie: JSESSIONID=your-session-id" \
  -H "Content-Type: application/json" \
  -d '{
    "otp": "123456",
    "newPassword": "NewSecure@Pass123"
  }'
```

**Sample Response:**
```json
{
  "success": true,
  "data": "Password changed successfully"
}
```

---

### 6.3 Change Username
**Endpoint:** `POST /api/settings/username`  
**Authentication:** Required (Admin session)  
**Request Type:** JSON in body

**Sample Request:**
```bash
curl -X POST http://localhost:8080/api/settings/username \
  -H "Cookie: JSESSIONID=your-session-id" \
  -H "Content-Type: application/json" \
  -d '{
    "currentPassword": "admin123",
    "newUsername": "newadmin"
  }'
```

**Sample Response:**
```json
{
  "success": true,
  "data": "Username changed successfully"
}
```

---

### 6.4 Change Receiver Email
**Endpoint:** `POST /api/settings/email/receiver`  
**Authentication:** Required (Admin session)  
**Request Type:** JSON in body  
**Content-Type:** `application/json`

**Description:** Updates the company email address that receives inquiry notifications and OTP emails.

**Sample Request:**
```bash
curl -X POST http://localhost:8080/api/settings/email/receiver \
  -H "Cookie: JSESSIONID=your-session-id" \
  -H "Content-Type: application/json" \
  -d '{
    "receiverEmail": "newcompany@voltherm.com"
  }'
```

**JavaScript Example:**
```javascript
fetch('http://localhost:8080/api/settings/email/receiver', {
  method: 'POST',
  credentials: 'include',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    receiverEmail: "newcompany@voltherm.com"
  })
})
.then(response => response.json())
.then(data => console.log(data));
```

**Sample Response:**
```json
{
  "success": true,
  "data": "Receiver email updated successfully to: newcompany@voltherm.com"
}
```

**Validation Rules:**
- Email cannot be empty
- Must be a valid email format

---

### 6.5 Change Sender Email (with App Password)
**Endpoint:** `POST /api/settings/email/sender`  
**Authentication:** Required (Admin session)  
**Request Type:** JSON in body  
**Content-Type:** `application/json`

**Description:** Updates the SMTP sender email and app password used to send emails from the system. The new credentials are tested before being applied.

**Sample Request:**
```bash
curl -X POST http://localhost:8080/api/settings/email/sender \
  -H "Cookie: JSESSIONID=your-session-id" \
  -H "Content-Type: application/json" \
  -d '{
    "senderEmail": "newsender@gmail.com",
    "appPassword": "abcd efgh ijkl mnop"
  }'
```

**JavaScript Example:**
```javascript
fetch('http://localhost:8080/api/settings/email/sender', {
  method: 'POST',
  credentials: 'include',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    senderEmail: "newsender@gmail.com",
    appPassword: "abcd efgh ijkl mnop"
  })
})
.then(response => response.json())
.then(data => console.log(data));
```

**Sample Response:**
```json
{
  "success": true,
  "data": "Sender email and credentials updated successfully"
}
```

**Validation Rules:**
- Sender email cannot be empty
- App password is required and cannot be null/empty
- Must be a valid email format
- Credentials are tested against SMTP server before applying changes

**Error Response (Invalid Credentials):**
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Failed to authenticate with provided credentials. Please verify your email and app password.",
    "status": 400
  }
}
```

**Important Notes:**
- For Gmail accounts, you must use an "App Password" instead of your regular password
- To generate a Gmail App Password:
  1. Enable 2-factor authentication on your Google account
  2. Go to Google Account Settings → Security → 2-Step Verification → App passwords
  3. Generate a new app password for "Mail"
  4. Use this 16-character password in the request
- The system tests the credentials before applying them to ensure email functionality won't break
- Both sender email and app password are required; you cannot update one without the other

---

## Error Response Format

All APIs return errors in a consistent format:

```json
{
  "success": false,
  "error": {
    "code": "ERROR_CODE",
    "message": "Human readable error message",
    "status": 400
  }
}
```

**Common Error Codes:**
- `UNAUTHORIZED` (401) - Admin authentication required
- `NOT_FOUND` (404) - Resource not found
- `VALIDATION_ERROR` (400) - Invalid input data
- `BAD_REQUEST` (400) - General bad request
- `INTERNAL_ERROR` (500) - Server error

---

## Important Notes for Frontend Team

1. **Session Management:**
   - Always use `credentials: 'include'` in fetch requests
   - Store and send JSESSIONID cookie with admin requests
   - Session expires after 30 minutes of inactivity

2. **CORS:**
   - Backend is configured for `http://localhost:3000` in development
   - Production uses `https://voltherm.vercel.app`

3. **File Uploads:**
   - Product images: Max 5MB, formats: JPG, PNG
   - Product PDFs: Max 15MB, format: PDF
   - Use FormData for multipart requests

4. **Email Notifications:**
   - Inquiry submissions trigger automatic emails to company
   - OTP emails sent for password changes
   - Receiver email can be changed via Settings API
   - Sender email and credentials can be updated (credentials are validated before applying)

5. **Base URL:**
   - Development: `http://localhost:8080`
   - Production: Set via environment variable

6. **Password Requirements:**
   - Minimum 8 characters
   - Must contain uppercase, lowercase, digit, and special character
   - Special characters: `!@#$%^&*()_+-=[]{}|;:,.<>?`

7. **Inquiry Email Notification:**
   - Automatically sent to company email configured in application properties
   - Email includes formatted inquiry details with HTML styling

8. **Email Configuration:**
   - Receiver email receives all inquiry and OTP notifications
   - Sender email must be a valid SMTP account (Gmail App Password recommended)
   - App Password is required for sender email changes and cannot be null
   - System validates SMTP credentials before applying changes to prevent email service disruption
