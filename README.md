# Finance Tracker - Full Stack Application

A complete financial tracking application with Spring Boot backend, MongoDB database, and Angular frontend.

## 📋 Overview

This application implements a comprehensive financial tracking system based on the provided DBML schema. It includes:

- **Users**: User account management
- **Categories**: Transaction categorization with visual properties
- **Items**: Specific items within categories
- **Transactions**: Income and expense tracking
- **Goals**: Monthly and yearly spending goals

## 🏗️ Architecture

### Backend: Spring Boot + MongoDB
- **Framework**: Spring Boot 3.2.1
- **Database**: MongoDB
- **Language**: Java 17
- **Build Tool**: Maven

### Frontend: Angular
- **Framework**: Angular 17 (Standalone Components)
- **Language**: TypeScript 5.2
- **HTTP Client**: Angular HttpClient

## 📁 Project Structure

```
zc/
├── schema.dbml                    # Database schema definition
├── spring-boot-backend/           # Spring Boot backend
│   ├── src/main/java/com/finance/
│   │   ├── Application.java
│   │   ├── config/
│   │   │   └── DatabaseSeeder.java
│   │   ├── controller/            # REST controllers
│   │   ├── enums/                 # Enumerations
│   │   ├── model/                 # Entity models
│   │   ├── repository/            # MongoDB repositories
│   │   └── service/               # Business logic
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
└── angular-frontend/              # Angular frontend
    ├── src/app/
    │   ├── components/
    │   │   └── dashboard/
    │   ├── models/
    │   ├── services/
    │   └── app.component.ts
    ├── package.json
    └── angular.json
```

## 🚀 Getting Started

### Prerequisites

1. **Java 17+** - For Spring Boot backend
2. **Maven 3.6+** - For building Spring Boot
3. **MongoDB 4.4+** - Database server
4. **Node.js 18+** - For Angular frontend
5. **npm** - Node package manager

### Step 1: Start MongoDB

```bash
# Check if MongoDB is running
sudo systemctl status mongod

# Start MongoDB if not running
sudo systemctl start mongod
```

### Step 2: Run Spring Boot Backend

```bash
cd spring-boot-backend
mvn clean install
mvn spring-boot:run
```

The backend will:
- Start on `http://localhost:8080`
- Automatically seed the database with sample data
- Expose REST APIs at `/api/*`

### Step 3: Run Angular Frontend

```bash
cd angular-frontend
npm install
npm start
```

The frontend will be available at `http://localhost:4200`

## 📊 Sample Data

The application automatically seeds the database with:

- **2 Users**: John Doe, Jane Smith
- **5 Categories**: Groceries, Transport, Entertainment, Utilities, Salary
- **5 Items**: Milk, Bread, Gas/Fuel, Movie Ticket, Electricity Bill
- **6 Transactions**: Mix of income and expenses
- **3 Goals**: Monthly and yearly spending limits

## 🔌 API Endpoints

All endpoints support full CRUD operations:

### Users
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Categories
- `GET /api/categories` - Get all categories
- `GET /api/categories/{id}` - Get category by ID
- `POST /api/categories` - Create category
- `PUT /api/categories/{id}` - Update category
- `DELETE /api/categories/{id}` - Delete category

### Items
- `GET /api/items` - Get all items
- `GET /api/items/{id}` - Get item by ID
- `POST /api/items` - Create item
- `PUT /api/items/{id}` - Update item
- `DELETE /api/items/{id}` - Delete item

### Transactions
- `GET /api/transactions` - Get all transactions
- `GET /api/transactions/{id}` - Get transaction by ID
- `POST /api/transactions` - Create transaction
- `PUT /api/transactions/{id}` - Update transaction
- `DELETE /api/transactions/{id}` - Delete transaction

### Goals
- `GET /api/goals` - Get all goals
- `GET /api/goals/{id}` - Get goal by ID
- `POST /api/goals` - Create goal
- `PUT /api/goals/{id}` - Update goal
- `DELETE /api/goals/{id}` - Delete goal

## ✨ Features

### Backend Features
- ✅ Complete CRUD operations for all entities
- ✅ MongoDB integration with Spring Data
- ✅ Automatic database seeding
- ✅ Entity relationships (DBRef)
- ✅ Input validation
- ✅ CORS configuration for Angular
- ✅ Auto-incrementing user IDs
- ✅ UUID generation for other entities

### Frontend Features
- ✅ Modern, responsive dashboard
- ✅ Real-time data fetching
- ✅ Color-coded categories
- ✅ Transaction type indicators (income/expense)
- ✅ Delete operations with confirmation
- ✅ Gradient background design
- ✅ Card-based layouts
- ✅ Hover effects and animations

## 🎨 UI Design

The Angular frontend features:
- **Purple gradient background** for modern aesthetics
- **Card-based layouts** for organized data display
- **Color-coded elements** for visual clarity
- **Responsive grid system** that adapts to screen sizes
- **Interactive hover effects** for better UX
- **Clean typography** using Segoe UI font family

## 🔧 Configuration

### Backend Configuration
Edit `spring-boot-backend/src/main/resources/application.properties`:
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/finance_tracker
server.port=8080
```

### Frontend Configuration
Edit API URLs in service files if backend runs on different port:
```typescript
private apiUrl = 'http://localhost:8080/api/...';
```

## 📝 Database Schema

The implementation follows the DBML schema with:
- **Users**: Integer ID (auto-increment), email uniqueness
- **Categories**: UUID, color and icon properties
- **Items**: UUID, linked to categories
- **Transactions**: UUID, linked to users, categories, and items
- **Goals**: UUID, monthly/yearly types, date ranges

## 🛠️ Technologies Used

### Backend
- Spring Boot 3.2.1
- Spring Data MongoDB
- Lombok (reduces boilerplate)
- Jakarta Validation
- Maven

### Frontend
- Angular 17 (Standalone Components)
- TypeScript 5.2
- RxJS 7.8
- Angular HttpClient
- CSS3 with modern features

## 📖 Additional Documentation

- [Spring Boot Backend README](spring-boot-backend/README.md)
- [Angular Frontend README](angular-frontend/README.md)

## 🎯 Next Steps

To extend the application:
1. Add authentication and authorization
2. Implement filtering and search
3. Add charts and visualizations
4. Create forms for adding/editing entities
5. Add pagination for large datasets
6. Implement real-time updates with WebSockets
