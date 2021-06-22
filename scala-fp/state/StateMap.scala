import cats.data.State

sealed trait Fruit
case object Apple extends Fruit
case object Banana extends Fruit
case object Orange extends Fruit

type Inventory[A] = State[Map[Fruit, Int], A]

case class FruitInventory(currentInventory: Map[Fruit, Int]) {
  def add(key: Fruit): Inventory[Unit] = {
    val fruitcount = currentInventory.get(key).map(maybeInt => maybeInt + 1).get
    State.modify(mapOfFruits => mapOfFruits.+((key, fruitcount)))
  }
  def reduce(key: Fruit): Inventory[Unit] = {
    val fruitcount = currentInventory.get(key).map(maybeInt => maybeInt - 1).get
    State.modify(mapOfFruits => mapOfFruits.+((key, fruitcount)))
  }
  def now(f: Map[Fruit, Int] => Map[Fruit, Int]) = State.inspect[Map[Fruit, Int], Map[Fruit, Int]](f)
  def performTotalStockCount(): Int = currentInventory.values.sum
}

val mapOfFruits: Map[Fruit, Int] = Map(
  Apple -> 15,
  Banana -> 25,
  Orange -> 10
)
val inventory = FruitInventory(mapOfFruits)

val program = for {
  _ <- inventory.add(Apple)
  _ <- inventory.reduce(Banana)
  _ <- inventory.add(Orange)
} yield ()

val programState: (Map[Fruit, Int], Unit) = program.run(inventory.currentInventory).value
val (oldState, newState) = inventory.now(_ => programState._1).run(inventory.currentInventory).value
