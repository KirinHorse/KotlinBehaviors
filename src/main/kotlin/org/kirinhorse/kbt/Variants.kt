package org.kirinhorse.kbt

import org.kirinhorse.kbt.KBTHelper.subBetween
import org.kirinhorse.kbt.Types.toString
import org.kirinhorse.kbt.Types.toValue
import kotlin.math.roundToInt
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

class Variants(val map: Map<String, Variant>) {
    data class Variant(val key: String, val type: Types.KBTType, var value: Any?) {
        val initValue = value //记录Variant的初始值
    }

    companion object {
        fun decodeVariant(text: String): Variant {
            val key = text.substringBefore(':').trim()
            val typeStr = text.subBetween(':', '=')!!.trim()
            val type = Types.KBTType.valueOf(typeStr)
            val valueStr = text.substringAfter('=').trim()
            val value = type.toValue(valueStr)
            return Variant(key, type, value)
        }

        fun Variant.encode() = "$key:${type.name}=${type.toString(value)}"
    }

    fun getVariant(key: String) = map[key] ?: KBTFactory.loader?.loadVariant(key)

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(key: String, clazz: KClass<T>): T? {
        val v = map[key] ?: KBTFactory.loader?.loadVariant(key) ?: return null
        var value = v.value ?: return null
        if (value is Int && clazz == Float::class) value = value.toFloat()
        else if (value is Float && clazz == Int::class) value = value.roundToInt()
        if (!value::class.isSubclassOf(clazz)) throw ErrorTypeWrong(key)
        return value as T?
    }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    operator fun <T : Any> set(key: String, value: T?) {
        val v = map[key] ?: throw ErrorVariantNotFound(key)
        val newValue = if (value is Int && v.type == Types.KBTType.Float) value.toFloat()
        else if (value is Float && v.type == Types.KBTType.Int) value.roundToInt()
        else value
        if (newValue != null && !newValue::class.isSubclassOf(v.type.clazz)) throw ErrorTypeWrong(key)
        v.value = newValue
    }
}