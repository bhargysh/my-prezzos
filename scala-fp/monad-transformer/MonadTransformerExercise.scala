import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

val customGreeting: Future[Option[String]] = Future.successful(Some("welcome back, Lola"))

val excitedGreeting: Future[Option[String]] = customGreeting.map(_.map(_ + "!"))

val hasWelcome: Future[Option[String]] = customGreeting.map(_.filter(_.contains("welcome")))

val noWelcome: Future[Option[String]] = customGreeting.map(_.filterNot(_.contains("welcome")))

val withFallback: Future[String] = customGreeting.map(_.getOrElse("hello, there!"))

// Let's use a monad transformer to convert our code to be more beautiful 💅

import cats.data.OptionT


// OptionT takes a F[Option[A]]
val customGreeting2: OptionT[Future, String] = OptionT(???)

// Uncomment line 25 and compile it
// What's the error and why do we need to add the special import?
// val excitedGreeting2: OptionT[Future, String] = ???

//val withWelcome2: OptionT[Future, String] = customGreeting2.filter(_.contains("welcome"))

//val noWelcome2: OptionT[Future, String] = customGreeting2.filterNot(_.contains("welcome"))

//val withFallback2: Future[String] = customGreeting2.getOrElse("hello, there!")

// COMPILE & TEST IT! 🚀

// ************************************************************************************************
// What if you have an Option[String] and F[String] that you want to _lift_ to OptionT?

// Define an Option[String]

// Define a F[String]

// yield them so they can be printed to stdout
//val result: OptionT[Future, String] = for {
//  f <- ???
//  l <- ???
//} yield s"$f $l"

// ************************************************************************************************
// What if we want to greet the customer with their custom greeting if it exists
// otherwise fall back to a default Future[String] greeting

// What it looks like without OptionT?
val defaultGreeting: Future[String] = Future.successful("hello, there")

val greeting: Future[String] = customGreeting.flatMap((custom: Option[String]) =>
  custom.map(Future.successful).getOrElse(defaultGreeting))

// What could it look like _with_ OptionT?
//val customGreeting3: OptionT[Future, String] = ???
