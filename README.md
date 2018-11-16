# lifting
Small library to allow easy lifting into arbitrary Monad/Container object

## What problem are we solving
When composing logic one often runs into a situation where one should lift the result of one operation into to be able naturally. This requires writing boilerplate, that can be avoided:
```scala
import scala.concurrent.Future

def expensiveOperation: Future[Int] = ???

val result: Future[Int] = for {
  expensiveResult <- expensiveOperation
  simpleResult <- Future.successful(1)
} yield expensiveResult + simpleResult
```

Always repeating code like `Future.successful`, can become cumbersome and can be prevented using this lirabry

## How to use it
This will allow you to lift an arbitry value into something else. Out of the box lifting is supported into `Future`, `Option` and `Either` (though using `LiftEither` type that pins down the `Left` type to `Nothing`) by typing the following code:

```scala
import codes.craftsman.Lifting.LiftOps

1.into[Option]: Option[Int]
1.into[Future]: Future[Int]
1.into[Future, Option]: Future[Option[Int]]

import codes.craftsman.Lifting.LiftEither
1.into[LiftEither]: Either[Nothing, Int]

import scala.concurrent.Future
val result: Future[Int] = for {
  expensiveResult <- expensiveOperation
  simpleResult <- 1.into[Future]
} yield expensiveResult + simpleResult
```

The `.into` extension method uses the `Lifter` Typeclass, which allows one to use this lifting for an arbitrary container. To allow this one needs to implement an `Lifter` and bring this into scope 
