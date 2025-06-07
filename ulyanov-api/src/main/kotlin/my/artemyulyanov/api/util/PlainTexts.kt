package my.artemyulyanov.api.util

fun String.convertToSlug(): String =
    this
        .lowercase()
        .replace(Regex("[^a-z0-9\\s-]"), "")
        .replace(Regex("\\s+"), "-")
        .replace(Regex("-+"), "-")
        .trim('-')