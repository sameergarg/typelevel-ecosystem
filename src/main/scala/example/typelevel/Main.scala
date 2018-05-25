package example.typelevel

import cats.effect.IO
import fs2.StreamApp.ExitCode
import fs2.{Stream, StreamApp}
import org.http4s.server.blaze._

import scala.concurrent.ExecutionContext.Implicits.global


object Main extends StreamApp[IO] {
  val exampleHttpService = new ExampleHttpService[IO]

  override def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] =
    BlazeBuilder[IO]
      .bindHttp(8080, "localhost")
      .mountService(exampleHttpService.helloWorldService, "/")
      .mountService(exampleHttpService.apiService, "/api")
      .mountService(exampleHttpService.personService, "/person")
      .serve
}