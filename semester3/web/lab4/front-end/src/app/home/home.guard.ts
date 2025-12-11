import {Injectable} from '@angular/core';
import {CanActivate, Router, UrlTree} from '@angular/router';

@Injectable({providedIn: 'root'})
export class HomeGuard implements CanActivate {
  constructor(private router: Router) {
  }

  canActivate(): boolean | UrlTree {
    return !localStorage.getItem('token') ? this.router.createUrlTree(['/auth']) : true;
  }
}
