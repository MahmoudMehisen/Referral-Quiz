import {Component} from '@angular/core';
import {NgForm} from "@angular/forms";
import {Clipboard} from '@angular/cdk/clipboard';
import {QuizGameService} from '../quiz-game/quiz-game.service';

@Component({
  selector: 'app-quiz-referral',
  templateUrl: './quiz-referral.component.html',
  styleUrls: ['./quiz-referral.component.css']
})
export class QuizReferralComponent {
  isLoading = false;
  isGeneratedSuccessfully = false;
  token = '';
  phoneNumber = '';

  constructor(private quizGameService: QuizGameService, private clipboard: Clipboard) {
  }

  copyToClipboard() {
    this.clipboard.copy(this.token);
  }

  async submit(form: NgForm) {
    if (form.valid) {
      if (this.isPhoneNumber(this.phoneNumber)) {
        this.isLoading = true;
        this.quizGameService.generateReferralToken(this.phoneNumber).subscribe(res => {
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
