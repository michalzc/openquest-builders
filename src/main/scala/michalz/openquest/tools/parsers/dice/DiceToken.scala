package michalz.openquest.tools.parsers.dice

sealed trait RollToken:
  def min: Long
  def max: Long
  def avg: Long = scala.math.round((min.toDouble + max.toDouble) / 2.0)

case class Dice(
  num: Option[Long],
  size: Long
) extends RollToken:
  def min: Long = num.getOrElse(1L)
  def max: Long = num.getOrElse(1L) * size

case class Number(
  num: Long
) extends RollToken:
  def min: Long = num
  def max: Long = num

case class PlusExpression(
  left: RollToken,
  right: RollToken
) extends RollToken:
  def min: Long = left.min + right.min
  def max: Long = left.max + right.max

case class MultiplyExpression(
  left: RollToken,
  right: RollToken
) extends RollToken:
  def min: Long = left.min * right.min
  def max: Long = left.max * right.max

case class MinusExpression(
  left: RollToken,
  right: RollToken
) extends RollToken:
  def min: Long = left.min - right.min
  def max: Long = left.max - right.max

case class DivExpression(
  left: RollToken,
  right: RollToken
) extends RollToken:
  def min: Long = scala.math.round(left.min.toDouble / right.min.toDouble)
  def max: Long = scala.math.round(left.max.toDouble / right.max.toDouble)

case object Empty extends RollToken:
  def min: Long = 0
  def max: Long = 0


