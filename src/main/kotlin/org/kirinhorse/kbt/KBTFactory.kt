package org.kirinhorse.kbt

import org.kirinhorse.kbt.actions.ActionPause
import org.kirinhorse.kbt.actions.ActionPrint
import org.kirinhorse.kbt.actions.ActionResume
import org.kirinhorse.kbt.actions.ActionStop
import org.kirinhorse.kbt.controls.Fallback
import org.kirinhorse.kbt.controls.Parallel
import org.kirinhorse.kbt.controls.Sequence
import org.kirinhorse.kbt.decorators.Delay
import org.kirinhorse.kbt.decorators.Repeat
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

object KBTFactory {
    class BTNodeBuilder<T : Node>(private val clazz: KClass<T>) {
        val inputs = clazz.annotations.filterIsInstance<KBTInput>()
        val outputs = clazz.annotations.filterIsInstance<KBTOutput>()

        fun build(behaviorTree: BehaviorTree, config: NodeConfig): T =
            clazz.constructors.first().call(behaviorTree, config)

        val isControl = clazz.isSubclassOf(Control::class)
        val isDecorator = clazz.isSubclassOf(Decorator::class)
        val isAction = clazz.isSubclassOf(Action::class)
        val isLeaf = clazz.isSubclassOf(Leaf::class)
        val hasChildren = isControl || isDecorator
    }

    private val nodeBuilders = mutableMapOf<String, BTNodeBuilder<*>>()

    private fun <T : Node> registerNode(type: String, clazz: KClass<T>) {
        nodeBuilders[type] = BTNodeBuilder(clazz)
    }

    init {
        //controls
        registerNode("SEQ", Sequence::class)
        registerNode("PAR", Parallel::class)
        registerNode("FAB", Fallback::class)
        //decorators
        registerNode("delay", Delay::class)
        registerNode("repeat", Repeat::class)
        //actions
        registerNode("pause", ActionPause::class)
        registerNode("resume", ActionResume::class)
        registerNode("stop", ActionStop::class)
        registerNode("print", ActionPrint::class)
    }

    fun getBuilder(type: String) = nodeBuilders[type] ?: throw ErrorActionNotSupport(type)

    fun createNode(behaviorTree: BehaviorTree, config: NodeConfig) = getBuilder(config.type).build(behaviorTree, config)

    fun createTree(script: String) = BehaviorTree(BehaviorTreeConfig.decode(script))

    fun SEQ(children: MutableList<NodeConfig>, name: String? = null) = NodeConfig(name, "SEQ", null, null, children)
    fun PAR(children: MutableList<NodeConfig>, name: String? = null) = NodeConfig(name, "PAR", null, null, children)
    fun FAB(children: MutableList<NodeConfig>, name: String? = null) = NodeConfig(name, "FAB", null, null, children)
    fun delay(duration: Int, child: NodeConfig? = null, name: String? = null): NodeConfig {
        val inputs = mutableMapOf("duration" to duration.toString())
        val children = if (child != null) mutableListOf(child) else null
        return NodeConfig(name, "delay", inputs, null, children)
    }

    fun repeat(times: Int, child: NodeConfig, name: String? = null) =
        NodeConfig(name, "repeat", mutableMapOf("times" to times.toString()), null, mutableListOf(child))

    fun print(text: String, name: String? = null) =
        NodeConfig(name, "print", mutableMapOf("text" to "\"text\""), null, null)

    fun pause(times: Int, name: String? = null) = NodeConfig(name, "pause", null, null, null)
    fun resume(times: Int, name: String? = null) = NodeConfig(name, "resume", null, null, null)
    fun stop(times: Int, name: String? = null) = NodeConfig(name, "stop", null, null, null)
}