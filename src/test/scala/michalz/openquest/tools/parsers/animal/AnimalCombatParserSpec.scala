package michalz.openquest.tools.parsers.animal

import michalz.openquest.tools.readers.model.AnimalCombat
import michalz.openquest.tools.readers.model.AnimalCombat.Attack
import org.specs2.matcher.{DataTable, DataTables, Matchers}
import org.specs2.Specification
import org.specs2.execute.{DecoratedResult, Result}
import org.specs2.specification.core.SpecStructure

import scala.language.implicitConversions
import cats.syntax.option.*

class AnimalCombatParserSpec extends Specification with DataTables with Matchers:
  def is: SpecStructure =
    s2"""
        |This is a specification for AnimalCombatParser
        | It should parse percentage $parsePercentage
        | It should parse attack with damage $parseAttackWithDamage
        | It should parse attack without damage $parseAttackWithoutDamage
        | It should parse two attacks $parseTwoAttacks
        | It should parse attack with description $parseAttackWithDescription
        | It should correctly parse attacks: $attacks
      """.stripMargin

  def parseAttackWithoutDamage: Result =
    val result = AnimalCombatParser.parseAll(AnimalCombatParser.attackParser, "Kick")
    checkParsingResult(result, Attack("Kick"))

  def parsePercentage: Result =
    val result = AnimalCombatParser.parseAll(AnimalCombatParser.percentage, "23%")
    checkParsingResult(result, 23)

  def parseAttackWithDamage: Result =
    val result = AnimalCombatParser.parseAll(AnimalCombatParser.attackParser, "Kick 2D6")
    checkParsingResult(result, Attack("Kick", "2D6"))

  def parseTwoAttacks: Result =
    val result = AnimalCombatParser.parseAll(AnimalCombatParser.attacks, "Kick 2D6, Bite 3D12")
    checkParsingResult(result, List(Attack("Kick", "2D6"), Attack("Bite", "3D12")))

  def parseAttackWithDescription: Result =
    val result = AnimalCombatParser.parseAll(AnimalCombatParser.attackParser, " Trunk (Grapple)")
    checkParsingResult(result, Attack("Trunk", None, "Grapple".some))


  def checkParsingResult[A](result: AnimalCombatParser.ParseResult[A], expected: A): Result =
    result match
      case AnimalCombatParser.Success(result, _) => result should equalTo(expected)
      case other => failure(s"Got: ${other}")

  // @formatter:off
  val attacks: DecoratedResult[DataTable] =
    "Expected Result"                       | "Attack String"                               |>
    AnimalCombat
      .oneAttack(50, "Bite", "1D6".some)    ! "50% Bite 1D6"                                |
    AnimalCombat(60,
      Attack("Bite", "1D8"),
      Attack("Claw", "1D6")
    )                                       ! "60% Bite 1D8, Claw 1D6"                      |
    AnimalCombat(45,
      Attack("Trample", "1D12"),
      Attack("Tusk", "1D10"),
      Attack("Trunk Grapple")
    )                                       ! "45% Trample 1D12, Tusk 1D10, Trunk Grapple"  |
    AnimalCombat(50,
      Attack("Bite", "1D6"),
      Attack("Gore", "1D8"),
      Attack("Trample", "1D12")
    )                                       ! "50% Bite 1D6, Gore 1D8, Trample 1D12"        |
    AnimalCombat(50,
      Attack("Bite", "1D6".some, "+Venom see below".some),
      Attack("Webbing", None, "Bigger desc".some)
    )                                       ! "50% Bite 1D6 (+Venom see below), Webbing (Bigger desc)"  |
    AnimalCombat(50,
      Attack("Tail lash", "1D12"),
      Attack("Gore", "1D10")
    )                                       ! "50% Tail lash 1D12, Gore 1D10"               |
    AnimalCombat(60,
      Attack("Bite", None, "+ Venom - see below".some)
    )                                       ! "60% Bite (+ Venom - see below)"                |
   // @formatter:on
      { (expected, stringToParse) =>
        AnimalCombatParser.parseCombat(stringToParse) should beRight(expected)
      }

