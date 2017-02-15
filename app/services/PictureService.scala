package services

import com.google.inject.Inject
import dao.PictureDao
import models.Picture

import scala.util.Success

/**
  * Created by manuel.zulian on 15/02/2017.
  */
class PictureService @Inject()(pictureDao: PictureDao) {
  def findOrCreate(picture: Picture): Picture = {
    pictureDao.findById(picture.id) match {
      case Success(Some(picture)) => picture
      case Success(None) =>
        pictureDao.create(picture)
        picture
    }
  }

  def getLastNotVotable(): Option[Picture] = {
    pictureDao.findLastNotVotable().get
  }

  def makeVotable(picture: Picture): Picture = {
    pictureDao.update(picture.copy(votable = true))
  }
}
