import {Component} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {InvitationService} from "./invitation.service";
import {QuizAuthService} from "../quiz/quiz-auth/quiz-auth.service";

@Component({
  selector: 'app-invitation',
  templateUrl: './invitation.component.html',
  styleUrls: ['./invitation.component.css']
})
export class InvitationComponent {
  token: string = '';
  isLoading = false;
  isTokenVerified = false;
  phone = '';
  phoneEntered = false;
  email = '';

  constructor(private route: ActivatedRoute, private invitationService: InvitationService, private quizAuthService: QuizAuthService, private router: Router) {
  }

  ngOnInit() {

    this.route.params.subscribe(params => {
      this.token = params['token'];
    });
  }

  checkToken() {
    this.isLoading = true;
    if (this.phone.length > 0 && this.token.length > 0) {
      this.invitationService.checkToken(this.token, this.phone).subscribe(res => {
        this.isLoading = false;
        this.phoneEntered = true;
        this.isTokenVerified = true;
        this.quizAuthService.loginFromInvitationToken(res);
      }, error => {
        this.isLoading = false;
        this.phoneEntered = true;
        this.isTokenVerified = false;
      });
    }
  }

  confirmRegister() {

  }
}
