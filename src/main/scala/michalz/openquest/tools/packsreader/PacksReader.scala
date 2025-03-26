package michalz.openquest.tools.packsreader

import cats.data.State

import io.circe.{Json, ParsingFailure}
import io.circe.yaml.parser.parse

import scala.annotation.unused
import scala.jdk.CollectionConverters.*

import michalz.openquest.tools.packsreader.PacksReader.skillBySlugMatcher
import michalz.openquest.tools.utils.Slugify

type ParsingResult = Either[ParsingFailure, Json]

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
):

  private val yamlFiles = os.walk(packsDir).filter(_.ext == "yaml")

  private val (successes: Seq[ParsingResult], errors: Seq[ParsingResult]) = yamlFiles
    .map(filePath => os.read(filePath))
    .map(content => parse(content))
    .partition(_.isRight)

  private val yamls: Seq[Json] = successes.collect { case Right(yaml) => yaml }

  errors
    .collect { case Left(error) => error }
    .foreach(error => println("Error during reading file: ${error}")) // FIXME

  private def findSkillAndCache(skillSlug: String): (PacksReader, Option[Json]) =
    skillsCache.get(skillSlug) match
      case Some(jsonOpt) => (this, jsonOpt)
      case None =>
        val matcher: Json => Boolean = skillBySlugMatcher(skillSlug)
        val result                   = yamls.find(matcher)
        (this.copy(skillsCache = this.skillsCache + (skillSlug -> result)), result)

  @unused
  private def findElement(matcher: Json => Boolean): Option[Json] =
    yamls.find(matcher)

object PacksReader {

  def findSkill(skillSlug: String): State[PacksReader, Option[Json]] =
    State(pr => pr.findSkillAndCache(skillSlug))

  def skillBySlugMatcher(skillSlug: String): Json => Boolean =
    json =>
      val cursor                   = json.hcursor
      val typeJson: Option[String] = cursor.downField("type").as[String].toOption
      val nameJson: Option[String] = cursor.downField("name").as[String].toOption
      (for {
        `type` <- typeJson
        name   <- nameJson
        skillSlug = Slugify(name)
      } yield `type` == "skill" && skillSlug == skillSlug).getOrElse(false)
}
