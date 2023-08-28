package org.kirinhorse.kbt

/**
 * #COMPONENTS_START
 * SEQ[延时打印]{
 *     delay(duration=1000)
 *     print(text=test)
 * }
 * #COMPONENTS_END
 */
class ComponentsConfig(nodes: List<NodeConfig>) {
    private val map = mutableMapOf(*nodes.map { it.name!! to it }.toTypedArray())

    fun getComponent(name: String) = map[name]

    fun putComponent(node: NodeConfig) {
        map[node.name!!] = node
    }

    fun removeComponent(name: String) = map.remove(name)

    fun encode() = map.values.joinToString("\n") { it.encode(0) }

    companion object {
        fun decode(text: String) = ComponentsConfig(NodeConfig.decodeList(text) ?: listOf())
    }
}