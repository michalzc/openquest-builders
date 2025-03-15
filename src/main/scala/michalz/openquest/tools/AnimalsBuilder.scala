package michalz.openquest.tools

import cats.data.{EitherT, NonEmptyList, Validated}
import cats.effect.{ExitCode, IO, IOApp, Sync}
import cats.syntax.either.*
import cats.syntax.semigroup.*
import michalz.openquest.tools.validation.{validateDirectory, validateFile}
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import java.nio.file.{Path, Paths}

case class AppPaths(sourceFile: Path, destinationDirectory: Path)

def validateFiles(sourceFile: Path, outputDir: Path): Either[NonEmptyList[OpenQuestError], Unit] =
  (validateFile(sourceFile) |+| validateDirectory(outputDir)).toEither

def createFiles(src: String, dst: String) = Either
  .catchNonFatal((Paths.get(src), Paths.get(dst)))
  .map(AppPaths(_, _))
  .leftMap(e => NonEmptyList.one(ErrorWrapper("Can't build paths", e)))

def validateInput(args: List[String]): IO[Either[NonEmptyList[OpenQuestError], AppPaths]] = args match

  case src :: dst :: Nil =>
    (for {
      paths <- EitherT(IO.blocking(createFiles(src, dst)))
      _     <- EitherT.fromEither[IO](validateFiles(paths.sourceFile, paths.destinationDirectory))
    } yield paths).value

  case _ =>
    IO.pure(
      Validated.invalidNel(ApplicationError(s"Two arguments are required, but got ${args.mkString(", ")}")).toEither
    )

def generateAnimals[F[_]: Sync](
  sourceFile: Path,
  outputDirectory: Path
): F[Either[NonEmptyList[OpenQuestError], Unit]] =
  EitherT.pure[F, NonEmptyList[OpenQuestError]](()).value

object AnimalsBuilder extends IOApp:
  val logger: Logger[IO]                    = Slf4jLogger.getLogger
  def run(args: List[String]): IO[ExitCode] =
    val result = for {
      paths <- EitherT(validateInput(args))
      _     <- EitherT(generateAnimals[IO](paths.sourceFile, paths.destinationDirectory))
    } yield ()

    result.foldF(
      _.traverse(error => logger.error(error)("Application Error")) >> IO.pure(ExitCode.Error),
      _ => IO.pure(ExitCode.Success)
    )
