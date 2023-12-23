package org.kirinhorse.kbt.types

import kotlin.math.roundToInt
import kotlin.math.sqrt


class KBTVector2(var x: Float, var y: Float) {
    constructor(x: Int, y: Int) : this(x.toFloat(), y.toFloat())

    companion object {
        fun fromText(text: String): KBTVector2? {
            if (!text.startsWith('(') || !text.endsWith(')')) return null
            val splits = text.substring(1, text.length - 1).split(',')
            if (splits.size != 2) return null
            val x = splits[0].trim().toFloatOrNull() ?: return null
            val y = splits[1].trim().toFloatOrNull() ?: return null
            return KBTVector2(x, y)
        }
    }

    fun offset(ox: Float, oy: Float) {
        x += ox
        y += oy
    }

    fun negate() {
        x = -x
        y = -y
    }

    fun plus(x: Float, y: Float) = KBTVector2(this.x, this.y).apply { offset(x, y) }

    operator fun plus(other: KBTVector2) = plus(other.x, other.y)

    fun minus(x: Float, y: Float) = KBTVector2(this.x, this.y).apply { offset(-x, -y) }

    operator fun minus(other: KBTVector2) = minus(other.x, other.y)

    operator fun times(scalar: Float) = KBTVector2(x * scalar, y * scalar)

    operator fun div(scalar: Float) = KBTVector2(x / scalar, y / scalar)

    operator fun unaryMinus() = KBTVector2(-x, -y)

    fun length() = sqrt(x * x + y * y)

    override fun toString(): String {
        val xs = if (x % 1f == 0f) x.roundToInt().toString() else x.toString()
        val ys = if (y % 1f == 0f) y.roundToInt().toString() else y.toString()
        return "($xs, $ys)"
    }

    override fun equals(other: Any?): Boolean {
        if (other !is KBTVector2) return false
        return x == other.x && y == other.y
    }

    override fun hashCode() = 31 * x.hashCode() + y.hashCode()
}