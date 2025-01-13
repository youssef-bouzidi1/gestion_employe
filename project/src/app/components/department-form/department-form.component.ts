import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DepartmentService, Department } from '../../services/department.service';

@Component({
  selector: 'app-department-form',
  templateUrl: './department-form.component.html',
  styleUrls: ['./department-form.component.css']
})
export class DepartmentFormComponent {
  departmentForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private departmentService: DepartmentService,
    private dialogRef: MatDialogRef<DepartmentFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Department | null
  ) {
    this.departmentForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required]
    });

    if (data) {
      this.departmentForm.patchValue(data);
    }
  }

  onSubmit(): void {
    if (this.departmentForm.valid) {
      const department = this.departmentForm.value;
      if (this.data?.id) {
        this.departmentService.updateDepartment(this.data.id, department)
          .subscribe(() => this.dialogRef.close(true));
      } else {
        this.departmentService.createDepartment(department)
          .subscribe(() => this.dialogRef.close(true));
      }
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}