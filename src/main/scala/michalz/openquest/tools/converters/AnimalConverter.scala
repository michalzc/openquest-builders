package michalz.openquest.tools.converters

import cats.syntax.option.*

import scala.language.implicitConversions

import michalz.openquest.tools.*
import michalz.openquest.tools.bestiary.model.*
import michalz.openquest.tools.parsers.dice.CharacteristicParser
import michalz.openquest.tools.readers.model.AnimalRow

class AnimalConverter

object AnimalConverter {

  extension (row: AnimalRow) def toAnimal(id: String): OpenQuestResultNel[Animal] = makeAnimal(id, row)

  private def parseMovement(input: String): OpenQuestResultNel[ModBaseAttr] =
    CharacteristicParser.parse(CharacteristicParser.numberParser, input).map(ModBaseAttr.apply)

  private def makeAnimal(id: String, row: AnimalRow): OpenQuestResultNel[Animal] =

    def createCharacteristics(row: AnimalRow): Characteristics = Characteristics(
      row.str,
      row.con,
      row.dex,
      row.siz,
      row.int,
      row.pow,
      row.cha
    )

    val characteristics = createCharacteristics(row)
    val description     = s"Move: ${row.move}".some

    parseMovement(row.move).map { movement =>
      val attributes = Attributes(
        hp = row.hp,
        dm = row.dm,
        mp = ModMaxValueAttr.zero,
        mr = movement,
        ap = ModBaseAttr.zero,
        initiative = none[Initiative]
      )
      Animal(id, row.name, characteristics, attributes, List.empty, description)
    }
}
