# Semigroup

![schitts creek hug](https://media1.giphy.com/media/LOFWDPFOTuHJZKL3hI/giphy.gif)

---

## What it provides

```scala
trait Semigroup[A] {
  def combine(x: A, y: A): A
}
```

- an associative binary operation 🤔

---

## Associativity ➕

```scala
combine(x, combine(y, z)) = combine(combine(x, y), z)
```

---

## Example: Semigroup for Int

```scala
import cats.Semigroup

implicit val intAdditionSemigroup: Semigroup[Int] = new Semigroup[Int] {
  def combine(x: Int, y: Int): Int = x + y
}
```

---

## Example: Usage

```scala
Semigroup[Int].combine(1, 2)

Semigroup[Int].combine(1, Semigroup[Int].combine(2, 3))
```

---

## Already defined instances

- 🐱s provides many `Semigroup` instances out of the box
  - `Int`
  - `String`
  - `List`
  - `Set`
  - `Tuple`
- Can everthing be solved with the cats implicits import? 🧠

---

## New type

```scala
FruitInventory(total: Int) //value case class
```

- Will there be a Semigroup instance for these case classes?

---

## Defining a new type instance

```scala
import cats.kernel.Semigroup

case class FruitInventory(count: Int)

implicit val fruitInventorySemigroup: Semigroup[FruitInventory] = new Semigroup[FruitInventory] {
  def combine(x: FruitInventory, y: FruitInventory): FruitInventory = FruitInventory(x.count + y.count)
}

Semigroup[FruitInventory].combine(FruitInventory(11), FruitInventory(24))
```

---

## What Semigroup is good for ⭐️

- When we want to be able to combine types, i.e.
  - when doing validation we want to combine all errors
  - done by another type [Validated](https://typelevel.org/cats/datatypes/validated.html)

---

## Exploiting laws

- must be associative
- e.g. summing a `List[Int]` we can do
  - 👉 `foldLeft()`
  - 👉 `foldRight()`
  - we can also split a list apart and sum the parts in parallel

---

## Associativity in List

```scala
val list = List(1, 2, 3, 4, 5)
val (left, right) = list.splitAt(2)
val sumLeft = left.foldLeft(0)(Semigroup.combine(_, _))
val sumRight = right.foldLeft(0)(Semigroup.combine(_, _))
val result = Semigroup.combine(sumLeft, sumRight)   // result: Int = 15
```

---

## Combine a generic List

```scala
def combineAll[A: Semigroup](as: List[A]): A =
  as.foldLeft(???)(Semigroup.combine(_, _))
```

- What should the _fallback_ or _default_ be for a generic list?

---

## Empty list?

```scala
def combineAll[A: Semigroup](as: List[A]): A =
  as.foldLeft(List.empty[A])(Semigroup.combine(_, _))
  // 🙅‍♀️ No implicits found for parameter ev: Semigroup[Any] 🙅‍♀️
```

- 💡 `Semigroup` does not give us an **identity** or **fallback** if the list is empty
- ✅ `Monoid` type class does

---

## Until next time 👋🏽

- Read more about Semigroup [here](https://typelevel.org/cats/typeclasses/semigroup.html)
- Learn about `Monoid` 💪

![schitts creek bye](https://media2.giphy.com/media/Qa4kmUlMejdHyclAYQ/giphy.gif)
