package my.artemyulyanov.api.configuration

import dev.d1s.exkt.ktor.server.koin.configuration.ApplicationConfigurer
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.plugins.cors.routing.*
import org.koin.core.module.Module

object Cors : ApplicationConfigurer {
    override fun Application.configure(module: Module, config: ApplicationConfig) {
        install(CORS) {
            anyHost()

            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Put)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Delete)

            allowHeader(HttpHeaders.ContentType)
            allowHeader(HttpHeaders.ContentLength)
            allowHeader(HttpHeaders.Authorization)

            allowHeader(HttpHeaders.AccessControlAllowOrigin)
            allowHeader(HttpHeaders.AccessControlAllowMethods)
            allowHeader(HttpHeaders.AccessControlAllowHeaders)
        }
    }
}