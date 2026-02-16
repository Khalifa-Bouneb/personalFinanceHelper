import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OcrResult } from '../models/models';

@Injectable({
    providedIn: 'root'
})
export class OcrService {
    private apiUrl = 'http://localhost:8080/api/ocr';

    constructor(private http: HttpClient) { }

    scanReceipt(file: File): Observable<OcrResult> {
        const formData = new FormData();
        formData.append('file', file);
        return this.http.post<OcrResult>(`${this.apiUrl}/scan`, formData);
    }

    scanReceiptText(text: string): Observable<OcrResult> {
        return this.http.post<OcrResult>(`${this.apiUrl}/scan-text`, { text });
    }
}
