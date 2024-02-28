package michalz.openquest.tools.parsers.dice

import org.scalatest.OptionValues
import org.scalatest.funspec.AnyFunSpecLike
import org.scalatest.matchers.should.Matchers

class RollStringParserTest extends AnyFunSpecLike with Matchers with OptionValues {
  describe("A RollStringParser") {
    describe("with plain numbers") {
      val fixture =
        "1" ->
          (1, 1, 1) ::
          "2" ->
          (2, 2, 2) ::
          "1+2" ->
          (3, 3, 3) ::
          "1+2+3" ->
          (6, 6, 6) ::
          "2+3*4" ->
          (14, 14, 14) ::
          "1 + 2*3" ->
          (7, 7, 7) ::
          "(1 + 3)*4/5" ->
          (3, 3, 3) :: Nil

      fixture.foreach { case (rollString, (expectedMin, expectedMax, expectedAvg)) =>
        it(s"should parse \"${rollString}\"") {
          val result = RollStringParser.parseString(rollString).value

          result.min shouldBe expectedMin
          result.max shouldBe expectedMax
          result.avg shouldBe expectedAvg
        }
      }
    }
  }
}
