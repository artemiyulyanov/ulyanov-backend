package my.artemyulyanov.api.entities

import dev.d1s.exkt.ktorm.ModificationTimestampAware
import my.artemyulyanov.common.ComplexText
import my.artemyulyanov.common.Identifier
import my.artemyulyanov.common.Timestamp
import org.ktorm.entity.Entity
import java.time.Instant

interface PortfolioItemEntity : ModificationTimestampAware<PortfolioItemEntity> {
    var id: Identifier
    var slug: Identifier
    var title: String
    var description: String
    var text: ComplexText
    var icon: String?
    var startDate: Instant
    var endDate: Instant?

    companion object : Entity.Factory<PortfolioItemEntity>()
}

val PortfolioItemEntity.asString
    get() = "PortfolioItemEntity{id = $id}"