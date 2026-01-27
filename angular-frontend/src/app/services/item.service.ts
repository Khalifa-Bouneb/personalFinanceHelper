import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Item } from '../models/models';

@Injectable({
    providedIn: 'root'
})
export class ItemService {
    private apiUrl = 'http://localhost:8080/api/items';

    constructor(private http: HttpClient) { }

    findAll(): Observable<Item[]> {
        return this.http.get<Item[]>(this.apiUrl);
    }

    findOne(id: string): Observable<Item> {
        return this.http.get<Item>(`${this.apiUrl}/${id}`);
    }

    create(item: Item): Observable<Item> {
        return this.http.post<Item>(this.apiUrl, item);
    }

    update(id: string, item: Item): Observable<Item> {
        return this.http.put<Item>(`${this.apiUrl}/${id}`, item);
    }

    delete(id: string): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
