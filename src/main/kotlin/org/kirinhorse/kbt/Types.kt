package org.kirinhorse.kbt

import com.xqkj.app.bigclicker.btree.types.KBTVector2
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

object Types {
    enum class BTType(val clazz: KClass<*>) {
        Bool(Boolean::class), Int(kotlin.Int::class), Float(kotlin.Float::class), Text(String::class), Vector2(KBTVector2::class)
    }

    fun BTType.toValue(text: String): Any? {
        return when (this) {
            BTType.Bool -> text.toBooleanStrictOrNull()
            BTType.Int -> text.toIntOrNull()
            BTType.Float -> text.toFloatOrNull()
            BTType.Text -> text.trim()
            BTType.Vector2 -> KBTVector2.fromText(text)
        }
    }

    fun BTType.toString(value: Any?): String {
        if (value == null) return ""
        return when (this) {
            BTType.Text -> value as String
            BTType.Vector2 -> (value as KBTVector2).toString()
            else -> value.toString()
        }
    }

    fun getType(value: Any?): BTType? {
        if (value == null) return null
        return BTType.values().find { value::class.isSubclassOf(it.clazz) }
    }
}