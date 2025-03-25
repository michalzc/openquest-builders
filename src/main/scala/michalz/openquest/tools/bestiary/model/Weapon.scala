package michalz.openquest.tools.bestiary.model

import cats.syntax.option.*

case class Weapon(
  id: Option[String],
  name: String,
  img: String,
  skillReference: String,
  hands: Weapon.Hands,
  state: Weapon.State,
  `type`: Weapon.Type,
  damageFormula: String,
  includeDamageMod: Boolean,
  key: Option[String]
) extends Item

object Weapon:
  def apply(
    name: String,
    img: String,
    skillReference: String,
    hands: Weapon.Hands,
    state: Weapon.State,
    `type`: Weapon.Type,
    damageFormula: String,
    includeDamageMod: Boolean
  ): Weapon = Weapon(
    id = none,
    name = name,
    img = img,
    skillReference = skillReference,
    hands = hands,
    state = state,
    `type` = `type`,
    damageFormula = damageFormula,
    includeDamageMod = includeDamageMod,
    key = none
  )

  enum Hands:
    case one, two, oneAndTwo

  enum State:
    case readied, carried, stored, natural

  enum Type:
    case melee, ranged, shield
