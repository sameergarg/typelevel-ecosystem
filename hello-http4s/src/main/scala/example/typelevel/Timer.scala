package example.typelevel

import cats.Monad
import cats.implicits._
import example.typelevel.TimedTask.{Callback, Time}

trait TimedTask[F[_]] {

  def time(): F[Time]

  def operation[T](callback: Callback[F, T]): F[T] = callback.fn()
}

object TimedTask {
  final case class Time(value: Long) extends AnyVal

  final case class Callback[F[_], T](fn: () => F[T]) extends AnyVal

  def apply[F[_]: TimedTask]: TimedTask[F] = implicitly

  def calculateTime[F[_]: Monad: TimedTask, T](callback: Callback[F, T]): F[Time] = for {
    start <- TimedTask[F].time
    _ = TimedTask[F].operation(callback)
    end <- TimedTask[F].time
    time <- Monad[F].pure(Time(end.value - start.value))
  } yield time
}