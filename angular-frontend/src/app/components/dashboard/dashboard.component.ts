import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user.service';
import { CategoryService } from '../../services/category.service';
import { TransactionService } from '../../services/transaction.service';
import { GoalService } from '../../services/goal.service';
import { User, Category, Transaction, Goal } from '../../models/models';

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
    users: User[] = [];
    categories: Category[] = [];
    transactions: Transaction[] = [];
    goals: Goal[] = [];
    loading = true;

    constructor(
        private userService: UserService,
        private categoryService: CategoryService,
        private transactionService: TransactionService,
        private goalService: GoalService
    ) { }

    ngOnInit(): void {
        this.loadData();
    }

    loadData(): void {
        this.loading = true;

        this.userService.findAll().subscribe({
            next: (data) => this.users = data,
            error: (err) => console.error('Error loading users:', err)
        });

        this.categoryService.findAll().subscribe({
            next: (data) => this.categories = data,
            error: (err) => console.error('Error loading categories:', err)
        });

        this.transactionService.findAll().subscribe({
            next: (data) => {
                this.transactions = data;
                this.loading = false;
            },
            error: (err) => {
                console.error('Error loading transactions:', err);
                this.loading = false;
            }
        });

        this.goalService.findAll().subscribe({
            next: (data) => this.goals = data,
            error: (err) => console.error('Error loading goals:', err)
        });
    }

    deleteTransaction(id: string | undefined): void {
        if (!id) return;

        if (confirm('Are you sure you want to delete this transaction?')) {
            this.transactionService.delete(id).subscribe({
                next: () => {
                    this.transactions = this.transactions.filter(t => t.id !== id);
                    alert('Transaction deleted successfully!');
                },
                error: (err) => console.error('Error deleting transaction:', err)
            });
        }
    }

    deleteGoal(id: string | undefined): void {
        if (!id) return;

        if (confirm('Are you sure you want to delete this goal?')) {
            this.goalService.delete(id).subscribe({
                next: () => {
                    this.goals = this.goals.filter(g => g.id !== id);
                    alert('Goal deleted successfully!');
                },
                error: (err) => console.error('Error deleting goal:', err)
            });
        }
    }
}
