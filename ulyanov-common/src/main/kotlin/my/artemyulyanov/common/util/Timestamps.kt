package my.artemyulyanov.common.util

import my.artemyulyanov.common.Timestamp
import java.time.Instant
import java.time.format.DateTimeParseException

fun Timestamp?.tryParseInstant(): Instant? =
    this?.let {
        try {
            Instant.parse(it)
        } catch (e: Throwable) {
            null
        }
    }

fun Instant?.parseTimestamp(): Timestamp =
    this?.toString() ?: "Now"