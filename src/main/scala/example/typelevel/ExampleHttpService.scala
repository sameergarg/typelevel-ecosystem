package example.typelevel
import cats.effect._
import example.typelevel.Domain.Greeting
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._

object ExampleHttpService {
  val helloWorldService = HttpService[IO] {
    case GET -> Root / "hello" / name => Ok(Greeting(s"Hello $name").asJson)
  }

  val apiService = HttpService[IO] {
    case GET -> Root / "ping" => Ok("pong")
  }

}