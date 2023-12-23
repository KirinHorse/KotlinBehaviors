package org.kirinhorse.kbt

import org.kirinhorse.kbt.types.KBTVector2
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

object Types {
    enum class KBTType(val clazz: KClass<*>) {
        Bool(Boolean::class), Int(kotlin.Int::class), Float(kotlin.Float::class), Text(String::class), Vector2(
            KBTVector2::class
        )
    }

    fun KBTType.toValue(text: String): Any? {
        return when (this) {
            KBTType.Bool -> text.toBooleanStrictOrNull()
            KBTType.Int -> text.toIntOrNull()
            KBTType.Float -> text.toFloatOrNull()
            KBTType.Vector2 -> KBTVector2.fromText(text)
            KBTType.Text -> text.run {
                if (length < 2 || !startsWith('"') || !endsWith('"')) null
                else substring(1, length - 1)
            }
        }
    }

    fun KBTType.toString(value: Any?): String {
        if (value == null) return ""
        return when (this) {
            KBTType.Text -> "\"$value\""
            KBTType.Vector2 -> (value as KBTVector2).toString()
            else -> value.toString()
        }
    }

    fun getType(value: Any?): KBTType? {
        if (value == null) return null
        return KBTType.values().find { value::class.isSubclassOf(it.clazz) }
    }
}