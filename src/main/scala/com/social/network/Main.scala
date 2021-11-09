package com.social.network

import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {
  def run(args: List[String]) =
    SocialNetworkServer.stream[IO].compile.drain.as(ExitCode.Success)
}
