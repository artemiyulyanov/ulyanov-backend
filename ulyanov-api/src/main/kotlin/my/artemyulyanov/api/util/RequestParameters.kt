package my.artemyulyanov.api.util

import io.ktor.server.application.*
import io.ktor.server.plugins.*

val ApplicationCall.requiredIdParameter
    get() = requiredParameter(Paths.ID_PARAMETER)

val ApplicationCall.requiredScopeParameter
    get() = requiredParameter(Paths.SCOPE_PARAMETER)

private fun ApplicationCall.requiredParameter(parameter: String) =
    requiredValue(parameters[parameter], message = "Parameter '$parameter' not found")

private fun ApplicationCall.requiredQueryParameter(parameter: String) =
    requiredValue(request.queryParameters[parameter], message = "Query parameter '$parameter' not found")

private fun requiredValue(value: String?, message: String) =
    value ?: throw BadRequestException(message)