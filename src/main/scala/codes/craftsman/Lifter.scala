package codes.craftsman

import scala.concurrent.Future
import scala.language.higherKinds

trait Lifter[F[_]] {
  def lift[A](value: A): F[A]
}

object Lifter {
  type LiftEither[A] = Either[Nothing, A]

  implicit class LiftOps[A](value: A) {
    @inline private def lifter[F[_]: Lifter] = implicitly[Lifter[F]]

    def into[F[_] : Lifter]: F[A] = lifter[F].lift(value)

    def into[F0[_] : Lifter, F1[_] : Lifter]: F0[F1[A]] = lifter[F0].lift(lifter[F1].lift(value))

    def into[F0[_] : Lifter, F1[_] : Lifter, F2[_] : Lifter]: F0[F1[F2[A]]] =
      lifter[F0].lift(lifter[F1].lift(lifter[F2].lift(value)))
  }

  implicit val futureLift: Lifter[Future] = new Lifter[Future] {
    def lift[A](value: A): Future[A] = Future.successful(value)
  }
  implicit val optionLift: Lifter[Option] = new Lifter[Option] {
    def lift[A](value: A): Option[A] = Some(value)
  }
  implicit val eitherLift: Lifter[LiftEither] = new Lifter[LiftEither] {
    def lift[A](value: A): LiftEither[A] = Right(value)
  }
}
