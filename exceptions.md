# Exceptions

---

## In the wild 🐗

```
try {
    //some logic here
} catch(Exception e) {
    //uh-oh something went wrong 😮
}

```
---

## Why not?

- It allows your program to lie 😰

```
def toInt(x: String): Int //does this look right?
```

- breaks the flow of your program

```
val s = str.toInt // throws if cannot convert
updatedName(s)
```

- generating an exception's stacktrace can be costly
---

## Exceptions in Scala

They exist! 🙈

```
type Exception = java.lang.Exception //this extends from Throwable

case object MyCustomException extends Exception(message)

throw new MyCustomException
```
---

## Exercise time 🏃🏾‍♀️

---

## Summary 🤓

- Exceptions have their shortcomings
- If you reaaaaally have to use it, you can
- There are alternatives to throwing Exceptions in Scala 💭
