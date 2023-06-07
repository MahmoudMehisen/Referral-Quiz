import {Component, OnDestroy, OnInit} from '@angular/core';
import {QuizAuthService} from "./quiz-auth.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-quiz-auth',
  templateUrl: './quiz-auth.component.html',
  styleUrls: ['./quiz-auth.component.css']
})
export class QuizAuthComponent implements OnInit, OnDestroy{

  username: string = '';
  isLoading = false;

  // @ts-ignore
  subscription: Subscription;

  constructor(private quizAuthService:QuizAuthService) {
  }

  ngOnInit() {
    this.subscription = this.quizAuthService.guest.subscribe(res=>{
      this.isLoading = false;
    })
  }

  onSubmit() {
    if (this.isPhoneNumber(this.username)) {
      this.isLoading = true;
      this.quizAuthService.loginUsingPhone(this.username)
    } else if (this.isEmail(this.username)) {
      this.isLoading = true;
      this.quizAuthService.loginUsingEmail(this.username)
    } else {
      console.log('Invalid input:', this.username);
    }
  }

  isPhoneNumber(input: string): boolean {
    const phoneRegex = /^\d{11}$/;
    return phoneRegex.test(input);
  }

  isEmail(input: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(input);
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
