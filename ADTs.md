## Algebraic Data Types

---

## Unofficial Definition

- ADTs are a way of _categorizing_ code

---

## ADTs in Scala

From Alvin Alexander:

If you create your data models using

- case classes with immutable fields and
- case objects, and
- those data types have no methods, you’re already writing ADTs.

---

## What is Algebraic about it?

- An “algebra” is a _set of objects_
- there are _operators_ that can be used on those objects
- and some _laws_ that govern the behavior

---

## Creating an ADT

```
case class Eye(colour: EyeColour)

sealed trait EyeColour

case object Brown extends EyeColour
case object Black extends EyeColour
case object Blue extends EyeColour
case object Green extends EyeColour
case object Hazel extends EyeColour
```

---

## Let's unpack it

```
case class Eye(colour: EyeColour) // The class constructor itself is an operator

sealed trait EyeColour // A new data type created with a set of objects

case object Brown extends EyeColour
case object Black extends EyeColour
case object Blue extends EyeColour
case object Green extends EyeColour
case object Hazel extends EyeColour
```

---

## Okay nothing fancy going on...yet

![ninja](https://media3.giphy.com/media/3ohhwtQGinneVw1FLy/giphy.gif)

---

## Different kinds of ADTs

1. Sum type
2. Product type
3. Hybrid

---

## The Sum type

- Sum types are typically created with a `sealed trait` as the base type, with instances created via `case object`s
- **We** define all possible instances of the base type
- Can use the phrases “is a” / “or” when talking about Sum types
- e.g. `Green` is a type of `EyeColour`, and `EyeColour` is a `Green` or a `Brown`

---

## Have we seen this before?

- The `Colour` trait and its case objects is a Sum type!
- Where else have we seen these? 🤔

---

## Scala Sum Types

- `Boolean` (`True` or `False`)
- `Option` (`Some(???)` or `None`)
- `Either` (`Right(???)` or `Left(e)`)

---

## Questions?

---

## I have some 🙋🏾‍♀️

- Why is a `sealed trait` used to define the base type?
- Why is a `case object` used?

---

## Sealed trait magic 🔮

- provides exhaustiveness checking...how?
- a sealed trait can only be extended in the file in which it was defined
- the compiler knows all of the subtypes of the trait that can possibly exist as it cannot be extended anywhere else

🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯🤯

---

## Case object FTW 🥇

- only require singleton instances
- e.g. for a `Boolean` it makes sense to only have one instance or `True` and `False`
- we don't want to create a new instance of `True` or `False` _every time_ we need to use a Boolean type
- BONUS: a case object allows us to pattern match by auto-generating an `unapply` method that handles match expressions

---

## Product type

- use case a class constructor to create a data type
- calculate the number of possible concrete instances by multiplying the number of possibilities of all of its constructor fields.
- in the example below: each parameter has 2 possibilities -> 2 \* 2 = 4 possible instances

```
sealed trait Bool
case object True extends Bool
case object False extends Bool

case class DoubleBool(firstBool: Bool, secondBool: Bool)
```

---

## How many possibilities here?

```
case class Person (
    firstName: String,
    lastName: String,
)
```

---

## Infinite possibilities 😱

- Do we see what kinds of problems might occur?
- For example: what might we have to deal with when a function takes in a String?

---

## Questions?

--

## Pattern matching ⭐️

```
def describeEyeColour(colour: EyeColour): String = colour match {
    case Brown => "Brown"
    case Black => "Black"
    case Blue => "Blue"
    case Green => "Green"
    case Hazel => "Hazel"
  }

```

---

## New Eye Colour?

- Let's imagine the `EyeColour` ADT is used by Department of Foreign Affairs for identifying eye colour from passport photos
- They discover that users are inputting grey as an eye colour more often
- How can we model this?

---

## One way

```
case class Eye(colour: EyeColour)

sealed trait EyeColour

case object Brown extends EyeColour
case object Black extends EyeColour
...
case object Grey extends EyeColour
```

---

## Another way

```
case class Eye(colour: EyeColour)

sealed trait EyeColour

case object Brown extends EyeColour
case object Black extends EyeColour
...
case class Other(colourStr: String) extends EyeColour
```

---

## Effects of adding a data type

- What will happen if we try to compile and run `describeEyeColour`?

---

## Not Exhaustive

![](https://git.realestate.com.au/scala-course/intro-slides/raw/master/algebraic-data-types/Jack/blue.png)

- How does this help us?

---

## Saviour in disguise 🕵️‍♀️

- If your app is dealing with `EyeColour` in 10 places, you get 10 compilation errors
- Forced to fix them, otherwise your program will not compile
- Super powerful, less 🐛🐛🐛

---

## The world we live in 🥴

![](https://git.realestate.com.au/scala-course/intro-slides/raw/master/algebraic-data-types/Jack/purity.png)

- Outer ring interacts with HTTP requests, user input, database calls
- We create a validation layer to turn them into valid data by using ADTs.
- Inner ring is where we do the business logic and use the clean data ✨

---

## Questions
