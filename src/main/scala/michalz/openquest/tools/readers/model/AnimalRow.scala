package michalz.openquest.tools.readers.model

import fs2.data.csv.{CellDecoder, DecoderError, DecoderResult, RowDecoder}
import fs2.data.csv.generic.semiauto.*
import cats.syntax.option.*
import cats.syntax.either.*
import michalz.openquest.tools.parsers.animal.AnimalArmourParser

import scala.compiletime.{constValue, erasedValue, summonInline}

case class AnimalRow(
  name: String,
  str: String,
  con: String,
  dex: String,
  siz: String,
  int: String,
  pow: String,
  cha: String,
  hp: String,
  dm: String,
  move: String,
  armour: AnimalArmour,
  combat: String
)

case class AnimalArmour(name: Option[String], value: Int)

object AnimalArmour:
  def apply(name: String, value: Int): AnimalArmour = AnimalArmour(name.some, value)
  def apply(value: Int): AnimalArmour = AnimalArmour(name = none[String], value = value)
  def noArmour: AnimalArmour = AnimalArmour(0)


object AnimalRow:

  implicit val animalArmourDecoder: CellDecoder[AnimalArmour] = new CellDecoder[AnimalArmour]:
    override def apply(cell: String): DecoderResult[AnimalArmour] = AnimalArmourParser.parseArmour(cell).leftMap(ope => DecoderError(ope.getMessage))

  implicit val decoder: RowDecoder[AnimalRow] = deriveRowDecoder[AnimalRow]
