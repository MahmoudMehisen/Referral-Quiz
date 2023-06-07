import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {Redeem} from "../../../shared/models/redeem.model";
import {AdminHomeService} from "../admin-home.service";
import {MatDialog} from "@angular/material/dialog";
import {AddRedeemComponent} from "../add-redeem/add-redeem.component";

@Component({
  selector: 'app-admin-redeems',
  templateUrl: './admin-redeems.component.html',
  styleUrls: ['./admin-redeems.component.css']
})
export class AdminRedeemsComponent implements OnInit, OnDestroy {

  isLoading = false;

  // @ts-ignore
  dataSubscription: Subscription;
  redeemList: Redeem[] = [];
  editModeList: boolean[] = [];

  constructor(private adminHomeService: AdminHomeService, private dialog: MatDialog) {
  }

  ngOnInit() {
    this.dataSubscription = this.adminHomeService.isDataLoading.subscribe(isLoading => {
      this.isLoading = isLoading;
      this.redeemList = this.adminHomeService.redeemList;
      this.editModeList = Array.from({length: this.redeemList.length}, () => false)
    });
    this.redeemList = this.adminHomeService.redeemList;
    this.editModeList = Array.from({length: this.redeemList.length}, () => false)
  }

  ngOnDestroy() {
    this.dataSubscription.unsubscribe();
  }

  toggleEditMode(index: number) {
    this.editModeList[index] = !this.editModeList[index];

    if (!this.editModeList[index]) {
      this.adminHomeService.updateRedeem(this.redeemList[index], index);
    }
  }

  onAddRedeem() {
    this.dialog.open(AddRedeemComponent);
  }
}
