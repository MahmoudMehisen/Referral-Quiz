import {Injectable} from "@angular/core";
import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpResponse
} from "@angular/common/http";
import {Observable} from "rxjs";
import {tap} from "rxjs/operators";
import {HandleErrorService} from "./handle-error.service";

@Injectable()
export class HttpResponseInterceptor implements HttpInterceptor {
  constructor(private errorService: HandleErrorService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      tap({
        next: (event) => {
          if (event instanceof HttpResponse) {
            //  console.log("interceptor : ")
            // console.log(event.body);
          }
          return event;
        },
        error: (error) => {
          if(error instanceof HttpErrorResponse)
          {
            this.errorService.handleError(error);

          }
        }
      }));
  }
}
