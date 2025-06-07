package my.artemyulyanov.api.converters

import dev.d1s.exkt.dto.DtoConverter
import my.artemyulyanov.api.entities.PortfolioItemEntity
import my.artemyulyanov.api.util.convertToSlug
import my.artemyulyanov.common.PortfolioItem
import my.artemyulyanov.common.PortfolioItemCreation
import my.artemyulyanov.common.PortfolioItemModification
import my.artemyulyanov.common.util.tryParseInstant
import org.koin.core.component.KoinComponent
import java.time.Instant

class PortfolioItemModificationDtoConverter : DtoConverter<PortfolioItemEntity, PortfolioItemModification>, KoinComponent {
    override suspend fun convertToEntity(dto: PortfolioItemModification): PortfolioItemEntity =
        PortfolioItemEntity {
            slug = dto.title.convertToSlug()
            title = dto.title
            description = dto.description
            text = dto.text
            startDate = dto.startDate.tryParseInstant() ?: error("Unable to parse portfolio item!")
            endDate = dto.endDate.tryParseInstant()
        }
}