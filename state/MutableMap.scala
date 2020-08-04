import scala.collection.mutable

sealed trait Fruit
case object Apple extends Fruit
case object Banana extends Fruit
case object Orange extends Fruit

class Inventory(currentInventory: mutable.Map[Fruit, Int]) {
  def add(key: Fruit): mutable.Map[Fruit, Int] = {
      val fruitcount = currentInventory.get(key).map(maybeInt => maybeInt + 1).get
      currentInventory.+=((key, fruitcount))
  }
  def reduce(key: Fruit): mutable.Map[Fruit, Int] = {
    val fruitcount = currentInventory.get(key).map(maybeInt => maybeInt - 1).get
    currentInventory.+=((key, fruitcount))
  }
  def now(): mutable.Map[Fruit, Int] = currentInventory
  def performTotalStockCount(): Int = currentInventory.values.sum
}

var currentInventory = new Inventory(mutable.Map[Fruit, Int](
  Apple -> 15,
  Banana -> 24,
  Orange -> 10
))

println(currentInventory.performTotalStockCount())
currentInventory.add(Apple)
println(currentInventory.now())
currentInventory.reduce(Banana)
println(currentInventory.now())

// 😈 😈 😈 🤔 🤔 🤔 🐛 🐛 🐛
currentInventory = new Inventory(mutable.Map.empty[Fruit, Int])
println(currentInventory.now())
