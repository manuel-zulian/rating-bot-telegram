package services

import com.google.inject.Inject
import play.api.{Configuration, Logger}
import play.api.libs.ws.WSClient

/**
  * Created by manuel.zulian on 15/02/2017.
  */
class SendMessageService @Inject() (
  ws: WSClient,
  config: Configuration) {
  def send(chatId: Int, text: String): Unit = {
    val url = s"${config.getString("telegramApiBaseUrl").get}${config.getString("telegramApiKey").get}/sendMessage?chat_id=$chatId&text=$text"
    Logger.debug(url)
    ws.url(url).get()
  }
}
