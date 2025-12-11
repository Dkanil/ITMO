import {Routes} from '@angular/router';
import {HomeComponent} from './home/home';
import {AuthComponent} from './auth/auth';
import {HomeGuard} from './home/home.guard';
import {AuthGuard} from './auth/auth.guard';


export const routes: Routes = [
  {path: "home", component: HomeComponent, canActivate: [HomeGuard]},
  {path: "auth", component: AuthComponent, canActivate: [AuthGuard]},
  {path: "", redirectTo: "auth", pathMatch: "full"}
];
