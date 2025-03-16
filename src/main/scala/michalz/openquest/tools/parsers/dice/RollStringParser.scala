package michalz.openquest.tools.parsers.dice

import cats.Id

import scala.util.parsing.combinator.JavaTokenParsers
import cats.syntax.option.*
import org.slf4j.{Logger, LoggerFactory}

case class RollStats(
  min: Long,
  max: Long,
  avg: Long,
  roll: String
)

trait RollStringParser extends JavaTokenParsers:

  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  private def d: Parser[String]       = "d" | "D"
  private def longValue: Parser[Long] = wholeNumber ^^ { _.toLong }
  private def number: Parser[Number]  = longValue ^^ { Number.apply }
  private def singleDice: Parser[Dice] = d ~ longValue ^^ { case d ~ size =>
    Dice(none, size)
  }
  private def diceWithMultiplier: Parser[Dice] =
    longValue ~ d ~ longValue ^^ {
      case multiplier ~ d ~ size => Dice(multiplier.some, size)
    }

  private def dice: Parser[Dice]              = singleDice | diceWithMultiplier
  private def diceOrNumber: Parser[RollToken] = dice | number

  private def diceFactor: Parser[RollToken] =
    diceOrNumber | "(" ~> diceExpression <~ ")"
  private def diceTerm: Parser[RollToken] =
    diceFactor ~ rep("*" ~ diceFactor | "/" ~ diceFactor) ^^ { case die ~ list =>
      list.foldLeft(die) {
        case (left, "/" ~ right) => DivExpression(left, right)
        case (left, "*" ~ right) => MultiplyExpression(left, right)
        case (left, any ~ right) => Empty
      }
    }

  private def diceExpression: Parser[RollToken] =
    diceTerm ~ rep("+" ~ diceTerm | "-" ~ diceTerm) ^^ { case dice ~ list =>
      list.foldLeft(dice) {
        case (left, "+" ~ right) => PlusExpression(left, right)
        case (left, "-" ~ right) => MinusExpression(left, right)
        case (left, any ~ right) => Empty
      }
    }

  def diceParser: Parser[RollToken] =
    diceExpression | diceTerm | diceFactor | diceOrNumber
  def parseStringRollToken(rollString: String): ParseResult[RollToken] =
    parseAll(diceParser, rollString)

  def parseRollString(rollString: String): Option[RollStats] =
    parseStringRollToken(rollString) match {
      case Success(result, next) if next.atEnd =>
        RollStats(min = result.min, max = result.max, avg = result.avg, roll = result.render).some

      case Success(result, next) =>
        logger.warn(
          s"Not all string \"${rollString}\" was parsed, remains: ${next.rest}"
        )
        RollStats(min = result.min, max = result.max, avg = result.avg, roll = result.render).some

      case Failure(failure, remains) =>
        logger.error(s"Parsing Failed: ${failure}, remains: ${remains}")
        none

      case Error(error, remains) =>
        logger.error(s"Parsing Error: ${error}, remains: ${remains}")
        none
    }

object RollStringParser extends RollStringParser
