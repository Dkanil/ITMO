import { Routes } from '@angular/router';
import {HomeComponent} from './home/home';
import {AuthComponent} from './auth/auth';


export const routes: Routes = [
  { path: "home", component: HomeComponent },
  { path: "auth", component: AuthComponent },
  { path: "", redirectTo: "auth", pathMatch: "full"  }
];
