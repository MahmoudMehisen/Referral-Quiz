import {Injectable} from "@angular/core";
import {Question} from "../../shared/models/question.model";
import {Subject, lastValueFrom, forkJoin} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {AdminAuthService} from "../admin-auth/admin-auth.service";
import {QuizMetadata} from "../../shared/models/quiz-metadata.model";
import {ReferralTokenModel} from "../../shared/models/referral-token.model";
import {QuestionGroup} from "../../shared/models/question-group.model";
import {Guest} from "../../shared/models/guest.model";
import {Redeem} from "../../shared/models/redeem.model";
import {RedeemHistory} from "../../shared/models/redeem-history.model";


@Injectable({providedIn: 'root'})
export class AdminHomeService {

  groupsList: QuestionGroup[] = [];
  guestsList: Guest[] = [];
  redeemList: Redeem[] = [];
  redeemHistoryList: RedeemHistory[] = []
  // @ts-ignore
  metadata: QuizMetadata;

  // @ts-ignore
  isDataLoading = new Subject<boolean>(true);

  constructor(private http: HttpClient, private adminAuthService: AdminAuthService) {
  }

  fetchAdminHomeData() {
    if (this.adminAuthService.admin.getValue() != null) {
      this.isDataLoading.next(true);
      const requests = [
        this.fetchGroupsList(),
        this.fetchQuizMetadata(),
        this.fetchGuests(),
        this.fetchRedeems(),
        this.fetchRedeemHistory()
      ];

      forkJoin(requests).subscribe(results => {

        this.groupsList = results[0] as QuestionGroup[];
        this.metadata = results[1] as QuizMetadata;
        this.guestsList = results[2] as Guest[];
        this.redeemList = results[3] as Redeem[];
        this.redeemHistoryList = results[4] as RedeemHistory[];
        this.isDataLoading.next(false);
      });
    }
  }

  addNewGroup(name: string) {
    this.isDataLoading.next(true);
    return this.http.post<QuestionGroup>('http://localhost:8080/api/admin/addGroup', {
      name: name,
    }).subscribe(res => {
      this.groupsList.push(res);
      this.isDataLoading.next(false);
    })
  }

  updateGroup(groupInd: number, group: QuestionGroup) {
    this.isDataLoading.next(true);
    return this.http.post<QuestionGroup>('http://localhost:8080/api/admin/updateGroup', {
      id: group.id,
      name: group.name
    }).subscribe(res => {
      this.groupsList[groupInd] = res;
      this.isDataLoading.next(false);
    }, error => {
      console.log(error);
      this.isDataLoading.next(false);
    });
  }

  addNewQuestion(question: string, options: any[], groupId: number) {
    return this.http.post('http://localhost:8080/api/admin/addQuestion', {
      groupId: groupId,
      questionText: question,
      questionOptions: options
    })
  }

  updateQuestion(groupInd: number, questionInd: number, question: Question) {
    return this.http.post<Question>('http://localhost:8080/api/admin/updateQuestion', {
      id: question.id,
      questionText: question.questionText,
      options: question.options
    }).subscribe(res => {
      this.groupsList[groupInd].questions[questionInd] = res;
      this.isDataLoading.next(false);
    }, error => {
      console.log(error);
      this.isDataLoading.next(false);
    });
  }

  deleteQuestion(groupInd: number, questionInd: number, question: Question) {
    return this.http.delete('http://localhost:8080/api/admin/deleteQuestion/' + question.id).subscribe(res => {
      console.log(res);
      this.groupsList[groupInd].questions.splice(questionInd, 1);
      this.isDataLoading.next(false);
    }, error => {
      console.log(error);
      this.isDataLoading.next(false);
    });
  }

  updateMetadata(newMetadata: QuizMetadata) {
    this.isDataLoading.next(true);
    return this.http.post<QuizMetadata>('http://localhost:8080/api/admin/updateMetadata', newMetadata).subscribe(res => {
      this.metadata = res;
      this.isDataLoading.next(false);
    });
  }

  activeGroup(groupId: number) {
    this.isDataLoading.next(true);
    return this.http.post<QuizMetadata>('http://localhost:8080/api/admin/activeGroup/' + groupId, {}).subscribe(res => {
      this.metadata = res;
      this.isDataLoading.next(false);
    });
  }

  stopQuiz() {
    this.isDataLoading.next(true);
    return this.http.post<QuizMetadata>('http://localhost:8080/api/admin/stopQuiz', {}).subscribe(res => {
      this.metadata = res;
      this.isDataLoading.next(false);
    });
  }

  generateReferralToken(phoneNumber: string) {
    return this.http.post<ReferralTokenModel>('http://localhost:8080/api/admin/generateReferral/' + phoneNumber, {})
  }

  guestEnablePlay(phoneNumber: string, index: number) {
    this.isDataLoading.next(true);
    return this.http.post<Guest>('http://localhost:8080/api/admin/guestEnablePlay/' + phoneNumber, {}).subscribe(res => {
      this.guestsList[index] = res;
      this.isDataLoading.next(false);
    })
  }

  guestDisablePlay(phoneNumber: string, index: number) {
    this.isDataLoading.next(true);
    return this.http.post<Guest>('http://localhost:8080/api/admin/guestDisablePlay/' + phoneNumber, {}).subscribe(res => {
      this.guestsList[index] = res;
      this.isDataLoading.next(false);
    })
  }

  updateRedeem(redeem: Redeem, index: number) {
    this.isDataLoading.next(true);
    return this.http.post<Redeem>('http://localhost:8080/api/admin/updateRedeem', redeem).subscribe(res => {
      this.redeemList[index] = res;
      this.isDataLoading.next(false);
    })
  }

  addRedeem(name: string, point: number) {
    return this.http.post<ReferralTokenModel>('http://localhost:8080/api/admin/addRedeem', {
      redeemName: name,
      pointsForRedeem: point
    })
  }


  private async fetchGroupsList(): Promise<QuestionGroup[]> {
    return await lastValueFrom(this.http.get<QuestionGroup[]>('http://localhost:8080/api/admin/allGroupWithQuestions'));
  }

  private async fetchQuizMetadata(): Promise<QuizMetadata> {
    return await lastValueFrom(this.http.get<QuizMetadata>('http://localhost:8080/api/admin/metadata'));
  }

  private async fetchGuests(): Promise<Guest[]> {
    return await lastValueFrom(this.http.get<Guest[]>('http://localhost:8080/api/admin/allGuests'));
  }

  private async fetchRedeems(): Promise<Redeem[]> {
    return await lastValueFrom(this.http.get<Redeem[]>('http://localhost:8080/api/admin/allRedeems'));
  }

  private async fetchRedeemHistory(): Promise<RedeemHistory[]> {
    return await lastValueFrom(this.http.get<RedeemHistory[]>('http://localhost:8080/api/admin/allRedeemHistory'));
  }
}
