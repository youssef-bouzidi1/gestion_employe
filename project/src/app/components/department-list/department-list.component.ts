import { Component, OnInit } from '@angular/core';
import { DepartmentService, Department } from '../../services/department.service';
import { MatDialog } from '@angular/material/dialog';
import { DepartmentFormComponent } from '../department-form/department-form.component';

@Component({
  selector: 'app-department-list',
  templateUrl: './department-list.component.html',
  styleUrls: ['./department-list.component.css']
})
export class DepartmentListComponent implements OnInit {
  departments: Department[] = [];
  displayedColumns: string[] = ['id', 'name', 'description', 'actions'];

  constructor(
    private departmentService: DepartmentService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.loadDepartments();
  }

  loadDepartments(): void {
    this.departmentService.getDepartments()
      .subscribe(departments => this.departments = departments);
  }

  openDepartmentDialog(department?: Department): void {
    const dialogRef = this.dialog.open(DepartmentFormComponent, {
      width: '500px',
      data: department
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadDepartments();
      }
    });
  }

  deleteDepartment(id: number): void {
    if (confirm('Are you sure you want to delete this department?')) {
      this.departmentService.deleteDepartment(id)
        .subscribe(() => this.loadDepartments());
    }
  }
}