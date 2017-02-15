package services

import com.google.inject.Inject
import dao.UserDao
import models.User

import scala.util.Success

/**
  * Created by manuel.zulian on 15/02/2017.
  */
class UserService @Inject() (userDao: UserDao) {
  def findOrCreate(user: User): User = {
    userDao.findById(user.id) match {
      case Success(Some(user)) => user
      case Success(None) =>
        userDao.create(user)
        user
    }
  }
}
