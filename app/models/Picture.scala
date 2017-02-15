package models

import org.joda.time.DateTime
import scalikejdbc.WrappedResultSet

/**
  * Created by manuel on 15/02/17.
  */
case class Picture(id: Int, name: String, url: Option[String], createdAt: Option[DateTime], votable: Boolean)

object Picture {
  def fromRS(rs: WrappedResultSet): Picture = {
    Picture(rs.int("id"),
      rs.string("name"),
      rs.stringOpt("url"),
      rs.jodaDateTimeOpt("created_at"),
      rs.boolean("votable"))
  }
}
