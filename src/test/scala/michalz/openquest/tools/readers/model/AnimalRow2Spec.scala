package michalz.openquest.tools.readers.model

import cats.data.NonEmptyList
import cats.effect.IO
import michalz.openquest.tools.cats.effect.specs2.CateEffect
import michalz.openquest.tools.readers.AnimalsReader
import org.specs2.Specification
import org.specs2.matcher.Matchers
import cats.syntax.either.*

import java.nio.file.{Files, Paths}

class AnimalRow2Spec extends Specification with Matchers with CateEffect:
  def is =
    s2"""
      AnimalRow specification
        All animal rows must be properly deserialized $deserialization
        Failed row in file should be skipped $skipFailed
        Convert rows to Animals $rowsAnimals
      """

  private val properFile  = Paths.get(getClass.getResource("/animals.csv").toURI)
  private val invalidFile = Paths.get(getClass.getResource("/animals-with-errors.csv").toURI)

  private def deserialization =
    val animalsStream = AnimalsReader[IO](properFile)
    animalsStream.compile.toList.map { animals =>
      animals must haveSize(24)
    }

  private def skipFailed =
    val animalsStream = AnimalsReader[IO](invalidFile)
    animalsStream.compile.toList.map { animals =>
      val (failures, successes) = animals.partition(_.isLeft)
      (failures must haveSize(1)) and (successes must haveSize(2))
    }

  private def rowsAnimals =
    import michalz.openquest.tools.converters.AnimalConverters.toAnimal

    val animalStream = AnimalsReader[IO](properFile)
    val result = animalStream.map(_.leftMap(e => NonEmptyList.one(e))).map(_.flatMap(_.toAnimal)).compile.toList
    result.map { animalResults =>
      val (failures, successes) = animalResults.partition(_.isLeft)
      (failures must beEmpty) and (successes must haveSize(24))
    }
