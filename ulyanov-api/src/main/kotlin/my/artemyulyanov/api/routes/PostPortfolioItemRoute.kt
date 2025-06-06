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
import my.artemyulyanov.common.PortfolioItemCreation
import my.artemyulyanov.common.PortfolioItemModification
import my.artemyulyanov.common.UserRole
import my.artemyulyanov.common.validation.validatePortfolioItem
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named

class PostPortfolioItemRoute : Route, KoinComponent {
    override val qualifier = named("post-portfolio-item-route")

    private val portfolioItemService by inject<PortfolioItemService>()

    private val portfolioItemCreationDtoConverter by inject<DtoConverter<PortfolioItemEntity, PortfolioItemCreation>>(DtoConverters.PortfolioItemCreationDtoConverterQualifier)

    override fun Routing.apply() =
        authenticateAll {
            post(Paths.POST_PORTFOLIO_ITEM) {
                requireRole(UserRole.ADMINISTRATOR)

                val body = call.receive<PortfolioItemCreation>()
                validatePortfolioItem(body.startDate, body.endDate).validate(body).orThrow()

                val item = portfolioItemCreationDtoConverter.convertToEntity(body)

                val createdItem = portfolioItemService.createPortfolioItem(item, requireDto = true).getOrThrow().requiredDto

                call.respond(HttpStatusCode.Created, createdItem)
            }
        }
}