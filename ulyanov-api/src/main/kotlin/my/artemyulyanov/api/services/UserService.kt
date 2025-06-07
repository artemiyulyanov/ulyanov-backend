package my.artemyulyanov.api.services

import dev.d1s.exkt.dto.DtoConverter
import dev.d1s.exkt.dto.ResultingEntityWithOptionalDto
import dev.d1s.exkt.dto.convertToDtoIf
import dev.d1s.exkt.dto.convertToDtoListIf
import dev.d1s.exkt.ktor.server.postgres.handlePsqlUniqueViolationThrowingConflictStatusException
import my.artemyulyanov.api.configuration.DtoConverters
import my.artemyulyanov.api.database.UserRepository
import my.artemyulyanov.api.entities.UserEntity
import my.artemyulyanov.api.entities.asString
import my.artemyulyanov.api.util.NotFoundException
import my.artemyulyanov.api.util.withIoCatching
import my.artemyulyanov.common.Identifier
import my.artemyulyanov.common.User
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.lighthousegames.logging.logging

interface UserService {
    suspend fun getUser(constraint: Identifier, requireDto: Boolean = false): ResultingEntityWithOptionalDto<UserEntity, User>

    suspend fun updateUser(id: Identifier, user: UserEntity, requireDto: Boolean = false): ResultingEntityWithOptionalDto<UserEntity, User>
    suspend fun deleteUser(id: Identifier): Result<Unit>
}

class DefaultUserService : UserService, KoinComponent {
    private val userRepository by inject<UserRepository>()

    private val authService by inject<AuthService>()

    private val userDtoConverter by inject<DtoConverter<UserEntity, User>>(DtoConverters.UserDtoConverterQualifier)

    private val log = logging()

    override suspend fun getUser(constraint: Identifier, requireDto: Boolean): ResultingEntityWithOptionalDto<UserEntity, User> =
        runCatching {
            val user = userRepository.findById(constraint).getOrElse {
                userRepository.findByUsername(constraint).getOrElse {
                    throw NotFoundException("User is not found!")
                }
            }

            user to userDtoConverter.convertToDtoIf(user) {
                requireDto
            }
        }

    override suspend fun updateUser(id: Identifier, user: UserEntity, requireDto: Boolean): ResultingEntityWithOptionalDto<UserEntity, User> =
        runCatching {
            log.i {
                "Updating user ${user.asString}..."
            }

            val (originalUser, originalUserDto) = getUser(id, requireDto = true).getOrThrow()
            requireNotNull(originalUserDto)

            val encryptedPassword = authService.encrypt(user.password)

            originalUser.apply {
                username = user.username
                firstName = user.firstName
                lastName = user.lastName
                role = user.role
                password = encryptedPassword
            }

            val updatedUser = handleUniqueUsernameViolation {
                userRepository.updateUser(originalUser).getOrThrow()
            }

            val updatedUserDto = userDtoConverter.convertToDto(updatedUser)

            updatedUser to updatedUserDto
        }

    override suspend fun deleteUser(id: Identifier): Result<Unit> =
        runCatching {
            log.i {
                "Deleting user #$id..."
            }

            val (user, userDto) = getUser(id, requireDto = true).getOrElse() {
                throw NotFoundException("User is not found!")
            }

            requireNotNull(userDto)

            userRepository.deleteUser(user).getOrThrow()
        }

    private inline fun <R> handleUniqueUsernameViolation(block: () -> R) =
        handlePsqlUniqueViolationThrowingConflictStatusException("Пользователь уже существует") {
            block()
        }
}