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
    private val values = mapOf(* inputs.map { it.key to BTInValue(it, inputConfig[it.key] ?: "") }.toTypedArray())

    companion object {
        fun create(behaviorTree: BehaviorTree, config: NodeConfig): BTInPort? {
            val builder = KBTFactory.getBuilder(config.type)
            if (builder.inputs.isEmpty()) return null
            val inputCfg = config.inputs ?: throw ErrorDataEmpty(builder.inputs.first().key)
            return BTInPort(behaviorTree.variants, builder.inputs, inputCfg)
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

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(key: String, clazz: KClass<T>): T? {
        val inValue = values[key] ?: throw ErrorInputNotFound(key)
        val value = inValue.read() ?: return null
        if (clazz == Int::class && value is Float) return value.roundToInt() as T
        if (value::class.isSubclassOf(clazz)) return value as T
        throw ErrorTypeWrong(key)
    }
}

class BTOutPort(val variants: Variants, outputs: List<KBTOutput>, outputConfig: MutableMap<String, String>) {
    private val values = mapOf(* outputs.map { it.key to BTOutValue(it, outputConfig[it.key] ?: "") }.toTypedArray())

    companion object {
        fun create(behaviorTree: BehaviorTree, config: NodeConfig): BTOutPort? {
            val builder = KBTFactory.getBuilder(config.type)
            if (builder.outputs.isEmpty()) return null
            val outputCfg = config.outputs ?: throw ErrorDataEmpty(builder.outputs.first().key)
            return BTOutPort(behaviorTree.variants, builder.outputs, outputCfg)
        }
    }

    inner class BTOutValue(private val output: KBTOutput, text: String) {
        private val variant = text.substringAfter('$', "")
        var value: Any? = null
            private set

        fun write(value: Any?) {
            if (value != null && !value::class.isSubclassOf(output.type.clazz)) throw ErrorTypeWrong(output.key)
            this.value = value
            if (variant.isBlank()) return
            variants[variant] = value
        }
    }


    fun set(key: String, value: Any?) {
        val outValue = values[key] ?: throw ErrorInputNotFound(key)
        outValue.write(value)
    }

    fun get(key: String): Any? {
        val outValue = values[key] ?: throw ErrorInputNotFound(key)
        return outValue.value
    }

    fun reset() {
        values.values.forEach { it.write(null) }
    }
}