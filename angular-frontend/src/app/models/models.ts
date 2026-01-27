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
