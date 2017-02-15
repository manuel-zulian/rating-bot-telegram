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
    val user = userService.findOrCreate(User.fromJson((update \ "message" \ "from").as[JsValue]))
    val chatId = (update \ "message" \ "chat" \ "id").as[Int]

    if(((update \ "message" \ "entities")(0) \ "type").as[String] == "url") {
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
      dispatch(command, args, user)
    }
  }

  private def dispatch(command: String, arguments: Seq[String], user: User): Unit = {
    command match {
      case "/name" => enableRating(arguments(0))
      case "/rate" => ratePic(arguments, user)
    }
  }

  private def enableRating(name: String) = {
    val pic = pictureService.makeVotable(pictureService.getLastNotVotable().get)
    pictureService.update(pic.copy(name = name))
  }

  private def ratePic(arguments: Seq[String], user: User) = {
    val name = arguments.head
    val ratings = arguments(1).split("/")
    val cosplayScore = BigDecimal(ratings(0))
    val otherScore = BigDecimal(ratings(1))

    val pic = pictureService.findByName(name)

    val vote = Vote(0, user.id, pic.get.id, Some(cosplayScore), Some(otherScore))
    Logger.debug(vote.toString)

    voteService.create(vote)
  }
}
