package org.kirinhorse.kbt

object KBTHelper {
    fun String.subBetween(start: Char, end: Char): String? {
        val startIndex = indexOf(start)
        val endIndex = lastIndexOf(end)
        if (startIndex < 0 || endIndex <= startIndex) return null
        return substring(startIndex + 1, endIndex)
    }

    fun String.subBetween(start: String, end: String): String? {
        val startIndex = indexOf(start)
        val endIndex = lastIndexOf(end)
        if (startIndex < 0 || endIndex <= startIndex) return null
        return substring(startIndex + start.length, endIndex)
    }
}