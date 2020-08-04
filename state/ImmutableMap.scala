sealed trait Fruit
case object Apple extends Fruit
case object Banana extends Fruit
case object Orange extends Fruit

case class Inventory(currentInventory: Map[Fruit, Int]) {
  def add(key: Fruit): Map[Fruit, Int] = {
    val fruitcount = currentInventory.get(key).map(maybeInt => maybeInt + 1).get
    currentInventory.+((key, fruitcount))
  }
  def reduce(key: Fruit): Map[Fruit, Int] = {
    val fruitcount = currentInventory.get(key).map(maybeInt => maybeInt - 1).get
    currentInventory.+((key, fruitcount))
  }
  def now(): Map[Fruit, Int] = currentInventory
  def performTotalStockCount(): Int = currentInventory.values.sum
}


val inventory: Map[Fruit, Int] = Map(
  Apple -> 15,
  Banana -> 25,
  Orange -> 10
)
val currentInventory = Inventory(inventory)

println(currentInventory.performTotalStockCount())
println(currentInventory.add(Apple))
println(currentInventory.reduce(Banana))
println(currentInventory.reduce(Orange))

println(currentInventory.now()) // 🤓 OG lives!
println(currentInventory.performTotalStockCount())

val inventory2 = Inventory(currentInventory.add(Apple))
val inventory3 = Inventory(inventory2.reduce(Banana))
val inventory4 = Inventory(inventory3.reduce(Orange))

println(inventory4.now())
println(inventory4.performTotalStockCount())
