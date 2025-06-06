package my.artemyulyanov.api.converters

import dev.d1s.exkt.dto.DtoConverter
import my.artemyulyanov.api.entities.UserEntity
import my.artemyulyanov.common.User
import org.koin.core.component.KoinComponent

class UserDtoConverter : DtoConverter<UserEntity, User>, KoinComponent {
    override suspend fun convertToDto(entity: UserEntity): User =
        with(entity) {
            User(
                id,
                username,
                firstName,
                lastName,
                role,
                createdAt.toString(),
                updatedAt.toString()
            )
        }
}