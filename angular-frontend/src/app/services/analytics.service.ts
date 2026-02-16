import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DashboardStats, ForecastResult } from '../models/models';

@Injectable({
    providedIn: 'root'
})
export class AnalyticsService {
    private apiUrl = 'http://localhost:8080/api/analytics';

    constructor(private http: HttpClient) { }

    getStats(userId: number): Observable<DashboardStats> {
        return this.http.get<DashboardStats>(`${this.apiUrl}/stats/${userId}`);
    }

    getForecast(userId: number): Observable<ForecastResult> {
        return this.http.get<ForecastResult>(`${this.apiUrl}/forecast/${userId}`);
    }
}
