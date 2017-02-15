package dao

import models.Picture
import scalikejdbc._

import scala.util.Try

/**
  * Created by manuel.zulian on 15/02/2017.
  */
class PictureDao {
  def update(picture: Picture) = {
    DB.localTx { implicit session =>
      sql"""update pictures set name=${picture.name}, url=${picture.url}, created_at=${picture.createdAt}, votable=${picture.votable} where id=${picture.id}""".update().apply()
      picture
    }
  }

  def create(picture: Picture): Try[Picture] =  Try {
    DB.localTx { implicit session =>
      val id = sql"""insert into pictures(name, url, created_at, votable) values
            (${picture.name}, ${picture.url}, ${picture.createdAt}, ${picture.votable});""".updateAndReturnGeneratedKey().apply()
      picture.copy(id = id)
    }
  }

  def findById(id: Long): Try[Option[Picture]] = Try {
    DB.readOnly { implicit session =>
      sql"""select * from pictures where id=$id""".map(Picture.fromRS).single().apply()
    }
  }

  def findByName(name: String): Try[Option[Picture]] = Try {
    DB.readOnly { implicit session =>
      sql"""select * from pictures where name=$name and votable=true""".map(Picture.fromRS).first().apply()
    }
  }

  def findLastNotVotable(): Try[Option[Picture]] = Try {
    DB.readOnly { implicit session =>
      sql"""select * from pictures where votable=false order by created_at desc""".map(Picture.fromRS).first().apply()
    }
  }
}
