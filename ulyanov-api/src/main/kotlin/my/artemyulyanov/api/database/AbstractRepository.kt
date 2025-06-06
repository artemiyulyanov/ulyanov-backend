/*
 * Copyright 2024-2025 MX.Store Team
 */

package my.artemyulyanov.api.database

import dev.d1s.exkt.ktorm.ModificationTimestampAware
import dev.d1s.exkt.ktorm.ModificationTimestampAwareEntities
import my.artemyulyanov.api.util.withIoCatching
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext
import org.ktorm.dsl.*
import org.ktorm.schema.ColumnDeclaring

interface AbstractRepository<E : ModificationTimestampAware<E>> {
    val table: ModificationTimestampAwareEntities<E>
}

abstract class DatabaseAwareRepository : KoinComponent {
    private val connector by inject<DatabaseConnector>()

    protected val database
        get() = connector.database
}