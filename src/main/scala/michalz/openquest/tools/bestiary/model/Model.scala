package michalz.openquest.tools.bestiary.model

import cats.syntax.option.*

import michalz.openquest.tools.parsers.dice.RollToken

case class Characteristic(base: Int, roll: String, mod: Int, parsed: Option[RollToken] = none)

object Characteristic:
  def apply(num: Int): Characteristic                = Characteristic(num, num.toString, 0)
  def apply(base: Int, roll: String): Characteristic = Characteristic(base, roll, 0)
  def apply(rollToken: RollToken): Characteristic =
    Characteristic(rollToken.avg.toInt, rollToken.render, 0, rollToken.some)

case class ModMaxValueAttr(mod: Int, max: Int, value: Int)
object ModMaxValueAttr:
  def apply(value: Int): ModMaxValueAttr = ModMaxValueAttr(mod = 0, max = value, value = value)
  def zero: ModMaxValueAttr              = ModMaxValueAttr(0, 0, 0)

case class StringModAttr(mod: String)

case class ModBaseAttr(mod: Int, base: Int)
object ModBaseAttr:
  def apply(value: Int): ModBaseAttr = ModBaseAttr(0, value)
  def zero: ModBaseAttr              = ModBaseAttr(0, 0)

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
