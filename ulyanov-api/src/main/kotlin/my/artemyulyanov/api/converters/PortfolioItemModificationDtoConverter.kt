package my.artemyulyanov.api.converters

import dev.d1s.exkt.dto.DtoConverter
import my.artemyulyanov.api.entities.PortfolioItemEntity
import my.artemyulyanov.common.PortfolioItem
import my.artemyulyanov.common.PortfolioItemCreation
import my.artemyulyanov.common.PortfolioItemModification
import org.koin.core.component.KoinComponent
import java.time.Instant

class PortfolioItemModificationDtoConverter : DtoConverter<PortfolioItemEntity, PortfolioItemModification>, KoinComponent {
    override suspend fun convertToEntity(dto: PortfolioItemModification): PortfolioItemEntity =
        PortfolioItemEntity {
            scope = dto.scope
            description = dto.description
            text = dto.text
            startDate = Instant.parse(dto.startDate)
            endDate = Instant.parse(dto.endDate)
        }
}