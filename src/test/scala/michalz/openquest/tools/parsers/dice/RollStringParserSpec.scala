package michalz.openquest.tools.parsers.dice

import org.specs2.Specification
import org.specs2.execute.DecoratedResult
import org.specs2.matcher.DataTable
import org.specs2.specification.Tables

import scala.language.implicitConversions

class RollStringParserSpec extends Specification with Tables:
  def is =
    s2"""
    This is a specification for RollStringParser
      All static strings shall be parser correctly $staticTable
      All roll string shall be parser correctly $rollTable

Static table:
${staticTable.message}

Roll table:
${rollTable.message}
  """

  //format: off
  val staticTable: DecoratedResult[DataTable] =
    "roll string"     | "min" | "max" | "avg" |>
    "1"               ! 1     ! 1     ! 1     |
    "(1 + 3)*4/5"     ! 3     ! 3     ! 3     |
    "1 + 2"           ! 3     ! 3     ! 3     |
    "1+2+3"           ! 6     ! 6     ! 6     |
    "2+3*4"           ! 14    ! 14    ! 14    |
    "1+ 2*3"          ! 7     ! 7     ! 7     |
    "(1 + 3)*4/5"     ! 3     ! 3     ! 3     |
      { (roll, expMin, expMax, expAvg) =>
      val result = RollStringParser.parseRollString(roll)

      (result.map(_.min) must beSome(expMin)) and
        (result.map(_.max) must beSome(expMax)) and
        (result.map(_.avg) must beSome(expAvg))
    }

  val rollTable =
    "roll string" | "min" | "max" | "avg" | "render"    |>
    "d6"          ! 1     ! 6     ! 4     ! "d6"        |
    "2d6"         ! 2     ! 12    ! 7     ! "2d6"       |
    "3d6"         ! 3     ! 18    ! 11    ! "3d6"       |
    "2d6 + 8"     ! 10    ! 20    ! 15    ! "2d6+8"     |
    "d6 * (3+2d6)"! 5     ! 90    ! 35    ! "d6*(3+2d6)"|
    { (roll, expMin, expMax, expAvg, render) =>
      val result = RollStringParser.parseRollString(roll)

      (result.map(_.min) must beSome(expMin)) and
        (result.map(_.max) must beSome(expMax)) and
        (result.map(_.avg) must beSome(expAvg)) and
        (result.map(_.roll) must beSome(render))
    }

  //format:on



