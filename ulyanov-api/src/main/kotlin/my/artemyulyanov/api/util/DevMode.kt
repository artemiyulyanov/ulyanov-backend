/*
 * Copyright 2024-2025 MX.Store Team
 */

package my.artemyulyanov.api.util

import io.ktor.server.config.*

val ApplicationConfig.developmentMode
    get() = property("ktor.development").getString().toBooleanStrict()