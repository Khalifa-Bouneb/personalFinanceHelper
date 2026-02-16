# Smart Finance Manager - Angular Frontend

Modern Angular frontend for the Smart Finance Manager application with AI-powered features.

## Features

- **JWT Authentication** — Login/Register with token-based security
- **Dashboard** — Overview with stats cards, category breakdown, monthly trends, and goal progress
- **Transactions** — Add, view, and delete income/expense transactions
- **Goals** — Set and track monthly/yearly spending goals per category
- **OCR Smart Scan** — Upload receipt images or paste text for AI-powered extraction
- **AI Forecasting** — View predicted end-of-month balance, daily spending forecasts, and anomaly alerts
- **PDF Export** — Download branded financial reports
- **Responsive Design** — Modern sidebar-based UI with card layouts

## Technology Stack

- **Angular 17** (Standalone Components)
- **TypeScript 5.2**
- **RxJS 7.8**
- **HttpClient** with JWT Interceptor for API communication

## Prerequisites

- Node.js 18+ and npm
- Spring Boot backend running on `http://localhost:8080`

## Getting Started

### 1. Install Dependencies

```bash
cd angular-frontend
npm install
```

### 2. Start Development Server

```bash
npm start
```

The application will be available at `http://localhost:4200`

## Project Structure

```
src/
├── app/
│   ├── components/
│   │   └── dashboard/              # Main dashboard component
│   │       ├── dashboard.component.ts
│   │       ├── dashboard.component.html
│   │       └── dashboard.component.css
│   ├── models/
│   │   └── models.ts               # TypeScript interfaces
│   ├── services/                   # HTTP services
│   │   ├── user.service.ts
│   │   ├── category.service.ts
│   │   ├── item.service.ts
│   │   ├── transaction.service.ts
│   │   └── goal.service.ts
│   ├── app.component.ts            # Root component
│   └── app.config.ts               # App configuration
├── main.ts                         # Bootstrap file
├── index.html                      # HTML entry point
└── styles.css                      # Global styles
```

## Features Demonstrated

### Dashboard Component
- Displays all users with their details
- Shows categories with color-coded cards
- Lists transactions in a table format with delete functionality
- Displays financial goals with delete capability
- Color-coded positive (income) and negative (expense) transactions

### Services
Each entity has a dedicated service with full CRUD operations:
- `findAll()` - Get all records
- `findOne(id)` - Get single record
- `create(entity)` - Create new record
- `update(id, entity)` - Update existing record
- `delete(id)` - Delete record

## API Integration

The frontend connects to the Spring Boot backend at `http://localhost:8080/api/`:
- `/users` - User management
- `/categories` - Category management
- `/items` - Item management
- `/transactions` - Transaction management
- `/goals` - Goal management

## Design Features

- **Gradient Background**: Purple gradient for modern look
- **Card-based Layout**: Clean, organized data presentation
- **Color Coding**: Visual indicators for transaction types and categories
- **Responsive Grid**: Adapts to different screen sizes
- **Hover Effects**: Interactive elements with smooth transitions
- **Delete Confirmations**: User-friendly delete operations

## Development

To add new features:
1. Create new components in `src/app/components/`
2. Add services in `src/app/services/`
3. Define models in `src/app/models/models.ts`
4. Update routing in `app.config.ts` if needed
