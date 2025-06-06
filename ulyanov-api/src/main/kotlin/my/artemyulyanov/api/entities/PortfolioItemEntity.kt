package my.artemyulyanov.api.entities

import dev.d1s.exkt.ktorm.ModificationTimestampAware
import my.artemyulyanov.common.Identifier
import my.artemyulyanov.common.Timestamp
import org.ktorm.entity.Entity
import java.time.Instant

interface PortfolioItemEntity : ModificationTimestampAware<PortfolioItemEntity> {
    var id: Identifier
    var scope: Identifier
    var description: String
    var text: String
    var startDate: Instant
    var endDate: Instant

    companion object : Entity.Factory<PortfolioItemEntity>()
}

val PortfolioItemEntity.asString
    get() = "PortfolioItemEntity{id = $id, scope = $scope}"