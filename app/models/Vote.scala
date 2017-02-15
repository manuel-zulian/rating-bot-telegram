package models

import scalikejdbc.WrappedResultSet

/**
  * Created by manuel on 15/02/17.
  */
case class Vote(id: Long, userId: Int, pictureId: Long, cosplayScore: Option[BigDecimal], otherScore: Option[BigDecimal])

object Vote {
  def fromRS(rs: WrappedResultSet): Vote = {
    Vote(rs.long("id"),
      rs.int("user_id"),
      rs.int("picture_id"),
      rs.bigDecimalOpt("cosplay_score").map(BigDecimal(_)),
      rs.bigDecimalOpt("other_score").map(BigDecimal(_)))
  }
}
