import {Injectable} from "@angular/core";
import {Question} from "../../shared/models/question.model";
import {Subject} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {Guest} from "../../shared/models/guest.model";
import {Answer} from "../../shared/models/answer.model";
import {QuizAuthService} from "../quiz-auth/quiz-auth.service";
import {ReferralTokenModel} from "src/app/shared/models/referral-token.model";
import {QuizHomeService} from "../quiz-home/quiz-home.service";

@Injectable({providedIn: 'root'})
export class QuizGameService {

  questionsList: Question[] = [];

  // @ts-ignore
  isDataLoading = new Subject<boolean>(true);


  constructor(private http: HttpClient, private router: Router, private quizHomeService: QuizHomeService) {
  }

  startGame(phone: string) {
    this.isDataLoading.next(true);

    this.http.post<Question[]>('http://localhost:8080/api/quiz/startQuiz/' + phone, {}).subscribe(res => {
      this.questionsList = res;
      this.isDataLoading.next(false);
    }, error => {
      this.isDataLoading.next(false);
      this.questionsList = [];
    });
  }

  submitGame(phone: string, answers: Answer[]) {
    this.isDataLoading.next(true);

    this.http.post<Guest>('http://localhost:8080/api/quiz/submitQuiz', {
      phoneNumber: phone,
      answerQuestionOptionList: answers
    }).subscribe(res => {

      this.quizHomeService.guest = res;
      this.quizHomeService.isDataLoading.next(false);
      this.isDataLoading.next(false);
      this.router.navigate(['/quiz/home'])
    }, error => {
      this.isDataLoading.next(false);
    });
  }

  generateReferralToken(phoneNumber: string) {
    return this.http.post<ReferralTokenModel>('http://localhost:8080/api/quiz/referralAnotherUser', {
      newGuestPhone: phoneNumber,
      currentGuestPhone: this.quizHomeService.guest.phoneNumber,
    })
  }

}
