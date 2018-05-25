package example.typelevel

object Domain {
  case class Greeting(content: String)
  case class Person(name: String, age: Int)

  sealed trait ServiceError

  object ServiceError {
    case object EmptyValue extends ServiceError
    case object DoesNotStartWithCapitalLetter extends ServiceError
  }
}
