package michalz.openquest.tools

import cats.Show
import cats.data.NonEmptyList

import cats.syntax.either.*

import java.nio.file.Path

trait OpenQuestError extends Throwable
object OpenQuestError:
  given show: Show[OpenQuestError] = Show.fromToString[OpenQuestError]

type OpenQuestErrorNel     = NonEmptyList[OpenQuestError]
type OpenQuestResult[A]    = Either[OpenQuestError, A]
type OpenQuestResultNel[A] = Either[NonEmptyList[OpenQuestError], A]

class FileError(message: String) extends Exception(message) with OpenQuestError

extension [A](result: OpenQuestResult[A]) def toNel: OpenQuestResultNel[A] = result.leftMap(NonEmptyList.one)

case class ApplicationError(message: String)               extends IllegalArgumentException(message) with OpenQuestError
case class ErrorWrapper(message: String, cause: Throwable) extends RuntimeException(message, cause) with OpenQuestError
case class ParsingError(message: String, remain: String)   extends RuntimeException(message) with OpenQuestError

object FileError:
  def apply(message: String): FileError = new FileError(message)
  def notExists(path: Path): FileError  = new FileError(s"${path} doesn't exists")
  def notAFile(path: Path): FileError   = new FileError(s"${path} is not a file")
  def notADir(path: Path): FileError    = new FileError(s"${path} is not a directory")

object Implicits:

  given showError: Show[OpenQuestError] = Show.fromToString[OpenQuestError]
