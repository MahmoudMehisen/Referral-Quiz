import {Component, OnDestroy, OnInit} from '@angular/core';
import {AdminHomeService} from "./admin-home.service";
import {AdminAuthService} from "../admin-auth/admin-auth.service";
import {Subscription} from "rxjs";
import {NgForm} from "@angular/forms";
import {GenerateReferralComponent} from "./generate-referral/generate-referral.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-admin-home',
  templateUrl: './admin-home.component.html',
  styleUrls: ['./admin-home.component.css'],
})
export class AdminHomeComponent implements OnInit, OnDestroy {

  isLoading = false;
  dataSubscription = new Subscription()
  newGroupText = '';

  constructor(private adminHomeService: AdminHomeService, private adminAuthService: AdminAuthService,private dialog: MatDialog) {
  }

  ngOnInit() {
    this.dataSubscription = this.adminHomeService.isDataLoading.subscribe(isLoading => {
      this.isLoading = isLoading;
    });
    this.adminHomeService.fetchAdminHomeData();
  }

  onLogout() {
    this.adminAuthService.logout();
  }

  onGenerateReferral() {
    this.dialog.open(GenerateReferralComponent);
  }


  addNewGroup(newGroupForm: NgForm) {
    if (newGroupForm.valid) {
      this.adminHomeService.addNewGroup(newGroupForm.value['newGroupText']);
      newGroupForm.resetForm();
    }
  }

  ngOnDestroy() {
    this.dataSubscription.unsubscribe();
  }

}
