package codes.craftsman

import org.scalatest.WordSpec

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ Await, Future }

class LifterSpec extends WordSpec {
  import codes.craftsman.Lifter.LiftOps

  "Lifter" should {
    "lift 1 into Future" in {
      assert(Await.result(1.into[Future], 1.seconds) === Await.result(Future.successful(1), 1.seconds))
    }

    "lift 1 into Option" in {
      assert(1.into[Option] === Some(1))
    }

    "lift 1 into Either using LiftEither" in {
      import codes.craftsman.Lifter.LiftEither

      assert(1.into[LiftEither] === Right(1))
    }

    "lift 1 into Option into Future" in {
      assert(Await.result(1.into[Future, Option], 1.seconds) === Await.result(Future.successful(Some(1)), 1.seconds))
    }

    "lift 1 into Option into Either into Future" in {
      import codes.craftsman.Lifter.LiftEither

      assert(Await.result(1.into[Future, LiftEither, Option], 1.seconds)
        === Await.result(Future.successful(Right(Some(1))), 1.seconds))
    }

    "lift \"foo\" into Option" in {
      assert("foo".into[Option] === Some("foo"))
    }

    "lift 'c' into Option" in {
      assert('c'.into[Option] === Some('c'))
    }

    "lift 1.1 into Option" in {
      assert(1.1.into[Option] === Some(1.1))
    }

    "lift List[Int] into Option" in {
      assert(List(1).into[Option] === Some(List(1)))
    }

    "lift Nil into Option" in {
      assert(Nil.into[Option] === Some(Nil))
    }
  }
}
