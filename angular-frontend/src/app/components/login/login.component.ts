import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [CommonModule, FormsModule],
    template: `
    <div class="auth-container">
      <div class="auth-card">
        <div class="auth-header">
          <h1>💰 Smart Finance Manager</h1>
          <p>Gestion Financière Intelligente & Prédictive</p>
        </div>

        <div class="tab-bar">
          <button [class.active]="mode === 'login'" (click)="mode = 'login'">Login</button>
          <button [class.active]="mode === 'register'" (click)="mode = 'register'">Register</button>
        </div>

        <!-- Login Form -->
        <form *ngIf="mode === 'login'" (ngSubmit)="login()" class="auth-form">
          <div class="form-group">
            <label>Email</label>
            <input type="email" [(ngModel)]="loginData.email" name="email" 
                   placeholder="john@example.com" required>
          </div>
          <div class="form-group">
            <label>Password</label>
            <input type="password" [(ngModel)]="loginData.password" name="password" 
                   placeholder="••••••" required>
          </div>
          <button type="submit" class="btn-primary" [disabled]="loading">
            {{ loading ? 'Logging in...' : 'Login' }}
          </button>
          <p class="demo-hint">Demo: john.doe&#64;example.com / password123</p>
        </form>

        <!-- Register Form -->
        <form *ngIf="mode === 'register'" (ngSubmit)="register()" class="auth-form">
          <div class="form-group">
            <label>Full Name</label>
            <input type="text" [(ngModel)]="registerData.name" name="name" 
                   placeholder="John Doe" required>
          </div>
          <div class="form-group">
            <label>Email</label>
            <input type="email" [(ngModel)]="registerData.email" name="email" 
                   placeholder="john@example.com" required>
          </div>
          <div class="form-group">
            <label>Password</label>
            <input type="password" [(ngModel)]="registerData.password" name="password" 
                   placeholder="Min 6 characters" required>
          </div>
          <div class="form-group">
            <label>Currency</label>
            <select [(ngModel)]="registerData.currency" name="currency">
              <option value="TND">TND - Dinar Tunisien</option>
              <option value="EUR">EUR - Euro</option>
              <option value="USD">USD - Dollar</option>
            </select>
          </div>
          <button type="submit" class="btn-primary" [disabled]="loading">
            {{ loading ? 'Creating account...' : 'Create Account' }}
          </button>
        </form>

        <div *ngIf="error" class="error-msg">{{ error }}</div>
      </div>
    </div>
  `,
    styles: [`
    .auth-container {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      padding: 20px;
    }
    .auth-card {
      background: white;
      border-radius: 16px;
      padding: 40px;
      width: 100%;
      max-width: 440px;
      box-shadow: 0 20px 60px rgba(0,0,0,0.3);
    }
    .auth-header {
      text-align: center;
      margin-bottom: 30px;
    }
    .auth-header h1 {
      color: #2c3e50;
      font-size: 1.8em;
      margin-bottom: 8px;
    }
    .auth-header p {
      color: #7f8c8d;
      font-size: 0.9em;
    }
    .tab-bar {
      display: flex;
      border-radius: 8px;
      overflow: hidden;
      margin-bottom: 30px;
      border: 2px solid #667eea;
    }
    .tab-bar button {
      flex: 1;
      padding: 12px;
      border: none;
      background: white;
      color: #667eea;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.3s;
      font-size: 1em;
    }
    .tab-bar button.active {
      background: #667eea;
      color: white;
    }
    .form-group {
      margin-bottom: 20px;
    }
    .form-group label {
      display: block;
      margin-bottom: 6px;
      font-weight: 600;
      color: #34495e;
      font-size: 0.9em;
    }
    .form-group input, .form-group select {
      width: 100%;
      padding: 12px 16px;
      border: 2px solid #e0e0e0;
      border-radius: 8px;
      font-size: 1em;
      transition: border-color 0.3s;
      box-sizing: border-box;
    }
    .form-group input:focus, .form-group select:focus {
      border-color: #667eea;
      outline: none;
    }
    .btn-primary {
      width: 100%;
      padding: 14px;
      background: linear-gradient(135deg, #667eea, #764ba2);
      color: white;
      border: none;
      border-radius: 8px;
      font-size: 1.1em;
      font-weight: 600;
      cursor: pointer;
      transition: transform 0.2s, box-shadow 0.2s;
    }
    .btn-primary:hover:not(:disabled) {
      transform: translateY(-2px);
      box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
    }
    .btn-primary:disabled {
      opacity: 0.7;
      cursor: not-allowed;
    }
    .error-msg {
      margin-top: 15px;
      padding: 12px;
      background: #fee;
      color: #e74c3c;
      border-radius: 8px;
      text-align: center;
      font-size: 0.9em;
    }
    .demo-hint {
      text-align: center;
      color: #95a5a6;
      font-size: 0.8em;
      margin-top: 15px;
    }
  `]
})
export class LoginComponent {
    mode: 'login' | 'register' = 'login';
    loading = false;
    error = '';

    loginData = { email: '', password: '' };
    registerData = { name: '', email: '', password: '', currency: 'TND' };

    constructor(private authService: AuthService, private router: Router) { }

    login(): void {
        this.loading = true;
        this.error = '';
        this.authService.login(this.loginData).subscribe({
            next: () => {
                this.loading = false;
                this.router.navigate(['/dashboard']);
            },
            error: (err: any) => {
                this.loading = false;
                this.error = err.error?.message || 'Invalid credentials';
            }
        });
    }

    register(): void {
        this.loading = true;
        this.error = '';
        this.authService.register(this.registerData).subscribe({
            next: () => {
                this.loading = false;
                this.router.navigate(['/dashboard']);
            },
            error: (err: any) => {
                this.loading = false;
                this.error = err.error?.message || 'Registration failed';
            }
        });
    }
}
