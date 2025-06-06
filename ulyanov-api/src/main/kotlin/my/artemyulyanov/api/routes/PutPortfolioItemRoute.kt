package my.artemyulyanov.api.routes

import dev.d1s.exkt.dto.DtoConverter
import dev.d1s.exkt.dto.requiredDto
import dev.d1s.exkt.ktor.server.koin.configuration.Route
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import my.artemyulyanov.api.configuration.DtoConverters
import my.artemyulyanov.api.configuration.authenticateAll
import my.artemyulyanov.api.entities.PortfolioItemEntity
import my.artemyulyanov.api.services.PortfolioItemService
import my.artemyulyanov.api.util.Paths
import my.artemyulyanov.api.util.orThrow
import my.artemyulyanov.api.util.requireRole
import my.artemyulyanov.api.util.requiredIdParameter
import my.artemyulyanov.common.PortfolioItemCreation
import my.artemyulyanov.common.PortfolioItemModification
import my.artemyulyanov.common.UserRole
import my.artemyulyanov.common.validation.validatePortfolioItem
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named

class PutPortfolioItemRoute : Route, KoinComponent {
    override val qualifier = named("put-portfolio-item-route")

    private val portfolioItemService by inject<PortfolioItemService>()

    private val portfolioItemModificationDtoConverter by inject<DtoConverter<PortfolioItemEntity, PortfolioItemModification>>(DtoConverters.PortfolioItemModificationDtoConverterQualifier)

    override fun Routing.apply() =
        authenticateAll {
            put(Paths.PUT_PORTFOLIO_ITEM) {
                requireRole(UserRole.ADMINISTRATOR)

                val id = call.requiredIdParameter

                val body = call.receive<PortfolioItemModification>()
                validatePortfolioItem(body.startDate, body.endDate).validate(body).orThrow()

                val item = portfolioItemModificationDtoConverter.convertToEntity(body)

                val updatedItem = portfolioItemService.updatePortfolioItem(id, item, requireDto = true).getOrThrow().requiredDto

                call.respond(HttpStatusCode.OK, updatedItem)
            }
        }
}