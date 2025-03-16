package michalz.openquest.tools.bestiary.model

case class Characteristic(base: Int, roll: String, mod: Int)

object Characteristic:
  def apply(num: Int): Characteristic                = Characteristic(num, num.toString, 0)
  def apply(base: Int, roll: String): Characteristic = Characteristic(base, roll, 0)

case class ModMaxValueAttr(mod: Int, max: Int, value: Int)

case class StringModAttr(mod: String)

case class ModBaseAttr(mod: Int, base: Int)

case class Initiative(reference: Option[String], mod: Option[Int])

case class Attributes(
  hp: ModMaxValueAttr,
  dm: StringModAttr,
  mp: ModMaxValueAttr,
  mr: ModBaseAttr,
  ap: ModBaseAttr,
  initiative: Option[Initiative]
)

case class Characteristics(
  str: Characteristic,
  con: Characteristic,
  dex: Characteristic,
  siz: Characteristic,
  int: Characteristic,
  pow: Characteristic,
  cha: Characteristic
)

case class ActorSystem(characteristics: Characteristics)

case class Actor(_id: String, name: String, img: String)
