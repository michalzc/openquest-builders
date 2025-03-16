package michalz.openquest.tools.parsers.dice

import michalz.openquest.tools.bestiary.model.Characteristic
import org.specs2.Specification
import org.specs2.matcher.Matchers
import org.specs2.specification.Tables

import scala.language.implicitConversions

class CharacteristicParserSpec extends Specification with Matchers with Tables {

  def is =
    s2"""
    | This is specification of CharactersitcParser
    |   It should properly parse charactersitics $characteristicTable
  """.stripMargin

  val characteristicTable =
    "char string"   | "characteristic"              |>
    "3D10+12 (28)"  ! Characteristic(28, "3d10+12") |
    "3D6 (11)"      ! Characteristic(11, "3d6")     |
    "3D6+12 (23)"   ! Characteristic(23, "3d6+12")  |
    "3D6+12 (24)"   ! Characteristic(24, "3d6+12")  |
    "3D6+12(19)"    ! Characteristic(19, "3d6+12")  |
    "3D6+15 (24)"   ! Characteristic(24, "3d6+15")  |
    "3D6+15 (25)"   ! Characteristic(25, "3d6+15")  |
    "3D6+18 (27)"   ! Characteristic(27, "3d6+18")  |
    "3D6+24 (35)"   ! Characteristic(35, "3d6+24")  |
    { (char, expected) =>
      val result = CharacteristicParser.parseCharacteristic(char)
      result must beRight(expected)
    }

}
