package my.artemyulyanov.common

import kotlinx.serialization.Serializable

interface AbstractPortfolioItem {
    val title: String
    val description: String
    val text: String
}

@Serializable
data class PortfolioItem(
    override val id: Identifier,
    override val title: String,
    override val description: String,
    override val text: String,
    val startDate: Timestamp,
    val endDate: Timestamp,
    override val createdAt: Timestamp,
    override val updatedAt: Timestamp
): Identified, AbstractPortfolioItem, TimestampAwareEntity

@Serializable
data class PortfolioItemModification(
    override val title: String,
    override val description: String,
    override val text: String,
    val startDate: Timestamp,
    val endDate: Timestamp
): AbstractPortfolioItem

typealias PortfolioItemCreation = PortfolioItemModification