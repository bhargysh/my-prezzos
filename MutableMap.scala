import scala.collection.mutable

sealed trait Fruit
case object Apple extends Fruit
case object Banana extends Fruit
case object Orange extends Fruit

case object Inventory

case class Inventory(currentInventory: mutable.Map[Fruit, Int]) {
  def add(key: Fruit): Inventory = {
      val fruitcount = currentInventory.get(key).map(maybeInt => maybeInt + 1).get
      Inventory(currentInventory.+((key, fruitcount)))
  }
  def reduce(key: Fruit): Inventory = {
    val fruitcount = currentInventory.get(key).map(maybeInt => maybeInt - 1).get
    Inventory(currentInventory.+((key, fruitcount)))
  }
  def now(): mutable.Map[Fruit, Int] = currentInventory
  def performTotalStockCount(): Int = currentInventory.values.sum
}

val currentInventory = Inventory(mutable.Map[Fruit, Int](
  Apple -> 15,
  Banana -> 24,
  Orange -> 10
))
println(currentInventory.performTotalStockCount())
println(currentInventory.add(Apple).now())
println(currentInventory.reduce(Banana).now())
