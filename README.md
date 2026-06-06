# salon-appointment-system
Beauty Salon Appointment Management System
#Overview
A RESTful backend API for a beauty salon built with Spring Boot 3.x and secured with Spring Security 6 + JWT. The system supports three roles — Customer, Staff, and Admin — and handles user authentication, service catalog management, staff scheduling, and leave management. Built as a solo portfolio project with a clean layered architecture and fully tested REST endpoints.

#Features

JWT authentication with access token (15 min) + refresh token (7 days) rotation
Role-based access control for CUSTOMER, STAFF, and ADMIN using @PreAuthorize
Refresh token revocation on logout and on every new login
Two-tier service catalog — Category → Service with NORMAL / BRIDAL type filtering
Admin can create, update, and toggle services and categories
Staff weekly schedule management (per day working hours)
Staff leave system with conflict detection and ownership-based cancellation
Soft delete (deactivate/reactivate) for users
Centralized exception handling with structured JSON error responses
Request validation using Jakarta Bean Validation


#Tech Stack
LayerTechnologyLanguageJava 21FrameworkSpring Boot 3.3.xSecuritySpring Security 6 + JWT (jjwt 0.12.6)DatabaseMySQL 8.xORMSpring Data JPA + HibernateValidationJakarta Bean ValidationBuild ToolMavenAPI TestingPostman

#Database Design
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

#API Endpoints
Auth
MethodEndpointAccessDescriptionPOST/api/auth/registerPublicRegister as customerPOST/api/auth/loginPublicLogin and receive tokensPOST/api/auth/refreshPublicRefresh access tokenPOST/api/auth/logoutBearerRevoke refresh tokens
User — Self
MethodEndpointAccessDescriptionGET/api/users/meAny roleGet own profilePUT/api/users/meAny roleUpdate name and phonePATCH/api/users/me/change-passwordAny roleChange password
Admin — User Management
MethodEndpointAccessDescriptionGET/api/admin/users/customersAdminList all customersGET/api/admin/users/staffAdminList all staffGET/api/admin/users/{id}AdminGet user by IDPOST/api/admin/users/staffAdminCreate staff accountPATCH/api/admin/users/{id}/deactivateAdminDeactivate userPATCH/api/admin/users/{id}/reactivateAdminReactivate userPATCH/api/admin/users/{id}/role?newRole= AdminChange user role
Service Catalog — Public
MethodEndpointAccessDescriptionGET/api/servicesPublicAll active servicesGET/api/services/{id}PublicSingle serviceGET/api/services/categoriesPublicAll active categoriesGET/api/services/categories?type=NORMALPublicFilter by typeGET/api/services/categories/{id}PublicCategory with its servicesGET/api/services/categories/{id}/servicesPublicServices by category
Admin — Service Catalog
MethodEndpointAccessDescriptionPOST/api/admin/services/categoriesAdminCreate categoryPUT/api/admin/services/categories/{id}AdminUpdate categoryPATCH/api/admin/services/categories/{id}/toggleAdminToggle active statusPOST/api/admin/servicesAdminCreate servicePUT/api/admin/services/{id}AdminUpdate servicePATCH/api/admin/services/{id}/toggleAdminToggle active status
Staff — Schedule & Leave
MethodEndpointAccessDescriptionGET/api/staff/scheduleStaffView own scheduleGET/api/staff/leavesStaffView upcoming leavesPOST/api/staff/leavesStaffApply for leaveDELETE/api/staff/leaves/{id}StaffCancel a leave
Admin — Staff Schedule & Leave
MethodEndpointAccessDescriptionPOST/api/admin/staff/{id}/scheduleAdminSet or update staff scheduleGET/api/admin/staff/{id}/scheduleAdminGet schedule of a staffGET/api/admin/staff/{id}/leavesAdminAll leaves of a staffGET/api/admin/staff/leaves?date=YYYY-MM-DDAdminAll leaves on a dateDELETE/api/admin/staff/leaves/{id}AdminDelete a leave

#Installation
Prerequisites

Java 21
MySQL 8.x
Maven

Steps
1. Clone the repository
bashgit clone https://github.com/anchaljoshi/salon-appointment.git
cd salon-appointment
2. Create the MySQL database
sqlCREATE DATABASE salon_db;
CREATE USER 'salon_user'@'localhost' IDENTIFIED BY 'salon123';
GRANT ALL PRIVILEGES ON salon_db.* TO 'salon_user'@'localhost';
FLUSH PRIVILEGES;
3. Configure src/main/resources/application.properties
propertiesspring.application.name=appointment

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
4. Run the application
bashmvn spring-boot:run
Tables are created automatically on first run.
5. Create the first Admin account
Register through the API:
bashPOST /api/auth/register
Content-Type: application/json

{
  "name": "Admin",
  "email": "admin@salon.com",
  "phone": "9999999999",
  "password": "password123"
}
Then promote to Admin via SQL:
sqlUPDATE users SET role = 'ADMIN' WHERE email = 'admin@salon.com';

Sample API Requests
Register
bashPOST /api/auth/register
Content-Type: application/json

{
  "name": "Priya Sharma",
  "email": "priya@gmail.com",
  "phone": "9876543210",
  "password": "priya1234"
}
Login
bashPOST /api/auth/login
Content-Type: application/json

{
  "email": "priya@gmail.com",
  "password": "priya1234"
}
Response:
json{
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
Create a Service Category (Admin)
bashPOST /api/admin/services/categories
Authorization: Bearer <admin_token>
Content-Type: application/json

{
  "name": "Hair Services",
  "description": "All hair related services",
  "serviceType": "NORMAL"
}
Create a Service (Admin)
bashPOST /api/admin/services
Authorization: Bearer <admin_token>
Content-Type: application/json

{
  "categoryId": 1,
  "name": "Hair Cutting and Trimming",
  "description": "Professional hair cut and trim",
  "price": 299.00,
  "durationMinutes": 45
}
Set Staff Schedule (Admin)
bashPOST /api/admin/staff/2/schedule
Authorization: Bearer <admin_token>
Content-Type: application/json

{
  "dayOfWeek": "MONDAY",
  "startTime": "10:00:00",
  "endTime": "19:00:00",
  "isWorking": true
}
Apply for Leave (Staff)
bashPOST /api/staff/leaves
Authorization: Bearer <staff_token>
Content-Type: application/json

{
  "leaveDate": "2026-06-20",
  "reason": "Personal work"
}
Refresh Token
bashPOST /api/auth/refresh
Content-Type: application/json

{
  "refreshToken": "d2e1f8a9-b4c7-d6e5..."
}

#Future Improvements

Appointment Booking — Full booking lifecycle with status flow PENDING → CONFIRMED → COMPLETED / CANCELLED and double-booking prevention
Slot Availability Engine — Auto-generate available time slots based on staff schedule, leaves, and existing bookings
Bridal Packages — Premium multi-session bookings tied to wedding event types (Engagement, Mehendi, Reception, Wedding Day)
Notification System — Internal notifications for booking confirmations, cancellations, and reminders
Reviews & Ratings — Customers can rate and review completed appointments
Admin Dashboard — Revenue summary, staff utilization, most booked services, and cancellation rate
Email / SMS Integration — Real delivery via SendGrid or Twilio
Payment Gateway — Online payments via Razorpay or Stripe


#Author
Anchal Joshi
LinkedIn: www.linkedin.com/in/anchal-joshi-617516398

