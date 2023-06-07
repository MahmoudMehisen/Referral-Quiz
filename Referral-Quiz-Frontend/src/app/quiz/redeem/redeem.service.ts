import {Injectable} from "@angular/core";
import {Subject} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Redeem} from "../../shared/models/redeem.model";
import {QuizHomeService} from "../quiz-home/quiz-home.service";
import {ToastrService} from "ngx-toastr";
import {Dialog} from "@angular/cdk/dialog";
import {OtpRedeemComponent} from "./otp-redeem/otp-redeem.component";
import {MatDialog} from "@angular/material/dialog";

@Injectable({providedIn: 'root'})
export class RedeemService {
  redeemList: Redeem[] = [];

  // @ts-ignore
  isDataLoading = new Subject<boolean>(true);

  // @ts-ignore
  recentSelectedRedeem: Redeem;

  dialogRef: any;

  constructor(private quizHomeService: QuizHomeService, private http: HttpClient, private dialog: MatDialog, private toast: ToastrService) {
  }

  fetchRedeemData() {
    this.isDataLoading.next(true);
    this.http.get<Redeem[]>('http://localhost:8080/api/redeem/allRedeems').subscribe(res => {
      this.redeemList = res;
      this.isDataLoading.next(false);
    }, error => {
      this.isDataLoading.next(false)
    })
  }

  requestRedeem(redeem: Redeem) {
    this.isDataLoading.next(true);
    this.http.post('http://localhost:8080/api/redeem/requestRedeem', {
      guestPhone: this.quizHomeService.guest.phoneNumber,
      redeemId: redeem.id
    }).subscribe(res => {
      this.isDataLoading.next(false);
      this.recentSelectedRedeem = redeem;
      // @ts-ignore
      this.toast.success(res.message, "Success");
      this.dialogRef = this.dialog.open(OtpRedeemComponent);
    }, error => {
      this.isDataLoading.next(false);
      // @ts-ignore
      this.toast.error(res.message, "Error");
    })
  }

  reRequestRedeem() {
    this.isDataLoading.next(true);
    this.http.post('http://localhost:8080/api/redeem/requestRedeem', {
      guestPhone: this.quizHomeService.guest.phoneNumber,
      redeemId: this.recentSelectedRedeem.id
    }).subscribe(res => {
      this.isDataLoading.next(false);
      // @ts-ignore
      this.toast.success(res.message, "OTP send to your phone");
    }, error => {
      this.isDataLoading.next(false);
      // @ts-ignore
      this.toast.error(res.message, "Error");
    })
  }

  submitOTP(otp: string) {
    this.http.post('http://localhost:8080/api/redeem/submitOTPRedeem', {
      phoneNumber: this.quizHomeService.guest.phoneNumber,
      token: otp
    }).subscribe(res => {
      this.isDataLoading.next(false);
      // @ts-ignore
      this.toast.success(res.message, "Congratulations");
      this.dialogRef.close();
    }, error => {
      this.isDataLoading.next(false);
      // @ts-ignore
      this.toast.error(res.message, "Error");
    })
  }

}
