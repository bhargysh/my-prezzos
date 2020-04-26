import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.semiauto.deriveEncoder
import io.circe.syntax._

/**
  * Circe (pronounced SUR-see, or KEER-kee in classical Greek, or CHEER-chay in Ecclesiastical Latin) is a JSON library for Scala.
  *
  * We like Circe as opposed to other libraries because it is functional, type-safe and very idiomatic.
  * It integrates very well with the Cats ecosystem.
  *
  * For more comprehensive docs on Circe:
  * https://circe.github.io/circe/
  *
  * There are 3 parts to these exercises.
  *
  * 1. Parsing (`String => Json`)
  * 2. Encoding (`A => Json`)
  * 3. Decoding (`Json => A`)
  */
object CirceExercises {

  /**
    * Json Parsing
    */

  /**
    * Why is the return type an `Either`?
    * We know an Either has 2 type holes, the left side returns an error and right returns some type
    * DOCS: https://github.com/circe/circe/blob/master/modules/parser/jvm/src/main/scala/io/circe/parser/package.scala
    */
  def strToJson(str: String): Either[ParsingFailure, Json] = {
    import io.circe.parser._
    parse(str)
  }

  case class Person(name: String, age: Int)

  /**
    * Encoding
    *
    * Hint: Use `Json.obj()`
    * Hint: Use `.asJson` to convert Scala standard types to Json
    *
    * For more comprehensive examples:
    * https://circe.github.io/circe/codecs/custom-codecs.html
    */

  def personToJson(person: Person): Json = Json.obj(
    "name" -> person.name.asJson,
    "age" -> person.age.asJson
  )

// talk about how implicits work in Scala

// comment out import io.circe.syntax._ and rebuild project
// get an error that asJson is not a member of string
// uncomment and CMD + hover over the first asJson
// we see Encoder[String] for person, and Encoder[Int] for age
// that is what the asJson is doing
// since String and Int are primitive types they already have encoders defined
// we could do the same for a custom type by making our own custom encoder
// AND declaring it as implicit
// we will do this for encoding Person

// ANY QUESTIONS?

  /**
    * Try make a syntax error in the following Json document and compile.
    * What happens?
    */
  val validJson: Json = {
    import io.circe.literal._

    json"""
      {
        "someKey": "someValue",
        "anotherKey": "anotherValue"
      }
    """
  }
  //  remove comma, raises an exception (invocation target)...caused by parse exception

  /**
    * Create an `Encoder` instance for `Person` by implementing the `apply` method below.
    *
    * Make `personEncoder` an `implicit` to avoid having to pass the `Encoder` instance
    * into `asJson` explicitly.
    */
  def encodePerson(person: Person): Json = {
    val personEncoder: Encoder[Person] = new Encoder[Person] {
      override def apply(person: Person): Json = personToJson(person)
    }
    person.asJson(personEncoder)
//    OR
//    person.asJson
//    if we make personEncoder implicit, so asJson can accept an implicit encoder
//    as we have seen previously, it goes looking in local scope
//    it finds an encoder for Person object and uses that

//    ANY QUESTIONS?
  }

  /**
    * There is an alternate way to construct an `Encoder` instance,
    * by using `Encoder.forProduct2`.
    *
    * This may sometimes be simpler than using `Json.obj`.
    */
  def encodePersonAgain(person: Person): Json = {
    implicit val personEncoder: Encoder[Person] =
      Encoder.forProduct2("name", "age")(person => (person.name, person.age))
    person.asJson
  }
//  forProduct takes the key of what we want to encode and the object, along with each param's encoders implicitly

  /**
    * Sick of writing custom encoders? You can use "semiauto derivation"
    * to create an `Encoder` instance for you using a Scala feature called macros.
    *
    * The downside to this is the keys of your `Json` are now tightly coupled with
    * how you have named the fields inside `Person`
    *
    * Hint: Use `deriveEncoder`
    *
    * For more comprehensive examples:
    * https://circe.github.io/circe/codecs/semiauto-derivation.html
    */
  def encodePersonSemiAuto(person: Person): Json = {
    import io.circe.generic.semiauto._

    implicit val personEncoder: Encoder[Person] = deriveEncoder[Person]
//    can also do deriveEncoder
    person.asJson
  }

// ANY QUESTIONS before we go to the last part?
// talk about cardinality

