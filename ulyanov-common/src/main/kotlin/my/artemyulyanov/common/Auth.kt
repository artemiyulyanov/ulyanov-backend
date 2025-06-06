/*
 * Copyright 2024-2025 MX.Store Team
 */

package my.artemyulyanov.common

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val username: Identifier,
    val password: String
)

@Serializable
data class AuthResponse(
    val user: Identifier,
    val token: String
)