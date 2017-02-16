package services

import com.google.inject.Inject
import dao.VoteDao
import models.{Picture, User, Vote}

import scala.util.Try

/**
  * Created by manuel on 15/02/17.
  */
class VoteService @Inject()(voteDao: VoteDao) {
  def create(vote: Vote): Vote = {
    voteDao.create(vote).get
  }

  def alreadyVoted(user: User, picture: Picture): Boolean = {
    voteDao.findByUserAndPicture(user, picture) match {
      case Some(_) => true
      case None => false
    }
  }
}
