import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Transaction } from '../models/models';

@Injectable({
    providedIn: 'root'
})
export class TransactionService {
    private apiUrl = 'http://localhost:8080/api/transactions';

    constructor(private http: HttpClient) { }

    findAll(): Observable<Transaction[]> {
        return this.http.get<Transaction[]>(this.apiUrl);
    }

    findOne(id: string): Observable<Transaction> {
        return this.http.get<Transaction>(`${this.apiUrl}/${id}`);
    }

    create(transaction: Transaction): Observable<Transaction> {
        return this.http.post<Transaction>(this.apiUrl, transaction);
    }

    update(id: string, transaction: Transaction): Observable<Transaction> {
        return this.http.put<Transaction>(`${this.apiUrl}/${id}`, transaction);
    }

    delete(id: string): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
