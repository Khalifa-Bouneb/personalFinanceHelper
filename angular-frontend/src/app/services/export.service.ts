import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from './auth.service';

@Injectable({
    providedIn: 'root'
})
export class ExportService {
    private apiUrl = 'http://localhost:8080/api/export';

    constructor(private http: HttpClient, private authService: AuthService) { }

    downloadPdf(): void {
        const userId = this.authService.currentUser?.userId;
        if (!userId) return;

        this.http.get(`${this.apiUrl}/pdf/${userId}`, {
            responseType: 'blob'
        }).subscribe(blob => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'finance-report.pdf';
            a.click();
            window.URL.revokeObjectURL(url);
        });
    }
}
