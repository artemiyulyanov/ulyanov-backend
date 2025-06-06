package my.artemyulyanov.api.routes

import dev.d1s.exkt.dto.requiredDto
import dev.d1s.exkt.dto.requiredDtoList
import dev.d1s.exkt.ktor.server.koin.configuration.Route
import io.ktor.server.response.*
import io.ktor.server.routing.*
import my.artemyulyanov.api.services.PortfolioItemService
import my.artemyulyanov.api.util.Paths
import my.artemyulyanov.api.util.requiredIdParameter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named

class GetAllPortfolioItemsRoute : Route, KoinComponent {
    override val qualifier: Qualifier = named("get-all-portfolio-items-route")

    private val portfolioItemService by inject<PortfolioItemService>()

    override fun Routing.apply() {
        get(Paths.GET_ALL_PORTFOLIO_ITEMS) {
            val items = portfolioItemService.getAll(requireDto = true).getOrThrow().requiredDtoList

            call.respond(items)
        }
    }
}