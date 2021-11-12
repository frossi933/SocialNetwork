package com.social.network.config

import cats.effect.{Async, ContextShift}
import doobie.LogHandler
import doobie.util.transactor.Transactor

trait DBConfig[F[_]] {

  implicit val logHanlder: LogHandler
  val xa: Transactor[F]
}

object DBConfig {

  def apply[F[_]: Async: ContextShift]() = new DBConfig[F] {

    override val logHanlder: LogHandler = LogHandler.jdkLogHandler

    override val xa: Transactor[F] = Transactor.fromDriverManager[F](
      "org.postgresql.Driver",
      "jdbc:postgresql:socialnetwork",
      "postgres",
      "postgres"
    )
  }
}
