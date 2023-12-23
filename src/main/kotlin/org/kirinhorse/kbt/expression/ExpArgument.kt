package org.kirinhorse.kbt.expression

import org.kirinhorse.kbt.types.KBTVector2

class ExpArgument(val exp: Expression, private val expText: String, value: Any? = null) {
    private val swc = Expression.STRING_WRAPPER_CHAR

    val isNull = value == null && expText.isBlank()

    private val boolValue = if (value is Boolean) value else expText.toBooleanStrictOrNull()
    val isBool = boolValue != null

    private val intValue = if (value is Int) value else expText.toIntOrNull()
    val isInt = intValue != null

    private val floatValue = when (value) {
        is Float -> value
        is Int -> value.toFloat()
        else -> expText.toFloatOrNull()
    }
    val isFloat = floatValue != null

    val isNumber = isInt || isFloat

    val isText = (value is String) || (expText.length > 1 && expText.startsWith(swc) && expText.endsWith(swc))
    private val textValue = when {
        value is String -> value
        isText -> expText.substring(1, expText.length - 1)
        else -> null
    }

    private val vector2Value = if (value is KBTVector2) value else KBTVector2.fromText(expText)
    val isVector2 = vector2Value != null

    val isExpression = !isNull && !isBool && !isFloat && !isText && !isVector2
    private val expressionValue = if (isExpression) expText else null

    val bool get() = boolValue!!
    val int get() = intValue!!
    val float get() = floatValue!!
    val text get() = textValue!!
    val vector2 get() = vector2Value!!
    val expression get() = expressionValue!!

    override fun toString() = when {
        isNull -> ""
        isBool -> bool.toString()
        isInt -> int.toString()
        isFloat -> float.toString()
        isVector2 -> vector2.toString()
        isText -> text
        else -> expText
    }

    override fun equals(other: Any?): Boolean {
        if (other !is ExpArgument) return false
        return when {
            isBool && other.isBool -> bool == other.bool
            isNumber && other.isNumber -> float == other.float
            isText && other.isText -> text == other.text
            isVector2 && other.isVector2 -> vector2 == other.vector2
            else -> false
        }
    }

    override fun hashCode(): Int = expText.hashCode()
}