package michalz.openquest.tools.packsreader

import com.typesafe.scalalogging.LazyLogging
import io.circe.yaml.parser.parse

class PacksReader(packsDir: os.Path) extends LazyLogging {
  val yamlFiles = os.walk(packsDir).filter(_.ext == "yaml")
  val (successes, errors)     = yamlFiles.map(filePath => os.read(filePath)).map(content => parse(content)).partition(_.isRight)
  val yamls = successes.collect { case Right(yaml) => yaml }
  errors.collect { case Left(error) => error }.foreach(error => logger.warn("Error during reading file", error))

}
