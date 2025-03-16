package michalz.openquest.tools.readers.model

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import fs2.data.csv.decodeWithoutHeaders
import fs2.io.file.{Files, Path}
import org.scalatest.flatspec.{AnyFlatSpec, AsyncFlatSpec}
import org.scalatest.matchers.should.Matchers

class AnimalRowSpec extends AsyncFlatSpec with AsyncIOSpec with Matchers:

  val animalsFileName = getClass.getResource("/animals.csv").getFile
  val fixture         = Files.forIO.readAll(Path(animalsFileName)).through(fs2.text.utf8.decode[IO])

  import AnimalRow.decoder

  behavior of "AnimalRow"

  "Stream" should "read csv" in {
    val r = fixture.take(1).compile.toList
    r.map { result =>
      result.headOption should not be empty
    }
  }

  it should "be properly decoded" in {
    val stream  = fixture.through(decodeWithoutHeaders[AnimalRow]())
    val animals = stream.compile.toList
    animals.map { animals =>
      animals should have size (24)
    }
  }
