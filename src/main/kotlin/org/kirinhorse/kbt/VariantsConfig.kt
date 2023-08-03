package org.kirinhorse.kbt

import org.kirinhorse.kbt.Variants.Companion.encode
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

class VariantsConfig(val map: MutableMap<String, Variants.Variant>) {
    companion object {
        fun decode(text: String): VariantsConfig {
            val lines = text.split('\n')
            val map = mutableMapOf<String, Variants.Variant>()
            lines.map {
                if (it.isBlank()) return@map
                val variant = Variants.decodeVariant(it)
                map[variant.key] = variant
            }
            return VariantsConfig(map)
        }
    }

    fun encode(indent: Int): String {
        var indents = ""
        for (i in 0 until indent) indents += 't'
        return map.values.joinToString("\n", indents) { it.encode() }
    }

    fun getVariant(key: String) = map[key]

    fun <T> get(key: String, clazz: KClass<*>): T? {
        val v = map[key] ?: return null
        if (!v.type.clazz.isSubclassOf(clazz)) throw ErrorTypeWrong(key)
        @Suppress("UNCHECKED_CAST") return v.value as T?
    }

    fun <T> set(key: String, value: T?) {
        if (value == null) map.remove(key)
        val type = Types.getType(value) ?: throw ErrorTypeWrong(key)
        val v = map[key]
        if (v == null) map[key] = Variants.Variant(key, type, value)
        else if (v.type != type) throw ErrorTypeWrong(key)
        else v.value = value
    }
}