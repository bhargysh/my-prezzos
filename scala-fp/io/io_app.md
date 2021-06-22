# IOApp

---
## Executing an IO

- Push our side effects to the edge of the 'world' 🌏
- `IO` is not _evaluated_ until we invoke `unsafeRunSync()`

```scala
object Main {
  def program(args: List[String]): IO[Unit] =
    IO(println(s"Hello world! Args $args"))

  def main(args: Array[String]): Unit =
    program(args.toList).unsafeRunSync()
}
```

---

## Unsafe...why

- From 🐱s Docs: This is an UNSAFE function as it is
    * impure
    * performs side effects
    * is blocking
    * may throw exceptions, and
    * does other things that are _at odds with reasonable software_

---

## Safer with IOApp

```scala
object MainSafe extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    args.headOption match {
      case Some(name) =>
        IO(println(s"Hello, $name.")).as(ExitCode.Success)
      case None =>
        IO(println("Usage: Bharg's cool as app")).as(ExitCode(2))
    }
}
```

---

## Main differences

1. `args` passed in as a `List` not an `Array`
2. We use an `ExitCode` to specify success or an error code
3. Some built in magic 🔮

---

## How it works

- if the program returns an `ExitCode.Success`, the main method exits and shutdown is handled by `IOApp`
- Let's have a look at what that means 👀

---

## How it works cont...

- if program completes with an exit code `!= 0` the program exits with that as the error code
- if the `IO` terminates in error, it is printed to standard error and `sys.exit` is called

---

## Is there a F[App]?

- `IOApp` doesn’t have an `F[_]` parameter unlike the other data types
- Different `F[_]` data types have different requirements for evaluation at the end of the world
- 💡 `IOApp` can be used for any `F[_]`
    - Any `Effect` or `ConcurrentEffect` can be converted to `IO`

---

## Some real examples

- Using `unsafeRunSync` 👉 [examples](https://git.realestate.com.au/search?q=unsaferunsync+filename%3AMain.scala+language%3AScala+language%3AScala&type=Code&ref=advsearch&l=Scala&l=Scala)
- Using `IOApp` 👉 [example](https://github.com/realestate-com-au/applied-scala/blob/master/src/main/scala/com/reagroup/appliedscala/Main.scala)
- _Not_ using `unsafeRunSync` or `IOApp` 👉 [example](https://git.realestate.com.au/rca/commercial-depth-service/blob/master/src/main/scala/com/reagroup/rca/commercialdepthservice/Main.scala)
