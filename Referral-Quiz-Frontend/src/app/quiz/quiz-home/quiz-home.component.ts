import {Component, OnDestroy, OnInit} from '@angular/core';
import {Guest} from "../../shared/models/guest.model";
import {Subscription} from "rxjs";
import {QuizAuthService} from "../quiz-auth/quiz-auth.service";
import {Router} from "@angular/router";
import {QuizMetadata} from 'src/app/shared/models/quiz-metadata.model';
import {HttpClient} from "@angular/common/http";
import {QuizHomeService} from "./quiz-home.service";

@Component({
  selector: 'app-quiz-home',
  templateUrl: './quiz-home.component.html',
  styleUrls: ['./quiz-home.component.css']
})
export class QuizHomeComponent implements OnInit, OnDestroy {
  isLoading = true;
  // @ts-ignore
  guest: Guest;
  // @ts-ignore
  subscription: Subscription;
  // @ts-ignore
  quizMetadata: QuizMetadata;
  email = '';

  constructor(private quizHomeService: QuizHomeService, private quizAuthService: QuizAuthService, private router: Router, private http: HttpClient) {
  }

  ngOnInit() {
    this.isLoading = true;

    this.subscription = this.quizHomeService.isDataLoading.subscribe(res => {
      this.isLoading = res;
      this.guest = this.quizHomeService.guest;
      this.quizMetadata = this.quizHomeService.metadata;
    })

    this.quizHomeService.fetchQuizHomeData();
  }

  playGame() {
    this.router.navigate(['/quiz/game'], {replaceUrl: true})
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  updateEmail() {
    if (this.email.length > 0) {
      this.isLoading = true;
      this.quizHomeService.updateGuest(this.guest.phoneNumber, this.email)
    }

  }

  logout() {
    this.quizAuthService.logout();
  }

  redeems() {
    this.router.navigate(['/quiz/redeem-list']);
  }
}
