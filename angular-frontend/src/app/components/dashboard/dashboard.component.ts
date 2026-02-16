import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { TransactionService } from '../../services/transaction.service';
import { CategoryService } from '../../services/category.service';
import { GoalService } from '../../services/goal.service';
import { AnalyticsService } from '../../services/analytics.service';
import { OcrService } from '../../services/ocr.service';
import { ExportService } from '../../services/export.service';
import {
    Transaction, Category, Goal, TransactionSign, GoalType,
    DashboardStats, ForecastResult, OcrResult, AuthResponse
} from '../../models/models';

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
    // User
    currentUser: AuthResponse | null = null;

    // Data
    transactions: Transaction[] = [];
    categories: Category[] = [];
    goals: Goal[] = [];
    stats: DashboardStats | null = null;
    forecast: ForecastResult | null = null;

    // UI state
    activeTab: 'overview' | 'transactions' | 'goals' | 'scan' | 'forecast' = 'overview';
    loading = true;
    showAddTransaction = false;
    showAddGoal = false;

    // New transaction form
    newTransaction: any = {
        amount: null,
        sign: 'NEGATIVE',
        categoryId: '',
        transactionDate: new Date().toISOString().slice(0, 16)
    };

    // New goal form
    newGoal: any = {
        categoryId: '',
        maxAmount: null,
        type: 'MONTHLY',
        startDate: '',
        endDate: ''
    };

    // OCR
    ocrResult: OcrResult | null = null;
    ocrLoading = false;
    receiptText = '';
    selectedFile: File | null = null;
    previewUrl: string | null = null;

    constructor(
        private authService: AuthService,
        private transactionService: TransactionService,
        private categoryService: CategoryService,
        private goalService: GoalService,
        private analyticsService: AnalyticsService,
        private ocrService: OcrService,
        private exportService: ExportService,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.currentUser = this.authService.currentUser;
        if (!this.currentUser) {
            this.router.navigate(['/login']);
            return;
        }
        this.loadAllData();
    }

    loadAllData(): void {
        this.loading = true;
        const userId = this.currentUser!.userId;

        this.categoryService.findAll().subscribe({
            next: (data) => this.categories = data,
            error: (err) => console.error('Error loading categories:', err)
        });

        this.transactionService.findByUser(userId).subscribe({
            next: (data) => {
                this.transactions = data;
                this.loading = false;
            },
            error: (err) => { console.error('Error:', err); this.loading = false; }
        });

        this.goalService.findAll().subscribe({
            next: (data) => this.goals = data.filter(g => g.userId === userId),
            error: (err) => console.error('Error loading goals:', err)
        });

        this.analyticsService.getStats(userId).subscribe({
            next: (data) => this.stats = data,
            error: (err) => console.error('Error loading stats:', err)
        });

        this.analyticsService.getForecast(userId).subscribe({
            next: (data) => this.forecast = data,
            error: (err) => console.error('Error loading forecast:', err)
        });
    }

    // Transactions
    addTransaction(): void {
        if (!this.newTransaction.amount || !this.newTransaction.categoryId) return;

        const tx: any = {
            userId: this.currentUser!.userId,
            amount: this.newTransaction.amount,
            sign: this.newTransaction.sign,
            categoryId: this.newTransaction.categoryId,
            transactionDate: this.newTransaction.transactionDate
        };

        this.transactionService.create(tx).subscribe({
            next: () => {
                this.showAddTransaction = false;
                this.resetTransactionForm();
                this.loadAllData();
            },
            error: (err) => console.error('Error creating transaction:', err)
        });
    }

    deleteTransaction(id: string | undefined): void {
        if (!id || !confirm('Delete this transaction?')) return;
        this.transactionService.delete(id).subscribe({
            next: () => this.loadAllData(),
            error: (err) => console.error('Error deleting transaction:', err)
        });
    }

    resetTransactionForm(): void {
        this.newTransaction = {
            amount: null,
            sign: 'NEGATIVE',
            categoryId: '',
            transactionDate: new Date().toISOString().slice(0, 16)
        };
    }

    // Goals
    addGoal(): void {
        if (!this.newGoal.maxAmount || !this.newGoal.categoryId) return;

        const goal: any = {
            userId: this.currentUser!.userId,
            categoryId: this.newGoal.categoryId,
            maxAmount: this.newGoal.maxAmount,
            type: this.newGoal.type,
            startDate: this.newGoal.startDate,
            endDate: this.newGoal.endDate
        };

        this.goalService.create(goal).subscribe({
            next: () => {
                this.showAddGoal = false;
                this.newGoal = { categoryId: '', maxAmount: null, type: 'MONTHLY', startDate: '', endDate: '' };
                this.loadAllData();
            },
            error: (err) => console.error('Error creating goal:', err)
        });
    }

    deleteGoal(id: string | undefined): void {
        if (!id || !confirm('Delete this goal?')) return;
        this.goalService.delete(id).subscribe({
            next: () => this.loadAllData(),
            error: (err) => console.error('Error deleting goal:', err)
        });
    }

    // OCR Smart Scan
    onFileSelected(event: any): void {
        const file = event.target.files[0];
        if (file) {
            this.selectedFile = file;
            const reader = new FileReader();
            reader.onload = () => this.previewUrl = reader.result as string;
            reader.readAsDataURL(file);
        }
    }

    scanReceipt(): void {
        if (!this.selectedFile) return;
        this.ocrLoading = true;
        this.ocrResult = null;

        this.ocrService.scanReceipt(this.selectedFile).subscribe({
            next: (result) => {
                this.ocrResult = result;
                this.ocrLoading = false;
            },
            error: (err) => {
                console.error('OCR error:', err);
                this.ocrLoading = false;
            }
        });
    }

    scanText(): void {
        if (!this.receiptText.trim()) return;
        this.ocrLoading = true;
        this.ocrResult = null;

        this.ocrService.scanReceiptText(this.receiptText).subscribe({
            next: (result) => {
                this.ocrResult = result;
                this.ocrLoading = false;
            },
            error: (err) => {
                console.error('OCR text error:', err);
                this.ocrLoading = false;
            }
        });
    }

    createTransactionFromOcr(): void {
        if (!this.ocrResult) return;

        const matchedCategory = this.categories.find(
            c => c.name.toLowerCase() === this.ocrResult!.category.toLowerCase()
        );

        const tx: any = {
            userId: this.currentUser!.userId,
            amount: this.ocrResult.amount,
            sign: 'NEGATIVE',
            categoryId: matchedCategory?.id || this.categories[0]?.id || '',
            transactionDate: this.ocrResult.date || new Date().toISOString()
        };

        this.transactionService.create(tx).subscribe({
            next: () => {
                this.ocrResult = null;
                this.selectedFile = null;
                this.previewUrl = null;
                this.receiptText = '';
                this.loadAllData();
                this.activeTab = 'transactions';
            },
            error: (err) => console.error('Error creating transaction from OCR:', err)
        });
    }

    // PDF Export
    exportPdf(): void {
        this.exportService.downloadPdf();
    }

    // Logout
    logout(): void {
        this.authService.logout();
        this.router.navigate(['/login']);
    }

    // Helpers
    getCategoryColor(categoryId: string | undefined): string {
        if (!categoryId) return '#95a5a6';
        const cat = this.categories.find(c => c.id === categoryId);
        return cat?.color || '#95a5a6';
    }

    getCategoryName(categoryId: string | undefined): string {
        if (!categoryId) return 'N/A';
        const cat = this.categories.find(c => c.id === categoryId);
        return cat?.name || 'N/A';
    }

    getBarWidth(percentage: number): string {
        return Math.min(percentage, 100) + '%';
    }
}
