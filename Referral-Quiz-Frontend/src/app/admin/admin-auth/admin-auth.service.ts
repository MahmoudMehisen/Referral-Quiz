import {Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError, BehaviorSubject, tap, throwError} from "rxjs";
import {Admin} from "../../shared/models/admin.model";
import {Router} from "@angular/router";

export interface AdminData {
  id: number;
  token: string;
  username: string;
  email: string;
  expiresIn: number;
}

@Injectable({providedIn: 'root'})
export class AdminAuthService {

  // @ts-ignore
  admin = new BehaviorSubject<Admin>(null)

  private tokenExpirationTimer: any;

  constructor(private http: HttpClient,private router:Router) {
  }

  login(username: string, password: string) {
    return this.http.post<AdminData>('http://localhost:8080/api/auth/login', {
      username: username,
      password: password,
    }).pipe(catchError(this.handelError), tap(resData => {
      this.handelAuth(resData.id, resData.email, resData.token, resData.expiresIn);
    }));
  }

  private handelAuth(userId: number, email: string, token: string, expiresIn: number) {
    const expirationDate = new Date(new Date().getTime() + expiresIn * 1000);
    const admin = new Admin(userId, email, token, expirationDate);

    this.admin.next(admin);
    this.autoLogout(expiresIn * 1000);
    // @ts-ignore
    localStorage.setItem('adminData', JSON.stringify(admin));
  }

  private handelError(errorRes: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred!';
    if (!errorRes.error || !errorRes.error.error) {
      return throwError(() => errorMessage);
    }

    switch (errorRes.error.error.message) {
      case 'EMAIL_EXISTS':
        errorMessage = 'This email exists already';
        break;
      case 'EMAIL_NOT_FOUND':
        errorMessage = 'this email not found';
        break;
      case 'INVALID_PASSWORD':
        errorMessage = 'The password is invalid'
        break;
    }
    return throwError(() => errorMessage);
  }

  autoLogin() {
    let adminData: { id: number; email: string; _token: string; _tokenExpirationDate: Date };

    // @ts-ignore
    adminData = JSON.parse(localStorage.getItem('adminData'));
    if (!adminData) {
      return;
    }

    const loadedAdmin = new Admin(
      adminData.id,
      adminData.email,
      adminData._token,
      new Date(adminData._tokenExpirationDate)
    );

    if (loadedAdmin.token) {
      this.admin.next(loadedAdmin);
      const expirationDuration = new Date(adminData._tokenExpirationDate).getTime() - new Date().getTime();
      this.autoLogout(expirationDuration);
      this.router.navigate(['/admin/home']);
    }
  }

  logout() {
    // @ts-ignore
    this.admin.next(null);
    this.router.navigate(['']);
    localStorage.removeItem('adminData');
    if (this.tokenExpirationTimer) {
      clearTimeout(this.tokenExpirationTimer);
    }
    this.tokenExpirationTimer = null;
    console.log('logout');
  }

  autoLogout(expirationDuration: number) {
    this.tokenExpirationTimer = setTimeout(() => {
      this.logout();
    }, expirationDuration);
  }
}
