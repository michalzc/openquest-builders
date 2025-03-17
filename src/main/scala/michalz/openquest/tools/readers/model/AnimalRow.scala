package michalz.openquest.tools.readers.model

import cats.data.NonEmptyList
import fs2.data.csv.{CsvRowDecoder, RowDecoder}
import fs2.data.csv.generic.semiauto.*
import michalz.openquest.tools.{OpenQuestResult, OpenQuestResultNel}
import michalz.openquest.tools.bestiary.model.Animal

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
  armour: String,
  combat: String
)

object AnimalRow:
  implicit val decoder: RowDecoder[AnimalRow] = deriveRowDecoder[AnimalRow]
