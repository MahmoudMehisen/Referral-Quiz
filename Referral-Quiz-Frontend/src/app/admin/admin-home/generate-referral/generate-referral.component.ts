import {Component} from '@angular/core';
import {NgForm} from "@angular/forms";
import {AdminHomeService} from "../admin-home.service";
import {Clipboard} from '@angular/cdk/clipboard';

@Component({
  selector: 'app-generate-referral',
  templateUrl: './generate-referral.component.html',
  styleUrls: ['./generate-referral.component.css']
})
export class GenerateReferralComponent {
  isLoading = false;
  isGeneratedSuccessfully = false;
  token = '';
  phoneNumber = '';

  constructor(private adminHomeService: AdminHomeService, private clipboard: Clipboard) {
  }

  copyToClipboard() {
    this.clipboard.copy(this.token);
  }

  async submit(form: NgForm) {
    if (form.valid) {
      this.isLoading = true;
      if (this.isPhoneNumber(this.phoneNumber)) {
        this.adminHomeService.generateReferralToken(this.phoneNumber).subscribe(res => {
          if (res.token) {
            this.token = 'http://localhost:4200/invitation/' + res.token;
            this.isGeneratedSuccessfully = true;
          } else {
            this.isGeneratedSuccessfully = false;
          }
          this.isLoading = false;
        })
      }
    }
  }
  isPhoneNumber(input: string): boolean {
    const phoneRegex = /^\d{11}$/;
    return phoneRegex.test(input);
  }
}
