package org.kirinhorse.kbt

import org.kirinhorse.kbt.controls.Fallback
import org.kirinhorse.kbt.controls.Parallel
import org.kirinhorse.kbt.controls.Sequence
import org.kirinhorse.kbt.decorators.Delay
import org.kirinhorse.kbt.decorators.Repeat
import org.kirinhorse.kbt.actions.ActionPause
import org.kirinhorse.kbt.actions.ActionResume
import org.kirinhorse.kbt.actions.ActionStop
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
    }

    fun getBuilder(type: String) = nodeBuilders[type] ?: throw ErrorActionNotSupport(type)

    fun createNode(behaviorTree: BehaviorTree, config: NodeConfig) = getBuilder(config.type).build(behaviorTree, config)
}