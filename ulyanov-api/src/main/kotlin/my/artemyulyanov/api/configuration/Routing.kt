package my.artemyulyanov.api.configuration

import dev.d1s.exkt.ktor.server.koin.configuration.ApplicationConfigurer
import dev.d1s.exkt.ktor.server.koin.configuration.builtin.configureRoutes
import io.ktor.server.application.*
import io.ktor.server.config.*
import my.artemyulyanov.api.routes.*
import org.koin.core.module.Module

object Routing : ApplicationConfigurer {
    override fun Application.configure(module: Module, config: ApplicationConfig) {
        module.configureRoutes {
            +GetPortfolioItemByIdRoute()
            +GetPortfolioItemsByScopeRoute()
            +GetAllPortfolioItemsRoute()

            +PostPortfolioItemRoute()

            +PutPortfolioItemRoute()

            +DeletePortfolioItemRoute()
        }
    }
}