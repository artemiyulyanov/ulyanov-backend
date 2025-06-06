/*
 * Copyright 2024-2025 MX.Store Team
 */

package my.artemyulyanov.api.configuration

import dev.d1s.exkt.ktor.server.koin.configuration.ApplicationConfigurer
import dev.d1s.exkt.ktor.server.statuspages.HttpStatusException
import dev.d1s.exkt.ktor.server.statuspages.httpStatusException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.module.Module
import org.lighthousegames.logging.logging
import io.ktor.server.plugins.statuspages.StatusPages as StatusPagesPlugin

object StatusPages : ApplicationConfigurer, KoinComponent {

    private val logger = logging()

    override fun Application.configure(module: Module, config: ApplicationConfig) {
        install(StatusPagesPlugin) {
            handleExceptions()
            handleStatuses()
            handleNotFoundStatus()

            httpStatusException { call, exception ->
                val status = exception.status
                val message = exception.toMessage(status)
                call.respond(status, message)
            }
        }
    }

    private fun StatusPagesConfig.handleExceptions() {
        exception<Throwable> { call, throwable ->
            logger.d {
                "Handling exception ${throwable::class.simpleName}: ${throwable.message}"
            }

            val status = when (throwable) {
                is BadRequestException -> HttpStatusCode.BadRequest
                is HttpStatusException -> throwable.status
                else -> HttpStatusCode.InternalServerError.also {
                    throwable.printStackTrace()
                }
            }

            val message = throwable.toMessage(status)
            call.respond(status, message)
        }
    }

    private fun StatusPagesConfig.handleStatuses() {
        status(HttpStatusCode.BadRequest, HttpStatusCode.Unauthorized) { call, status ->
            logger.d {
                "Handling status $status on path ${call.request.path()}"
            }

            val message = Message(status.description)
            call.respond(status, message)
        }
    }

    private fun StatusPagesConfig.handleNotFoundStatus() {
        status(HttpStatusCode.NotFound) { call, _ ->
            logger.d {
                "Handling 404 code on path ${call.request.path()}"
            }

            call.processNotFoundStatus()
        }
    }

    private suspend fun ApplicationCall.processNotFoundStatus() {
        val message = Message(HttpStatusCode.NotFound.description)
        respond(HttpStatusCode.NotFound, message)
    }

    private fun Throwable.toMessage(statusCode: HttpStatusCode) =
        Message((message ?: "No message").takeIf { statusCode != HttpStatusCode.InternalServerError }
            ?: HttpStatusCode.InternalServerError.description)

    @Serializable
    private data class Message(
        val message: String
    )
}