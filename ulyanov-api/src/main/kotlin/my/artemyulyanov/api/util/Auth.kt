package my.artemyulyanov.api.util

import io.ktor.server.routing.*
import my.artemyulyanov.api.configuration.subject
import my.artemyulyanov.api.services.AuthService
import my.artemyulyanov.common.UserRole
import org.koin.core.context.GlobalContext

private val authService by lazy {
    GlobalContext.get().get<AuthService>()
}

suspend fun RoutingContext.requireRole(role: UserRole) {
    val subject = call.subject
    authService.requireRole(subject, role)
}