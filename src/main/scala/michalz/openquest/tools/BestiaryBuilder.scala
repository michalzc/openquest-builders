package michalz.openquest.tools

import scala.util.CommandLineParser


given CommandLineParser.FromString[os.Path] with
  def fromString(value: String): os.Path = os.Path(value)

@main
def bestiaryBuilder(sourceFile: os.Path): Unit =
  println(s"Bestiary builder, looking defs in ${sourceFile}")
