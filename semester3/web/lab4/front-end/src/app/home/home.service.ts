import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Point} from './point';

@Injectable({
  providedIn: 'root'
})
export class HomeService {
  baseUrl = 'http://localhost:8080/home';

  constructor(private http: HttpClient) {
  }

  submit(x: number, y: number, r: number): Observable<any> {
    return this.http.post(this.baseUrl + "/submit", {x, y, r});
  }

  getAllPoints(): Observable<any> {
    return this.http.get<Point[]>(this.baseUrl + '/points');
  }
}
