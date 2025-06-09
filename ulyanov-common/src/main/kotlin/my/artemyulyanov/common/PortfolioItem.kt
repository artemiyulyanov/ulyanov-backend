package my.artemyulyanov.common

import kotlinx.serialization.Serializable

interface AbstractPortfolioItem {
    val title: String
    val description: String
    val text: ComplexText
    val icon: String?
    val startDate: Timestamp
    val endDate: Timestamp?
}

@Serializable
data class PortfolioItem(
    override val id: Identifier,
    val slug: Identifier,
    override val title: String,
    override val description: String,
    override val text: ComplexText,
    override val icon: String?,
    override val startDate: Timestamp,
    override val endDate: Timestamp? = "Now",
    override val createdAt: Timestamp,
    override val updatedAt: Timestamp
): Identified, AbstractPortfolioItem, TimestampAwareEntity

@Serializable
data class PortfolioItemModification(
    override val title: String,
    override val description: String,
    override val text: ComplexText,
    override val icon: String?,
    override val startDate: Timestamp,
    override val endDate: Timestamp? = "Now"
): AbstractPortfolioItem

typealias PortfolioItemCreation = PortfolioItemModification