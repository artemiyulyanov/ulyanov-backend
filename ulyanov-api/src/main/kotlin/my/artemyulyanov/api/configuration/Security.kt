/*
 * Copyright 2024-2025 MX.Store Team
 */

package my.artemyulyanov.api.configuration

import com.auth0.jwt.algorithms.Algorithm
import dev.d1s.exkt.ktor.server.koin.configuration.ApplicationConfigurer
import dev.d1s.ktor.staticauth.staticToken
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.routing.*
import my.artemyulyanov.api.services.UserService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.module.Module
import org.lighthousegames.logging.logging
import com.auth0.jwt.JWT as JWTAuth

object Security : ApplicationConfigurer, KoinComponent {

    const val JWT = "jwt-auth"
    const val STATIC = "static-auth"

    val jwtAlgorithm get() = _jwtAlgorithm ?: error("Algorithm is not available")

    private var _jwtAlgorithm: Algorithm? = null

    private val userService by inject<UserService>()

    private val log = logging()

    override fun Application.configure(module: Module, config: ApplicationConfig) {
        authentication {
            staticToken(name = STATIC) {
                token = config.staticAuthToken
                realm = config.staticAuthRealm
            }

            jwt(name = JWT) {
                _jwtAlgorithm = Algorithm.HMAC256(config.jwtSecret)

                verify()
                validate()

                realm = config.jwtRealm
            }
        }
    }

    private fun JWTAuthenticationProvider.Config.verify() {
        val jwtVerifier = JWTAuth.require(jwtAlgorithm).build()
        verifier(jwtVerifier)
    }

    private fun JWTAuthenticationProvider.Config.validate() {
        validate { credential ->
            val payload = credential.payload
            val subject = payload.subject

            log.d {
                "Got JWT payload: $subject"
            }

            if (
                subject != null
                && subject.isNotBlank()
                && userService.getUser(subject).isSuccess
            ) {
                log.d {
                    "Produced JWT principal; subject: ${payload.subject}"
                }

                JWTPrincipal(payload)
            } else {
                null
            }
        }
    }
}

val ApplicationCall.subject
    get() = principal<JWTPrincipal>()?.payload?.subject

val ApplicationConfig.staticAuthToken
    get() = property("security.static-auth.token").getString()
val ApplicationConfig.staticAuthRealm
    get() = property("security.static-auth.realm").getString()

val ApplicationConfig.jwtSecret
    get() = property("security.jwt-auth.secret").getString()
val ApplicationConfig.jwtRealm
    get() = property("security.jwt-auth.realm").getString()

fun Route.authenticateAll(build: Route.() -> Unit) {
    authenticate(Security.STATIC, Security.JWT, strategy = AuthenticationStrategy.FirstSuccessful, build = build)
}