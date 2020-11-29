# Monoid

![schitts creek empty](https://media1.giphy.com/media/26gs78HRO8sOuhTkQ/giphy.gif)

---

## What it provides

```scala
trait Monoid[A] extends Semigroup[A] {
  def empty: A
}
```

- an identity for the combine operation
- `combine(x, empty) = combine(empty, x) = x`

---

![diagram of the two type classes](./semigroup_monoid_diagram.jpg)

---

## Monoids in the wild 🐻

- Ints (with 0)
- Strings (with "")

```scala
import cats.Monoid

implicit val intAdditionMonoid: Monoid[Int] = new Monoid[Int] {
  def empty: Int = 0
  def combine(x: Int, y: Int): Int = x + y
}

Monoid[Int].combine(x, Monoid[Int].empty)   // 1
Monoid[Int].combine(Monoid[Int].empty, x)   // 1
```

---

## Revisit a previous constraint

```scala
def combineAll[A: Monoid](as: List[A]): A =
  as.foldLeft(Monoid[A].empty)(Monoid[A].combine)

combineAll(List(1, 2, 3))                   // res1: Int = 6
combineAll(List("hello", " ", "world"))     // res2: String = hello world
combineAll(List.empty[String])              // res3: String = ""
```

---

## What have we achieved so far

- ✅ We can combine defined types with `Semigroup`
- ✅ We can combine empty types with `Monoid`
- ...can we combine non-optional types with what we have ❓
  - e.g. `NonEmptyList`
  - `NonEmptyList` type forms a semigroup through `++`
  - what is the identity element?

---

## Option monoid

- Can be used to collapse `List[NonEmptyList[A]]`

```scala
def empty: Option[A] = None

def combine(x: Option[A], y: Option[A]): Option[A] = // look in OptionInstances.scala
```

- There is a `Monoid[Option[A]]`

---

## Revising the data structures 🤓

```scala
import cats.{ Semigroup, Monoid }
import cats.data.NonEmptyList

val myList: List[NonEmptyList[Int]] =
    List(NonEmptyList(1, List(2, 3)), NonEmptyList(4, List.empty[Int]))
val lifted: List[Option[NonEmptyList[Int]]] =
    myList.map(nel => Option.apply(nel))
Monoid.combineAll(lifted)           // Some(NonEmptyList(1, 2, 3, 4))

Semigroup.combineAllOption(myList)  // Some(NonEmptyList(1, 2, 3, 4))
```

---

## Part 2, finito 💃🏽

- [Monoid](https://typelevel.org/cats/typeclasses/monoid.html)

![schitts creek bye](https://media0.giphy.com/media/l1J9EurFGl1o5j3uU/giphy.gif)
