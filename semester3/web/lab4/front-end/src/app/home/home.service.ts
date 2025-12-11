import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Point} from './point';

@Injectable({
  providedIn: 'root'
})
export class HomeService {

  constructor(private http: HttpClient) {
  }

  submit(point: Point): Observable<any> {
    return this.http.post("/home", { point });
  }

  getAllPoints(): Observable<any> {
    return this.http.get<Point[]>('/home');
  }
}
