# Either 👈 | 👉

---

## Recap

- Exceptions don't allow for local reasoning
- We cannot determine from the method signature that it may throw an error 🙅‍♀️

---

### ...plausible?

```
def convertToInt(x: String): Int
```

---

### Uh oh

```
def convertToInt(x: String): Int = {
    "🌈".toInt
}
```

---

### Option

![option](https://jaxenter.com/wp-content/uploads/2017/08/hauck_scala_3.png)

---

### Same same but different 👯
![either](https://jaxenter.com/wp-content/uploads/2017/08/hauck_scala_4.png)

Looks familar?

---

### Definition

```
sealed trait Either[+E, +A]
case class Right[A](value: A) extends Either[Nothing, A]
case class Left[E](error: E) extends Either[E, Nothing]
```

---

### Using Either 👩‍💻

```
def isEven(number: Int): Either[String, Int] = {
  if (number % 2 == 0)
    Right(number)
  else
    Left("The number isn't even")
}
```

---

### Exercise time 🧠
