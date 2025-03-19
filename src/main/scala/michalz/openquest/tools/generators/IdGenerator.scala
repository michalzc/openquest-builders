package michalz.openquest.tools.generators

import cats.effect.kernel.Sync
import cats.effect.std.Random
import fs2.Stream

import cats.syntax.traverse.*
import cats.instances.list.*
import cats.syntax.functor.*

object IdGenerator:

  val IdSize = 16
  val Characters: List[Char] = List(
    'a' to 'z',
    'A' to 'Z',
    '0' to '9'
  ).flatten

  def nextId[F[_]: Sync](random: Random[F]): F[String] =
    List
      .fill(IdSize)(random.elementOf(Characters))
      .sequence
      .map(_.mkString)

  def stream[F[_]: Sync](random: Random[F]): Stream[F, String] =
    Stream.eval(nextId(random)).repeat

  def stream[F[_]: Sync]: Stream[F, String] =
    Stream
      .eval(Random.scalaUtilRandom[F])
      .flatMap(stream(_))
