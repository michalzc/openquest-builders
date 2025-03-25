package michalz.openquest.tools.converters

import cats.data.NonEmptyList
import cats.instances.either.*
import cats.syntax.either.*
import cats.syntax.option.*
import cats.syntax.parallel.*
import michalz.openquest.tools.*
import michalz.openquest.tools.bestiary.model.*
import michalz.openquest.tools.parsers.dice.CharacteristicParser
import michalz.openquest.tools.readers.model.AnimalRow

import scala.language.implicitConversions

class AnimalConverter

object AnimalConverter {

  given resultConversion[A]: Conversion[CharacteristicParser.ParseResult[A], OpenQuestResultNel[A]] with
    def apply(result: CharacteristicParser.ParseResult[A]): OpenQuestResultNel[A] = result match
      case CharacteristicParser.Success(result, _)     => result.asRight
      case CharacteristicParser.Failure(error, remain) => NonEmptyList.one(ParsingError(error, remain.toString)).asLeft
      case CharacteristicParser.Error(error, remain)   => NonEmptyList.one(ParsingError(error, remain.toString)).asLeft

  extension (row: AnimalRow) def toAnimal(id: String): OpenQuestResultNel[Animal] = makeAnimal(id, row)

  extension (input: String)
    def toCharacteristic: OpenQuestResultNel[Characteristic] = parseCharacteristic(input)
    def toModMaxValueAttr: OpenQuestResultNel[ModMaxValueAttr] = Either
      .catchNonFatal(input.toInt)
      .map(ModMaxValueAttr.apply)
      .leftMap(e => NonEmptyList.one(ErrorWrapper(s"Can't convert $input to attr", e)))

  def parseMovement(input: String): OpenQuestResultNel[ModBaseAttr] =
    CharacteristicParser.parse(CharacteristicParser.numberParser, input).map(ModBaseAttr.apply)

  def makeAnimal(id: String, row: AnimalRow): OpenQuestResultNel[Animal] =
    val characteristics = (
      row.str.toCharacteristic,
      row.con.toCharacteristic,
      row.dex.toCharacteristic,
      row.siz.toCharacteristic,
      row.int.toCharacteristic,
      row.pow.toCharacteristic,
      row.cha.toCharacteristic
    ).parMapN(Characteristics.apply)

    val attributes = (
      row.hp.toModMaxValueAttr,
      StringModAttr(row.dm).asRight[OpenQuestErrorNel],
      ModMaxValueAttr.zero.asRight[OpenQuestErrorNel],
      parseMovement(row.move),
      ModBaseAttr.zero.asRight[OpenQuestErrorNel],
      none[Initiative].asRight[OpenQuestErrorNel]
    ).parMapN(Attributes.apply)

    (characteristics, attributes).parMapN((c, a) => Animal(id, row.name, c, a, List.empty))

  def parseCharacteristic(input: String): OpenQuestResultNel[Characteristic] =
    CharacteristicParser.parseCharacteristic(input).leftMap(NonEmptyList.one)
}
