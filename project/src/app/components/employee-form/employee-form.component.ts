import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { EmployeeService } from '../../services/employee.service';
import { DepartmentService } from '../../services/department.service';

@Component({
  selector: 'app-employee-form',
  templateUrl: './employee-form.component.html',
  styleUrls: ['./employee-form.component.css']
})
export class EmployeeFormComponent implements OnInit {
  employeeForm: FormGroup;
  departments: any[] = [];
  isEditMode = false;
  employeeId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private employeeService: EmployeeService,
    private departmentService: DepartmentService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.employeeForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      position: ['', Validators.required],
      salary: ['', [Validators.required, Validators.min(0)]],
      departmentId: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadDepartments();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.employeeId = +id;
      this.loadEmployee(this.employeeId);
    }
  }

  loadDepartments(): void {
    this.departmentService.getDepartments()
      .subscribe(departments => this.departments = departments);
  }

  loadEmployee(id: number): void {
    this.employeeService.getEmployee(id)
      .subscribe(employee => this.employeeForm.patchValue(employee));
  }

  onSubmit(): void {
    if (this.employeeForm.valid) {
      const employee = this.employeeForm.value;
      if (this.isEditMode && this.employeeId) {
        this.employeeService.updateEmployee(this.employeeId, employee)
          .subscribe(() => this.router.navigate(['/employees']));
      } else {
        this.employeeService.createEmployee(employee)
          .subscribe(() => this.router.navigate(['/employees']));
      }
    }
  }
}