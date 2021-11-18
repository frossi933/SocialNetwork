package com.social.network.utils

sealed trait Sorting extends Product with Serializable
case object Asc extends Sorting
case object Desc extends Sorting

object Sorting {

  def from(sort: Option[String]): Sorting = sort match {
    case Some(v) if "desc".equalsIgnoreCase(v) => Desc
    case _                                     => Asc
  }

}