  /**
    * Decoding
    */

  /**
    * 1. Why is the return type an `Either`?
    *
    * 2. Use the provided `HCursor` to navigate through the `Json`, and try to
    *    create an instance of `Person`.
    *
    * Hint: Use `cursor.downField("name")` to navigate to the `"name"` field.
    * `cursor.downField("name").as[String]` will navigate to the `"name"` field
    * and attempt to decode the value as a `String`.
    *
    * Alternatively, you can use `cursor.get[String]("name")` to do the same thing.
    *
    * Once you have retrieved the `name` and `age`, construct a `Person`!
    *
    * For more comprehensive cursor docs:
    * https://circe.github.io/circe/api/io/circe/HCursor.html
    */
  def jsonToPerson(json: Json): Either[DecodingFailure, Person] = {
    val cursor: HCursor = json.hcursor
//    1. let's go down the json object and find the fields we want to construct a Person object
    val name: Result[String] = cursor.downField("name").as[String]
    val age: Result[Int] = cursor.downField("age").as[Int]
//    2. How do we construct a Person? Just pass these vals to construct a Person right?
//    3. Uh oh, we expect a String but it is actually giving us a Result[???] How do we deal with this?
//    and what is Result?
//    4. Result is just a type alias and because it is a wrapper -> Monad, we can use flatMap on it
    for {
      name <- cursor.downField("name").as[String]
      age <- cursor.downField("age").as[Int]
    } yield Person(name, age)
//    we can leave it like this, or we can do it another way
    for {
      name <- cursor.get("name")[String]
      age <- cursor.get("age")[Int]
    } yield Person(name, age)
  }

  /**
    * Construct a `Decoder` instance for `Person`, that uses the `HCursor` to
    * navigate through the `Json`.
    *
    * Note: Result[A] is an alias for Either[DecodingFailure, A].
    *
    * For more comprehensive examples:
    * https://circe.github.io/circe/codecs/custom-codecs.html
    */
  def decodePerson(json: Json): Either[DecodingFailure, Person] = {
    import cats.implicits._

    implicit val personDecoder: Decoder[Person] = new Decoder[Person] {
      override def apply(cursor: HCursor): Result[Person] =
        for {
          name <- cursor.downField("name").as[String]
          age <- cursor.downField("age").as[Int]
        } yield Person(name, age)
    }
//    difference between this solution and previous shows us two ways of decoding, where we can either
//    1. explicitly create a cursor or 2. create a decoder instance and override apply()
//    which one you use is personal / team or project preference

    // This says "Turn this Json to a Person"
    json.as[Person]
  }

  /**
    * Use `Decoder.forProduct2` to construct a `Decoder` instance
    * for `Person`.
    */
  def decodePersonAgain(json: Json): Either[DecodingFailure, Person] = {
    implicit val personDecoder: Decoder[Person] =
      Decoder.forProduct2("name", "age")(Person.apply)
// TODO: whats the long version of this
    json.as[Person]
  }

  /**
    * Hint: Use deriveDecoder
    */
  def decodePersonSemiAuto(json: Json): Either[DecodingFailure, Person] = {
    import io.circe.generic.semiauto._

    implicit val personDecoder: Decoder[Person] = deriveDecoder
// TODO: how does deriveDecoder actually work?
    json.as[Person]
  }

  /**
    * Parse and then decode
    *
    * Hint: Use `decode`, which does both at the same time.
    */
  def strToPerson(str: String): Either[Error, Person] = {
    import io.circe.parser._
    import io.circe.generic.semiauto._

    implicit val personDecoder: Decoder[Person] = deriveDecoder
// TODO: figure out how this is different from above exercise
//    look at the tests and the different between ParsingFailure vs DecodingFailure
    decode[Person](str)
  }

}
