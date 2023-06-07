import {Component, OnDestroy, OnInit} from '@angular/core';
import {Observable, Subscription, timer} from "rxjs";
import {RedeemService} from "../redeem.service";

@Component({
  selector: 'app-otp-redeem',
  templateUrl: './otp-redeem.component.html',
  styleUrls: ['./otp-redeem.component.css']
})
export class OtpRedeemComponent implements OnInit, OnDestroy {
  // @ts-ignore
  otpCode: string = '';
  cooldownTime: number = 60;
  // @ts-ignore
  cooldownTimer$: Observable<number>;
  // @ts-ignore
  cooldownTimerSubscription: Subscription;
  cooldownExpired: boolean = true;

  constructor(private redeemService: RedeemService) {
  }
  ngOnInit() {
    this.startCooldownTimer();
  }

  startCooldownTimer() {
    this.cooldownExpired = false;
    this.cooldownTimer$ = timer(0, 1000);
    this.cooldownTimerSubscription = this.cooldownTimer$.subscribe(() => {
      if (this.cooldownTime > 0) {
        this.cooldownTime--;
      } else {
        this.cooldownExpired = true;
        this.cooldownTimerSubscription.unsubscribe();
      }
    });
  }

  requestOTP() {
    this.redeemService.reRequestRedeem();
    this.startCooldownTimer();
  }

  submitOTP() {
    this.redeemService.submitOTP(this.otpCode);
  }

  ngOnDestroy() {
    this.cooldownTimerSubscription.unsubscribe();
  }

}
