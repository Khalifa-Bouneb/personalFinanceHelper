import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { LoginRequest, RegisterRequest, AuthResponse } from '../models/models';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private apiUrl = 'http://localhost:8080/api/auth';
    private currentUserSubject = new BehaviorSubject<AuthResponse | null>(null);
    public currentUser$ = this.currentUserSubject.asObservable();

    constructor(private http: HttpClient) {
        const stored = localStorage.getItem('currentUser');
        if (stored) {
            this.currentUserSubject.next(JSON.parse(stored));
        }
    }

    get currentUser(): AuthResponse | null {
        return this.currentUserSubject.value;
    }

    get isLoggedIn(): boolean {
        return !!this.currentUser;
    }

    get token(): string | null {
        return this.currentUser?.token || null;
    }

    login(request: LoginRequest): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${this.apiUrl}/login`, request).pipe(
            tap((response: AuthResponse) => {
                localStorage.setItem('currentUser', JSON.stringify(response));
                this.currentUserSubject.next(response);
            })
        );
    }

    register(request: RegisterRequest): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${this.apiUrl}/register`, request).pipe(
            tap((response: AuthResponse) => {
                localStorage.setItem('currentUser', JSON.stringify(response));
                this.currentUserSubject.next(response);
            })
        );
    }

    logout(): void {
        localStorage.removeItem('currentUser');
        this.currentUserSubject.next(null);
    }
}
