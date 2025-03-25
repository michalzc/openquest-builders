package michalz.openquest.tools.bestiary.model

case class Armour(
  id: Option[String],
  name: String,
  img: String,
  ap: Int,
  state: String,
  key: Option[String]
) extends Item
