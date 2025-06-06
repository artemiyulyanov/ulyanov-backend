package my.artemyulyanov.api.services

import dev.d1s.exkt.dto.*
import my.artemyulyanov.api.configuration.DtoConverters
import my.artemyulyanov.api.database.PortfolioItemRepository
import my.artemyulyanov.api.entities.PortfolioItemEntity
import my.artemyulyanov.api.entities.asString
import my.artemyulyanov.api.util.NotFoundException
import my.artemyulyanov.common.Identifier
import my.artemyulyanov.common.PortfolioItem
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.lighthousegames.logging.logging

interface PortfolioItemService {
    suspend fun getAll(requireDto: Boolean = false): ResultingEntityWithOptionalDtoList<PortfolioItemEntity, PortfolioItem>
    suspend fun getById(id: Identifier, requireDto: Boolean = false): ResultingEntityWithOptionalDto<PortfolioItemEntity, PortfolioItem>
    suspend fun getByScope(scope: Identifier, requireDto: Boolean = false): ResultingEntityWithOptionalDto<PortfolioItemEntity, PortfolioItem>

    suspend fun createPortfolioItem(item: PortfolioItemEntity, requireDto: Boolean = false): ResultingEntityWithOptionalDto<PortfolioItemEntity, PortfolioItem>

    suspend fun updatePortfolioItem(id: Identifier, item: PortfolioItemEntity, requireDto: Boolean = false): ResultingEntityWithOptionalDto<PortfolioItemEntity, PortfolioItem>
    suspend fun deletePortfolioItem(id: Identifier): Result<Unit>
}

class DefaultPortfolioItemService : PortfolioItemService, KoinComponent {
    private val portfolioItemRepository by inject<PortfolioItemRepository>()

    private val portfolioItemDtoConverter by inject<DtoConverter<PortfolioItemEntity, PortfolioItem>>(DtoConverters.PortfolioItemDtoConverterQualifier)

    private val log = logging()

    override suspend fun getAll(requireDto: Boolean): ResultingEntityWithOptionalDtoList<PortfolioItemEntity, PortfolioItem> =
        runCatching {
            val items = portfolioItemRepository.findAll().getOrElse {
                throw NotFoundException("Items are not found!")
            }

            items to portfolioItemDtoConverter.convertToDtoListIf(items) {
                requireDto
            }
        }

    override suspend fun getById(id: Identifier, requireDto: Boolean): ResultingEntityWithOptionalDto<PortfolioItemEntity, PortfolioItem> =
        runCatching {
            val item = portfolioItemRepository.findById(id).getOrElse {
                throw NotFoundException("Item is not found!")
            }

            item to portfolioItemDtoConverter.convertToDtoIf(item) {
                requireDto
            }
        }

    override suspend fun getByScope(scope: Identifier, requireDto: Boolean): ResultingEntityWithOptionalDto<PortfolioItemEntity, PortfolioItem> =
        runCatching {
            val item = portfolioItemRepository.findByScope(scope).getOrThrow()

            item to portfolioItemDtoConverter.convertToDtoIf(item) {
                requireDto
            }
        }

    override suspend fun createPortfolioItem(
        item: PortfolioItemEntity,
        requireDto: Boolean
    ): ResultingEntityWithOptionalDto<PortfolioItemEntity, PortfolioItem> =
        runCatching {
            log.i {
                "Creating portfolio item ${item.asString}..."
            }

            val item = portfolioItemRepository.addPortfolioItem(item).getOrThrow()

            item to portfolioItemDtoConverter.convertToDtoIf(item) {
                requireDto
            }
        }

    override suspend fun updatePortfolioItem(
        id: Identifier,
        item: PortfolioItemEntity,
        requireDto: Boolean
    ): ResultingEntityWithOptionalDto<PortfolioItemEntity, PortfolioItem> =
        runCatching {
            log.i {
                "Updating portfolio item ${item.asString}..."
            }

            val (originalPortfolioItem, originalPortfolioItemDto) = getById(id, requireDto = true).getOrThrow()
            requireNotNull(originalPortfolioItemDto)

            originalPortfolioItem.apply {
                scope = item.scope
                description = item.description
                text = item.text
                startDate = item.startDate
                endDate = item.endDate
            }

            val updatedPortfolioItem = portfolioItemRepository.updatePortfolioItem(originalPortfolioItem).getOrThrow()

            updatedPortfolioItem to portfolioItemDtoConverter.convertToDtoIf(updatedPortfolioItem) {
                requireDto
            }
        }

    override suspend fun deletePortfolioItem(id: Identifier): Result<Unit> =
        runCatching {
            log.i {
                "Deleting portfolio item #$id..."
            }

            val (originalPortfolioItem, originalPortfolioItemDto) = getById(id, requireDto = true).getOrElse() {
                throw NotFoundException("Item is not found!")
            }

            requireNotNull(originalPortfolioItemDto)

            portfolioItemRepository.deletePortfolioItem(originalPortfolioItem).getOrThrow()
        }
}