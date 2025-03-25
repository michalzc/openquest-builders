package michalz.openquest.tools.bestiary.model

import cats.Show

case class Animal(
  id: String,
  name: String,
  characteristics: Characteristics,
  attributes: Attributes,
  items: List[Item]
)

object Animal:
  given showAnimal: Show[Animal] = Show.fromToString[Animal]