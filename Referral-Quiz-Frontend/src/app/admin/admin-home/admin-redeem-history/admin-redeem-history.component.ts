import {Component} from '@angular/core';
import {RedeemHistory} from "../../../shared/models/redeem-history.model";
import {Subscription} from "rxjs";
import {Redeem} from "../../../shared/models/redeem.model";
import {AdminHomeService} from "../admin-home.service";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-admin-redeem-history',
  templateUrl: './admin-redeem-history.component.html',
  styleUrls: ['./admin-redeem-history.component.css']
})
export class AdminRedeemHistoryComponent {

  redeemHistoryList: RedeemHistory[] = [];

  isLoading = false;

  // @ts-ignore
  dataSubscription: Subscription;

  constructor(private adminHomeService: AdminHomeService) {
  }

  ngOnInit() {
    this.dataSubscription = this.adminHomeService.isDataLoading.subscribe(isLoading => {
      this.isLoading = isLoading;
      this.redeemHistoryList = this.adminHomeService.redeemHistoryList;
    });
    this.redeemHistoryList = this.adminHomeService.redeemHistoryList;
  }

}
