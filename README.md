# Smart Finance Manager

**Intelligent & Predictive Personal Finance Web Application**

A full-stack finance management app combining **data sovereignty**, **fluid UX**, and **AI-powered intelligence** — built for students and young professionals.

> **INDP2E — SUP'COM** (Ecole Superieure des Communications de Tunis)  
> Academic Year 2025–2026 | Supervisor: M. Zied CHOUKAIR

---

## Table of Contents

1. [Architecture](#architecture)
2. [Prerequisites](#prerequisites)
3. [Quick Start (How to Test)](#quick-start-how-to-test)
4. [Demo Credentials](#demo-credentials)
5. [Features Overview](#features-overview)
6. [AI Integration](#ai-integration)
7. [API Reference](#api-reference)
8. [Project Structure](#project-structure)
9. [Team](#team)

---

## Architecture

| Layer        | Technology                                  |
|--------------|---------------------------------------------|
| Frontend     | Angular 17 (Standalone Components)          |
| Backend      | Spring Boot 3.2.1 + Java 17                |
| Database     | MongoDB 4.4+                                |
| AI / OCR     | Groq API (Llama 3.3 70B + Llama 4 Scout Vision) |
| Auth         | JWT (jjwt 0.12.3) + BCrypt                 |
| PDF Export   | iText 7                                     |

```
┌────────────────┐       HTTP/JSON       ┌──────────────────────┐       ┌──────────┐
│  Angular 17    │ ◄──────────────────►  │  Spring Boot 3.2     │ ◄──► │ MongoDB  │
│  :4200         │   JWT Auth Header     │  :8080               │      │ :27017   │
└────────────────┘                       │  ┌──────────────┐    │      └──────────┘
                                         │  │ Groq AI API  │    │
                                         │  │ (OCR/NLP)    │    │
                                         │  └──────────────┘    │
                                         └──────────────────────┘
```

---

## Prerequisites

Make sure the following are installed on your machine **before testing**:

| Tool          | Version    | Download Link                                    |
|---------------|------------|--------------------------------------------------|
| **Java JDK**  | 17+        | https://adoptium.net/                            |
| **Maven**     | 3.6+       | https://maven.apache.org/download.cgi            |
| **MongoDB**   | 4.4+       | https://www.mongodb.com/try/download/community   |
| **Node.js**   | 18+        | https://nodejs.org/                              |
| **npm**       | 9+         | (comes with Node.js)                             |

> **Groq API Key** (free): Get one at https://console.groq.com — set it via the `GROQ_API_KEY` environment variable or directly in `application.properties`.

---

## Quick Start (How to Test)

### Step 1 — Start MongoDB

Make sure MongoDB is running on `localhost:27017`.

```bash
# Windows (if installed as service, it runs automatically)
# Otherwise:
mongod

# Linux / macOS
sudo systemctl start mongod
```

Verify it's running:

```bash
mongosh --eval "db.runCommand({ ping: 1 })"
# Expected output: { ok: 1 }
```

### Step 2 — Start the Backend (Spring Boot)

```bash
cd spring-boot-backend
mvn clean install -DskipTests
mvn spring-boot:run
```

Wait until you see:

```
Started Application in X seconds
Database seeding completed successfully!
```

The backend is now running at **http://localhost:8080**.  
The database is **auto-seeded** with demo data (2 users, 9 categories, 5 items, 12 transactions, 3 goals).

### Step 3 — Start the Frontend (Angular)

Open a **new terminal** and run:

```bash
cd angular-frontend
npm install
npx ng serve
```

Wait until you see:

```
** Angular Live Development Server is listening on localhost:4200 **
```

### Step 4 — Open the App

Open your browser and go to: **http://localhost:4200**

---

## Demo Credentials

The database is seeded with two demo users on every startup:

| User       | Email                     | Password      | Currency |
|------------|---------------------------|---------------|----------|
| John Doe   | `john.doe@example.com`    | `password123` | TND      |
| Jane Smith | `jane.smith@example.com`  | `password123` | EUR      |

---

## Features Overview

### Standard Features

| Feature                    | Description                                                                 |
|----------------------------|-----------------------------------------------------------------------------|
| **User Authentication**    | Register / Login with JWT tokens and BCrypt password hashing               |
| **CRUD Transactions**      | Add, view, edit, and delete income and expense entries                      |
| **Dynamic Categories**     | 9 built-in categories (Groceries, Transport, Entertainment, etc.)          |
| **Item Tracking**          | Track specific items within categories with costs                          |
| **Spending Goals**         | Set monthly or yearly budget limits per category                           |
| **Dashboard Overview**     | Stats cards showing total income, expenses, balance, and transaction count  |
| **Category Breakdown**     | Pie-chart-style percentage breakdown of expenses by category               |
| **Monthly Trends**         | 6-month income vs expenses trend visualization                             |
| **Goal Progress**          | Visual tracking of spending against budget goals with exceeded alerts      |
| **Multi-Currency**         | Support for TND, EUR, and USD                                              |

### AI-Powered Features

| Feature                     | Description                                                                 |
|-----------------------------|-----------------------------------------------------------------------------|
| **Smart Scan (OCR)**        | Upload a receipt photo → AI extracts date, amount, category, and items     |
| **Text Receipt Scan**       | Paste raw receipt text → AI parses and structures it                       |
| **AI Forecasting**          | Linear regression predicts end-of-month balance and safe daily budget      |
| **Anomaly Detection**       | Flags expenses that exceed 20%+ above their category average               |
| **AI Recommendations**      | Personalized budget advice generated by Llama 3.3                          |
| **PDF Export**              | Download a branded, certified PDF financial report                         |

---

## AI Integration

The application integrates AI through the **[Groq API](https://console.groq.com)** — a high-speed inference platform running open-source Llama models. All AI logic is centralized in `GroqAiService.java` and consumed by other services.

### Models Used

| Model | Task |
|-------|------|
| `meta-llama/llama-4-scout-17b-16e-instruct` | Vision/OCR — analyzes receipt **images** |
| `llama-3.3-70b-versatile` | Text — parses receipt **text** and generates budget recommendations |

### How It Works

```
Angular Frontend
      │
      │  POST /api/ocr/scan          (image upload)
      │  POST /api/ocr/scan-text     (pasted text)
      │  GET  /api/analytics/forecast/{userId}
      ▼
Spring Boot Backend
      │
      ├── OcrController ──────────► GroqAiService.analyzeReceipt()
      │                                  └─► Groq API (Llama 4 Scout Vision)
      │                                        └─► JSON: amount, category, date, confidence
      │
      ├── OcrController ──────────► GroqAiService.analyzeReceiptText()
      │                                  └─► Groq API (Llama 3.3 70B)
      │                                        └─► JSON: amount, category, date, confidence
      │
      └── AnalyticsController ────► AnalyticsService.getForecast()
                                         ├─► Linear regression on spending history
                                         ├─► Anomaly detection (>20% above category avg)
                                         └─► GroqAiService.getBudgetRecommendation()
                                                  └─► Groq API (Llama 3.3 70B)
                                                        └─► Personalized budget advice (text)
```

### Feature 1 — Smart Scan (Receipt Image OCR)

**Endpoint:** `POST /api/ocr/scan` (multipart image upload)

1. The frontend sends the receipt photo as a multipart file.
2. `OcrController` passes the raw bytes and MIME type to `GroqAiService.analyzeReceipt()`.
3. The image is Base64-encoded and sent to the **Llama 4 Scout** vision model via the Groq API.
4. The model returns structured JSON with extracted fields.
5. The backend parses the JSON into an `OcrResult` DTO and returns it to the frontend.
6. The frontend pre-fills the "Add Transaction" form with the extracted data.

**AI Response Format:**
```json
{
  "amount": 24.50,
  "category": "Groceries",
  "itemName": "Carrefour Market",
  "description": "Weekly grocery shopping",
  "date": "2025-03-14 10:30:00",
  "currency": "TND",
  "confidence": 0.92
}
```

### Feature 2 — Text Receipt Scan

**Endpoint:** `POST /api/ocr/scan-text` (JSON body: `{ "text": "..." }`)

Same flow as image OCR but the user pastes raw receipt text instead. Uses **Llama 3.3 70B** instead of the vision model.

### Feature 3 — AI Forecasting & Budget Recommendations

**Endpoint:** `GET /api/analytics/forecast/{userId}`

1. `AnalyticsService.getForecast()` calculates:
   - **Average daily spending** (current month expenses ÷ elapsed days)
   - **Predicted end-of-month balance** via linear regression
   - **Safe daily budget** (remaining balance ÷ days left)
   - **Anomaly alerts**: transactions > 20% above their category average
2. A financial context summary is built (income, expenses, anomalies, etc.).
3. This context is sent to `GroqAiService.getBudgetRecommendation()`.
4. The **Llama 3.3 70B** model returns 2–3 paragraphs of personalized budget advice.
5. The full `ForecastResult` (predictions, anomalies, recommendation) is returned to the frontend.

### Configuration

The Groq API key and model are configured in `application.properties`:

```properties
groq.api.key=${GROQ_API_KEY:your-groq-api-key-here}
groq.api.url=https://api.groq.com/openai/v1/chat/completions
groq.model=llama-3.3-70b-versatile
```

You can set the key as an environment variable (`GROQ_API_KEY`) or replace the placeholder directly. Get a free key at **https://console.groq.com**.

> See `spring-boot-backend/src/main/resources/application.properties.example` for a full configuration template.

---

## API Reference

All endpoints are prefixed with `/api`. Authenticated endpoints require the header:  
`Authorization: Bearer <jwt_token>`

### Authentication (Public)

| Method | Endpoint             | Body                                       | Description           |
|--------|----------------------|--------------------------------------------|-----------------------|
| POST   | `/api/auth/register` | `{ name, email, password, currency }`      | Register a new user   |
| POST   | `/api/auth/login`    | `{ email, password }`                      | Login, returns JWT    |

### Transactions

| Method | Endpoint                           | Description                    |
|--------|------------------------------------|-------------------------------|
| GET    | `/api/transactions`                | List all transactions          |
| GET    | `/api/transactions/user/{userId}`  | List user's transactions       |
| POST   | `/api/transactions`                | Create a transaction           |
| PUT    | `/api/transactions/{id}`           | Update a transaction           |
| DELETE | `/api/transactions/{id}`           | Delete a transaction           |

### Categories

| Method | Endpoint               | Description              |
|--------|------------------------|--------------------------|
| GET    | `/api/categories`      | List all categories       |
| POST   | `/api/categories`      | Create a category         |
| DELETE | `/api/categories/{id}` | Delete a category         |

### Items

| Method | Endpoint                          | Description                |
|--------|-----------------------------------|----------------------------|
| GET    | `/api/items`                      | List all items              |
| GET    | `/api/items/category/{categoryId}`| List items by category     |
| POST   | `/api/items`                      | Create an item              |
| DELETE | `/api/items/{id}`                 | Delete an item              |

### Goals

| Method | Endpoint                     | Description             |
|--------|------------------------------|-------------------------|
| GET    | `/api/goals`                 | List all goals           |
| GET    | `/api/goals/user/{userId}`   | List user's goals        |
| POST   | `/api/goals`                 | Create a goal            |
| DELETE | `/api/goals/{id}`            | Delete a goal            |

### Users

| Method | Endpoint            | Description          |
|--------|---------------------|----------------------|
| GET    | `/api/users`        | List all users        |
| GET    | `/api/users/{id}`   | Get user by ID        |
| PUT    | `/api/users/{id}`   | Update user           |
| DELETE | `/api/users/{id}`   | Delete user           |

### AI / OCR

| Method | Endpoint             | Body / Params             | Description                            |
|--------|----------------------|---------------------------|----------------------------------------|
| POST   | `/api/ocr/scan`      | `file` (multipart image)  | Upload receipt image for AI OCR        |
| POST   | `/api/ocr/scan-text` | `{ "text": "..." }`      | Paste receipt text for AI extraction   |

### Analytics

| Method | Endpoint                        | Description                                         |
|--------|---------------------------------|-----------------------------------------------------|
| GET    | `/api/analytics/stats/{userId}` | Dashboard stats (totals, breakdown, trends, goals)  |
| GET    | `/api/analytics/forecast/{userId}` | AI forecast, anomaly alerts, daily predictions   |

### Export

| Method | Endpoint                   | Description                          |
|--------|----------------------------|--------------------------------------|
| GET    | `/api/export/pdf/{userId}` | Download PDF financial report        |

---

## Project Structure

```
personalFinanceHelper/
├── .gitignore
├── README.md                          # This file
├── schema.dbml                        # Database schema documentation
├── docs/
│   └── ZC_P.pdf                       # Project specification document
│
├── spring-boot-backend/               # Java Spring Boot REST API
│   ├── pom.xml                        # Maven dependencies
│   └── src/main/java/com/finance/
│       ├── Application.java           # Entry point
│       ├── config/
│       │   ├── SecurityConfig.java    # JWT + CORS security config
│       │   └── DatabaseSeeder.java    # Auto-seeds demo data on startup
│       ├── controller/                # REST API endpoints
│       │   ├── AuthController.java
│       │   ├── TransactionController.java
│       │   ├── CategoryController.java
│       │   ├── ItemController.java
│       │   ├── GoalController.java
│       │   ├── UserController.java
│       │   ├── AnalyticsController.java
│       │   ├── OcrController.java
│       │   └── ExportController.java
│       ├── dto/                       # Data Transfer Objects
│       │   ├── LoginRequest.java
│       │   ├── RegisterRequest.java
│       │   ├── AuthResponse.java
│       │   ├── DashboardStats.java
│       │   ├── ForecastResult.java
│       │   └── OcrResult.java
│       ├── enums/
│       │   ├── TransactionSign.java   # POSITIVE / NEGATIVE
│       │   └── GoalType.java          # MONTHLY / YEARLY
│       ├── model/                     # MongoDB document models
│       │   ├── User.java
│       │   ├── Transaction.java
│       │   ├── Category.java
│       │   ├── Item.java
│       │   └── Goal.java
│       ├── repository/                # Spring Data MongoDB repositories
│       ├── security/
│       │   ├── JwtUtil.java           # JWT token generation/validation
│       │   └── JwtAuthenticationFilter.java
│       └── service/                   # Business logic
│           ├── UserService.java
│           ├── TransactionService.java
│           ├── CategoryService.java
│           ├── ItemService.java
│           ├── GoalService.java
│           ├── AnalyticsService.java   # Dashboard stats + forecasting
│           ├── GroqAiService.java      # Groq API integration (OCR + NLP)
│           └── PdfExportService.java   # iText PDF generation
│
└── angular-frontend/                  # Angular 17 SPA
    ├── package.json
    ├── angular.json
    └── src/
        ├── index.html
        ├── main.ts
        ├── styles.css
        └── app/
            ├── app.component.ts       # Root component with routing
            ├── app.config.ts          # App configuration + providers
            ├── components/
            │   ├── login/             # Login / Register page
            │   └── dashboard/         # Main dashboard (all tabs)
            ├── interceptors/
            │   └── auth.interceptor.ts # Adds JWT to HTTP requests
            ├── models/
            │   └── models.ts          # TypeScript interfaces
            └── services/              # HTTP service layer
                ├── auth.service.ts
                ├── transaction.service.ts
                ├── category.service.ts
                ├── item.service.ts
                ├── goal.service.ts
                ├── analytics.service.ts
                ├── ocr.service.ts
                ├── export.service.ts
                └── user.service.ts
```

---

## Team

| Name               | Role      |
|--------------------|-----------|
| Khalifa BOUNEB     | Developer |
| Badereddine GUESMI | Developer |
| Wassim HAJJI       | Developer |
| Ahmed Dhia DRIDI   | Developer |
| Omar BAJAR         | Developer |
| Mohsen KHOUAJA     | Developer |

**Supervisor:** M. Zied CHOUKAIR  
**Institution:** SUP'COM — INDP2E  
**Academic Year:** 2025–2026
