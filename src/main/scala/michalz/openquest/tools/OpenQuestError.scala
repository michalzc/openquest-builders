package michalz.openquest.tools

import java.nio.file.Path
import scala.StringContext.InvalidEscapeException

trait OpenQuestError extends Throwable

class FileError(message: String) extends Exception(message) with OpenQuestError

case class ApplicationError(message: String) extends IllegalArgumentException(message) with OpenQuestError
case class ErrorWrapper(message: String, cause: Throwable) extends RuntimeException(message, cause) with OpenQuestError

object FileError:
  def apply(message: String): FileError = new FileError(message)
  def notExists(path: Path): FileError = new FileError(s"${path} doesn't exists")
  def notAFile(path: Path): FileError = new FileError(s"${path} is not a file")
  def notADir(path: Path): FileError = new FileError(s"${path} is not a directory")
