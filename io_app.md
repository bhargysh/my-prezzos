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

---

## How does it work

- if the program returns an `ExitCode.Success`, the main method exits and shutdown is handled by `IOApp`
- if completed with an exit code != 0, app exits with that as the error code
- if the IO terminates in error, it is printed to standard error and `sys.exit` is called

---

## Is there a FApp?

- `IOApp` doesn’t have an `F[_]` parameter, unlike the other data types
- Different `F[_]` data types have different requirements for evaluation at the end of the world
- Monix’s `Task` comes with its own `TaskApp`
- 💡 `IOApp` can be used for any `F[_]`
    - Any `Effect` or `ConcurrentEffect` can be converted to `IO`
