package my.artemyulyanov.api.database

import dev.d1s.exkt.ktorm.ModificationTimestampAwareEntities
import my.artemyulyanov.api.entities.UserEntity
import org.ktorm.schema.text

object Users : ModificationTimestampAwareEntities<UserEntity>(tableName = "users") {
    val id = text("id").primaryKey().bindTo {
        it.id
    }

    val username = text("username").bindTo {
        it.username
    }

    val password = text("password").bindTo {
        it.password
    }

    val firstName = text("first_name").bindTo {
        it.firstName
    }

    val lastName = text("last_name").bindTo {
        it.lastName
    }
}