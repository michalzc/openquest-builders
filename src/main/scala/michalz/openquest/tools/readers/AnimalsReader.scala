package michalz.openquest.tools.readers

import cats.effect
import cats.effect.Sync
import cats.effect.kernel.{Async, Resource}
import com.github.tototoshi.csv.CSVReader
import fs2.Stream
import michalz.openquest.tools.OpenQuestError
import michalz.openquest.tools.readers.model.AnimalRow

import java.nio.file.Path


object AnimalsReader:
  def apply[F[_]: Async](sourcePath: Path): Stream[F, Either[OpenQuestError, AnimalRow]] =
    val r: Resource[F, Iterator[Seq[String]]] = Resource
      .make[F, CSVReader](effect.Sync[F].blocking(CSVReader.open(sourcePath.toFile)))(csv =>
        Sync[F].blocking(csv.close())
      )
      .map(r => r.iterator)

    val s = Stream.resource(r).flatMap(iter => Stream.fromBlockingIterator(iter, 1))

    ???
