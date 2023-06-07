import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {QuestionGroup} from "../../../shared/models/question-group.model";
import {QuizMetadata} from "../../../shared/models/quiz-metadata.model";
import {AdminHomeService} from "../admin-home.service";
import {MatDialog} from "@angular/material/dialog";
import {AddQuestionComponent} from "../add-question/add-question.component";
import {Guest} from "../../../shared/models/guest.model";

@Component({
  selector: 'app-guests',
  templateUrl: './guests.component.html',
  styleUrls: ['./guests.component.css']
})
export class GuestsComponent implements OnInit, OnDestroy {

  isLoading = false;
  dataSubscription = new Subscription();
  guestsList: Guest[] = [];

  constructor(private adminHomeService: AdminHomeService) {
  }

  ngOnInit() {
    this.dataSubscription = this.adminHomeService.isDataLoading.subscribe(isLoading => {
      this.isLoading = isLoading;
      this.guestsList = this.adminHomeService.guestsList;
    });
    this.guestsList = this.adminHomeService.guestsList;
  }

  ngOnDestroy() {
    this.dataSubscription.unsubscribe();
  }

  disable(index: number) {
    this.adminHomeService.guestDisablePlay(this.guestsList[index].phoneNumber, index);
  }

  enable(index: number) {
    this.adminHomeService.guestEnablePlay(this.guestsList[index].phoneNumber, index);
  }
}
