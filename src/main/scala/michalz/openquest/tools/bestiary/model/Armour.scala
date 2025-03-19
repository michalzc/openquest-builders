package michalz.openquest.tools.bestiary.model

case class Armour(
  id: String,
  name: String,
  img: String,
  ap: Int,
  state: String,
  key: String
) extends Item
