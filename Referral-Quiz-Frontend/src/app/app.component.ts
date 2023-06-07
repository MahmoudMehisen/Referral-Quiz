import {Component, OnInit} from '@angular/core';
import {AdminAuthService} from "./admin/admin-auth/admin-auth.service";
import {QuizAuthService} from "./quiz/quiz-auth/quiz-auth.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  constructor(private adminAuthService: AdminAuthService,private quizAuthService:QuizAuthService) {}

  ngOnInit() {
    this.adminAuthService.autoLogin();
    this.quizAuthService.autoLogin();
  }
}
