package services

import com.google.inject.Inject
import dao.VoteDao
import models.Vote

/**
  * Created by manuel on 15/02/17.
  */
class VoteService @Inject() (voteDao: VoteDao) {
  def create(vote: Vote): Vote = {
    voteDao.create(vote).get
  }
}
