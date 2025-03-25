package michalz.openquest.tools.parsers.animal

import scala.language.implicitConversions

import org.specs2.Specification
import org.specs2.execute.DecoratedResult
import org.specs2.matcher.{DataTable, Matchers}
import org.specs2.specification.Tables
import org.specs2.specification.core.SpecStructure

import michalz.openquest.tools.readers.model.AnimalArmour

class AnimalArmourParserSpec extends Specification with Matchers with Tables:
  def is: SpecStructure =
    s2"""
      This is specification of AnimalArmourParser
        It should properly parse armour strings $armoursTable
      """

  val armoursTable: DecoratedResult[DataTable] =
    "Armour String" | "AnimalArmour" |>
      "Chitin (5AP)" ! AnimalArmour("Chitin", 5) |
      "Tough hide (3AP)" ! AnimalArmour("Tough hide", 3) |
      "Hide (2AP)" ! AnimalArmour("Hide", 2) |
      "Chitin (5AP)" ! AnimalArmour("Chitin", 5) |
      "Hide (2AP)" ! AnimalArmour("Hide", 2) |
      "Thick Shell (6AP)" ! AnimalArmour("Thick Shell", 6) |
      "Thick Hide (5AP)" ! AnimalArmour("Thick Hide", 5) |
      "Thick feathers (3AP)" ! AnimalArmour("Thick feathers", 3) |
      "None" ! AnimalArmour.noArmour |
      "Thick hide (3 AP)" ! AnimalArmour("Thick hide", 3) |
      "None" ! AnimalArmour.noArmour |
      "thick feathers (3 AP)" ! AnimalArmour("thick feathers", 3) |
      "Hide (2AP)" ! AnimalArmour("Hide", 2) |
      "Tough skin (4 AP)" ! AnimalArmour("Tough skin", 4) |
      "Tough hide (3 AP)" ! AnimalArmour("Tough hide", 3) |
      "Scales (3 AP)" ! AnimalArmour("Scales", 3) | { (string, armour) =>
        val result = AnimalArmourParser.parseArmour(string)
        result should beRight(armour)
      }
