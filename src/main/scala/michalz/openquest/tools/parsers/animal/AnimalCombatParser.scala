package michalz.openquest.tools.parsers.animal

import cats.syntax.either.*
import michalz.openquest.tools.readers.model.AnimalCombat
import michalz.openquest.tools.readers.model.AnimalCombat.Attack
import michalz.openquest.tools.{OpenQuestError, ParsingError}
import org.slf4j.{Logger, LoggerFactory}

import scala.util.parsing.combinator.JavaTokenParsers

/*
Examples:
50% Bite 1D6
60% Bite 1D8, Claw 1D6
60% Bite 1D8, Claw 1D6
50% Bite 1D8
40% Charge 1D8, Trample 1D8
50% Claw 1D10
50% Bite 1D8
45% Peck 1D8, Kick 1D6
40% Bite 1D6
45% Trample 1D12, Tusk 1D10, Trunk Grapple
50% Claw 1D6, Bite 1D4
80% Claw 1D8, Bite 1D6
40% Kick 1D6
25% Bite 1D6, Kick 1D8
50% Bite 1D8, Arm 1D4
50% Bite 1D8, Claw 1D6
50% Bite 1D4, Constrict 1D8
50% Bite 1D6, Gore 1D8, Trample 1D12
50% Bite 1D6+ Venom see below, Webbing (Entangles Athletics vs rolled attack to escape or spiders POW x2 as Hit Points to destroy)
50% Tail lash 1D12, Gore 1D10
60% Bite 1D10, Stomp 1D10
50% Bite 1D8, Claw 1D6, Fore claw 1D4
60% Bite + Venom (see below)
50% Bite 1D8, Claw 1D6
 */

trait AnimalCombatParser extends JavaTokenParsers:

  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  override def skipWhitespace: Boolean = false

  // Parser for percentage (e.g., "50%")
  val percentage: Parser[Int] = """\d{1,3}""".r <~ "%" ^^ {
    _.toInt
  }

  val spaces: Parser[String] = "\\s+".r

  val comma = ","

  // Parser for word (attack name)
  val word: Parser[String] = """[a-zA-Z]+""".r

  // Parser for dice roll format (e.g., "1D6", "1D10")
  val dice: Parser[String] = """\d+D\d+""".r

  val description: Parser[String] = opt(spaces) ~> ("(" ~> repsep("""[\w\+\-]+""".r, spaces) <~ ")") <~ opt(spaces) ^^ {
    words => words.mkString(" ")
  }

//  val attackWithDamage: Parser[Attack] = opt(spaces) ~> (repsep(word, spaces) <~ opt(spaces)) ~ opt(dice) ^^ {
//    case words ~ Some(dice) => Attack(words.mkString(" "), dice)
//    case words ~ None       => Attack(words.mkString(" "))
//  }
//
  val attackParser: Parser[Attack] =
    opt(spaces) ~> (repsep(word, spaces) <~ opt(spaces)) ~ opt(dice) ~ opt(description) ^^ {
//    case words ~ Some(dice) ~ Somedescription => Attack(words.mkString(" "), dice.some, description.some)
//    case words ~ None ~ description      => Attack(words.mkString(" "), None, description.some)
      case words ~ dice ~ description => Attack(words.mkString(" "), dice, description)
    }

  val attacks: Parser[List[Attack]] = repsep(attackParser, comma)

  val combatParser: Parser[AnimalCombat] =
    opt(spaces) ~> (percentage <~ opt(spaces)) ~ attacks <~ opt(spaces) ^^ { case percentage ~ attacks =>
      AnimalCombat(percentage, attacks)
    }

  def parseCombat(attackString: String): Either[OpenQuestError, AnimalCombat] =
    parseAll(combatParser, attackString) match
      case Success(result, next) if next.atEnd => result.asRight

      case Success(result, next) =>
        logger.warn(s"Not whole attack (${attackString}) was parsed, \"${next.rest}\" remains")
        result.asRight

      case Failure(error, remains) =>
        logger.error(s"Failed to parse combat: ${error}. Remaining input: ${remains}")
        ParsingError(error, remains.source.toString).asLeft

      case Error(msg, next) =>
        logger.error(s"An error occurred while parsing combat: ${msg}. Remaining input: ${next.source}")
        ParsingError(msg, next.source.toString).asLeft

object AnimalCombatParser extends AnimalCombatParser
