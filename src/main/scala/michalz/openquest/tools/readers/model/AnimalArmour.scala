package michalz.openquest.tools.readers.model

import cats.syntax.option.*

case class AnimalArmour(name: Option[String], value: Int)

object AnimalArmour:
  def apply(name: String, value: Int): AnimalArmour = AnimalArmour(name.some, value)
  def apply(value: Int): AnimalArmour               = AnimalArmour(name = none[String], value = value)
  def noArmour: AnimalArmour                        = AnimalArmour(0)
