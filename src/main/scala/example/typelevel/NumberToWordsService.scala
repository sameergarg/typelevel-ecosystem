package example.typelevel

import cats.Monad
import cats.effect.Effect
import cats.implicits._
import example.typelevel.NumberConversionError.{InWordsConversionFailure, NotANumber}
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl

import scala.util.Try

class NumberToWordsService[F[_]: Effect] extends Http4sDsl[F] {

  val mappedNumbers = Map(
    0 -> "Zero",
    1 -> "One",
    2 -> "Two",
    3 -> "Three",
    4 -> "Four",
    5 -> "Five",
    6 -> "Six",
    7 -> "Seven",
    8 -> "Eight",
    9 -> "Nine",
    10 -> "Ten"
  )
  val numberToWords: HttpService[F] = HttpService {
    case GET -> Root / n => {

      val inWordsE: F[Either[NumberConversionError, String]] = inWordsProgram(n)

      inWordsE.flatMap(_.fold(error => BadRequest(s"Unable to convert $error"), inWords => Ok(inWords)))
    }
  }

  private def inWordsProgram(n: String) = {
    val parseToInt = Try(n.toInt).toEither.left.map[NumberConversionError](_ => NotANumber(n))
    val mapToWords: Either[NumberConversionError, Int] => Either[NumberConversionError, String] = number => number.map(parsedNumber => mappedNumbers.get(parsedNumber).getOrElse("Some large number"))

    val inWordsE = for {
      number <- Monad[F].pure(parseToInt)
      inWords <- Monad[F].pure(mapToWords(number))
    } yield inWords
    inWordsE
  }
}

sealed trait NumberConversionError extends Exception

object NumberConversionError {
  case class NotANumber(str: String) extends NumberConversionError
  case class InWordsConversionFailure(number: Int) extends NumberConversionError


}
