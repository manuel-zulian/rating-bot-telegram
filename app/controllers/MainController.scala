package controllers

import javax.inject._

import models.User
import play.api.mvc._
import play.api.{Configuration, Logger}
import play.api.libs.json.JsValue
import play.api.libs.ws.WSClient
import services.{UpdateProcessService, UserService}

@Singleton
class MainController @Inject()(updateService: UpdateProcessService)
  extends Controller {

  def webhook = Action { request =>
    val updateMaybe = request.body.asJson
    updateMaybe.foreach { update =>
      Logger.debug(s"Update received: $update")
      updateService.process(update.as[JsValue])
    }
    Ok
  }

}
