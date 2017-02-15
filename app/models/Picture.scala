package models

import org.joda.time.DateTime
import scalikejdbc.WrappedResultSet

/**
  * Created by manuel on 15/02/17.
  */
case class Picture(id: Long, name: String, url: Option[String], createdAt: Option[DateTime], votable: Boolean)

object Picture {
  def fromRS(rs: WrappedResultSet): Picture = {
    Picture(rs.long("id"),
      rs.string("name"),
      rs.stringOpt("url"),
      rs.jodaDateTimeOpt("created_at"),
      rs.boolean("votable"))
  }
}
