export class QuestionOption {
  constructor(public id: number, public optionText: string, public isCorrectAnswer: boolean, public selectedTimes: number) {
  }
}
