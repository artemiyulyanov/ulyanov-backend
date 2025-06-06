package my.artemyulyanov.api.routes

import dev.d1s.exkt.dto.requiredDto
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

class GetPortfolioItemByIdRoute : Route, KoinComponent {
    override val qualifier = named("get-portfolio-item-by-id-route")

    private val portfolioItemService by inject<PortfolioItemService>()

    override fun Routing.apply() {
        get(Paths.GET_PORTFOLIO_ITEM_BY_ID) {
            val id = call.requiredIdParameter

            val item = portfolioItemService.getById(id, requireDto = true).getOrThrow().requiredDto

            call.respond(item)
        }
    }
}