package dao

import models.User
import scalikejdbc._

import scala.util.Try

/**
  * Created by manuel.zulian on 15/02/2017.
  */
class UserDao {
  def create(user: User): Try[Unit] =  Try {
    DB.localTx { implicit session =>
      sql"""insert into users(id, first_name, last_name, username) values
            (${user.id}, ${user.firstName}, ${user.lastName}, ${user.username});""".update().apply()
    }
  }

  def findById(id: Int): Try[Option[User]] = Try {
    DB.readOnly { implicit session =>
      sql"""select * from users where id=$id""".map(User.fromRS).single().apply()
    }
  }
}
