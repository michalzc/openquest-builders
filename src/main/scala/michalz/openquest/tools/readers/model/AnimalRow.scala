package michalz.openquest.tools.readers.model

import cats.syntax.either.*
import cats.syntax.option.*

import scala.compiletime.{constValue, erasedValue, summonInline}

import michalz.openquest.tools.bestiary.model.{Characteristic, ModBaseAttr, ModMaxValueAttr, StringModAttr}
import michalz.openquest.tools.parsers.animal.AnimalArmourParser
import michalz.openquest.tools.parsers.dice.CharacteristicParser

import fs2.data.csv.{CellDecoder, DecoderError, DecoderResult, RowDecoder}
import fs2.data.csv.generic.semiauto.*

case class AnimalRow(
  name: String,
  str: Characteristic,
  con: Characteristic,
  dex: Characteristic,
  siz: Characteristic,
  int: Characteristic,
  pow: Characteristic,
  cha: Characteristic,
  hp: ModMaxValueAttr,
  dm: StringModAttr,
  move: ModBaseAttr,
  armour: AnimalArmour,
  combat: String
)

case class AnimalArmour(name: Option[String], value: Int)

object AnimalArmour:
  def apply(name: String, value: Int): AnimalArmour = AnimalArmour(name.some, value)
  def apply(value: Int): AnimalArmour               = AnimalArmour(name = none[String], value = value)
  def noArmour: AnimalArmour                        = AnimalArmour(0)

object AnimalRow:

  given characteristicDecoder: CellDecoder[Characteristic] = new CellDecoder[Characteristic]:
    override def apply(cell: String): DecoderResult[Characteristic] =
      CharacteristicParser.parseCharacteristic(cell).leftMap(ope => DecoderError(ope.getMessage))

  given strModAttrDecoder: CellDecoder[StringModAttr] = new CellDecoder[StringModAttr]:
    override def apply(cell: String): DecoderResult[StringModAttr] = StringModAttr(cell).asRight

  given animalArmourDecoder: CellDecoder[AnimalArmour] = new CellDecoder[AnimalArmour]:
    override def apply(cell: String): DecoderResult[AnimalArmour] =
      AnimalArmourParser.parseArmour(cell).leftMap(ope => DecoderError(ope.getMessage))

  given modMaxValueAttrDecoder: CellDecoder[ModMaxValueAttr] =
    new CellDecoder[ModMaxValueAttr]:
      override def apply(cell: String): DecoderResult[ModMaxValueAttr] = Either
        .catchNonFatal(cell.toInt)
        .map(ModMaxValueAttr.apply)
        .leftMap(e => DecoderError(s"Can't parse ${cell}", inner = e))

  given modBaseAttrDecoder: CellDecoder[ModBaseAttr] = new CellDecoder[ModBaseAttr]:
    override def apply(cell: String): DecoderResult[ModBaseAttr] =
      CharacteristicParser.parse(CharacteristicParser.numberParser, cell).map(ModBaseAttr.apply) match
        case CharacteristicParser.Success(result, _)     => result.asRight
        case CharacteristicParser.Failure(error, remain) => DecoderError(s"Can't parse ${cell} to ModBaseAttr").asLeft
        case CharacteristicParser.Error(error, remain)   => DecoderError(s"Can't parse ${cell} to ModBaseAttr").asLeft

  implicit val decoder: RowDecoder[AnimalRow] = deriveRowDecoder[AnimalRow]
