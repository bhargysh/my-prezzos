# What makes FP

- Immutability
- Pure functions
- Referential transparency
- Read more [here](https://www.freecodecamp.org/news/an-introduction-to-the-basic-principles-of-functional-programming-a2c2a15c84/)

---

# Referential Transparency

---

## Expressions 🤨

- Functional languages operate on expressions not statements
- Whats the difference between an expression vs statement?
- Example of an expression:

```
{ 1 + 2 * 5 } => evaluates to 11
```

---

## What does it mean?

An expression is referentially transparent if it can be replaced with its value without changing the program’s behavior.

---

## Let's see an example ✨

```
val multiplyByTwo = (number: Int) => number * 2
```

👀 https://scalafiddle.io/sf/zGRoQCR/1

---

## Let's see a 🤮 example

```
val random = scala.util.Random
val multiplyByRandom = (number: Int) => number * random.nextInt
```

👀 https://scalafiddle.io/sf/zGRoQCR/2

---

## What makes Random not RT?

1. It is generating a new integer everytime you call `nextInt` (a side effect).
2. For the same input we get a different output, i.e. 🙅‍♀️ RT & also not pure
3. We can't think of `multiplyByRandom` as a black box.

---

## Let's try it together 💪

Pure random number generation!

👀 https://scalafiddle.io/sf/zGRoQCR/3
