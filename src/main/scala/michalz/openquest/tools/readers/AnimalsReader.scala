package michalz.openquest.tools.readers

import cats.effect
import cats.effect.kernel.Async
import cats.syntax.either.*
import fs2.Stream
import fs2.data.csv.lenient.attemptDecodeWithoutHeaders
import fs2.io.file.{Files, Path}
import fs2.text.utf8
import michalz.openquest.tools.readers.model.AnimalRow
import michalz.openquest.tools.{ErrorWrapper, OpenQuestError}
import org.typelevel.log4cats.Logger

import java.nio.file

object AnimalsReader:

  import AnimalRow.decoder

  def apply[F[_]: Async](sourcePath: file.Path): Stream[F, Either[OpenQuestError, AnimalRow]] =
    Files
      .forAsync[F]
      .readAll(Path.fromNioPath(sourcePath))
      .through(utf8.decode[F])
      .through(attemptDecodeWithoutHeaders[AnimalRow]())
      .map(e => e.leftMap(e => ErrorWrapper("Error during decoding animal", e)))
