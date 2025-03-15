package michalz.openquest.tools.validation

import cats.data.{Validated, ValidatedNel}
import michalz.openquest.tools.{FileError, OpenQuestError}
import java.nio.file.Path

def validateDirectory(outputDir: Path): ValidatedNel[OpenQuestError, Unit] =
  Validated
    .condNel(outputDir.toFile.exists(), (), FileError.notExists(outputDir))
    .andThen(_ => Validated.condNel(outputDir.toFile.isDirectory, (), FileError.notADir(outputDir)))

def validateFile(sourceFile: Path): ValidatedNel[OpenQuestError, Unit] =
  Validated
    .condNel(sourceFile.toFile.exists(), (), FileError.notExists(sourceFile))
    .andThen(_ => Validated.condNel(sourceFile.toFile.isFile, (), FileError.notAFile(sourceFile)))
