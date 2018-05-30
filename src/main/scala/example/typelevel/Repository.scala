package example.typelevel

import cats.Applicative
import cats.data.{Validated, ValidatedNel}
import cats.implicits._
import example.typelevel.Domain.{Person, ServiceError}
import example.typelevel.Domain.ServiceError.PersonDoesNotExist
import example.typelevel.ServiceValidator.ValidationResult

import scala.collection.concurrent.TrieMap

object Repository {

  trait PersonRepositoryAlgebra[F[_]] {
    def find(name: String): F[ValidationResult[Person]]

    def save(person: Person): F[Person]
  }

  class InMemoryPersonRepositoryInterpreter[F[_]: Applicative] extends PersonRepositoryAlgebra[F] {

    private val persons = new TrieMap[String, Person]

    override def find(name: String): F[ValidationResult[Person]] = {
      val p = persons.get(name.toLowerCase)
      val validPerson: ValidatedNel[PersonDoesNotExist, Person] = Validated.fromOption(p, PersonDoesNotExist(name)).toValidatedNel
      Applicative[F].pure(validPerson)
    }

    override def save(person: Person): F[Person] = {
      persons.put(person.name.toLowerCase, person)
      person.pure[F]
    }
  }

}
