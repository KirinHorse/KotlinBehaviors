package org.kirinhorse.kbt.actions

import org.kirinhorse.kbt.Action
import org.kirinhorse.kbt.BehaviorTree
import org.kirinhorse.kbt.KBTInput
import org.kirinhorse.kbt.Node
import org.kirinhorse.kbt.NodeConfig
import org.kirinhorse.kbt.Types

@KBTInput("name", Types.KBTType.Text)
class ActionComponent(tree: BehaviorTree, config: NodeConfig) : Action(tree, config) {
    private var node: Node? = null
    override suspend fun onExecute(): Boolean {
        val name = getInput("name", String::class)
        node = tree.components.getComponent(name)
        return node!!.execute()
    }

    override fun onCancel() {
        node?.cancel()
    }

    override fun onPause() {
        node?.pause()
    }

    override fun onResume() {
        node?.resume()
    }

    override fun onReset() {
        node = null
    }
}