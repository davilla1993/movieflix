import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { AddMovieComponent } from './components/add-movie/add-movie.component';

export const routes: Routes = [
    {path: '', title: "movieflix", component: HomeComponent},
    {path: 'login', title: "movieflix - Login", component: LoginComponent},
    {path: 'register', title: "movieflix - Register", component: RegisterComponent},
    {path: 'add-movie', title: "movieflix - Add movie", component: AddMovieComponent}
];
