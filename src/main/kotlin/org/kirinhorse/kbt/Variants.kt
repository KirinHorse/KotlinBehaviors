package org.kirinhorse.kbt

import org.kirinhorse.kbt.KBTHelper.subBetween
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

class Variants(val config: VariantsConfig) {

    data class Variant(val key: String, val type: Types.BTType, var value: Any?)
    companion object {
        fun decodeVariant(text: String): Variant {
            val key = text.substringBefore(':').trim()
            val type = text.subBetween(':', '=')!!.trim()
            val value = text.substringAfter('=').trim()
            return Variant(key, Types.BTType.valueOf(type), value)
        }

        fun Variant.encode() = "$key:${type.name}=$value"
    }

    private val map = config.map.toMap()

    fun getVariant(key: String) = map[key]

    fun <T : Any> get(key: String, clazz: KClass<T>): T? {
        val v = map[key] ?: return null
        if (!v.type.clazz.isSubclassOf(clazz)) throw ErrorTypeWrong(key)
        @Suppress("UNCHECKED_CAST") return v.value as T?
    }

    operator fun <T : Any> set(key: String, value: T?) {
        val v = map[key] ?: throw ErrorVariantNotFound(key)
        if (value != null && !value::class.isSubclassOf(v.type.clazz)) throw ErrorTypeWrong(key)
        v.value = value
    }
}