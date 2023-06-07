export class QuizMetadata {
  constructor(public id: number, public pointsPerQuestion: number, public pointsPerReferral: number, public referralExpirationTime: number, public canUserDoReferral: boolean, public activeGroupId: number) {
  }

}
