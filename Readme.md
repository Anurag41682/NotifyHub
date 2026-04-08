# NotifyHub

A real-time notification service with async processing, retry mechanisms, and multi-channel delivery.

**Live:** [https://notify.agnomerf.store](https://notify.agnomerf.store)

---

## What It Does

NotifyHub is a centralized notification service that other services (payment, order, auth, etc.) can call via REST API to send notifications to users. It handles queuing, delivery, retries, and deduplication — so the calling service doesn't have to.

**Example flow:** A payment service calls NotifyHub → "Send user 42 a payment confirmation email" → NotifyHub queues it, delivers the email, retries on failure, and tracks the status.

---

## Tech Stack

- **Java 17** + **Spring Boot 3.5**
- **MySQL 8.4** — persistent storage for users and notifications
- **RabbitMQ 4** — async message processing
- **Redis 8.6** — idempotency checks to prevent duplicate notifications
- **Resend** — email delivery
- **Docker Compose** — local development environment
- **Railway** — production deployment

---

## Architecture

```
Client (API call)
    │
    ▼
┌──────────────────────┐
│   Spring Boot API    │
│  (Auth + CRUD + JWT) │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐     ┌───────────────┐
│   Redis              │     │   MySQL        │
│   (Idempotency)      │     │   (Storage)    │
└──────────────────────┘     └───────────────┘
           │
           ▼
┌──────────────────────┐
│   RabbitMQ           │
│   (Message Queue)    │
│                      │
│  notification-queue  │
│  notification-dlq    │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│   Consumer           │
│  ┌────────────────┐  │
│  │ Process msg     │  │
│  │ Send email      │  │
│  │ Update status   │  │
│  │ Retry on fail   │  │
│  └────────────────┘  │
└──────────────────────┘
           │
           ▼
┌──────────────────────┐
│   Resend (Email)     │
└──────────────────────┘
```

---

## Key Features

**JWT Authentication** — Register and login with email/password. All notification endpoints are secured with JWT tokens.

**Async Notification Processing** — Notifications are created as PENDING, pushed to RabbitMQ, and processed asynchronously by a consumer. The API responds instantly without waiting for email delivery.

**Retry with Exponential Backoff** — If delivery fails, the system retries up to 3 times with increasing delays (2s → 4s → 8s) before marking the notification as FAILED.

**Dead Letter Queue** — Notifications that fail after all retry attempts are moved to a DLQ for inspection and potential reprocessing.

**Redis Idempotency** — Clients send an `Idempotency-Key` header with each request. Redis tracks processed keys (TTL: 24 hours) to prevent duplicate notifications.

**Email Delivery** — Real email delivery via Resend API integration.

**Notification Lifecycle** — Each notification transitions through: `PENDING → PROCESSING → SENT / FAILED`

---

## API Endpoints

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and receive JWT token |

### Notifications
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/notifications` | Create a notification (requires `Idempotency-Key` header) |
| GET | `/api/notifications/{id}` | Get notification by ID |
| GET | `/api/notifications/recipient/{recipientId}` | Get all notifications for a user |
| DELETE | `/api/notifications/{id}` | Delete a notification |

---

## Request Examples

**Register:**
```json
POST /api/auth/register
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Login:**
```json
POST /api/auth/login
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Create Notification:**
```
POST /api/notifications
Headers:
  Authorization: Bearer <jwt_token>
  Idempotency-Key: <unique-uuid>

{
  "title": "Payment Successful",
  "message": "Your payment of ₹500 has been processed",
  "type": "EMAIL",
  "recipientId": 1
}
```

---

## Notification Status Flow

```
PENDING ──→ PROCESSING ──→ SENT
                │
                ▼
             FAILED (after 3 retries) ──→ Dead Letter Queue
```

---

## Local Setup

**Prerequisites:** Docker Desktop, Java 17, Maven

1. Clone the repository:
```bash
git clone https://github.com/your-username/NotifyHub.git
cd NotifyHub
```

2. Start infrastructure services:
```bash
docker-compose up -d
```
This starts MySQL (port 3307), RabbitMQ (port 5672, dashboard at 15672), and Redis (port 6379).

3. Configure `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3307/notification_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
jwt.secret=your_jwt_secret
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.password=root
resend.api.key=your_resend_api_key
```

4. Run the application:
```bash
mvn spring-boot:run
```

5. Test with Postman or curl at `http://localhost:8080`

---

## Project Structure

```
src/main/java/com/anurag/notifyhub/
├── config/          # RabbitMQ configuration
├── consumer/        # RabbitMQ message consumer
├── controller/      # REST controllers (Auth, Notification)
├── dto/             # Request and Response DTOs
├── enums/           # NotificationType, NotificationStatus
├── exception/       # Custom exceptions + GlobalExceptionHandler
├── model/           # JPA entities (User, Notification)
├── producer/        # RabbitMQ message producer
├── repository/      # JPA repositories
├── security/        # JWT utils, filters, SecurityConfig
├── service/         # Business logic (Notification, Email, Redis)
└── NotifyhubApplication.java
```

---

## Design Decisions

- **Monolith over Microservices** — The scope doesn't justify microservices overhead. Clean package separation allows future decomposition if needed.
- **RabbitMQ over Kafka** — RabbitMQ fits better for task queue patterns with routing and acknowledgment. Kafka is better suited for high-throughput event streaming which isn't needed here.
- **Redis for Idempotency** — In-memory lookups are faster than database queries for deduplication. Auto-expiry (TTL) eliminates the need for cleanup jobs.
- **Retry in Application Code** — Implementing retry logic in the consumer (vs RabbitMQ native retry) gives full control over backoff strategy and status tracking in the database.