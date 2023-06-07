import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {WelcomeComponent} from "./welcome/welcome.component";
import {AdminComponent} from "./admin/admin.component";
import {AdminAuthComponent} from "./admin/admin-auth/admin-auth.component";
import {AdminGuard} from "./admin/admin-auth/admin-guard";
import {AdminHomeComponent} from "./admin/admin-home/admin-home.component";
import {AddQuestionComponent} from "./admin/admin-home/add-question/add-question.component";
import {InvitationComponent} from "./invitation/invitation.component";
import {PagenotfoundComponent} from "./pagenotfound/pagenotfound.component";
import {QuizComponent} from "./quiz/quiz.component";
import {QuizAuthComponent} from "./quiz/quiz-auth/quiz-auth.component";
import {QuizHomeComponent} from "./quiz/quiz-home/quiz-home.component";
import {QuizGameComponent} from "./quiz/quiz-game/quiz-game.component";
import {RedeemListComponent} from "./quiz/redeem/redeem-list/redeem-list.component";

const routes: Routes = [
  {path: '', component: WelcomeComponent},
  {
    path: 'admin', component: AdminComponent, children: [
      {path: 'auth', component: AdminAuthComponent},
      {path: 'home', component: AdminHomeComponent},
    ]
  },
  {
    path: 'quiz', component: QuizComponent, children: [
      {path: 'auth', component: QuizAuthComponent},
      {path: 'home', component: QuizHomeComponent},
      {path: 'game', component: QuizGameComponent},
      {path: 'redeem-list', component: RedeemListComponent}
    ]
  },
  {path: 'invitation/:token', component: InvitationComponent},
  {path: '**', pathMatch: 'full', component: PagenotfoundComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
