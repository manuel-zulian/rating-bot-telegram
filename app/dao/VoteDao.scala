package dao

import models.Vote
import scalikejdbc._

import scala.util.Try

/**
  * Created by manuel.zulian on 15/02/2017.
  */
class VoteDao {
  def create(vote: Vote): Try[Unit] =  Try {
    DB.localTx { implicit session =>
      sql"""insert into votes(id, user_id, cosplay_score, other_score) values
            (${vote.id}, ${vote.userId}, ${vote.cosplayScore}, ${vote.otherScore});""".update().apply()
    }
  }

  def findById(id: Int): Try[Option[Vote]] = Try {
    DB.readOnly { implicit session =>
      sql"""select * from votes where id=$id""".map(Vote.fromRS).single().apply()
    }
  }
}
