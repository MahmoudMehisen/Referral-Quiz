import {Component, OnDestroy, OnInit} from '@angular/core';
import {Redeem} from "../../../shared/models/redeem.model";
import {QuizAuthService} from "../../quiz-auth/quiz-auth.service";
import {HttpClient} from "@angular/common/http";
import {ToastrService} from 'ngx-toastr';
import {QuizHomeService} from "../../quiz-home/quiz-home.service";
import {RedeemService} from "../redeem.service";

@Component({
  selector: 'app-redeem-list',
  templateUrl: './redeem-list.component.html',
  styleUrls: ['./redeem-list.component.css']
})
export class RedeemListComponent implements OnInit, OnDestroy {
  redeemList: Redeem[] = [];

  // @ts-ignore
  subscription: Subscription;

  isLoading = true;

  constructor(private redeemService: RedeemService) {
  }

  ngOnInit() {
    this.redeemList = this.redeemService.redeemList;
    this.subscription = this.redeemService.isDataLoading.subscribe(res => {
      this.redeemList = this.redeemService.redeemList;
      this.isLoading = res;
    })
    this.redeemService.fetchRedeemData();
  }

  requestRedeem(redeem: Redeem) {
    this.redeemService.requestRedeem(redeem);
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
