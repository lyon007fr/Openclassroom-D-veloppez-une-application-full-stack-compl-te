import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms'; // Import FormsModule ici
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { RegisterComponent } from './feature/auth/register/register.component'; // Import du composant RegisterComponent

import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; // Nécessaire pour Angular Material
import { MatFormFieldModule } from '@angular/material/form-field'; // Pour mat-form-field et mat-label
import { MatInputModule } from '@angular/material/input'; // Pour matInput
import { MatButtonModule } from '@angular/material/button'; // Pour les boutons
import { MatIconModule } from '@angular/material/icon';
import { HomeComponent } from './components/home/home.component';
import { NavbarComponent } from './components/navbar/navbar.component'; // Import du module des icônes
import { MatToolbarModule } from '@angular/material/toolbar';
import { ArticleListComponent } from './feature/articles/components/article-list/article-list.component';
import { ArticleDetailComponent } from './feature/articles/components/article-detail/article-detail.component';
import {MatCardModule} from '@angular/material/card';
import { MatSelectModule } from '@angular/material/select';
import { MatListModule } from '@angular/material/list';
import { LoginComponent } from './feature/auth/login/login.component';
import { JwtInterceptor } from './feature/auth/interceptors/jwt.interceptor';
import { ReactiveFormsModule } from '@angular/forms';
import { CreateArticleComponent } from './feature/articles/components/form/form.component';
import { ThemeComponent } from './feature/themes/theme/theme.component';
import { MeComponent } from './feature/me/component/me/me.component';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSidenavModule } from '@angular/material/sidenav';


@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    HomeComponent,
    NavbarComponent,
    ArticleListComponent,
    ArticleDetailComponent,
    LoginComponent,
    CreateArticleComponent,
    ThemeComponent,
    MeComponent,
    NavbarComponent, 
        
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule, // Toujours inclure pour Angular Material
    FormsModule, // Ajout de FormsModule
    HttpClientModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    MatCardModule,
    MatSelectModule,
    MatListModule,
    ReactiveFormsModule,
    MatSnackBarModule,
    MatSidenavModule,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }, // Ajout de l'intercepteur JWT
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
