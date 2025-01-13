import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LeaveService, Leave } from '../../services/leave.service';

@Component({
  selector: 'app-leave-request',
  templateUrl: './leave-request.component.html',
  styleUrls: ['./leave-request.component.css']
})
export class LeaveRequestComponent implements OnInit {
  leaveForm: FormGroup;
  leaves: Leave[] = [];
  displayedColumns: string[] = ['id', 'startDate', 'endDate', 'type', 'status', 'reason', 'actions'];
  leaveTypes = ['VACATION', 'SICK', 'PERSONAL', 'MATERNITY', 'PATERNITY'];

  constructor(
    private fb: FormBuilder,
    private leaveService: LeaveService
  ) {
    this.leaveForm = this.fb.group({
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      type: ['', Validators.required],
      reason: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadLeaves();
  }

  loadLeaves(): void {
    this.leaveService.getLeaves()
      .subscribe(leaves => this.leaves = leaves);
  }

  onSubmit(): void {
    if (this.leaveForm.valid) {
      const leave = {
        ...this.leaveForm.value,
        employeeId: 1, // Replace with actual logged-in employee ID
        status: 'PENDING'
      };
      this.leaveService.requestLeave(leave)
        .subscribe(() => {
          this.loadLeaves();
          this.leaveForm.reset();
        });
    }
  }

  approveLeave(id: number): void {
    this.leaveService.approveLeave(id, 1) // Replace with actual approver ID
      .subscribe(() => this.loadLeaves());
  }

  rejectLeave(id: number): void {
    this.leaveService.rejectLeave(id, 1) // Replace with actual approver ID
      .subscribe(() => this.loadLeaves());
  }
}