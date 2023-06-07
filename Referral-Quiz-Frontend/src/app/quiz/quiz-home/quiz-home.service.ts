import {Injectable} from "@angular/core";
import {QuizMetadata} from "../../shared/models/quiz-metadata.model";
import {forkJoin, lastValueFrom, Subject} from "rxjs";
import {Guest} from "../../shared/models/guest.model";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {QuestionGroup} from "../../shared/models/question-group.model";
import {Redeem} from "../../shared/models/redeem.model";
import {RedeemHistory} from "../../shared/models/redeem-history.model";
import {QuizAuthService} from "../quiz-auth/quiz-auth.service";

@Injectable({providedIn: 'root'})
export class QuizHomeService {
  // @ts-ignore
  metadata: QuizMetadata;

  // @ts-ignore
  isDataLoading = new Subject<boolean>(true);

  // @ts-ignore
  guest: Guest;

  constructor(private quizAuthService: QuizAuthService, private http: HttpClient, private router: Router) {
  }

  fetchQuizHomeData() {
    if (this.quizAuthService.guest.getValue() != null) {
      this.isDataLoading.next(true);
      const requests = [
        this.fetchCurrentGuest(this.quizAuthService.guest.getValue().phoneNumber),
        this.fetchQuizMetadata()
      ];

      forkJoin(requests).subscribe(results => {

        this.guest = results[0] as Guest;
        this.metadata = results[1] as QuizMetadata;

        this.isDataLoading.next(false);
      });
    }
  }

  updateGuest(phoneNumber: string, email: string) {
    this.isDataLoading.next(true);
    return this.http.post<Guest>('http://localhost:8080/api/guest/updateInfo', {
      email: email,
      phoneNumber: phoneNumber
    }).subscribe(res => {
      this.guest = res;
      this.isDataLoading.next(false);
    },error => {
      this.isDataLoading.next(false);
    })
  }

  private async fetchCurrentGuest(phone: string): Promise<Guest> {
    return await lastValueFrom(this.http.get<Guest>('http://localhost:8080/api/guest/getGuestByPhone/' + phone));
  }

  private async fetchQuizMetadata(): Promise<QuizMetadata> {
    return await lastValueFrom(this.http.get<QuizMetadata>('http://localhost:8080/api/guest/getQuizMetadata'));
  }

}
