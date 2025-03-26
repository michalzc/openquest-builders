package michalz.openquest.tools.converters

import cats.syntax.either.*
import cats.syntax.option.*

import scala.language.implicitConversions

import michalz.openquest.tools.*
import michalz.openquest.tools.bestiary.model.*
import michalz.openquest.tools.readers.model.AnimalRow

class AnimalConverter

object AnimalConverter {

  extension (row: AnimalRow) def toAnimal(id: String): OpenQuestResultNel[Animal] = makeAnimal(id, row)

//  extension (input: String)
//    def toCharacteristic: OpenQuestResultNel[Characteristic] = parseCharacteristic(input)
//    def toModMaxValueAttr: OpenQuestResultNel[ModMaxValueAttr] = Either
//      .catchNonFatal(input.toInt)
//      .map(ModMaxValueAttr.apply)
//      .leftMap(e => NonEmptyList.one(ErrorWrapper(s"Can't convert $input to attr", e)))

//  def parseMovement(input: String): OpenQuestResultNel[ModBaseAttr] =
//    CharacteristicParser.parse(CharacteristicParser.numberParser, input).map(ModBaseAttr.apply)

  def makeAnimal(id: String, row: AnimalRow): OpenQuestResultNel[Animal] =
    val characteristics = Characteristics(
      row.str,
      row.con,
      row.dex,
      row.siz,
      row.int,
      row.pow,
      row.cha
    )

    val attributes = Attributes(
      row.hp,
      row.dm,
      ModMaxValueAttr.zero,
      row.move,
      ModBaseAttr.zero,
      none[Initiative]
    )

//    (characteristics, attributes).parMapN((c, a) => Animal(id, row.name, c, a, List.empty))
    Animal(id, row.name, characteristics, attributes, List.empty).asRight

//  def parseCharacteristic(input: String): OpenQuestResultNel[Characteristic] =
//    CharacteristicParser.parseCharacteristic(input).leftMap(NonEmptyList.one)
}
