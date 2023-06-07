import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Guest} from "../shared/models/guest.model";
import {Admin} from "../shared/models/admin.model";

@Injectable({providedIn: 'root'})
export class InvitationService {


  constructor(private http: HttpClient) {
  }


  checkToken(token: string, phone: string) {
    return this.http.post<Guest>('http://localhost:8080/api/guest/acceptReferral', {
      token: token,
      phoneNumber: phone
    })
  }

}
