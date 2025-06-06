/*
 * Copyright 2024-2025 MX.Store Team
 */

package my.artemyulyanov.api.util

import dispatch.core.withIO
import kotlinx.coroutines.CoroutineScope
import my.artemyulyanov.api.database.DatabaseConnector
import org.koin.core.context.GlobalContext
import org.lighthousegames.logging.logging
import java.sql.SQLException

private val connector by lazy {
    GlobalContext.get().get<DatabaseConnector>()
}

private val log = logging()

suspend fun <R> withIoCatching(block: suspend CoroutineScope.() -> R): Result<R> =
    withIO {
        var result: R

        while (true) {
            try {
                result = block()
                break
            } catch (e: Throwable) {
                if (e is SQLException && e.message?.contains("is closed") == true) {
                    log.w {
                        "Got connection closed error. Restarting connection..."
                    }

                    connector.reconnect()

                    continue
                }

                return@withIO Result.failure(e)
            }
        }

        Result.success(result)
    }