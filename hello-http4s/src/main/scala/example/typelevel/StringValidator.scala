package example.typelevel

import cats.data.{Validated, ValidatedNel}
import cats.implicits._
import example.typelevel.Domain.ServiceError
import example.typelevel.Domain.ServiceError.{DoesNotStartWithCapitalLetter, EmptyValue}
import example.typelevel.ServiceValidator.ValidationResult

trait StringValidator {

  val isCapital = """[A-Z].*""".r

  def isNotEmpty(name: String): ValidationResult[String] = name.trim match {
    case ""           => EmptyValue invalidNel
    case _            => name valid
  }

  def isCapitalised(name: String): ValidationResult[String] = name.trim match {
    case isCapital() => name valid
    case _           => DoesNotStartWithCapitalLetter invalidNel
  }
}

object ServiceValidator extends StringValidator {
  type ValidationResult[A] = ValidatedNel[ServiceError, A]

  def validateHelloRequest(name: String): ValidationResult[String] =

    (isNotEmpty(name), isCapitalised(name)) mapN {
      case (name1, _) => name1
    }
}
