package example.typelevel

import cats.Applicative
import cats.implicits._
import example.typelevel.Domain.Person

import scala.collection.concurrent.TrieMap

object Repository {

  trait PersonRepositoryAlgebra[F[_]] {
    def find(name: String): F[Option[Person]]

    def save(person: Person): F[Person]
  }

  class InMemoryPersonRepositoryInterpreter[F[_]: Applicative] extends PersonRepositoryAlgebra[F] {

    private val persons = new TrieMap[String, Person]

    override def find(name: String): F[Option[Person]] = Applicative[F].pure(persons.get(name.toLowerCase))

    override def save(person: Person): F[Person] = {
      persons.put(person.name.toLowerCase, person)
      person.pure[F]
    }
  }

}
