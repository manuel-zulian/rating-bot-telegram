package dao

import models.Picture
import scalikejdbc._

import scala.util.Try

/**
  * Created by manuel.zulian on 15/02/2017.
  */
class PictureDao {
  def create(picture: Picture): Try[Unit] =  Try {
    DB.localTx { implicit session =>
      sql"""insert into pictures(id, name, url, createdAt, votable) values
            (${picture.id}, ${picture.name}, ${picture.url}, ${picture.createdAt}, ${picture.votable});""".update().apply()
    }
  }

  def findById(id: Int): Try[Option[Picture]] = Try {
    DB.readOnly { implicit session =>
      sql"""select * from pictures where id=$id""".map(Picture.fromRS).single().apply()
    }
  }
}
