## ![](https://media.giphy.com/media/qHl1u2oppzOVi/giphy.gif)

---

## What is JSON?

Jason Derulo? 😅

![](https://media.giphy.com/media/3o6fIQCVGc7zr3wGOI/giphy.gif)

---

## What is JSON? 🎬 #2

```
{
  "name":"John",
  "age":30,
  "cars": {
    "car1":"Ford",
    "car2":"BMW",
    "car3":"Fiat"
  }
}
```

---

## Why JSON?

- A lightweight way passing data around
- Commonly used by APIs

---

## What is Circe

- JSON Library for Scala
- We can see how it is used in the `applied-scala` project by looking at the `build.sbt` file
- 👉 [Docs](https://circe.github.io/circe/)

---

## Why do we need it?

- _Functional_ AND _type-safe_
- Works well with `cats` ecosystem

---

## Alternatives to Circe

- Argonaut 👩‍🚀
- Play-Json ⛹️‍♀️

---

## Enough talking...for now

💪 EXERCISE TIME 💪

![](https://media.giphy.com/media/lPdnkrxkqnS48/giphy.gif)

---

## Part 1: Parsing JSON

- Think about the return type on the first exercise
- Can any string be parsed into valid JSON?

---

## Part 2: Encoding JSON 🔏

- In other words, creating a JSON object
- I.e. we encode when we want to transport data to another place/API

---

## Implicits 🤔

- Circe isn't complicated, implicits are
- What is happening in `personToJson()`?
- Implicits are a way of making our code cleaner by using scope

---

## Implicits 101 🌈

There are 3 different ways of how _implicits_ are defined in Scala

```
1. Defined in local scope, e.g. within an object or file
2. Defined in an import
3. Defined on a companion object
```

Let's take a closer look at how `encodePerson` works

---

## Part 3: Decoding JSON

![](https://media.giphy.com/media/XZLV4qFzC1WtNzRnmN/giphy.gif)

---

## Cardinality

![](https://media.giphy.com/media/xT4uQdzZVwhWUcbpG8/giphy.gif)

---

## Cardinality in 10 seconds ⏱

- In our exercise we are going to take a JSON object and construct it into a Person
- Can anyone see why this may not work for every JSON?

- There is no guarantee that we can convert every JSON string or object to a Person
- That is why we are returning an `Either` in `decodePerson()`

---

## You have uncovered the goddess of magic (in Scala) 🧙‍♀️🔮

_\*Note: Circe actually means Goddess of Magic in Greek_

_\*Another Note: If you have any more questions, feel free to Slack me or post in #sig-scala_
