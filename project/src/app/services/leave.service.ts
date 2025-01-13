import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Leave {
  id?: number;
  employeeId: number;
  startDate: string;
  endDate: string;
  type: 'VACATION' | 'SICK' | 'PERSONAL' | 'MATERNITY' | 'PATERNITY';
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  reason: string;
  approverId?: number;
}

@Injectable({
  providedIn: 'root'
})
export class LeaveService {
  private apiUrl = 'http://localhost:8080/api/leaves';

  constructor(private http: HttpClient) { }

  getLeaves(): Observable<Leave[]> {
    return this.http.get<Leave[]>(this.apiUrl);
  }

  getLeave(id: number): Observable<Leave> {
    return this.http.get<Leave>(`${this.apiUrl}/${id}`);
  }

  requestLeave(leave: Leave): Observable<Leave> {
    return this.http.post<Leave>(this.apiUrl, leave);
  }

  approveLeave(id: number, approverId: number): Observable<Leave> {
    return this.http.put<Leave>(`${this.apiUrl}/approve/${id}`, { approverId });
  }

  rejectLeave(id: number, approverId: number): Observable<Leave> {
    return this.http.put<Leave>(`${this.apiUrl}/reject/${id}`, { approverId });
  }
}