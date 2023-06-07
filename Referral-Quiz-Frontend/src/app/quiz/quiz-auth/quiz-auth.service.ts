import {Injectable} from "@angular/core";
import {Guest} from "../../shared/models/guest.model";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {BehaviorSubject} from "rxjs";
import {Admin} from "../../shared/models/admin.model";
import {QuizMetadata} from "src/app/shared/models/quiz-metadata.model";

@Injectable({providedIn: 'root'})
export class QuizAuthService {

  // @ts-ignore
  guest = new BehaviorSubject<Guest>(null)

  // @ts-ignore
  quizMetadata: QuizMetadata;

  private tokenExpirationTimer: any;

  constructor(private http: HttpClient, private router: Router) {
  }

  loginFromInvitationToken(guest: Guest) {
    this.handelAuth(guest);
  }

  loginUsingPhone(phone: string) {
    return this.http.get<Guest>('http://localhost:8080/api/guest/getGuestByPhone/' + phone).subscribe(res => {
      this.handelAuth(res);
    }, error => {
      // @ts-ignore
      this.guest.next(null);
    })
  }

  loginUsingEmail(email: string) {
    return this.http.get<Guest>('http://localhost:8080/api/guest/getGuestByEmail/' + email).subscribe(res => {
      this.handelAuth(res);
    }, error => {
      // @ts-ignore
      this.guest.next(null);
    })
  }


  private handelAuth(guest: Guest) {

    this.guest.next(guest);

    // 30 min
    this.autoLogout(1800000);
    const expirationDate = new Date(new Date().getTime() + 1800000);

    // @ts-ignore
    localStorage.setItem('guestData', JSON.stringify(guest));
    localStorage.setItem('guestExpire', JSON.stringify(expirationDate));
    this.router.navigate(['/quiz/home'], {replaceUrl: true});
  }

  autoLogin() {
    let guest: { phoneNumber: string, email: string, totalPoints: number, canPlay: boolean };

    // @ts-ignore
    guest = JSON.parse(localStorage.getItem('guestData'));
    if (!guest) {
      return;
    }
    const loadedGuest = new Guest(
      guest.phoneNumber,
      guest.email,
      guest.totalPoints,
      guest.canPlay
    );

    this.guest.next(loadedGuest);
    // @ts-ignore
    let expirationDate = new Date(JSON.parse(localStorage.getItem('guestExpire')));

    const expirationDuration = expirationDate.getTime() - new Date().getTime();
    this.autoLogout(expirationDuration);
    this.router.navigate(['/quiz/home'], {replaceUrl: true});
  }

  logout() {
    // @ts-ignore
    this.guest.next(null);
    this.router.navigate(['']);
    localStorage.removeItem('guestData');
    localStorage.removeItem('guestExpire');

    if (this.tokenExpirationTimer) {
      clearTimeout(this.tokenExpirationTimer);
    }
    this.tokenExpirationTimer = null;
  }

  autoLogout(expirationDuration: number) {
    this.tokenExpirationTimer = setTimeout(() => {
      this.logout();
    }, expirationDuration);
  }


}
