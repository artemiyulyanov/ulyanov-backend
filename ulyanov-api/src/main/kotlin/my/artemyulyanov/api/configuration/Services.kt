package my.artemyulyanov.api.configuration

import dev.d1s.exkt.ktor.server.koin.configuration.ApplicationConfigurer
import io.ktor.server.application.*
import io.ktor.server.config.*
import my.artemyulyanov.api.services.*
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf

object Services : ApplicationConfigurer {
    override fun Application.configure(module: Module, config: ApplicationConfig) {
        module.apply {
            singleOf<UserService>(::DefaultUserService)
            singleOf<PortfolioItemService>(::DefaultPortfolioItemService)
            singleOf<AuthService>(::DefaultAuthService)
        }
    }
}