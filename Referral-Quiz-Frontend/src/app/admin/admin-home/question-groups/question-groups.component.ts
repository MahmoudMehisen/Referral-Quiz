import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {AdminHomeService} from "../admin-home.service";
import {AdminAuthService} from "../../admin-auth/admin-auth.service";
import {MatDialog} from "@angular/material/dialog";
import {QuestionGroup} from "../../../shared/models/question-group.model";
import {QuizMetadata} from "../../../shared/models/quiz-metadata.model";
import {AddQuestionComponent} from "../add-question/add-question.component";

@Component({
  selector: 'app-question-groups',
  templateUrl: './question-groups.component.html',
  styleUrls: ['./question-groups.component.css']
})
export class QuestionGroupsComponent implements OnInit, OnDestroy {

  isLoading = false;
  dataSubscription = new Subscription();
  activeTabIndex = 0;
  groupsList: QuestionGroup[] = [];
  // @ts-ignore
  quizMetadata: QuizMetadata;

  constructor(private adminHomeService: AdminHomeService, private addDialog: MatDialog) {
  }

  ngOnInit() {
    this.dataSubscription = this.adminHomeService.isDataLoading.subscribe(isLoading => {
      this.isLoading = isLoading;
      this.groupsList = this.adminHomeService.groupsList;
      this.quizMetadata = this.adminHomeService.metadata;
    });
    this.groupsList = this.adminHomeService.groupsList;
    this.quizMetadata = this.adminHomeService.metadata;
  }

  setActiveTab(index: number) {
    this.activeTabIndex = index;
  }

  ngOnDestroy() {
    this.dataSubscription.unsubscribe();
  }

  activateGroup(groupId: number) {
    this.adminHomeService.activeGroup(groupId);
  }

  onAddQuestion(groupId: number) {
    this.addDialog.open(AddQuestionComponent, {
      data: {
        groupId: groupId,
      }
    })
  }
}
