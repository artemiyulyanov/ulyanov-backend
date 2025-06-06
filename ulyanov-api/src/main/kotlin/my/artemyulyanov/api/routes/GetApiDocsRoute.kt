package my.artemyulyanov.api.routes

import dev.d1s.exkt.ktor.server.koin.configuration.Route
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import my.artemyulyanov.api.util.Paths
import org.koin.core.component.KoinComponent
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named

class GetApiDocsRoute : Route, KoinComponent {
    override val qualifier = named("get-api-docs-route")

    override fun Routing.apply() {
        if (application.developmentMode) {
            swaggerUI(Paths.GET_API_DOCS, swaggerFile = SWAGGER_FILE)
        }
    }

    private companion object {
        private const val SWAGGER_FILE = "openapi/documentation.yaml"
    }
}