import { Component } from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthService} from './auth.service';

@Component({
  selector: 'app-auth',
  imports: [FormsModule, ReactiveFormsModule],
  templateUrl: './auth.html',
  styleUrl: './auth.css',
})

export class AuthComponent {
  form:FormGroup;
  constructor(private fb: FormBuilder,
              private authService: AuthService,
              private router: Router) {
    this.form = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  login() {
    const val = this.form.value;

    if (val.username && val.password) {
      this.authService.login(val.username, val.password)
        .subscribe(
          (response) => {
            console.log("Login successful");
            localStorage.setItem('token', response.token);
            this.router.navigate(['/home']);
          }
        );
    }
  }
}
