## ![](https://media.giphy.com/media/l0MYt5jPR6QX5pnqM/giphy.gif)

---

# Typeclasses

---

## ...tell me more

- What
- How

---

## What is a typeclass?

- supports ad hoc polymorphism
- adds constraints to type variables in parametrically polymorphic types

(Wikipedia says so)

🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯

---

## What is a typeclass? 🎬 #2

- _In other words:_ it defines some behavior for a type, AND implements that behavior differently depending on the type
- 💁🏽‍♀️ Kinda sounds like inheritance, why don't we do just stick with OO
- ...well Bharg, it is because they are more powerful, extensible and dare I say it...simpler 🌈

---

## Example

- Convention in Scala is to have a `trait` followed by methods on a companion object or class
- What is 💃🏽?
- Have you 👀 this before?

```
trait Danceable[💃🏽] {
    def dance(a: 💃🏽): Boolean
}
```

---

## Functor, Monad & more

```
@typeclass trait Functor[F[_]] extends Invariant[F] { self =>
  def map[A, B](fa: F[A])(f: A => B): F[B]
  ...
}
```

🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯

---

## ![](https://cdn.rawgit.com/tpolecat/cats-infographic/master/cats.svg)

---

## Implement Danceable

Let's do this team! [It's mobbbing time](https://scalafiddle.io/sf/rulCu1j/0)
![](https://media.giphy.com/media/QWwEdgDbYjFbfOMJ3z/giphy.gif)

---

## What it could look like

```
case class Person(canDance: Boolean)

object Danceable {
    val danceableOfPerson: Danceable[Person] = new Danceable[Person] {
    override def dance(a: Person): Boolean = a.canDance
  }
}
```

---

## How do we use it

```
val bharg = Person(canDance = false)
val canBhargDance = Danceable.danceableOfPerson.dance(bharg)
```

This doesn't read all that well though, `Danceable.danceableOfPerson.dance`? 🤨

---

## Implicits (handwave-ish)

```
object Danceable {
  def apply[A](implicit danceable: Danceable[A]): Danceable[A] = implicitly[Danceable[A]]

  implicit val danceableOfPerson: Danceable[Person] = new Danceable[Person] {
    override def dance(a: Person): Boolean = a.canDance
  }
}

val bharg = Person(canDance = false)
val canBhargDance = Danceable.apply.dance(bharg)
```

---

## What is the main separation of concern here?

- Danceable doesn't care what it's given and Person doesn't care how it is going to be used with something.
- They are both independent of each other

---

## Key Takeaways

Typeclasses:

- define behaviors separate from the objects & types that implement those behaviors
- are expressed in Scala with traits that take type parameters

---

## More exercises!

- [Link](https://www.scala-exercises.org/scala_tutorial/type_classes)
