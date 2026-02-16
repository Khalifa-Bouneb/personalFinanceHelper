export interface User {
    id?: number;
    name: string;
    email: string;
    passwordHash?: string;
    createdAt?: Date;
    currency?: string;
}

export interface Category {
    id?: string;
    name: string;
    color?: string;
    icon?: string;
    createdAt?: Date;
}

export interface Item {
    id?: string;
    name: string;
    cost: number;
    categoryId?: string;
    category?: Category;
}

export enum TransactionSign {
    POSITIVE = 'POSITIVE',
    NEGATIVE = 'NEGATIVE'
}

export interface Transaction {
    id?: string;
    userId: number;
    user?: User;
    amount: number;
    transactionDate?: Date;
    sign: TransactionSign;
    categoryId?: string;
    category?: Category;
    itemId?: string;
    item?: Item;
}

export enum GoalType {
    YEARLY = 'YEARLY',
    MONTHLY = 'MONTHLY'
}

export interface Goal {
    id?: string;
    userId: number;
    user?: User;
    categoryId?: string;
    category?: Category;
    maxAmount: number;
    type: GoalType;
    startDate?: Date;
    endDate?: Date;
    createdAt?: Date;
}

// Auth DTOs
export interface LoginRequest {
    email: string;
    password: string;
}

export interface RegisterRequest {
    name: string;
    email: string;
    password: string;
    currency: string;
}

export interface AuthResponse {
    token: string;
    userId: number;
    name: string;
    email: string;
    currency: string;
}

// OCR
export interface OcrResult {
    amount: number;
    category: string;
    itemName: string;
    description: string;
    date: string;
    currency: string;
    rawText: string;
    confidence: number;
}

// Analytics
export interface CategoryBreakdown {
    categoryId: string;
    categoryName: string;
    color: string;
    total: number;
    percentage: number;
}

export interface MonthlyTrend {
    month: string;
    income: number;
    expenses: number;
}

export interface GoalProgress {
    goalId: string;
    categoryName: string;
    type: string;
    maxAmount: number;
    currentSpent: number;
    percentage: number;
    exceeded: boolean;
}

export interface DashboardStats {
    totalIncome: number;
    totalExpenses: number;
    balance: number;
    monthlyIncome: number;
    monthlyExpenses: number;
    totalTransactions: number;
    categoryBreakdown: CategoryBreakdown[];
    monthlyTrends: MonthlyTrend[];
    goalProgress: GoalProgress[];
}

// Forecast
export interface DailyPrediction {
    date: string;
    predictedBalance: number;
}

export interface AnomalyAlert {
    transactionId: string;
    amount: number;
    categoryName: string;
    date: string;
    categoryAverage: number;
    deviationPercentage: number;
    message: string;
}

export interface ForecastResult {
    predictedEndOfMonthBalance: number;
    predictedMonthlyExpenses: number;
    averageDailySpending: number;
    daysRemaining: number;
    safeDailyBudget: number;
    recommendation: string;
    predictions: DailyPrediction[];
    anomalies: AnomalyAlert[];
}
