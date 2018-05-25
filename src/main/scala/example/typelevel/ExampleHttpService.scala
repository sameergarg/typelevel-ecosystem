package example.typelevel

import cats.Monad
import cats.data.Validated.{Invalid, Valid}
import cats.effect._
import cats.implicits._
import example.typelevel.Domain.{Greeting, Person}
import example.typelevel.Repository.InMemoryPersonRepositoryInterpreter
import io.circe.Decoder
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.{EntityDecoder, HttpService}
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

import scala.language.higherKinds

class ExampleHttpService[F[_]: Effect](personRepository: InMemoryPersonRepositoryInterpreter[F]) extends Http4sDsl[F] {

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

  implicit def decoders[F[_]: Sync, A: Decoder]: EntityDecoder[F, A] = jsonOf[F, A]

  val personService: HttpService[F] = HttpService {
    case GET        -> Root / name =>
      val existingPerson = for {
        //TODO add validation
        p <- personRepository.find(name)
      } yield p

      existingPerson.flatMap {person => person.fold(NotFound(s"No person with name $name"))(p => Ok(p.asJson))}

    case req @ POST -> Root =>
      val result = for {
        person <- req.as[Person]
        _      <- personRepository.save(person)
      } yield person

      result.flatMap { r =>
        Ok(r.asJson)
      }
  }

}