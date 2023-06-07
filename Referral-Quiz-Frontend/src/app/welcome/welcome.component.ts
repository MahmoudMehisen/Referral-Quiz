import {Component, OnInit} from '@angular/core';
import {QuizAuthService} from "../quiz/quiz-auth/quiz-auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css'],
})
export class WelcomeComponent {

  constructor(private router:Router) {

  }

  play(){
    this.router.navigate(['/quiz']);
  }

}
