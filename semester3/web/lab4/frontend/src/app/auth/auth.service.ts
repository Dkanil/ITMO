import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {
  }

  authenticate(username: string, password: string, isRegistration: boolean): Observable<any> {
    const url = `http://localhost:8080/auth/sign-${(isRegistration ? 'up' : 'in')}`;
    return this.http.post(url, {username, password});
  }
}
