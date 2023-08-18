package org.kirinhorse.kbt

object KBTHelper {

    enum class SubBetweenType {
        TwoSide, StartSide, EndSide, CenterSide
    }

    fun String.subBetween(start: Char?, end: Char?, side: SubBetweenType = SubBetweenType.TwoSide): String? {
        val startIndex = if (start == null) 0
        else if (side == SubBetweenType.TwoSide || side == SubBetweenType.StartSide) indexOf(start)
        else lastIndexOf(start)
        val endIndex = if (end == null) length
        else if (side == SubBetweenType.StartSide || side == SubBetweenType.CenterSide) indexOf(end)
        else lastIndexOf(end)
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