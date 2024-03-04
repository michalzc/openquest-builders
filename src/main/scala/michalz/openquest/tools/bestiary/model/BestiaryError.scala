package michalz.openquest.tools.bestiary.model

import michalz.openquest.tools.OpenQuestError

trait BestiaryError extends OpenQuestError

case class CSVReadingError(fieldName: String, errorMessage: String) extends BestiaryError
