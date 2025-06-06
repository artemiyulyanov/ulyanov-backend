package my.artemyulyanov.api.util

private val randomChar = ('0'..'9')

fun generateId() = randomChar.shuffled().take(9).joinToString("")