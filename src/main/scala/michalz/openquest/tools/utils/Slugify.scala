package michalz.openquest.tools.utils

import scala.jdk.CollectionConverters.*

import com.github.slugify.Slugify as JSlugify

object Slugify:
  private val slugify: JSlugify =
    JSlugify
      .builder()
      .customReplacements(Map("(" -> "", ")" -> "").asJava)
      .build()

  def apply(value: String): String = slugify.slugify(value)
