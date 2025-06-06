package my.artemyulyanov.common

import kotlinx.serialization.Serializable

interface AbstractPortfolioItem {
    val scope: Identifier
    val description: String
    val text: String
}

@Serializable
data class PortfolioItem(
    override val id: Identifier,
    override val scope: Identifier,
    override val description: String,
    override val text: String,
    val startDate: Timestamp,
    val endDate: Timestamp,
    override val createdAt: Timestamp,
    override val updatedAt: Timestamp
): Identified, AbstractPortfolioItem, TimestampAwareEntity

@Serializable
data class PortfolioItemModification(
    override val scope: Identifier,
    override val description: String,
    override val text: String,
    val startDate: Timestamp,
    val endDate: Timestamp
): AbstractPortfolioItem

typealias PortfolioItemCreation = PortfolioItemModification