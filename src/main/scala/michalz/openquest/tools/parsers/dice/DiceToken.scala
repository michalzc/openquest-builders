package michalz.openquest.tools.parsers.dice

sealed trait RollToken:
  def min: Long
  def max: Long
  def avg: Long = scala.math.round(davg)
  def davg: Double
  def render: String

sealed trait Expression extends RollToken:
  def left: RollToken
  def right: RollToken
  def min: Long    = op(left.min, right.min)
  def max: Long    = op(left.max, right.max)
  def davg: Double = op(left.davg, right.davg)
  def op[A: Numeric](lval: A, rval: A): A

sealed trait InParentheses extends Expression:
  def symbol: String
  def render: String = s"${left.render}${symbol}${right.render}"

sealed trait UseParentheses extends Expression:
  def symbol: String

  def render: String =
    s"${renderChild(left)}${symbol}${renderChild(right)}"
  private def renderChild(child: RollToken): String = child match
    case inP: InParentheses => s"(${inP.render})"
    case noP                => s"${noP.render}"

case class Dice(
  num: Option[Long],
  size: Long
) extends RollToken:
  def min: Long    = num.getOrElse(1L)
  def max: Long    = num.getOrElse(1L) * size
  def davg: Double = (min.toDouble + max.toDouble) / 2.0

  def render: String = s"${num.getOrElse("")}d${size}"

case class Number(
  num: Long
) extends RollToken:
  def min: Long      = num
  def max: Long      = num
  def davg: Double   = num.toDouble
  def render: String = s"${num}"

case class PlusExpression(
  left: RollToken,
  right: RollToken
) extends InParentheses:
  def symbol: String = "+"
  def op[A: Numeric](lval: A, rval: A): A = Numeric[A].plus(lval, rval)

case class MultiplyExpression(
  left: RollToken,
  right: RollToken
) extends UseParentheses:
  def symbol: String = "*"
  def op[A: Numeric](lval: A, rval: A): A = Numeric[A].times(lval, rval)

case class MinusExpression(
  left: RollToken,
  right: RollToken
) extends InParentheses:
  def symbol: String = "-"
  def op[A: Numeric](lval: A, rval: A): A = Numeric[A].minus(lval, rval)

case class DivExpression(
  left: RollToken,
  right: RollToken
) extends UseParentheses:
  def symbol: String = "/"
  override def op[A: Numeric](lval: A, rval: A): A = {
    val num = implicitly[Numeric[A]]
    num match
      case i: Integral[A] => i.quot(lval, rval)
      case f: Fractional[A] => f.div(lval, rval)
  }

case object Empty extends RollToken:
  def min: Long      = 0
  def max: Long      = 0
  def davg: Double   = 0
  def render: String = ""
