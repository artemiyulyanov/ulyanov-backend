package my.artemyulyanov.api.converters

import dev.d1s.exkt.dto.DtoConverter
import my.artemyulyanov.api.entities.PortfolioItemEntity
import my.artemyulyanov.api.util.convertToSlug
import my.artemyulyanov.common.PortfolioItem
import my.artemyulyanov.common.util.parseTimestamp
import org.koin.core.component.KoinComponent

class PortfolioItemDtoConverter : DtoConverter<PortfolioItemEntity, PortfolioItem>, KoinComponent {
    override suspend fun convertToDto(entity: PortfolioItemEntity): PortfolioItem =
        with(entity) {
            PortfolioItem(
                id,
                title.convertToSlug(),
                title,
                description,
                text,
                icon,
                startDate.parseTimestamp(),
                endDate.parseTimestamp(),
                createdAt.toString(),
                updatedAt.toString()
            )
        }
}