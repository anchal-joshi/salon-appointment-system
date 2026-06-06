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
