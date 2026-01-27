# Finance Tracker - Spring Boot Backend

A RESTful API backend for financial tracking built with Spring Boot and MongoDB.

## Features

- **User Management**: Create and manage user accounts
- **Categories**: Organize transactions with customizable categories
- **Items**: Track specific items within categories
- **Transactions**: Record income (positive) and expenses (negative)
- **Goals**: Set monthly or yearly spending goals

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.1**
- **Spring Data MongoDB**
- **Lombok**
- **Maven**

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MongoDB 4.4+ running on `localhost:27017`

## Getting Started

### 1. Install MongoDB

Make sure MongoDB is installed and running on your system:

```bash
# Check if MongoDB is running
sudo systemctl status mongod

# Start MongoDB if not running
sudo systemctl start mongod
```

### 2. Build the Project

```bash
cd spring-boot-backend
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080` and automatically seed the database with sample data.

## API Endpoints

### Users
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Categories
- `GET /api/categories` - Get all categories
- `GET /api/categories/{id}` - Get category by ID
- `POST /api/categories` - Create new category
- `PUT /api/categories/{id}` - Update category
- `DELETE /api/categories/{id}` - Delete category

### Items
- `GET /api/items` - Get all items
- `GET /api/items/{id}` - Get item by ID
- `POST /api/items` - Create new item
- `PUT /api/items/{id}` - Update item
- `DELETE /api/items/{id}` - Delete item

### Transactions
- `GET /api/transactions` - Get all transactions
- `GET /api/transactions/{id}` - Get transaction by ID
- `POST /api/transactions` - Create new transaction
- `PUT /api/transactions/{id}` - Update transaction
- `DELETE /api/transactions/{id}` - Delete transaction

### Goals
- `GET /api/goals` - Get all goals
- `GET /api/goals/{id}` - Get goal by ID
- `POST /api/goals` - Create new goal
- `PUT /api/goals/{id}` - Update goal
- `DELETE /api/goals/{id}` - Delete goal

## Sample Data

The application automatically seeds the database with:
- 2 users (John Doe, Jane Smith)
- 5 categories (Groceries, Transport, Entertainment, Utilities, Salary)
- 5 items (Milk, Bread, Gas/Fuel, Movie Ticket, Electricity Bill)
- 6 transactions (mix of income and expenses)
- 3 goals (monthly and yearly spending limits)

## Project Structure

```
src/main/java/com/finance/
├── Application.java              # Main application class
├── config/
│   └── DatabaseSeeder.java       # Database seeding
├── controller/                   # REST controllers
│   ├── UserController.java
│   ├── CategoryController.java
│   ├── ItemController.java
│   ├── TransactionController.java
│   └── GoalController.java
├── enums/                        # Enumerations
│   ├── TransactionSign.java
│   └── GoalType.java
├── model/                        # Entity models
│   ├── User.java
│   ├── Category.java
│   ├── Item.java
│   ├── Transaction.java
│   └── Goal.java
├── repository/                   # MongoDB repositories
│   ├── UserRepository.java
│   ├── CategoryRepository.java
│   ├── ItemRepository.java
│   ├── TransactionRepository.java
│   └── GoalRepository.java
└── service/                      # Business logic
    ├── UserService.java
    ├── CategoryService.java
    ├── ItemService.java
    ├── TransactionService.java
    └── GoalService.java
```

## Configuration

Edit `src/main/resources/application.properties` to configure:
- MongoDB connection URI
- Server port
- CORS settings
- Logging levels
