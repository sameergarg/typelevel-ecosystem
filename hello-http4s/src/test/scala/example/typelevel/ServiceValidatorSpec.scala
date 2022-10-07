package example.typelevel

import cats.data.NonEmptyList
import cats.data.Validated.{Invalid, Valid}
import example.typelevel.Domain.ServiceError
import org.scalatest.{Matchers, WordSpec}

class ServiceValidatorSpec extends WordSpec with Matchers {
  "Service Validator" should {
    "reject empty String" in new StringValidator {
      isNotEmpty("   ") shouldBe Invalid(NonEmptyList.one(ServiceError.EmptyValue))
    }

    "accept non empty String" in new StringValidator {
      isNotEmpty("Sameer") shouldBe Valid("Sameer")
    }
  }
}
