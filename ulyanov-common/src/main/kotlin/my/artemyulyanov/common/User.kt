package my.artemyulyanov.common

import kotlinx.serialization.Serializable

interface AbstractUser {
    val username: String
    val firstName: String
    val lastName: String
    val role: UserRole
}

interface AbstractUserPassword {
    val password: String
}

@Serializable
data class User(
    override val id: Identifier,
    override val username: String,
    override val firstName: String,
    override val lastName: String,
    override val role: UserRole,
    override val createdAt: Timestamp,
    override val updatedAt: Timestamp
): Identified, AbstractUser, TimestampAwareEntity

@Serializable
data class UserModification(
    override val username: String,
    override val password: String,
    override val firstName: String,
    override val lastName: String,
    override val role: UserRole
): AbstractUser, AbstractUserPassword

enum class UserRole {
    DEFAULT, ADMINISTRATOR
}

typealias UserCreation = UserModification