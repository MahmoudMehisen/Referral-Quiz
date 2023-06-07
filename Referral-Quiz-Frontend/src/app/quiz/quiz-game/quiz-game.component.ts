import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {QuizGameService} from "./quiz-game.service";
import {Question} from "../../shared/models/question.model";
import {Answer} from "../../shared/models/answer.model";
import {QuizAuthService} from "../quiz-auth/quiz-auth.service";
import {MatDialog} from '@angular/material/dialog';
import {QuizReferralComponent} from '../quiz-referral/quiz-referral.component';
import {QuizHomeService} from "../quiz-home/quiz-home.service";

@Component({
  selector: 'app-quiz-game',
  templateUrl: './quiz-game.component.html',
  styleUrls: ['./quiz-game.component.css']
})
export class QuizGameComponent implements OnInit, OnDestroy {

  // @ts-ignore
  subscription: Subscription;
  isLoading = true;

  questions: Question[] = [];

  answers: Answer[] = [];

  canSubmit = false;

  constructor(private quizGameService: QuizGameService, private dialog: MatDialog, private quizHomeService: QuizHomeService) {
  }

  ngOnInit() {
    this.isLoading = true;
    this.subscription = this.quizGameService.isDataLoading.subscribe(res => {
      this.isLoading = res;
      this.questions = this.quizGameService.questionsList;
      this.canSubmit = false;
    })
    this.quizGameService.startGame(this.quizHomeService.guest.phoneNumber);
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  selectAnswer(questionId: number, optionId: number) {
    const answer = this.answers.find(ans => ans.questionId === questionId);
    if (answer) {
      answer.optionId = optionId;
    } else {
      this.answers.push(new Answer(questionId, optionId));
    }
    if (this.answers.length === this.questions.length) {
      this.canSubmit = true;
    }
  }

  submitAnswers() {
    if (this.quizHomeService.metadata.canUserDoReferral) {
      this.dialog.open(QuizReferralComponent).afterClosed().subscribe(res => {
        this.quizGameService.submitGame(this.quizHomeService.guest.phoneNumber, this.answers);
      });
    } else {
      this.quizGameService.submitGame(this.quizHomeService.guest.phoneNumber, this.answers);
    }
  }
}
