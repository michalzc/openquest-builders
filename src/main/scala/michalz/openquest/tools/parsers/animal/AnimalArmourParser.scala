package michalz.openquest.tools.parsers.animal

import cats.syntax.either.*

import scala.util.parsing.combinator.JavaTokenParsers

import org.slf4j.{Logger, LoggerFactory}

import michalz.openquest.tools.{OpenQuestError, ParsingError}
import michalz.openquest.tools.readers.model.AnimalArmour

trait AnimalArmourParser extends JavaTokenParsers:

  override def skipWhitespace: Boolean = false

  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  val noArmour: Parser[AnimalArmour] = "None" ^^ { _ => AnimalArmour.noArmour }
  val word: Parser[String]           = """[a-zA-Z]+""".r
  val spaces: Parser[String]         = """\s+""".r
  val armourName: Parser[String]     = repsep(word, spaces) <~ opt(spaces) ^^ { words => words.mkString(" ") }
  val ap: Parser[String]             = opt(spaces) ~> "AP" 
  val num: Parser[Int]               = "(" ~> wholeNumber <~ (opt(ap) ~ ")") ^^ { _.toInt }
  val namedAnimalArmourParser: Parser[AnimalArmour] = armourName ~ num ^^ { case name ~ value =>
    AnimalArmour(name, value)
  }
  val animalArmourParser: Parser[AnimalArmour] = noArmour | namedAnimalArmourParser

  def parseArmour(input: String): Either[OpenQuestError, AnimalArmour] = parseAll(animalArmourParser, input) match
    case Success(armour, next) if next.atEnd =>
      armour.asRight

    case Success(result, next) =>
      logger.warn(s"Not whole value was parsed: \"${next.rest}\" remains")
      result.asRight

    case Failure(error, remains) =>
      ParsingError(error, remains.toString).asLeft

    case Error(error, remains) =>
      ParsingError(error, remains.toString).asLeft

object AnimalArmourParser extends AnimalArmourParser
