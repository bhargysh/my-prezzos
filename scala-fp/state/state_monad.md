# Mutability isn't your _worst_ enemy

...sometimes
...hear me out 👩‍🏫

---

## Mutability in the wild

- var 😰
- a = b 😱
- ...other crimes you may have committed

---

## Example time

![run](https://media2.giphy.com/media/Sux3kje9eOx1e/giphy.gif)

---

## Reflection on Mutable Example 🧢

- `+=` If the map already contains a mapping for the key, it will be overridden by the new value.
- overriding vars can cause bugs and make debugging very fun

---

## Immutability in the wild

- val ✅
- no overriding or reassignment
- helps maintain referential transparency

---

## Example time

![locked out](https://media0.giphy.com/media/Gx9JkhbjcL8E8/giphy.gif)

---

## Reflection on Immutable Example 🧢

- `+` a new map with the new binding added to this map
- each new inventory represents a snapshot of what is happening in our inventory, i.e it maintains a history of the inventory
- however, not the most elegant looking code & is it easily testable? 🤷‍♀️

---

## What if...there's another way?

![shocked](https://media0.giphy.com/media/3o6ozC2VM9R0XSMNKo/giphy.gif)

---

## State Monad

`State[S, A]`

```
def apply[S, A](f: S => (S, A)): State[S, A]

```

- `S` is the type representing your state
- `A` is the result the function produces

---

## Dive Deeper

Type alias! Of course 😄

```
type State[S, A] = StateT[Eval, S, A]
```

---

## Short digresssion to Eval

- a monad that controls evaluation
- wraps a value `Eval[+A]`
- the `.value` method gets the result, `A`
- Eval has 3 different ways evaluating strategies:
  - Now -> evaluated immediately
  - Later -> evaluated once when value is needed
  - Always -> evaluated every time it is needed

---

## Short digresssion to StateT

```
type StateT[F[_], S, A] = IndexedStateT[F, S, S, A]
```

Doesn't help too much does it? However, we can see there is a `T` hanging out 🤔
- `F[_]` -> Context
- `A` -> Input type
- `B` -> Output type

---

## Monad Transformer 🤖

- _Enables composition of functions that return a monadic value_
- Eliminates nested monads e.g. `List[Option[String]]` or `IO[Either[Error, Int]]`
- Fancy term for composing different monads to make code cleaner and more readable
- Allows our code to decide how to compose Monads

---

## Same state...but with an informed perspective 👀

```
type State[S, A] = StateT[Eval, S, A]
```

---

## Example time

![below deck](https://media0.giphy.com/media/DFqxrRWUarhF6/giphy.gif)

---

## Reflection on State Example 🧢

- What advantage does the State Monad give us in the example?
- 🤔🤔🤔🤔🤔🤔🤔🤔🤔🤔🤔🤔🤔🤔🤔🤔🤔
- Look at cats [docs](https://typelevel.org/cats/datatypes/state.html) for more
