## Monad Transformer 🤖

- Eliminates nested monads e.g. `List[Option[String]]` or `IO[Either[Error, Int]]`
- Fancy term for composing different monads to make code cleaner and more readable
- Allows our code to decide how to compose Monads

---

## What does it do? 🤓

- Allows us to compose the Monad instance of the data type with any other Monad instance
- e.g. `OptionT[F[_], A]` allows us to compose Option with any other `F[_]`
- 👉 `List[Option[String]]` can be written as `OptionT[List, String]`

---

### Use case

Here is an example (thanks Cats 🐱) of where a Monad transformer _may_ be useful:

```
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

val customGreeting: Future[Option[String]] = Future.successful(Some("welcome back, Lola"))

val excitedGreeting: Future[Option[String]] = customGreeting.map(_.map(_ + "!"))

val hasWelcome: Future[Option[String]] = customGreeting.map(_.filter(_.contains("welcome")))

val noWelcome: Future[Option[String]] = customGreeting.map(_.filterNot(_.contains("welcome")))

val withFallback: Future[String] = customGreeting.map(_.getOrElse("hello, there!"))
```

Can we see a pattern here?

---

## Let's transform it 🧠

_cool GIF while I bring up IntelliJ_

![](https://media.giphy.com/media/8r74OZnPUDnPy/giphy.gif)

---

## Pros & cons

- ✅ Good for some use cases but nesting more than two monads gets trickyyyyy
- 🤔 There’s a lot of wrapping and unwrapping involved so not highly performant
- 🤔 They’re not built into Scala (multiple library implementations), should call `.value` on transformers and expose as `G[F[A]]` which doesn’t impose opinionated choice on users

---

## Does it only exist for OptionT?

- There is also EitherT for nested Either Monad
- _And then there is Kleisli_ 😱 which is more sophisticated than `OptionT` and `EitherT` (for another day)
- If you want to read more about what we covered today _or_ Kleisli, go to the Cats [docs](https://typelevel.org/cats/datatypes.html) or this [awesome] blog(https://blog.buildo.io/monad-transformers-for-the-working-programmer-aa7e981190e7)

---

## Summary

🌈 Monad transformers are just a flattened representation of two nested monads, that is also a monad itself 🌈
