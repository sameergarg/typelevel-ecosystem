package example.typelevel

object Domain {
  case class Greeting(content: String)

  sealed trait ServiceError

  object ServiceError {
    case object EmptyValue extends ServiceError
    case object DoesNotStartWithCapitalLetter extends ServiceError
  }
}
