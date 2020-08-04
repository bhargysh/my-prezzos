## Mutability

- `+=` If the map already contains a mapping for the key, it will be overridden by the new value.
- overriding vars can cause bugs or make
---

## Immutability
- `+` A new map with the new binding added to this map
- each inventory represent a different snapshot of what is happening in our inventory
- not very elegant looking code
---

## State
- `State[S, A]`

```
def apply[S, A](f: S => (S, A)): State[S, A]

```
---

## Dive Deeper

```
type State[S, A] = StateT[Eval, S, A]
```
---

## Eval
- a monad that controls evaluation
- wraps a value or computation `Eval[+A]`
- the `.value` method gets the result, `A`
- Eval has 3 different ways evaluating strategies:
    - Now -> evaluated immediately
    - Later -> evaluated once when value is needed
    - Always -> evaluated every time it is needed
---

## But what is StateT

```
type StateT[F[_], S, A] = IndexedStateT[F, S, S, A]
```

- doesn't help too much, see there is a `T` hanging out
- AKA Monad Transformer

---

## Monad Transformer 🤖

- Eliminates nested monads e.g. `List[Option[String]]` or `IO[Either[Error, Int]]`
- Fancy term for composing different monads to make code cleaner and more readable
- Allows our code to decide how to compose Monads
---

## State

```
type State[S, A] = StateT[Eval, S, A]
```

