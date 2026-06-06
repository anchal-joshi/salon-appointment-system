# Beauty Salon Appointment Management System

## Overview

A RESTful backend API for a beauty salon built with **Spring Boot 3.x** and secured with **Spring Security 6 + JWT**. The system supports three roles — Customer, Staff, and Admin — and handles user authentication, service catalog management, staff scheduling, and leave management. Built as a solo portfolio project with a clean layered architecture and fully tested REST endpoints.

---

## Features

- JWT authentication with access token (15 min) + refresh token (7 days) rotation
- Role-based access control for `CUSTOMER`, `STAFF`, and `ADMIN` using `@PreAuthorize`
- Refresh token revocation on logout and on every new login
- Two-tier service catalog — Category → Service with `NORMAL` / `BRIDAL` type filtering
- Admin can create, update, and toggle services and categories
- Staff weekly schedule management (per day working hours)
- Staff leave system with conflict detection and ownership-based cancellation
- Soft delete (deactivate/reactivate) for users
- Centralized exception handling with structured JSON error responses
- Request validation using Jakarta Bean Validation

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.3.x |
| Security | Spring Security 6 + JWT (jjwt 0.12.6) |
| Database | MySQL 8.x |
| ORM | Spring Data JPA + Hibernate |
| Validation | Jakarta Bean Validation |
| Build Tool | Maven |
| API Testing | Postman |

---

## Database Design
users
id, name, email (unique), phone, password, role (CUSTOMER/STAFF/ADMIN),
is_active, created_at, updated_at
refresh_tokens
id, token (unique), user_id (FK → users), expiry_date, is_revoked, created_at
service_categories
id, name (unique), description, service_type (NORMAL/BRIDAL),
is_active, created_at, updated_at
salon_services
id, category_id (FK → service_categories), name, description,
price, duration_minutes, is_active, created_at, updated_at
staff_schedules
id, staff_id (FK → users), day_of_week, start_time, end_time, is_working
UNIQUE (staff_id, day_of_week)
staff_leaves
id, staff_id (FK → users), leave_date, reason, created_at
UNIQUE (staff_id, leave_date)

---

---

## API Endpoints

### Auth
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/auth/register` | Public | Register as customer |
| POST | `/api/auth/login` | Public | Login and receive tokens |
| POST | `/api/auth/refresh` | Public | Refresh access token |
| POST | `/api/auth/logout` | Bearer | Revoke refresh tokens |

### User — Self
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/users/me` | Any role | Get own profile |
| PUT | `/api/users/me` | Any role | Update name and phone |
| PATCH | `/api/users/me/change-password` | Any role | Change password |

### Admin — User Management
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/admin/users/customers` | Admin | List all customers |
| GET | `/api/admin/users/staff` | Admin | List all staff |
| GET | `/api/admin/users/{id}` | Admin | Get user by ID |
| POST | `/api/admin/users/staff` | Admin | Create staff account |
| PATCH | `/api/admin/users/{id}/deactivate` | Admin | Deactivate user |
| PATCH | `/api/admin/users/{id}/reactivate` | Admin | Reactivate user |
| PATCH | `/api/admin/users/{id}/role?newRole= ` | Admin | Change user role |

### Service Catalog — Public
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/services` | Public | All active services |
| GET | `/api/services/{id}` | Public | Single service |
| GET | `/api/services/categories` | Public | All active categories |
| GET | `/api/services/categories?type=NORMAL` | Public | Filter by type |
| GET | `/api/services/categories/{id}` | Public | Category with its services |
| GET | `/api/services/categories/{id}/services` | Public | Services by category |

### Admin — Service Catalog
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/admin/services/categories` | Admin | Create category |
| PUT | `/api/admin/services/categories/{id}` | Admin | Update category |
| PATCH | `/api/admin/services/categories/{id}/toggle` | Admin | Toggle active status |
| POST | `/api/admin/services` | Admin | Create service |
| PUT | `/api/admin/services/{id}` | Admin | Update service |
| PATCH | `/api/admin/services/{id}/toggle` | Admin | Toggle active status |

### Staff — Schedule & Leave
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/staff/schedule` | Staff | View own schedule |
| GET | `/api/staff/leaves` | Staff | View upcoming leaves |
| POST | `/api/staff/leaves` | Staff | Apply for leave |
| DELETE | `/api/staff/leaves/{id}` | Staff | Cancel a leave |

