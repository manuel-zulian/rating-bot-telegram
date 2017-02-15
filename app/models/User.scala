package models

import scalikejdbc.WrappedResultSet

/**
  * Created by manuel on 15/02/17.
  */
case class User(id: Int, firstName: String, lastName: Option[String], username: Option[String])

object User {
  def fromRS(rs: WrappedResultSet): User = {
    User(rs.int("id"), rs.string("first_name"), rs.stringOpt("last_name"), rs.stringOpt("username"))
  }
}
