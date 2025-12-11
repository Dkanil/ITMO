import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HomeService {

  constructor(private http: HttpClient) {
  }

  submit(token: string, password: string): Observable<any> {
    const url = `http://localhost:8080/home/submit}`;
    return this.http.post(url, {token, password});
  }
}
