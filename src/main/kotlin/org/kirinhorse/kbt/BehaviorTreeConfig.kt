package org.kirinhorse.kbt

import org.kirinhorse.kbt.KBTHelper.subBetween

class BehaviorTreeConfig(val variantsConfig: VariantsConfig, val nodeConfig: NodeConfig) {
    companion object {
        fun decode(text: String): BehaviorTreeConfig {
            val variantsText = text.subBetween("#VARIANTS_START", "#VARIANTS_END") ?: ""
            val variantsConfig = VariantsConfig.decode(variantsText)
            val nodeText = text.subBetween("#NODE_START", "#NODE_END") ?: ""
            val nodeConfig = NodeConfig.decode(nodeText)
            return BehaviorTreeConfig(variantsConfig, nodeConfig)
        }
    }

    fun encode(): String {
        val variants = variantsConfig.encode(0)
        val node = nodeConfig.encode(0)
        return """#VARIANTS_START
            |$variants
            |#VARIANTS_END
            |#NODE_START
            |$node
            |#NODE_END""".trimMargin()
    }
}