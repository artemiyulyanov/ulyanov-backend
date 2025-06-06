package my.artemyulyanov.api.util

import dev.d1s.exkt.ktor.server.statuspages.HttpStatusException
import io.ktor.http.*

open class NotFoundException(message: String = "Resource is not found") :
    HttpStatusException(HttpStatusCode.NotFound, message)

open class UnauthorizedException(message: String = "Unauthorized") :
    HttpStatusException(HttpStatusCode.Unauthorized, message)

open class ForbiddenException(message: String = "Forbidden") :
    HttpStatusException(HttpStatusCode.Forbidden, message)

open class ConflictException(message: String = "Conflict") :
    HttpStatusException(HttpStatusCode.Conflict, message)

open class UnprocessableEntityException(message: String = "Unprocessable entity") :
    HttpStatusException(HttpStatusCode.UnprocessableEntity, message)

class UserRestrictedException : ForbiddenException("User is restricted")

class NotAuthenticatedException : UnauthorizedException("Authentication is failed")