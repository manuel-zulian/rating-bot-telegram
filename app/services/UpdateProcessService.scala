package services

import com.google.inject.Inject
import models.{Picture, User, Vote}
import org.joda.time.DateTime
import play.api.{Configuration, Logger}
import play.api.libs.json.JsValue

/**
  * Created by manuel.zulian on 15/02/2017.
  */
class UpdateProcessService @Inject() (
  userService: UserService,
  pictureService: PictureService,
  voteService: VoteService,
  sendMessageService: SendMessageService,
  config: Configuration) {
  def process(update: JsValue): Unit = {
    val updateJson = (update \ "message" \ "from").asOpt[JsValue].getOrElse(return)
    val user = userService.findOrCreate(User.fromJson(updateJson))
    val chatId = (update \ "message" \ "chat" \ "id").as[Int]
    val updateType = ((update \ "message" \ "entities")(0) \ "type").asOpt[String].getOrElse(return)

    if(updateType == "url") {
      val offset = ((update \ "message" \ "entities")(0) \ "offset").as[Int]
      val length = ((update \ "message" \ "entities")(0) \ "length").as[Int]

      val url = (update \ "message" \ "text").as[String].substring(offset, offset + length)
      Logger.debug(s"Url detected: $url")

      pictureService.findOrCreate(Picture(0, "temp", Some(url), Some(DateTime.now), false))

      sendMessageService.send(chatId, "If the link posted was a cosplay, reply with /name [name] to name it and vote it (eg, 8/7). Name and vote in separate messages.")
    }

    if(((update \ "message" \ "entities")(0) \ "type").as[String] == "bot_command") {
      val offset = ((update \ "message" \ "entities")(0) \ "offset").as[Int]
      val length = ((update \ "message" \ "entities")(0) \ "length").as[Int]

      val command = (update \ "message" \ "text").as[String].substring(offset, offset + length)
      Logger.debug(s"Command detected: $command")

      val args = (update \ "message" \ "text").as[String].split(" ").drop(1)
      dispatch(command, args, user, chatId)
    }
  }

  private def dispatch(command: String, arguments: Seq[String], user: User, chatId: Int): Unit = {
    command match {
      case "/name" => enableRating(arguments(0), chatId)
      case "/rate" => ratePic(arguments, user)
      case "/leaderboard" => sendLeaderBoard(chatId)
      case _ => Logger.error(s"Command not recognized $command")
    }
  }

  private def enableRating(name: String, chatId: Int) = {
    val picMaybe = pictureService.getLastNotVotable()
    picMaybe match {
      case Some(pic) =>
        pictureService.makeVotable(pic)
        pictureService.update(pic.copy(name = name))
      case None => sendMessageService.send(chatId, "No picture available to start a vote.")
    }
  }

  private def ratePic(arguments: Seq[String], user: User) = {
    val name = arguments.head
    val ratings = arguments(1).split("/")
    val cosplayScore = BigDecimal(ratings(0))
    val otherScore = BigDecimal(ratings(1))

    val pic = pictureService.findByName(name).get

    if(!voteService.alreadyVoted(user, pic)) {
      val vote = Vote(0, user.id, pic.id, Some(cosplayScore), Some(otherScore))
      Logger.debug(vote.toString)

      voteService.create(vote)
      val averages = voteService.getScoresAverage(pic)
      pictureService.update(pic.copy(avgCosplay = averages.cosplayScore, avgOther = averages.otherScore))
    } else {
      Logger.error(s"User ${user.id} ${user.username} already voted.")
    }
  }

  private def sendLeaderBoard(chatId: Int): Unit = {
    val topPictures = pictureService.getTopTen()
    val text = topPictures.zipWithIndex.map{ case(e, i) => s"$i ${e.name} ${e.avgCosplay} ${e.avgOther}" }.foldRight("")((line, acc) => acc + line + "\n")
    sendMessageService.send(chatId, text)
  }
}
