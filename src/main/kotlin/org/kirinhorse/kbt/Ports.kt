package org.kirinhorse.kbt

import org.kirinhorse.kbt.KBTHelper.subBetween
import org.kirinhorse.kbt.Types.toValue
import org.kirinhorse.kbt.expression.ExpFactory
import kotlin.math.roundToInt
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

@Repeatable
annotation class KBTInput(val key: String, val type: Types.KBTType)

@Repeatable
annotation class KBTOutput(val key: String, val type: Types.KBTType)

class BTInPort(val variants: Variants, inputs: List<KBTInput>, inputConfig: MutableMap<String, String>) {
    val values = inputs.associate { it.key to BTInValue(it, inputConfig[it.key] ?: "") }
    private val extraValues = mutableMapOf<String, BTInValue>()

    companion object {
        fun create(component: Component, config: NodeConfig): BTInPort? {
            val builder = KBTFactory.getBuilder(config.type)
            if (builder.inputs.isEmpty()) return null
            val inputCfg = config.inputs ?: throw ErrorDataEmpty(builder.inputs.first().key)
            return BTInPort(component.variants, builder.inputs, inputCfg)
        }
    }

    inner class BTInValue(private val input: KBTInput, private val text: String) {
        private val isExpression = text.startsWith('{') && text.endsWith('}')
        private val isVariant = !isExpression && text.startsWith('$')
        private val isValue = !isExpression && !isVariant

        private val express = if (isExpression) text.subBetween('{', '}')!! else null
        private val variant = if (isVariant) text.substringAfter('$') else null
        private val value = readValue()

        fun read() = when {
            isValue -> value
            isVariant -> readVariant()
            isExpression -> readExpression()
            else -> null
        }

        private fun readExpression() = ExpFactory.evaluate(variants, express!!)

        private fun readVariant() = variants.get(variant!!, input.type.clazz)

        private fun readValue() = if (!isValue) null else input.type.toValue(text)
    }

    fun setExtraValue(key: String, type: Types.KBTType, value: String) {
        extraValues[key] = BTInValue(KBTInput(key, type), value)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(key: String, clazz: KClass<T>): T? {
        val inValue = values[key] ?: extraValues[key] ?: throw ErrorInputNotFound(key)
        val value = inValue.read() ?: return null
        if (clazz == Int::class && value is Float) return value.roundToInt() as T
        if (value::class.isSubclassOf(clazz)) return value as T
        throw ErrorTypeWrong(key)
    }
}

class BTOutPort(val variants: Variants, outputs: List<KBTOutput>, outputConfig: Map<String, String>) {
    val outValues = outputs.associate { it.key to BTOutValue(outputConfig[it.key] ?: "") }
    private val extraValues = mutableMapOf<String, BTOutValue>()

    companion object {
        fun create(component: Component, config: NodeConfig): BTOutPort? {
            val builder = KBTFactory.getBuilder(config.type)
            if (builder.outputs.isEmpty() && config.outputs.isNullOrEmpty()) return null
            val outputCfg = config.outputs ?: mapOf()
            return BTOutPort(component.variants, builder.outputs, outputCfg)
        }
    }

    inner class BTOutValue(text: String) {
        val variant = text.substringAfter('$', "")

        fun write(value: Any?) {
            if (variant.isBlank()) return
            variants[variant] = value
        }
    }

    fun setExtraValue(key: String, value: String) {
        extraValues[key] = BTOutValue(value)
    }

    fun set(key: String, value: Any?) {
        val outValue = outValues[key] ?: extraValues[key] ?: throw ErrorInputNotFound(key)
        outValue.write(value)
    }

    fun reset() {
        outValues.values.forEach { it.write(variants.getVariant(it.variant)?.initValue) }
        extraValues.values.forEach { it.write(variants.getVariant(it.variant)?.initValue) }
    }
}