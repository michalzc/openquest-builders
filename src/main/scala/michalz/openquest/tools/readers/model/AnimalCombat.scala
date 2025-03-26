package michalz.openquest.tools.readers.model

import cats.syntax.option.*

import michalz.openquest.tools.readers.model.AnimalCombat.Attack

case class AnimalCombat(
  skill: Int,
  attacks: List[Attack]
)

object AnimalCombat:
  def empty: AnimalCombat = AnimalCombat(0, List.empty)
  def oneAttack(skill: Int, name: String, damage: Option[String] = none): AnimalCombat =
    AnimalCombat(skill, Attack(name, damage))
  def apply(skill: Int, seqAttacks: Attack*): AnimalCombat = AnimalCombat(skill, seqAttacks.toList)

  case class Attack(
    name: String,
    damage: Option[String] = none,
    notes: Option[String] = none
  )

  object Attack:
    def apply(name: String, damage: String): Attack = Attack(name, damage.some)
