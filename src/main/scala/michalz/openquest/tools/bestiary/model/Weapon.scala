package michalz.openquest.tools.bestiary.model

case class Weapon(
  id: String,
  name: String,
  img: String,
  skillReference: String,
  state: String,
  `type`: String,
  damageFormula: String,
  includeDamageMod: Boolean,
  key: String
) extends Item
