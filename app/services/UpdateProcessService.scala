package services

import com.google.inject.Inject
import models.User
import play.api.{Configuration, Logger}
import play.api.libs.json.JsValue

/**
  * Created by manuel.zulian on 15/02/2017.
  */
class UpdateProcessService @Inject() (
  userService: UserService,
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

      // TODO Save in the db a picture with votable false.

      sendMessageService.send(chatId, "If the link posted was a cosplay, reply with /name [name] to name it and vote it (eg, 8/7). Name and vote in separate messages.")
    }

    if(((update \ "message" \ "entities")(0) \ "type").as[String] == "bot_command") {
      val offset = ((update \ "message" \ "entities")(0) \ "offset").as[Int]
      val length = ((update \ "message" \ "entities")(0) \ "length").as[Int]

      val command = (update \ "message" \ "text").as[String].substring(offset, offset + length)
      Logger.debug(s"Command detected: $command")

      // TODO Find the last picture not votable and make it votable.
    }

    val url = s"${config.getString("telegramApiBaseUrl").get}${config.getString("telegramApiKey").get}/sendMessage?chat_id=$chatId&text=Ack"
    Logger.debug(url)
    //ws.url(url).get()
  }
}
