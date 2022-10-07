package example.typelevel

import cats.Id
import cats.implicits._
import example.typelevel.TimedTask.{Callback, Time}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{Matchers, WordSpec}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TimedTaskSpec extends WordSpec with Matchers with ScalaFutures {

  implicit val timedTaskUsingId = new TimedTask[Id] {
    override def time(): Id[TimedTask.Time] = Time(System.currentTimeMillis())
  }

  implicit override val patienceConfig = PatienceConfig(timeout = Span(2, Seconds), interval = Span(20, Millis))
  implicit val timedTaskUsingFuture = new TimedTask[Future] {
    override def time(): Future[Time] = Future.successful(Time(System.currentTimeMillis()))
  }


  "Timed Task" should {
    "calculate time using simple interpreter" in {
      val timeTaken = TimedTask.calculateTime(Callback[Id, Unit](() => Thread.sleep(1000)))
      timeTaken.value should be > 0L
    }

    "calculate time using future based interpreter" in {
      val timeTaken = TimedTask.calculateTime(Callback[Future, Unit](() => Future.successful(Thread.sleep(1000))))
      whenReady(timeTaken){ time =>
        time.value should be > 0L
      }
    }

  }

}
