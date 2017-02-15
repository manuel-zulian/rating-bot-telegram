package models

import scalikejdbc.WrappedResultSet

/**
  * Created by manuel on 15/02/17.
  */
case class Vote(id: Int, userId: Int, cosplayScore: Option[BigDecimal], otherScore: Option[BigDecimal])

object Vote {
  def fromRS(rs: WrappedResultSet): Vote = {
    Vote(rs.int("id"),
      rs.int("user_id"),
      rs.bigDecimalOpt("cosplay_score").map(BigDecimal(_)),
      rs.bigDecimalOpt("other_score").map(BigDecimal(_)))
  }
}
