import {Component, OnDestroy, OnInit} from '@angular/core';
import {QuizMetadata} from "../../../shared/models/quiz-metadata.model";
import {NgForm} from "@angular/forms";
import {Subscription} from "rxjs";
import {AdminHomeService} from "../admin-home.service";
import {AdminAuthService} from "../../admin-auth/admin-auth.service";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-admin-metadata',
  templateUrl: './admin-metadata.component.html',
  styleUrls: ['./admin-metadata.component.css']
})
export class AdminMetadataComponent implements OnInit, OnDestroy {
  isLoading: boolean = false;

  // @ts-ignore
  quizMetaData: QuizMetadata;
  dataSubscription = new Subscription()

  constructor(private adminHomeService: AdminHomeService) {
  }

  ngOnInit() {
    this.dataSubscription = this.adminHomeService.isDataLoading.subscribe(isLoading => {
      this.isLoading = isLoading;
      this.quizMetaData = this.adminHomeService.metadata;
    });
    this.quizMetaData = this.adminHomeService.metadata;
  }

  onSave(form: NgForm) {
    if (!form.valid) {
      return;
    }
    const metadata = form.value as QuizMetadata;
    this.isLoading = true;
    this.adminHomeService.updateMetadata(metadata);
  }

  stopQuiz() {
    this.adminHomeService.stopQuiz();
  }

  ngOnDestroy() {
    this.dataSubscription.unsubscribe();
  }
}
