package example.typelevel

import cats.Monad
import cats.effect.Effect
import cats.implicits._
import cats.mtl.FunctorRaise
import cats.mtl.implicits._
import example.typelevel.NumberConversionError.{OutOfRange, NotANumber, NumberConversionResult}
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl

import scala.util.Try

class NumberToWordsService[F[_] : Effect : NumberConversionResult] extends Http4sDsl[F] {

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

      val inWords = inWordsProgram(n)
      inWords.flatMap {Ok(_)}
    }.handleErrorWith {
      case e: Exception => BadRequest(e.toString)
    }
  }

  private def inWordsProgram(n: String) = {

    val notANumber: String => F[Int] = str => (NotANumber(str): Exception).raise[F, Int]
    val parseToInt = Try(n.toInt).map(Monad[F].pure(_)).getOrElse(notANumber(n))

    val outOfRange: Int => F[String] = number => (OutOfRange(number): Exception).raise[F, String]
    val mapToWords: Int => F[String] = number => mappedNumbers.get(number).map(Monad[F].pure(_)).getOrElse(outOfRange(number))


    val inWordsE = for {
      number <- parseToInt
      inWords <- mapToWords(number)
    } yield inWords
    inWordsE
  }
}

sealed trait NumberConversionError extends Exception

object NumberConversionError {
  type NumberConversionResult[F[_]] = FunctorRaise[F, Exception]

  case class NotANumber(str: String) extends NumberConversionError {
    override def toString: String = s"$str cannot be parsed to number"
  }

  case class OutOfRange(number: Int) extends NumberConversionError {
    override def toString: String = s"$number is too large to be converted to words"
  }


}
