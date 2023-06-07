import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {Router} from "@angular/router";
import {QuizAuthService} from "./quiz-auth/quiz-auth.service";

@Component({
  selector: 'app-quiz',
  templateUrl: './quiz.component.html',
  styleUrls: ['./quiz.component.css']
})
export class QuizComponent implements OnInit, OnDestroy {

  // @ts-ignore
  subscription: Subscription;

  constructor(private quizAuthService: QuizAuthService, private router: Router) {
  }

  ngOnInit() {
    this.subscription = this.quizAuthService.guest.subscribe(user => {
      if (!user) {
        this.router.navigate(['/quiz/auth'],{ replaceUrl: true })
      } else {
        this.router.navigate(['/quiz/home'], { replaceUrl: true });
      }
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

}
