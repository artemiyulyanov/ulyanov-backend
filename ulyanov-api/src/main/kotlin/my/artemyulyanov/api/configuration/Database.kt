package my.artemyulyanov.api.configuration

import dev.d1s.exkt.ktor.server.koin.configuration.ApplicationConfigurer
import dev.d1s.exkt.ktor.server.koin.configuration.Configurers
import dev.d1s.exkt.ktor.server.koin.configuration.ServerApplication
import io.ktor.server.application.*
import io.ktor.server.config.*
import my.artemyulyanov.api.database.DatabaseConnector
import my.artemyulyanov.api.database.DefaultDatabaseConnector
import org.koin.core.component.KoinComponent
import org.koin.core.module.Module

object Database : ApplicationConfigurer {
    override fun Application.configure(module: Module, config: ApplicationConfig) {
        val connector = DefaultDatabaseConnector()

        with(connector) {
            initialize(config)
        }

        module.single<DatabaseConnector> {
            connector
        }
    }
}