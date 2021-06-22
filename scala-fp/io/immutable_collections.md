# Scala Immutable Collection Cheat Sheet

![immutable collection hierarchy](https://docs.scala-lang.org/resources/images/tour/collections-immutable-diagram.svg)

## Commonly used collections

[Big O cheat sheet](https://www.bigocheatsheet.com/)

| Collection        | Good for           | Performance  | Should I use? |
| ------------- |:-------------:| -----:| -----:|
| `List[+A]`      | Last in first out (LIFO) | `O(1)` prepend<br> `O(1)` head/tail access<br> `O(n)` for other operations | ⚠️ In Scala a `List` is singly-linked, extends from `LinearSeq[+A]` |
| `Vector[+A]`      | Random access & updates      |  `O(1)` append, prepend <br> `O(log n)` rancom access & update  |  💭 Default implementation of `IndexedSeq[+A]`, tree-shaped  |
| `Set[A]` | When we don't care about order and don't want duplicates     | `O(1)` contains, add, remove <br> `O(n)` to search  | ⚠️ Use when you want to see whether elements belong to set of values |
| `Map[K, +V]` | General purpose key value store  |  `O(1)` lookup, insert, delete |  💭 There are [other Map types](https://alvinalexander.com/scala/scala-immutable-map-class-methods-examples-syntax/) that may suit better depending on its use |

### List vs Vector

- A good summary highlighted in this blog [here](https://www.lihaoyi.com/post/BenchmarkingScalaCollections.html#lists-vs-vectors)

- Is `Vector[+A]` the best 'general purpose' type? Look no [further](https://www.lihaoyi.com/post/BenchmarkingScalaCollections.html#vectors-are-ok) 👀

### What about Seq?

- In Scala, `Seq` is both an _interface_ **and** a _concrete_ implementation., i.e. you can instantiate it
    - E.g. `Seq[+A]`
- `Seq` is also backed by `List`
    - E.g. `val s: Seq[Int] = List(1,2,3)`
- Difficult to program with type classes in Cats 🐱
    - Typeclass instance for `Seq` is not provided

### What is [+A]?

💡 Covariance as explained [here](https://docs.scala-lang.org/tour/variances.html)

```scala
abstract class Animal {
  def name: String
}
case class Cat(name: String) extends Animal
case class Dog(name: String) extends Animal
```

- In Scala, `List[+A]` is covariant
- This means that a `List[Cat]` is a `List[Animal]` and a `List[Dog]` is a `List[Animal]`

```scala
def printAnimalNames(animals: List[Animal]): Unit =
  animals.foreach {
    animal => println(animal.name)
  }
val cats: List[Cat] = List(Cat("Whiskers"), Cat("Tom"))
val dogs: List[Dog] = List(Dog("Fido"), Dog("Rex"))

// prints: Whiskers, Tom
printAnimalNames(cats)

// prints: Fido, Rex
printAnimalNames(dogs)
```

- ❗️ If `List[+A]` was not covariant, the last two method calls would not compile

## Uncommon collections 🕵️‍♀️

### Map types

| Collection        | What is it?           | Performance  | Should I use? |
| ------------- |:-------------:| -----:| -----:|
| `ListMap[K, +V]`      | List-based<br> Entries are stored in reverse insertion order, newest key is at the head of the list | `O(1)` last, init<br> `O(n)` getting head or tail, adding, removing | ⚠️ Suitable only for a small number of elements |
| `SortedMap[K, +V]`      | Key-value pairs are sorted according to an `[[scala.math.Ordering]]` on the keys |  `O(n log n)` - `O(n^2)` add, remove, search |  ⚠️ Suitable only for a small number of elements  |
| `TreeMap[K, +V]` | `SortedMap` stored in a red-black tree  | `O(1)` contains, add, remove <br> `O(log n)` to search, insert, delete  | ⚠️ Optimal when range queries will be performed<br> When traversing in order of an ordering |
| `SeqMap[K, +V]` | Generic trait for ordered immutable linked maps  | `O(log n)`- `O(n)` to search, insert, delete  | ⚠️ Useful when you need insertion order without an ordering<br> Concrete classes have to provide functionality for the abstract methods in `SeqMap`

