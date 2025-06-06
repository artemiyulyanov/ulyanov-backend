package my.artemyulyanov.common.validation

import dev.d1s.exkt.konform.isNotBlank
import io.konform.validation.Validation
import io.konform.validation.ValidationBuilder
import my.artemyulyanov.common.AbstractPortfolioItem
import my.artemyulyanov.common.PortfolioItem
import my.artemyulyanov.common.Timestamp
import java.time.Instant
import java.time.format.DateTimeParseException

fun validatePortfolioItem(
    startDate: Timestamp,
    endDate: Timestamp
): Validation<AbstractPortfolioItem> =
    Validation {
        AbstractPortfolioItem::text {
            isNotBlank() hint "Text must be filled out"
        }

        AbstractPortfolioItem::description {
            isNotBlank() hint "Description must be filled out"
        }

        AbstractPortfolioItem::scope {
            isNotBlank() hint "Scope must be filled out"
        }

        validateDates(startDate, endDate)
    }

private fun ValidationBuilder<AbstractPortfolioItem>.validateDates(
    startDate: Timestamp,
    endDate: Timestamp
) {
    val now = Instant.now()

    val parsedStart = tryParseInstant(startDate)
    val parsedEnd = tryParseInstant(endDate)

    run {
        if (parsedStart == null) {
            addConstraint("Start date has invalid format") { false }
        } else {
            addConstraint("Start date must not be in the past") {
                !parsedStart.isBefore(now)
            }
        }
    }

    run {
        if (parsedEnd == null) {
            addConstraint("End date has invalid format") { false }
        } else {
            addConstraint("End date must not be in the past") {
                !parsedEnd.isBefore(now)
            }
        }
    }

    run {
        if (parsedStart != null && parsedEnd != null) {
            addConstraint("End date must be after start date") {
                parsedEnd.isAfter(parsedStart)
            }
        }
    }
}

private fun tryParseInstant(value: String): Instant? =
    try {
        Instant.parse(value)
    } catch (e: DateTimeParseException) {
        null
    }