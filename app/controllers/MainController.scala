package controllers

import javax.inject._

import models.User
import play.api.mvc._
import play.api.Logger
import play.api.libs.json.JsValue
import services.UserService

@Singleton
class MainController @Inject() (userService: UserService) extends Controller {

  def webhook = Action { request =>
    val updateMaybe = request.body.asJson
    updateMaybe.map { update =>
      Logger.debug(s"Update received: $update")
      val user = userService.findOrCreate(User.fromJson((update \ "message" \ "from").as[JsValue]))
    }
    Ok
  }

}
