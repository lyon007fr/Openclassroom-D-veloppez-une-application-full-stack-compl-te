import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './feature/auth/register/register.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './feature/auth/login/login.component';
import { ArticleListComponent } from './feature/articles/components/article-list/article-list.component';
import { UnGuard } from './feature/auth/guards/unautGard';
import { AuthGuard } from './feature/auth/guards/authGard';
import { ArticleDetailComponent } from './feature/articles/components/article-detail/article-detail.component';
import { CreateArticleComponent } from './feature/articles/components/form/form.component';
import { ThemeComponent } from './feature/themes/theme/theme.component';
import { MeComponent } from './feature/me/component/me/me.component';

const routes: Routes = [
  // Route pour la page d'inscription, protégée par UnGuard (seulement accessible si non connecté)
  { path: 'register', component: RegisterComponent, canActivate: [UnGuard] },

  // Route pour la page d'accueil, accessible à tous
  { path: 'home', component: HomeComponent },

  // Route pour la page de connexion, protégée par UnGuard (seulement accessible si non connecté)
  { path: 'connexion', component: LoginComponent, canActivate: [UnGuard] },

  // Route pour led articles, protégée par AuthGuard (seulement accessible si connecté)
  {
    path: 'articles',
    component: ArticleListComponent,
    canActivate: [AuthGuard],
  },

  // Route pour led articles, protégée par AuthGuard (seulement accessible si connecté)
  {
    path: 'article/:id',
    component: ArticleDetailComponent,
    canActivate: [AuthGuard],
  },

  // Route pour led articles, protégée par AuthGuard (seulement accessible si connecté)
  {
    path: 'article',
    component: CreateArticleComponent,
    canActivate: [AuthGuard],
  },

  // Route pour led articles, protégée par AuthGuard (seulement accessible si connecté)
  { path: 'themes', component: ThemeComponent, canActivate: [AuthGuard] },

  //Route pour la page de profil, protégée par AuthGuard (seulement accessible si connecté)
  { path: 'me', component: MeComponent, canActivate: [AuthGuard] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
