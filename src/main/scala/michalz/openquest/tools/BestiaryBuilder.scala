package michalz.openquest.tools

import michalz.openquest.tools.packsreader.PacksReader

import scala.util.CommandLineParser

given CommandLineParser.FromString[os.Path] with
  def fromString(value: String): os.Path = os.Path(value)

@main
def bestiaryBuilder(sourceFile: os.Path, packsDir: os.Path, outputDir: os.Path): Unit =
  println(s"Bestiary builder, looking defs in $sourceFile, item data in ${packsDir} and output to $outputDir directory")
  val packs = new PacksReader(packsDir)
