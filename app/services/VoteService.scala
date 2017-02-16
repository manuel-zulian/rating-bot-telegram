package services

import com.google.inject.Inject
import dao.VoteDao
import models.{Picture, User, Vote}

import scala.util.Try

/**
  * Created by manuel on 15/02/17.
  */
class VoteService @Inject()(voteDao: VoteDao) {
  def getScoresAverage(pic: Picture): AverageScores = {
    AverageScores(voteDao.getCosplayScoreAverage(pic.id), voteDao.getOtherScoreAverage(pic.id))
  }

  def create(vote: Vote): Vote = {
    voteDao.create(vote).get
  }

  def update(vote: Vote): Vote = {
    voteDao.update(vote).get
  }

  def alreadyVoted(user: User, picture: Picture): Boolean = {
    voteDao.findByUserAndPicture(user, picture) match {
      case Some(_) => true
      case None => false
    }
  }
}

case class AverageScores(cosplayScore: BigDecimal, otherScore: BigDecimal)