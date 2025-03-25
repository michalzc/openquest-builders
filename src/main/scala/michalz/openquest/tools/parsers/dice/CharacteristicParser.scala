package michalz.openquest.tools.parsers.dice

import cats.syntax.either.*
import cats.syntax.option.*

import org.slf4j.{Logger, LoggerFactory}

import michalz.openquest.tools.{OpenQuestError, ParsingError}
import michalz.openquest.tools.bestiary.model.Characteristic

trait CharacteristicParser extends RollStringParser:

  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  def numberParser: Parser[Int]                                  = wholeNumber ^^ { _.toInt }
  def numericCharacteristic: Parser[Characteristic]              = numberParser ^^ { Characteristic(_) }
  def numberInParentheses: Parser[Int]                           = "(" ~> wholeNumber <~ ")" ^^ { _.toInt }
  def numericInParenthesesCharacteristic: Parser[Characteristic] = numberInParentheses ^^ { Characteristic(_) }
  def rollWithDefaultParser: Parser[Characteristic] = diceParser ~ numberInParentheses ^^ { case roll ~ value =>
    Characteristic(value, roll.render, 0, roll.some)
  }
  def onlyRollCharacteristic: Parser[Characteristic] = diceParser ^^ { Characteristic(_) }

  def characteristicParser: Parser[Characteristic] =
    rollWithDefaultParser | onlyRollCharacteristic | numericInParenthesesCharacteristic | numericCharacteristic

  def parseCharacteristic(input: String): Either[OpenQuestError, Characteristic] =
    parseAll(characteristicParser, input) match
      case Success(result, next) if next.atEnd =>
        result.asRight

      case Success(result, next) =>
        logger.warn(s"Not whole value was parsed: \"${next.rest}\" remains")
        result.asRight

      case Failure(error, remains) =>
        ParsingError(error, remains.toString).asLeft

      case Error(error, remains) =>
        ParsingError(error, remains.toString).asLeft

object CharacteristicParser extends CharacteristicParser
