package my.artemyulyanov.api.routes

import dev.d1s.exkt.dto.requiredDto
import dev.d1s.exkt.dto.requiredDtoList
import dev.d1s.exkt.ktor.server.koin.configuration.Route
import io.ktor.server.response.*
import io.ktor.server.routing.*
import my.artemyulyanov.api.services.PortfolioItemService
import my.artemyulyanov.api.util.Paths
import my.artemyulyanov.api.util.requiredScopeParameter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named

class GetPortfolioItemsByScopeRoute : Route, KoinComponent {
    override val qualifier = named("get-portfolio-items-by-scope-qualifier")

    private val portfolioItemService by inject<PortfolioItemService>()

    override fun Routing.apply() {
        get(Paths.GET_PORTFOLIO_ITEMS_BY_SCOPE) {
            val scope = call.requiredScopeParameter

            val items = portfolioItemService.getByScope(scope, requireDto = true).getOrThrow().requiredDto

            call.respond(items)
        }
    }
}