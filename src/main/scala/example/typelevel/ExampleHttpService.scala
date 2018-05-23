package example.typelevel

import cats.effect._, org.http4s._, org.http4s.dsl.io._, scala.concurrent.ExecutionContext.Implicits.global

object ExampleHttpService {
  val helloWorldService = HttpService[IO] {
    case GET -> Root / "hello" / name => Ok(s"Hello $name")
  }

  val apiService = HttpService[IO] {
    case GET -> Root / "ping" => Ok("pong")
  }

}