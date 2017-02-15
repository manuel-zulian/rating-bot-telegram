package models

import play.api.libs.json.JsValue
import scalikejdbc.WrappedResultSet

/**
  * Created by manuel on 15/02/17.
  */
case class User(id: Int, firstName: String, lastName: Option[String], username: Option[String])

object User {
  def fromRS(rs: WrappedResultSet): User = {
    User(rs.int("id"), rs.string("first_name"), rs.stringOpt("last_name"), rs.stringOpt("username"))
  }

  def fromJson(jsValue: JsValue): User = {
    User((jsValue \ "id").as[Int], (jsValue \ "first_name").as[String],
      (jsValue \ "last_name").asOpt[String], (jsValue \ "username").asOpt[String])
  }
}
