import {QuestionOption} from "./question-option.model";

export class Question {
  constructor(public id: number, public questionText: string, public options:QuestionOption[]) {
  }
}
