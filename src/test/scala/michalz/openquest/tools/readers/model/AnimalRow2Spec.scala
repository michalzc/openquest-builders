package michalz.openquest.tools.readers.model

import cats.Show
import cats.data.NonEmptyList
import cats.effect.IO
import cats.effect.std.Random

import cats.syntax.either.*

import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import java.nio.file.Paths

import org.specs2.Specification
import org.specs2.matcher.Matchers

import michalz.openquest.tools.Implicits.*
import michalz.openquest.tools.Implicits.showError
import michalz.openquest.tools.OpenQuestError
import michalz.openquest.tools.bestiary.model.Animal
import michalz.openquest.tools.bestiary.model.Animal.showAnimal
import michalz.openquest.tools.cats.effect.specs2.CateEffect
import michalz.openquest.tools.converters.AnimalConverter.toAnimal
import michalz.openquest.tools.generators.IdGenerator
import michalz.openquest.tools.readers.AnimalsReader

class AnimalRow2Spec extends Specification with Matchers with CateEffect:
  def is =
    s2"""
      AnimalRow specification
        All animal rows must be properly deserialized $deserialization
        Failed row in file should be skipped $skipFailed
        Convert rows to Animals $rowsAnimals
      """

  val logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  private val properFile                = Paths.get(getClass.getResource("/animals.csv").toURI)
  private val invalidFile               = Paths.get(getClass.getResource("/animals-with-errors.csv").toURI)
  implicit val randomIO: IO[Random[IO]] = Random.scalaUtilRandom[IO]

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

    val animalStream = AnimalsReader[IO](properFile)
    val result =
      animalStream
        .map(_.leftMap(e => NonEmptyList.one(e)))
        .zip(IdGenerator.stream[IO])
        .map { case (animalEth, id) =>
          animalEth.flatMap(_.toAnimal(id))
        }
        .evalTap(elem => logger.info(elem.show))
        .compile
        .toList

    result.map { animalResults =>
      val (failures, successes) = animalResults.partition(_.isLeft)
      (failures must beEmpty) and (successes must haveSize(24))
    }
