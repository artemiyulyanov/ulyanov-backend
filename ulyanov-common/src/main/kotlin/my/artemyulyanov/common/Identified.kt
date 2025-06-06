package my.artemyulyanov.common

interface Identified {
    val id: Identifier
}

fun <T: Identified> Iterable<T>.find(id: Identifier): T? =
    find {
        it.id == id
    }

fun <T: Identified> Iterable<T>.get(id: Identifier): T =
    find(id) ?: error("Item is not found")