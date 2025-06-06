package my.artemyulyanov.api.converters

import dev.d1s.exkt.dto.DtoConverter
import my.artemyulyanov.api.entities.PortfolioItemEntity
import my.artemyulyanov.common.PortfolioItem
import org.koin.core.component.KoinComponent

class PortfolioItemDtoConverter : DtoConverter<PortfolioItemEntity, PortfolioItem>, KoinComponent {
    override suspend fun convertToDto(entity: PortfolioItemEntity): PortfolioItem =
        with(entity) {
            PortfolioItem(
                id,
                scope,
                title,
                description,
                text,
                startDate.toString(),
                endDate.toString(),
                createdAt.toString(),
                updatedAt.toString()
            )
        }
}