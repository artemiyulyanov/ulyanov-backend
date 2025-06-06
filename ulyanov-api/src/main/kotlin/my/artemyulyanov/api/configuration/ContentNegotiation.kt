package my.artemyulyanov.api.configuration

import dev.d1s.exkt.ktor.server.koin.configuration.ApplicationConfigurer
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json
import org.koin.core.module.Module

object ContentNegotiation : ApplicationConfigurer {
    override fun Application.configure(module: Module, config: ApplicationConfig) {
        install(ContentNegotiation) {
            val json = Json(DefaultJson) {
                explicitNulls = false
            }

            json(json)
        }
    }
}