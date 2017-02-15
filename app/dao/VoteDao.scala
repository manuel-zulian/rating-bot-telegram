package dao

import models.Vote
import scalikejdbc._

import scala.util.Try

/**
  * Created by manuel.zulian on 15/02/2017.
  */
class VoteDao {
  def create(vote: Vote): Try[Vote] =  Try {
    DB.localTx { implicit session =>
      val id = sql"""insert into votes(user_id, picture_id, cosplay_score, other_score) values
            (${vote.userId}, ${vote.pictureId}, ${vote.cosplayScore}, ${vote.otherScore});""".updateAndReturnGeneratedKey().apply()
      vote.copy(id = id)
    }
  }

  def findById(id: Int): Try[Option[Vote]] = Try {
    DB.readOnly { implicit session =>
      sql"""select * from votes where id=$id""".map(Vote.fromRS).single().apply()
    }
  }
}