### Admin — Staff Schedule & Leave
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/admin/staff/{id}/schedule` | Admin | Set or update staff schedule |
| GET | `/api/admin/staff/{id}/schedule` | Admin | Get schedule of a staff |
| GET | `/api/admin/staff/{id}/leaves` | Admin | All leaves of a staff |
| GET | `/api/admin/staff/leaves?date=YYYY-MM-DD` | Admin | All leaves on a date |
| DELETE | `/api/admin/staff/leaves/{id}` | Admin | Delete a leave |

---

## Installation

### Prerequisites
- Java 21
- MySQL 8.x
- Maven

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/yourusername/salon-appointment.git
cd salon-appointment
```

**2. Create the MySQL database**
```sql
CREATE DATABASE salon_db;
CREATE USER 'salon_user'@'localhost' IDENTIFIED BY 'salon123';
GRANT ALL PRIVILEGES ON salon_db.* TO 'salon_user'@'localhost';
FLUSH PRIVILEGES;
```

**3. Configure `src/main/resources/application.properties`**
```properties
spring.application.name=appointment

spring.datasource.url=jdbc:mysql://localhost:3306/salon_db
spring.datasource.username=salon_user
spring.datasource.password=salon123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

app.jwt.secret=3f8a2b1c9d4e7f6a5b0c3d2e1f8a9b4c7d6e5f0a1b2c3d4e5f6a7b8c9d0e1f2a
app.jwt.access-token-expiration=900000
app.jwt.refresh-token-expiration=604800000

server.port=8080
```

**4. Run the application**
```bash
mvn spring-boot:run
```

Tables are created automatically on first run.

**5. Create the first Admin account**

Register through the API:
```json
POST /api/auth/register
{
  "name": "Admin",
  "email": "admin@salon.com",
  "phone": "9999999999",
  "password": "password123"
}
```
Then promote via SQL:
```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'admin@salon.com';
```

---

## Sample API Requests

### Register
```json
POST /api/auth/register
{
  "name": "Priya Sharma",
  "email": "priya@gmail.com",
  "phone": "9876543210",
  "password": "priya1234"
}
```

### Login
```json
POST /api/auth/login
{
  "email": "priya@gmail.com",
  "password": "priya1234"
}
```
Response:
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "d2e1f8a9-b4c7-d6e5...",
    "tokenType": "Bearer",
    "name": "Priya Sharma",
    "email": "priya@gmail.com",
    "role": "CUSTOMER"
  }
}
```

### Create Service Category (Admin)
```json
POST /api/admin/services/categories
Authorization: Bearer <admin_token>
{
  "name": "Hair Services",
  "description": "All hair related services",
  "serviceType": "NORMAL"
}
```

### Create Service (Admin)
```json
POST /api/admin/services
Authorization: Bearer <admin_token>
{
  "categoryId": 1,
  "name": "Hair Cutting and Trimming",
  "description": "Professional hair cut and trim",
  "price": 299.00,
  "durationMinutes": 45
}
```

### Set Staff Schedule (Admin)
```json
POST /api/admin/staff/2/schedule
Authorization: Bearer <admin_token>
{
  "dayOfWeek": "MONDAY",
  "startTime": "10:00:00",
  "endTime": "19:00:00",
  "isWorking": true
}
```

### Apply for Leave (Staff)
```json
POST /api/staff/leaves
Authorization: Bearer <staff_token>
{
  "leaveDate": "2026-06-20",
  "reason": "Personal work"
}
```

### Refresh Token
```json
POST /api/auth/refresh
{
  "refreshToken": "d2e1f8a9-b4c7-d6e5..."
}
```

---

## Future Improvements

- **Appointment Booking** — Full booking lifecycle `PENDING → CONFIRMED → COMPLETED / CANCELLED` with double-booking prevention
- **Slot Availability Engine** — Auto-generate available time slots based on staff schedule, leaves, and existing bookings
- **Bridal Packages** — Premium multi-session bookings tied to wedding event types (Engagement, Mehendi, Reception, Wedding Day)
- **Notification System** — Internal notifications for booking confirmations, cancellations, and reminders
- **Reviews & Ratings** — Customers can rate and review completed appointments
- **Admin Dashboard** — Revenue summary, staff utilization, most booked services, and cancellation rate
- **Email / SMS Integration** — Real delivery via SendGrid or Twilio
- **Payment Gateway** — Online payments via Razorpay or Stripe

---

## Author

**Anchal Joshi**
**LinkedIn:** www.linkedin.com/in/anchal-joshi-617516398
