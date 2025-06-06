package my.artemyulyanov.api.database

import dev.d1s.exkt.ktorm.ModificationTimestampAwareEntities
import my.artemyulyanov.api.entities.PortfolioItemEntity
import org.ktorm.schema.text
import org.ktorm.schema.timestamp

object PortfolioItems : ModificationTimestampAwareEntities<PortfolioItemEntity>(tableName = "portfolio_items") {
    val id = text("id").primaryKey().bindTo {
        it.id
    }

    val scope = text("scope").bindTo {
        it.scope
    }

    val title = text("title").bindTo {
        it.title
    }

    val description = text("description").bindTo {
        it.description
    }

    val text = text("text").bindTo {
        it.text
    }

    val startDate = timestamp("start_date").bindTo {
        it.startDate
    }

    val endDate = timestamp("end_date").bindTo {
        it.endDate
    }
}