import {Component} from '@angular/core';
import {NgForm} from "@angular/forms";
import {AdminHomeService} from "../admin-home.service";
import {Clipboard} from '@angular/cdk/clipboard';

@Component({
  selector: 'app-add-redeem',
  templateUrl: './add-redeem.component.html',
  styleUrls: ['./add-redeem.component.css']
})
export class AddRedeemComponent {
  isLoading = false;
  redeemName = '';
  pointsToRedeem = 1;
  isAddedSuccessfully = false;

  constructor(private adminHomeService: AdminHomeService, private clipboard: Clipboard) {
  }

  async submit(form: NgForm) {
    if (form.valid) {
      this.isLoading = true;
      this.adminHomeService.addRedeem(form.value['redeemName'],form.value['pointsToRedeem']).subscribe(res => {
        this.isAddedSuccessfully = true;
        this.isLoading = false;
      })

    }
  }
}
