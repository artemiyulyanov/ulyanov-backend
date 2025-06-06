package my.artemyulyanov.api.configuration

import dev.d1s.exkt.ktor.server.koin.configuration.ApplicationConfigurer
import io.ktor.server.application.*
import io.ktor.server.config.*
import my.artemyulyanov.api.database.*
import org.koin.core.component.KoinComponent
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf

object Repositories : ApplicationConfigurer {
    override fun Application.configure(module: Module, config: ApplicationConfig) {
        module.apply {
            singleOf<PortfolioItemRepository>(::DefaultPortfolioItemRepository)
            singleOf<UserRepository>(::DefaultUserRepository)
        }
    }
}