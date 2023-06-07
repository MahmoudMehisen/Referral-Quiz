import {Injectable} from "@angular/core";
import {
  HttpErrorResponse,
  HttpHandler,
  HttpHeaders,
  HttpInterceptor,
  HttpRequest,
  HttpResponse
} from "@angular/common/http";
import {AdminAuthService} from "./admin-auth.service";
import {exhaustMap, take} from "rxjs/operators";
import {catchError, tap, throwError} from "rxjs";

@Injectable()
export class AuthInterceptorService implements HttpInterceptor {
  constructor(private adminAuthService: AdminAuthService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    return this.adminAuthService.admin.pipe(
      take(1),
      exhaustMap(admin => {
        if (!admin) {
          return next.handle(req);
        }
        const headers = new HttpHeaders({
          'Authorization': 'Bearer ' + admin.token,
          'Content-Type': 'application/json; charset=utf-8',
          'Accept': 'application/json'
        });
        const modifiedReq = req.clone({
          headers: headers
        });
        return next.handle(modifiedReq).pipe(
          catchError((error: HttpErrorResponse) => {
            if (error && error.status === 401) {
              console.log("here");
              this.adminAuthService.logout();
              return next.handle(modifiedReq);
            } else {
              return throwError(error);
            }
          })
        );
      })
    );
  }
}
