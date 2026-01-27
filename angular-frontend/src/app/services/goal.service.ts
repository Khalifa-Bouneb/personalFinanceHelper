import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Goal } from '../models/models';

@Injectable({
    providedIn: 'root'
})
export class GoalService {
    private apiUrl = 'http://localhost:8080/api/goals';

    constructor(private http: HttpClient) { }

    findAll(): Observable<Goal[]> {
        return this.http.get<Goal[]>(this.apiUrl);
    }

    findOne(id: string): Observable<Goal> {
        return this.http.get<Goal>(`${this.apiUrl}/${id}`);
    }

    create(goal: Goal): Observable<Goal> {
        return this.http.post<Goal>(this.apiUrl, goal);
    }

    update(id: string, goal: Goal): Observable<Goal> {
        return this.http.put<Goal>(`${this.apiUrl}/${id}`, goal);
    }

    delete(id: string): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
