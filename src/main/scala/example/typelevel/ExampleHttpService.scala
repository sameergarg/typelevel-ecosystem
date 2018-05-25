package example.typelevel

import cats.Monad
import cats.data.Validated.{Invalid, Valid}
import cats.effect._
import cats.implicits._
import example.typelevel.Domain.Greeting
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.HttpService
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

import scala.language.higherKinds

class ExampleHttpService[F[_]: Effect] extends Http4sDsl[F] {

  val helloWorldService: HttpService[F] = HttpService {
    case GET -> Root / "hello" / name => {

      val result = for {
        validName <- Monad[F].pure(ServiceValidator.validateHelloRequest(name))
      } yield validName

      result.flatMap {
        case Valid(validName) => Ok(Greeting(s"Hello $validName").asJson)
        case Invalid(errors) => BadRequest(errors.asJson)
      }
    }
  }

  val apiService: HttpService[F] = HttpService {
    case GET -> Root / "ping" => Ok("pong")
  }

}