package my.artemyulyanov.api.database

import dev.d1s.exkt.ktorm.ExportedSequence
import dev.d1s.exkt.ktorm.ModificationTimestampAwareEntities
import my.artemyulyanov.api.entities.PortfolioItemEntity
import my.artemyulyanov.api.util.generateId
import my.artemyulyanov.api.util.withIoCatching
import my.artemyulyanov.common.Identifier
import my.artemyulyanov.common.User
import org.ktorm.support.postgresql.contains
import org.koin.core.component.KoinComponent
import org.ktorm.dsl.eq
import org.ktorm.dsl.like
import org.ktorm.entity.*
import org.ktorm.support.postgresql.ilike

interface PortfolioItemRepository : AbstractRepository<PortfolioItemEntity> {
    suspend fun findAll(): Result<List<PortfolioItemEntity>>
    suspend fun findById(id: Identifier): Result<PortfolioItemEntity>
    suspend fun findByScope(scope: Identifier): Result<List<PortfolioItemEntity>>

    suspend fun addPortfolioItem(item: PortfolioItemEntity): Result<PortfolioItemEntity>

    suspend fun updatePortfolioItem(item: PortfolioItemEntity): Result<PortfolioItemEntity>
    suspend fun deletePortfolioItem(item: PortfolioItemEntity): Result<Unit>
}

class DefaultPortfolioItemRepository : PortfolioItemRepository, KoinComponent, DatabaseAwareRepository() {
    override val table = PortfolioItems

    override suspend fun findAll(): Result<List<PortfolioItemEntity>> =
        withIoCatching {
            database.portfolioItems.toList()
        }

    override suspend fun findById(id: Identifier): Result<PortfolioItemEntity> =
        withIoCatching {
            database.portfolioItems.find {
                it.id eq id
            } ?: error("Portfolio item is not found")
        }

    override suspend fun findByScope(scope: Identifier): Result<List<PortfolioItemEntity>> =
        withIoCatching {
            database.portfolioItems.filter {
                it.scope ilike "%$scope%"
            }.toList()
        }

    override suspend fun addPortfolioItem(item: PortfolioItemEntity): Result<PortfolioItemEntity> =
        withIoCatching {
            item.apply {
                id = generateId()
                setCreatedAt()
                setUpdatedAt()

                database.portfolioItems.add(entity = item)
            }
        }

    override suspend fun updatePortfolioItem(item: PortfolioItemEntity): Result<PortfolioItemEntity> =
        withIoCatching {
            item.apply {
                setUpdatedAt()
                flushChanges()
            }
        }

    override suspend fun deletePortfolioItem(item: PortfolioItemEntity): Result<Unit> =
        withIoCatching {
            item.delete()
            Unit
        }
}