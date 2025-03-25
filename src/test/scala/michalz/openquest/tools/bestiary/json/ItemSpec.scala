package michalz.openquest.tools.bestiary.json

import io.circe.syntax.*
import io.circe.yaml.Printer

import scala.io.Source
import scala.language.implicitConversions
import scala.util.Using

import org.specs2.Specification
import org.specs2.matcher.Matchers

import michalz.openquest.tools.bestiary.model.Weapon

class ItemSpec extends Specification with Matchers:

  import michalz.openquest.tools.bestiary.json.Item.{WeaponDecoder, WeaponEncoder}

  def is = s2"""
  This is specification of Item json codec
   Weapon json/yaml decoder should read yaml properly $weaponYamlRead
   Weapon json/yaml encode should serialize object to yaml properly $weaponYamlWrite
  """

  private val yamlPrinter: Printer = io.circe.yaml.Printer(
    dropNullKeys = true,
    indent = 2,
    preserveOrder = true
  )

  private val clawString = Using(Source.fromURL(getClass.getResource("/animals/claw.yaml"))) { src =>
    src.mkString
  }.get

  private def weaponYamlRead =
    val parsedYaml = io.circe.yaml.parser.parse(clawString)
    val clawEth    = parsedYaml.flatMap(_.as[Weapon])
    clawEth should beRight(ItemSpec.claw)

  private def weaponYamlWrite =
    val json    = ItemSpec.claw.asJson
    val printed = yamlPrinter.pretty(json)
    printed should beEqualTo(clawString)

object ItemSpec:

  val claw: Weapon = Weapon(
    name = "Claw",
    img = "systems/oq/assets/icons/grasping-claws.svg",
    skillReference = "natural-combat",
    hands = Weapon.Hands.one,
    state = Weapon.State.natural,
    `type` = Weapon.Type.melee,
    damageFormula = "1d1",
    includeDamageMod = true
  )
