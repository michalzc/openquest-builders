package michalz.openquest.tools.bestiary.model

import cats.data.ValidatedNel
import cats.syntax.validated.*
import cats.syntax.option.*
import cats.syntax.apply.*

extension (self: String)
  def nonEmpty(field: => String): ValidatedNel[CSVReadingError, String] =
    self match
      case s if s.isBlank =>
        CSVReadingError(fieldName = field, errorMessage = "is empty").invalidNel
      case s =>
        s.validNel

  def blankOption: Option[String] =
    if self.isBlank then
      none
    else
      self.some

  def emptyOption: Option[String] =
    if self.isEmpty then
      none
    else
      self.some

case class CSVRow(
  name: String,
  image: Option[String],
  folder: Option[String],
  strRoll: String,
  conRoll: String,
  dexRoll: String,
  sizRoll: String,
  intRoll: String,
  powRoll: String,
  chaRoll: String,
  move: Option[Int],
  items: List[String]
)

object CSVRow:

  def readRow(
    row: (String, String, String, String, String, String, String, String, String, String, String, String)
  ): ValidatedNel[CSVReadingError, CSVRow] =
    row match
      case (name, image, folder, strRoll, conRoll, dexRoll, sizRoll, intRoll, powRoll, chaRoll, move, items) =>
        val validatedMove: ValidatedNel[CSVReadingError, Option[Int]] = move match
          case sm if sm.isBlank => none.validNel
          case sm => sm.toIntOption.map(_.some).toValidNel(CSVReadingError("move", "invalid value"))
        (
          name.nonEmpty("name"),
          name.nonEmpty("strRoll"),
          name.nonEmpty("conRoll"),
          name.nonEmpty("dexRoll"),
          name.nonEmpty("sizRoll"),
          name.nonEmpty("intRoll"),
          name.nonEmpty("powRoll"),
          name.nonEmpty("chaRoll"),
          validatedMove,
        ).mapN { case (name, strRoll, conRoll, dexRoll, sizRoll, intRoll, powRoll, chaRoll, move) =>
          CSVRow(
            name,
            image.blankOption,
            folder.blankOption,
            strRoll = strRoll,
            conRoll = conRoll,
            dexRoll = dexRoll,
            sizRoll = sizRoll,
            intRoll = intRoll,
            powRoll = powRoll,
            chaRoll = chaRoll,
            move = move,
            items = items.split(util.Properties.lineSeparator).toList
          )
        }
