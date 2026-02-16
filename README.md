# Smart Finance Manager 💰

**Application Web de Gestion Financière Intelligente & Prédictive**

A full-stack personal finance application combining **data sovereignty**, **fluid UX**, and **AI-powered intelligence** for budget management — built for students and young professionals.

## 🎯 Project Overview

This project was built as part of the INDP2E program at **SUP'COM** (École Supérieure des Communications de Tunis).

### Key Innovation
Unlike traditional finance apps, Smart Finance Manager offers:
- **Privacy-First** architecture with MongoDB (no bank connections required)
- **AI-Powered OCR** receipt scanning using Groq/Llama
- **Predictive Analytics** for budget forecasting
- **Anomaly Detection** for unusual spending alerts

## 🏗️ Architecture

| Layer | Technology |
|-------|-----------|
| Frontend | Angular 17 (Standalone Components) |
| Backend | Spring Boot 3.2.1 + Java 17 |
| Database | MongoDB |
| AI/OCR | Groq API (Llama 3.3 70B + Llama 3.2 90B Vision) |
| Auth | JWT + BCrypt |
| PDF Export | iText 7 |

## ✨ Features

### 🆓 Standard Pack (Free)
- ✅ **CRUD Transactions** — Quick income/expense entry (Cash & Card)
- ✅ **Dynamic Categorization** — Custom tags (e.g., "SupCom Project", "Outings")
- ✅ **Monthly Visualization** — Pie charts and histograms
- ✅ **Multi-Currency** — TND/EUR/USD support
- ✅ **JWT Security** — Robust authentication with BCrypt password hashing

### 🤖 Premium Pack (AI-Powered)
- ✅ **Smart Scan (OCR)** — Photo of receipt → automatic extraction (Date, Amount, Category) via Groq Llama Vision
- ✅ **Text Receipt Scan** — Paste receipt text for AI extraction
- ✅ **AI Forecasting** — End-of-month balance prediction using linear regression
- ✅ **Anomaly Detection** — Immediate alert if an expense exceeds category average by 20%+
- ✅ **AI Budget Recommendations** — Personalized advice from Llama 3.3
- ✅ **Certified PDF Reports** — Clean PDF export for administrative files

## 🚀 Getting Started

### Prerequisites
- **Java 17+** and **Maven 3.6+**
- **MongoDB 4.4+** running on `localhost:27017`
- **Node.js 18+** and **npm**
- **Groq API Key** (free at https://console.groq.com)

### Step 1: Configure Groq API Key
```bash
# Set environment variable
export GROQ_API_KEY=gsk_your_key_here

# Or edit spring-boot-backend/src/main/resources/application.properties
groq.api.key=gsk_your_key_here
```

### Step 2: Start MongoDB
```bash
# Windows
mongod

# Linux/Mac
sudo systemctl start mongod
```

### Step 3: Run Backend
```bash
cd spring-boot-backend
mvn clean install
mvn spring-boot:run
```
Backend starts at `http://localhost:8080` with auto-seeded demo data.

### Step 4: Run Frontend
```bash
cd angular-frontend
npm install
npm start
```
Frontend at `http://localhost:4200`

### Demo Credentials
- **Email:** `john.doe@example.com`
- **Password:** `password123`

## 📊 API Endpoints

### Authentication (Public)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login & get JWT token |

### Transactions (Authenticated)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/transactions` | Get all transactions |
| GET | `/api/transactions/user/{userId}` | Get user's transactions |
| POST | `/api/transactions` | Create transaction |
| PUT | `/api/transactions/{id}` | Update transaction |
| DELETE | `/api/transactions/{id}` | Delete transaction |

### AI / OCR (Authenticated)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/ocr/scan` | Upload receipt image for AI OCR |
| POST | `/api/ocr/scan-text` | Analyze pasted receipt text |

### Analytics (Authenticated)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/analytics/stats/{userId}` | Dashboard statistics |
| GET | `/api/analytics/forecast/{userId}` | AI forecast + anomaly alerts |

### Export (Authenticated)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/export/pdf/{userId}` | Download PDF financial report |

### CRUD Endpoints
Categories, Items, Goals, Users — all support full CRUD at `/api/{entity}`.

## 🧠 AI Architecture

### OCR Smart Scan Pipeline
1. User uploads receipt photo → sent as base64 to Groq API
2. **Llama 3.2 90B Vision** model analyzes the image
3. AI extracts: amount, date, category, store name, currency
4. NLP classifies items into spending categories automatically
5. User confirms → transaction created with zero friction

### Budget Forecasting
- Linear regression on daily spending data
- Projects end-of-month balance
- Calculates safe daily budget
- AI-generated personalized recommendations via Llama 3.3

### Anomaly Detection
- Computes per-category spending averages
- Flags any transaction exceeding 20% above category average
- Active security alerts for unusual patterns (hidden subscriptions, etc.)

## 🛠️ Technologies

- **Spring Boot 3.2.1** + Spring Security + Spring WebFlux
- **MongoDB** with Spring Data
- **Angular 17** (Standalone Components)
- **Groq API** (Llama 3.3 70B Versatile + Llama 3.2 90B Vision)
- **JWT** (jjwt 0.12.3) + BCrypt
- **iText 7** for PDF generation
- **TypeScript 5.2** + RxJS

## 👥 Team (INDP2E - SUP'COM)

| Name | Role |
|------|------|
| Khalifa BOUNEB | Developer |
| Badereddine GUESMI | Developer |
| Wassim HAJJI | Developer |
| Ahmed Dhia DRIDI | Developer |
| Omar BAJAR | Developer |
| Mohsen KHOUAJA | Developer |

**Supervisor:** M. Zied CHOUKAIR  
**Academic Year:** 2025 - 2026
