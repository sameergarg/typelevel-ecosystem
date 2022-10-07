package example.typelevel

import cats.Functor
import cats.effect.IO
import cats.mtl.FunctorRaise
import example.typelevel.Repository.InMemoryPersonRepositoryInterpreter
import fs2.StreamApp.ExitCode
import fs2.{Stream, StreamApp}
import org.http4s.server.blaze._

import scala.concurrent.ExecutionContext.Implicits.global


object Main extends StreamApp[IO] {
  val exampleHttpService = new ExampleHttpService[IO](new InMemoryPersonRepositoryInterpreter[IO])

  private implicit val functorRaiseIO: FunctorRaise[IO, Exception] = new FunctorRaise[IO, Exception] {

    override val functor: Functor[IO] = implicitly[Functor[IO]]

    override def raise[A](e: Exception): IO[A] = IO.raiseError(e)
  }

  override def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] =
    BlazeBuilder[IO]
      .bindHttp(8080, "localhost")
      .mountService(exampleHttpService.helloWorldService, "/")
      .mountService(exampleHttpService.apiService, "/api")
      .mountService(exampleHttpService.personService, "/person")
      .mountService(new NumberToWordsService[IO].numberToWords, "/number")
      .serve
}