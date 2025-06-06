package my.artemyulyanov.api.util

import io.konform.validation.ValidationResult
import io.ktor.server.plugins.*

fun ValidationResult<*>.orThrow() {
    if (errors.isNotEmpty()) {
        val messages = errors.joinToString("; ") {
            it.message
        }

        throw BadRequestException("Invalid body: $messages")
    }
}

fun ValidationResult<*>.orElse(block: () -> Unit) {
    if (errors.isNotEmpty()) {
        block()
    }
}