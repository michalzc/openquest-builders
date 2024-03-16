package michalz.openquest.tools.packsreader

import cats.data.State
import com.github.slugify.Slugify
import com.typesafe.scalalogging.LazyLogging
import io.circe.Json
import io.circe.yaml.parser.parse
import michalz.openquest.tools.packsreader.PacksReader.skillBySlugMatcher
import os.Path

import scala.jdk.CollectionConverters.*

enum ItemType:
  case SKILL
  case WEAPON
  case ARMOUR
  case SPELL

case class PacksReader(
  packsDir: os.Path,
  skillsCache: Map[String, Option[Json]] = Map.empty,
  weaponCache: Map[String, Option[Json]] = Map.empty,
  armourCache: Map[String, Option[Json]] = Map.empty,
  spellCache: Map[String, Option[Json]] = Map.empty
) extends LazyLogging:
  private val yamlFiles = os.walk(packsDir).filter(_.ext == "yaml")
  val (successes, errors) = yamlFiles
    .map(filePath => os.read(filePath))
    .map(content => parse(content))
    .partition(_.isRight)
  val yamls = successes.collect { case Right(yaml) =>
    yaml
  }
  errors
    .collect { case Left(error) =>
      error
    }
    .foreach(error => logger.warn("Error during reading file", error))

  private def findSkillAndCache(skillSlug: String): (PacksReader, Option[Json]) =
    skillsCache.get(skillSlug) match
      case Some(jsonOpt) =>
        (this, jsonOpt)
      case None =>
        val matcher: Json => Boolean = skillBySlugMatcher(skillSlug)
        val result                   = yamls.find(matcher)
        (this.copy(skillsCache = this.skillsCache + (skillSlug -> result)), result)

  private def findElement(matcher: Json => Boolean): Option[Json] = yamls.find(matcher)

object PacksReader {
  val slugify = Slugify.builder().customReplacements(Map("(" -> "", ")" -> "").asJava).build()

  def findSkill(skillSlug: String): State[PacksReader, Option[Json]] = State(pr => pr.findSkillAndCache(skillSlug))

  def skillBySlugMatcher(skillSlug: String): Json => Boolean =
    (json) =>
      val cursor                   = json.hcursor
      val typeJson: Option[String] = cursor.downField("type").as[String].toOption
      val nameJson: Option[String] = cursor.downField("name").as[String].toOption
      (
        for {
          `type` <- typeJson
          name   <- nameJson
          skillSlug = slugify.slugify(name)
        } yield `type` == "skill" && skillSlug == skillSlug
      ).getOrElse(false)
}
