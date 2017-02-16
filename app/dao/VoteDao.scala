package dao

import models.{Picture, User, Vote}
import scalikejdbc._

import scala.util.Try

/**
  * Created by manuel.zulian on 15/02/2017.
  */
class VoteDao {
  def getCosplayScoreAverage(picId: Long): BigDecimal = Try {
    DB.readOnly { implicit session =>
      sql"""select avg(cosplay_score) from votes where picture_id=$picId""".map(_.bigDecimal("avg")).single().apply()
    }
  }.map(x => BigDecimal(x.get)).getOrElse(BigDecimal(0))

  def getOtherScoreAverage(picId: Long): BigDecimal = Try {
    DB.readOnly { implicit session =>
      sql"""select avg(other_score) from votes where picture_id=$picId""".map(_.bigDecimal("avg")).single().apply()
    }
  }.map(x => BigDecimal(x.get)).getOrElse(BigDecimal(0))

  def create(vote: Vote): Try[Vote] =  Try {
    DB.localTx { implicit session =>
      val id = sql"""insert into votes(user_id, picture_id, cosplay_score, other_score) values
            (${vote.userId}, ${vote.pictureId}, ${vote.cosplayScore}, ${vote.otherScore});""".updateAndReturnGeneratedKey().apply()
      vote.copy(id = id)
    }
  }

  def update(vote: Vote) = Try {
    DB.localTx { implicit session =>
      sql"""update votes set other_score=${vote.otherScore},
            cosplay_score=${vote.cosplayScore} where id=${vote.id}""".update().apply()
      vote
    }
  }

  def findById(id: Int): Try[Option[Vote]] = Try {
    DB.readOnly { implicit session =>
      sql"""select * from votes where id=$id""".map(Vote.fromRS).single().apply()
    }
  }

  def findByUserAndPicture(user: User, picture: Picture): Option[Vote] = Try {
    DB.readOnly { implicit session =>
      sql"""select * from votes where user_id=${user.id} and picture_id=${picture.id}""".map(Vote.fromRS).single().apply()
    }
  } getOrElse None
}
