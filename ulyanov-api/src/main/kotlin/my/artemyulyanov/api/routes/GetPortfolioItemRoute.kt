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
import org.koin.core.qualifier.named

class GetPortfolioItemRoute : Route, KoinComponent {
    override val qualifier = named("get-portfolio-item-route")

    private val portfolioItemService by inject<PortfolioItemService>()

    override fun Routing.apply() {
        get(Paths.GET_PORTFOLIO_ITEM) {
            val id = call.requiredIdParameter

            val item = portfolioItemService.getPortfolioItem(id, requireDto = true).getOrThrow().requiredDto

            call.respond(item)
        }
    }
}