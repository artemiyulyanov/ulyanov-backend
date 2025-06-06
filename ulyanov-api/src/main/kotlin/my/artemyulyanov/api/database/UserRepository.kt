package my.artemyulyanov.api.database

import dev.d1s.exkt.ktorm.ModificationTimestampAwareEntities
import my.artemyulyanov.api.entities.PortfolioItemEntity
import my.artemyulyanov.api.entities.UserEntity
import my.artemyulyanov.api.util.withIoCatching
import my.artemyulyanov.common.Identifier
import org.koin.core.component.KoinComponent
import org.ktorm.dsl.eq
import org.ktorm.dsl.like
import org.ktorm.entity.filter
import org.ktorm.entity.find
import org.ktorm.entity.toList

interface UserRepository : AbstractRepository<UserEntity> {
    suspend fun findById(id: Identifier): Result<UserEntity>
    suspend fun findByUsername(username: String): Result<UserEntity>
    suspend fun findByFirstName(firstName: String): Result<List<UserEntity>>
    suspend fun findByLastName(lastName: String): Result<List<UserEntity>>

    suspend fun updateUser(user: UserEntity): Result<UserEntity>
    suspend fun deleteUser(user: UserEntity): Result<Unit>
}

class DefaultUserRepository : UserRepository, KoinComponent, DatabaseAwareRepository() {
    override val table = Users

    override suspend fun findById(id: Identifier): Result<UserEntity> =
        withIoCatching {
            database.users.find {
                it.id eq id
            } ?: error("User is not found!")
        }

    override suspend fun findByUsername(username: String): Result<UserEntity> =
        withIoCatching {
            database.users.find {
                it.username eq username
            } ?: error("User is not found!")
        }

    override suspend fun findByFirstName(firstName: String): Result<List<UserEntity>> =
        withIoCatching {
            database.users.filter {
                it.firstName like "%$firstName%"
            }.toList()
        }

    override suspend fun findByLastName(lastName: String): Result<List<UserEntity>> =
        withIoCatching {
            database.users.filter {
                it.lastName like "%$lastName%"
            }.toList()
        }

    override suspend fun updateUser(user: UserEntity): Result<UserEntity> =
        withIoCatching {
            user.apply {
                setUpdatedAt()
                flushChanges()
            }
        }

    override suspend fun deleteUser(user: UserEntity): Result<Unit> =
        withIoCatching {
            user.delete()
            Unit
        }
}