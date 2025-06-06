/*
 * Copyright 2024-2025 MX.Store Team
 */

package my.artemyulyanov.api.services

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import dev.d1s.exkt.dto.entity
import my.artemyulyanov.api.configuration.Security
import my.artemyulyanov.api.entities.UserEntity
import my.artemyulyanov.api.util.ForbiddenException
import my.artemyulyanov.api.util.NotAuthenticatedException
import my.artemyulyanov.common.AuthRequest
import my.artemyulyanov.common.AuthResponse
import my.artemyulyanov.common.Identifier
import my.artemyulyanov.common.UserRole
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.lighthousegames.logging.logging

interface AuthService {

    suspend fun authenticate(request: AuthRequest): Result<AuthResponse>

    suspend fun encrypt(password: String): String

    suspend fun requireRole(subject: Identifier?, role: UserRole)

    suspend fun isAllowed(subject: Identifier?, role: UserRole): Boolean
}

class DefaultAuthService : AuthService, KoinComponent {

    private val userService by inject<UserService>()

    private val log = logging()

    override suspend fun authenticate(request: AuthRequest): Result<AuthResponse> =
        runCatching {
            val user = userService.getUser(request.username).getOrElse {
                log.d {
                    "Authentication is failed: user is not found. Request: $request"
                }

                throw NotAuthenticatedException()
            }.entity

            if (!user.password.passwordMatches(request.password)) {
                log.d {
                    "Authentication is failed: passwords don't match. Request: $request; Password hash: ${user.password}"
                }

                throw NotAuthenticatedException()
            }

            val token = issue(user)

            log.i {
                "User ${user.id} authenticated with username '${request.username}'"
            }

            AuthResponse(user.id, token)
        }

    override suspend fun encrypt(password: String): String =
        BCrypt.withDefaults().hashToString(DEFAULT_BCRYPT_COST, password.toCharArray())

    override suspend fun requireRole(subject: Identifier?, role: UserRole) {
        if (!isAllowed(subject, role)) {
            throw ForbiddenException("Недостаточно прав для выполнения операции")
        }
    }

    override suspend fun isAllowed(subject: Identifier?, role: UserRole): Boolean =
        subject?.let {
            val user = userService.getUser(it).getOrNull()?.entity

            log.d {
                "Required role: $role; got user: $user"
            }

            when {
                user == null || user.role < role
                        || ((user.role == UserRole.DEFAULT)) -> false

                else -> true
            }
        } ?: true

    private fun String.passwordMatches(password: String) =
        BCrypt.verifyer().verify(password.toCharArray(), this).let {
            log.d {
                "bcrypt verified: ${it.verified}; details: ${it.formatErrorMessage}"
            }

            it.verified
        }

    private fun issue(user: UserEntity): String {
        val subject = user.id

        return JWT.create()
            .withSubject(subject)
            .sign(Security.jwtAlgorithm)
    }

    private companion object {
        private const val DEFAULT_BCRYPT_COST = 12
    }
}