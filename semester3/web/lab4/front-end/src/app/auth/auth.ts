import {ChangeDetectorRef, Component} from '@angular/core';
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
  form: FormGroup;
  errorMessage = '';

  constructor(private fb: FormBuilder,
              private authService: AuthService,
              private router: Router,
              private cdr: ChangeDetectorRef) {
    this.form = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      isRegistration: [false]
    });

    this.form.valueChanges.subscribe(() => {
      this.errorMessage = '';
    });
  }

  submit() {
    const val = this.form.value;
    this.errorMessage = '';
    if (!val.username || !val.password) {
      this.errorMessage = 'Введите имя пользователя и пароль';
      return;
    }

    this.authService.authenticate(val.username, val.password, val.isRegistration)
      .subscribe({
        next: (response) => {
          if (!response || !response.token) {
            this.errorMessage = 'Ошибка авторизации: Сервер не вернул токен';
            this.cdr.detectChanges(); // todo нужно ли?
            console.error(this.errorMessage);
            return;
          }
          localStorage.setItem('token', response.token);
          console.log("Авторизация успешна, токен сохранен в localStorage");
          this.router.navigate(['/home']);
        },
        error: (error) => {
          if (error.status === 0 || error.status >= 500) {
            this.errorMessage = 'Сервер временно недоступен';
          } else if (error.status === 401) {
            this.errorMessage = this.form.value.isRegistration
              ? 'Данное имя пользователя уже занято'
              : 'Неверное имя пользователя или пароль';
          } else {
            this.errorMessage = error.message;
          }
          this.cdr.detectChanges();
          console.error(this.errorMessage);
          return;
        }
      });
  }
}
