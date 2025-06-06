package my.artemyulyanov.api.entities

import dev.d1s.exkt.ktorm.ModificationTimestampAware
import my.artemyulyanov.common.Identifier
import my.artemyulyanov.common.UserRole
import org.ktorm.entity.Entity

interface UserEntity: ModificationTimestampAware<UserEntity> {
    var id: Identifier
    var username: String
    var password: String
    var firstName: String
    var lastName: String
    var role: UserRole

    companion object : Entity.Factory<UserEntity>()
}

val UserEntity.asString
    get() = "UserEntity{id = $id, username = $username}"