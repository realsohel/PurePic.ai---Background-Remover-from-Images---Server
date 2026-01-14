# Pure Pic.ai – AI Background Image Remover (Backend)

**Motto**: “Pure Images, Zero Distractions. Secure. Scalable. AI-Powered Image Processing Backend.” 

---

## Overview

Pure Pic.ai Backend is a **Spring Boot–based REST API** that powers the Pure Pic.ai platform.  
It handles **authentication, user management, credit tracking, AI background removal, and secure payments**.

---
## LLD with Flowchart
![LLD of Pure Pic.ai](https://drive.google.com/uc?export=view&id=1YMXAHt1QHZiRr4FueBMMgJI3OIrearE7)


---

## Backend Integrations

- **Clerk Authentication** (JWT + Webhooks)
- **ClipDrop AI API**
- **Razorpay Payment Gateway**
- **MySQL Database**

---

## Tech Stack

- **Java**
- **Spring Boot**
- **Spring Security**
- **Spring Data JPA (Hibernate)**
- **OpenFeign**
- **JWT (RS256)**
- **Clerk**
- **Razorpay**
- **ClipDrop API**
- **MySQL**

---

## System Architecture

Client (Frontend)  
↓  
Spring Controllers  
↓  
Service Layer  
↓  
Repository Layer  
↓  
MySQL Database  

External Services:
- Clerk (Authentication & Webhooks)
- Razorpay (Payments)
- ClipDrop (AI Background Removal)

---

## Database Entities

### User Entity
Stores authenticated users synced from Clerk.

- `clerkId`
- `username`
- `email`
- `firstName`
- `lastName`
- `photoUrl`
- `credits`

---

### Order Entity
Stores payment and credit purchase details.

- `orderId`
- `clerkId`
- `plan`
- `credits`
- `amount`
- `paymentStatus`

---

## Data Transfer Objects (DTOs)

### UserDto
Used for transferring user data between layers.

### RazorpayOrderDto
Used to send Razorpay order details to the frontend.

---

## Service Layer

### UserService
- Create or update user
- Fetch user by Clerk ID
- Delete user
- Manage user credits

---

### RemoveBackgroundService
- Uses **OpenFeign**
- Sends image to **ClipDrop API**
- Returns background-removed image bytes

---

### OrderService

Handles credit purchase logic using predefined plans.

**Available Plans**

| Plan | Credits | Price |
|------|--------|-------|
| Basic | 100 | ₹499 |
| Premium | 300 | ₹899 |
| Ultimate | 1000 | ₹1499 |

---

### RazorpayService
- Create Razorpay orders
- Verify payment status
- Add credits after successful payment
- Prevent duplicate credit allocation

---

## Authentication & Security

### Clerk JWT Authentication
- RS256 token verification
- JWKS public key fetching
- Cached keys for performance

---

### Spring Security Configuration
- Stateless authentication
- Custom JWT authentication filter
- Webhook endpoints excluded from authentication
- CORS enabled for frontend domains

---

## Clerk Webhooks Integration

**Endpoint**
- POST /api/v1/webhooks/clerkwebhook

**Handled Events**
- `user.created`
- `user.updated`
- `user.deleted`

Keeps backend users synchronized with Clerk.

---

## REST API Endpoints

### User APIs
- `POST /api/v1/users/createupdateuser`
- `GET /api/v1/users/credits`

---

### Image APIs
- `POST /api/v1/images/remove-background`

Consumes image, checks credits, removes background, returns Base64 image.

---

### Order APIs
- `POST /api/v1/orders/create-order`
- `POST /api/v1/orders/verify-order`

Handles Razorpay order creation and payment verification.

---

## Key Features
- Secure Clerk-based authentication
- Credit-based usage system
- Razorpay payment integration
- AI-powered background removal
- Webhook-driven user synchronization
- Production-ready Spring Security setup

---

## Error Handling

- Invalid or expired JWT tokens
- Insufficient credits validation
- Razorpay payment failures
- File upload size handling
- Clean and consistent API responses

---

## Conclusion

Pure Pic.ai Backend is a **robust, secure, and scalable Spring Boot application** built for real-world production usage.  
It seamlessly integrates **authentication, payments, AI processing, and data persistence**, forming a solid foundation for the Pure Pic.ai platform.
