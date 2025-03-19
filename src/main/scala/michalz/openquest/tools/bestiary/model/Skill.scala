package michalz.openquest.tools.bestiary.model

case class Skill(
  id: String,
  name: String,
  img: String,
  description: String,
  formula: String,
  `type`: String,
  advancement: Int,
  key: String
) extends Item
