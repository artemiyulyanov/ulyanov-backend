package my.artemyulyanov.common

import kotlinx.serialization.Serializable

@Serializable
data class ComplexTextSegment(
    val type: ComplexTextSegmentType,
    val text: String
)

enum class ComplexTextSegmentType {
    DEFAULT, BOLD, ITALIC, HEADING_1, HEADING_2, HEADING_3, IMAGE
}

typealias ComplexText = List<ComplexTextSegment>