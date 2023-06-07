import {Question} from "./question.model";

export class QuestionGroup {
  constructor(public id: number, public name: string, public numberOfPlayed: number, public questions: Question[]) {
  }
}
