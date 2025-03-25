package michalz.openquest.tools.bestiary.model

case class Skill(
  id: Option[String],
  name: String,
  img: String,
  description: String,
  formula: String,
  `type`: String,
  advancement: Int,
  key: Option[String]
) extends Item
