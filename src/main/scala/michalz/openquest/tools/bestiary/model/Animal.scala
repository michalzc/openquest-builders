package michalz.openquest.tools.bestiary.model

case class Animal(
  id: String,
  name: String,
  characteristics: Characteristics,
  attributes: Attributes,
  items: List[Item]
)

