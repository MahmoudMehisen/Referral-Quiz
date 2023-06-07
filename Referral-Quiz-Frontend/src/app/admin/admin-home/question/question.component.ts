import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Question} from "../../../shared/models/question.model";
import {AdminHomeService} from "../admin-home.service";
import {Subscription} from "rxjs";
import {MatDialog} from "@angular/material/dialog";
import {AddQuestionComponent} from "../add-question/add-question.component";

@Component({
  selector: 'app-question',
  templateUrl: './question.component.html',
  styleUrls: ['./question.component.css']
})
export class QuestionComponent implements OnInit, OnDestroy {
  // @ts-ignore
  @Input() groupInd: number;
  // @ts-ignore
  @Input() questionInd: number;
  // @ts-ignore
  @Input() question: Question;

  // @ts-ignore
  @Input() playedTimes: number;
  isLoading = false;

  itemSubscription: Subscription = new Subscription();

  constructor(private adminHomeService: AdminHomeService, private editDialog: MatDialog) {
  }

  ngOnInit() {
    this.itemSubscription = this.adminHomeService.isDataLoading.subscribe(res => {
      this.isLoading = false;
    })
  }

  onDelete() {
    this.isLoading = true;
    this.adminHomeService.deleteQuestion(this.groupInd, this.questionInd, this.question);
  }

  onEdit() {
    this.editDialog.open(AddQuestionComponent, {
      data: {
        question: this.question,
        isEditMode: true,
        questionInd: this.questionInd,
        groupInd: this.groupInd
      }
    })
  }

  ngOnDestroy() {
    this.itemSubscription.unsubscribe();
  }
}
