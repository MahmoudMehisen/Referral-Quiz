import {Injectable} from "@angular/core";
import {HttpErrorResponse} from "@angular/common/http";
import {ToastrService} from "ngx-toastr";

@Injectable({providedIn: "root"})
export class HandleErrorService {

  constructor(private toaster: ToastrService) {
  }

  public handleError(err: HttpErrorResponse) {
    this.toaster.error(err.error.message,"Error");
  }
}
