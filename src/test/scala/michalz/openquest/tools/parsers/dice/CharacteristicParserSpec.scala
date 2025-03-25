package michalz.openquest.tools.parsers.dice

import scala.language.implicitConversions

import org.specs2.Specification
import org.specs2.execute.DecoratedResult
import org.specs2.matcher.{DataTable, Matchers}
import org.specs2.specification.Tables
import org.specs2.specification.core.SpecStructure

class CharacteristicParserSpec extends Specification with Matchers with Tables {

  def is: SpecStructure =
    s2"""
    | This is specification of CharactersitcParser
    |   It should properly parse charactersitics $characteristicTable
    |   It should properly parse crazy charactersitics $crazyCharacteristicTable
  """.stripMargin

  //format: off
  private val characteristicTable: DecoratedResult[DataTable] =
    "char string"   | "characteristic"  |>
    "3D10+12 (28)"  ! (28, "3d10+12")   |
    "3D6 (11)"      ! (11, "3d6")       |
    "3D6+12 (23)"   ! (23, "3d6+12")    |
    "3D6+12 (24)"   ! (24, "3d6+12")    |
    "3D6+12(19)"    ! (19, "3d6+12")    |
    "3D6+15 (24)"   ! (24, "3d6+15")    |
    "3D6+15 (25)"   ! (25, "3d6+15")    |
    "3D6+18 (27)"   ! (27, "3d6+18")    |
    "3D6+24 (35)"   ! (35, "3d6+24")    |
    "3d6"           ! (11, "3d6")       |
    "12"            ! (12, "12")        |
    "22 (11)"       ! (11, "22")        |
    "(15)"          ! (15, "15")        |
    { (char, expected) =>
      val result = CharacteristicParser.parseCharacteristic(char).map(c => c.base -> c.roll)
      result must beRight(expected)
    }
  

  private val crazyCharacteristicTable: DecoratedResult[DataTable] =
    "char string"          | "characteristic"      |>
    "3D10*(2+1d4) (42)"    ! (42, "3d10*(2+1d4)")  |
    "(2d6+3)*4/2 (19)"     ! (19, "(2d6+3)*4/2")   |
    "d100/2+10 (35)"       ! (35, "d100/2+10")     |
    "2d20-1d6+4 (27)"      ! (27, "2d20-1d6+4")    |
    "(3d6+2)*(1d4) (28)"   ! (28, "(3d6+2)*1d4")   |
    "d100 (55)"            ! (55, "d100")          |
    "3d6*2-5 (25)"         ! (25, "3d6*2-5")       |
    "(d12+d8)/2+15 (22)"   ! (22, "(d12+d8)/2+15") |
    "d20+d12+d10+d8 (41)"  ! (41, "d20+d12+d10+d8")|
    "4d4*3-2d6 (33)"       ! (33, "4d4*3-2d6")     |
    { (char, expected) =>
      val result = CharacteristicParser.parseCharacteristic(char).map(c => c.base -> c.roll)
      result must beRight(expected)
    }
    //format: on

}
