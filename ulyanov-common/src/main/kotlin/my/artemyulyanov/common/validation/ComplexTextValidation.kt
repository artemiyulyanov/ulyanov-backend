package my.artemyulyanov.common.validation

import dev.d1s.exkt.konform.isNotBlank
import io.konform.validation.Validation
import io.konform.validation.ValidationBuilder
import io.konform.validation.onEach
import my.artemyulyanov.common.ComplexText
import my.artemyulyanov.common.ComplexTextSegment
import my.artemyulyanov.common.ComplexTextSegmentType
import my.artemyulyanov.common.util.Regex

fun ValidationBuilder<ComplexText>.validateComplexText() {
    onEach {
        validateComplexTextSegment()
    }
}

private fun ValidationBuilder<ComplexTextSegment>.validateComplexTextSegment() {
    ComplexTextSegment::text {
        isNotBlank() hint "Text of segment must be filled out!"
    }

    addConstraint("Minio link is invalid!") {
        if (it.type == ComplexTextSegmentType.IMAGE) {
            it.text.matches(Regex.ServerS3Link)
        } else {
            true
        }
    }
}