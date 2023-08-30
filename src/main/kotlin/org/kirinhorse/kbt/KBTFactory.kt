package org.kirinhorse.kbt

import org.kirinhorse.kbt.actions.ActionComponent
import org.kirinhorse.kbt.actions.ActionPause
import org.kirinhorse.kbt.actions.ActionPrint
import org.kirinhorse.kbt.actions.ActionResume
import org.kirinhorse.kbt.actions.ActionStop
import org.kirinhorse.kbt.actions.ActionSubTree
import org.kirinhorse.kbt.controls.Fallback
import org.kirinhorse.kbt.controls.Parallel
import org.kirinhorse.kbt.controls.Sequence
import org.kirinhorse.kbt.decorators.Delay
import org.kirinhorse.kbt.decorators.Force
import org.kirinhorse.kbt.decorators.Not
import org.kirinhorse.kbt.decorators.Repeat
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

object KBTFactory {
    interface ILoader {
        fun loadTree(name: String): BehaviorTree
        fun loadComponent(name: String): Node
    }

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
    var loader: ILoader? = null

    fun <T : Node> registerNode(type: String, clazz: KClass<T>) {
        nodeBuilders[type] = BTNodeBuilder(clazz)
    }

    init {
        //controls
        registerNode("SEQ", Sequence::class)
        registerNode("PAR", Parallel::class)
        registerNode("FAB", Fallback::class)
        registerNode("SUB", ActionSubTree::class)
        registerNode("COM", ActionComponent::class)
        //decorators
        registerNode("delay", Delay::class)
        registerNode("repeat", Repeat::class)
        registerNode("force", Force::class)
        registerNode("not", Not::class)
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
    fun SUB(subTree: String, name: String? = null) =
        NodeConfig(name, "SUB", mutableMapOf("name" to subTree), null, null)

    fun COM(component: String, name: String? = null) =
        NodeConfig(name, "COM", mutableMapOf("name" to component), null, null)

    fun delay(duration: String, child: NodeConfig? = null, name: String? = null): NodeConfig {
        val inputs = mutableMapOf("duration" to duration)
        val children = if (child != null) mutableListOf(child) else null
        return NodeConfig(name, "delay", inputs, null, children)
    }

    fun repeat(times: String, stop: String, child: NodeConfig, name: String? = null) = NodeConfig(
        name, "repeat", mutableMapOf("times" to times, "stop" to stop), null, mutableListOf(child)
    )

    fun force(status: String, child: NodeConfig, name: String? = null) = NodeConfig(
        name, "force", mutableMapOf("status" to status), null, mutableListOf(child)
    )

    fun not(child: NodeConfig, name: String? = null) = NodeConfig(name, "not", null, null, mutableListOf(child))

    fun print(text: String, name: String? = null) =
        NodeConfig(name, "print", mutableMapOf("text" to "\"$text\""), null, null)

    fun pause(name: String? = null) = NodeConfig(name, "pause", null, null, null)
    fun resume(name: String? = null) = NodeConfig(name, "resume", null, null, null)
    fun stop(name: String? = null) = NodeConfig(name, "stop", null, null, null)
}