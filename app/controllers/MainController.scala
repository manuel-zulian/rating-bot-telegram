package controllers

import javax.inject._

import play.api.mvc._
import play.api.Logger

@Singleton
class MainController @Inject() extends Controller {

  def webhook = Action { request =>
    val updateMaybe = request.body.asJson
    updateMaybe.map { update =>
      Logger.debug(s"Update received: $update")
    }
    Ok
  }

}
