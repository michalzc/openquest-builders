package michalz.openquest.tools.parsers.dice

import scala.util.parsing.combinator.JavaTokenParsers
import cats.syntax.option.*
import com.typesafe.scalalogging.LazyLogging

case class RollStats(
  min: Long,
  max: Long,
  avg: Long
)

object RollStringParser extends JavaTokenParsers with LazyLogging:

  private def longValue: Parser[Long] = wholeNumber ^^ { _.toLong }
  private def number: Parser[Number]  = longValue ^^ { Number.apply }
  private def singleDice: Parser[Dice] = "d" ~ longValue ^^ { case "d" ~ size =>
    Dice(none, size)
  }
  private def diceWithMultiplier: Parser[Dice] =
    longValue ~ "d" ~ longValue ^^ { 
      case multiplier ~ "d" ~ size => Dice(multiplier.some, size)
      case _ => throw new Exception("Invalid match")
    }

  private def dice: Parser[Dice]              = singleDice | diceWithMultiplier
  private def diceOrNumber: Parser[RollToken] = dice | number

  private def diceFactor: Parser[RollToken] =
    diceOrNumber | "(" ~> diceExpression <~ ")"
  private def diceTerm: Parser[RollToken] =
    diceFactor ~ rep("*" ~ diceFactor | "/" ~ diceFactor) ^^ {
      case die ~ list =>
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

  def parseString(rollString: String): Option[RollStats] =
    parseStringRollToken(rollString) match {
      case Success(result, next) if next.atEnd =>
        RollStats(min = result.min, max = result.max, avg = result.avg).some

      case Success(result, next) =>
        logger.warn(
          s"Not all string \"${rollString}\" was parsed, remains: ${next.rest}"
        )
        RollStats(min = result.min, max = result.max, avg = result.avg).some

      case Failure(failure, remains) =>
        logger.error(s"Parsing Failed: ${failure}, remains: ${remains}")
        none

      case Error(error, remains) =>
        logger.error(s"Parsing Error: ${error}, remains: ${remains}")
        none
    }
