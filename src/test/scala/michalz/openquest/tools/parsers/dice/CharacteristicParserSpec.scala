package michalz.openquest.tools.parsers.dice

import michalz.openquest.tools.bestiary.model.Characteristic
import org.specs2.Specification
import org.specs2.execute.DecoratedResult
import org.specs2.matcher.{DataTable, Matchers}
import org.specs2.specification.Tables
import org.specs2.specification.core.SpecStructure

import scala.language.implicitConversions

class CharacteristicParserSpec extends Specification with Matchers with Tables {

  def is: SpecStructure =
    s2"""
    | This is specification of CharactersitcParser
    |   It should properly parse charactersitics $characteristicTable
  """.stripMargin

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

}
