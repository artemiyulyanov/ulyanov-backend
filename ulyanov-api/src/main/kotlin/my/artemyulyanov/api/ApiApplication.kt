package my.artemyulyanov.api

import com.typesafe.config.ConfigFactory
import dev.d1s.exkt.ktor.server.koin.configuration.Configurers
import dev.d1s.exkt.ktor.server.koin.configuration.ServerApplication
import dev.d1s.exkt.ktor.server.koin.configuration.builtin.Connector
import dev.d1s.exkt.ktor.server.koin.configuration.builtin.ApplicationConfig as ApplicationConfigBean
import dev.d1s.exkt.ktor.server.koin.configuration.builtin.Di
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import my.artemyulyanov.api.configuration.*
import my.artemyulyanov.api.util.developmentMode
import org.koin.core.component.KoinComponent
import org.lighthousegames.logging.logging

object ApiApplication : ServerApplication(), KoinComponent {
    override val configurers: Configurers = listOf(
        Connector,
        ApplicationConfigBean,
        ContentNegotiation,
        Cors,
        Security,
        Database,
        DtoConverters,
        Repositories,
        Services,
        Routing,
        StatusPages,
        Di
    )

    private val log = logging()

    override fun launch() {
        try {
            log.i {
                "Starting Ulyanov API..."
            }

            val loadedConfig = loadConfig()
            System.setProperty("io.ktor.development", loadedConfig.developmentMode.toString())

            val applicationEngineEnvironment = createApplicationEngineEnvironment(config = loadedConfig)

            embeddedServer(
                Netty,
                environment = applicationEngineEnvironment,
                configure = { applyEngineConfiguration(config = loadedConfig) },
                module = { applyApplicationConfigurersAndRoutes(config = loadedConfig) }
            ).start(wait = true)
        } catch (e: Throwable) {
            log.i {
                "Server crashed! Caused by: ${e.message}"
            }
        }
    }

    private fun loadConfig(): ApplicationConfig {
        val config = ConfigFactory.load("api")

        return HoconApplicationConfig(config)
    }
}