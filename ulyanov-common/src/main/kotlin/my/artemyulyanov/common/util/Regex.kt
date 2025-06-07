package my.artemyulyanov.common.util

import kotlin.text.Regex

object Regex {
    val ServerS3Link = Regex("^https://.+\\.(png|jpg|jpeg)\$", RegexOption.IGNORE_CASE)
}