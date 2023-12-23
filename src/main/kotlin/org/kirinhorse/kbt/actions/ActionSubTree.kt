package org.kirinhorse.kbt.actions

import org.kirinhorse.kbt.Action
import org.kirinhorse.kbt.BehaviorTree
import org.kirinhorse.kbt.Component
import org.kirinhorse.kbt.ErrorTreeNotFound
import org.kirinhorse.kbt.KBTFactory
import org.kirinhorse.kbt.KBTInput
import org.kirinhorse.kbt.NodeConfig
import org.kirinhorse.kbt.Types

@KBTInput("name", Types.KBTType.Text)
class ActionSubTree(component: Component, config: NodeConfig) : Action(component, config) {
    private var subTree: BehaviorTree? = null
    override suspend fun onExecute(): Boolean {
        val name = getInput("name", String::class)
        subTree = KBTFactory.loader?.loadTree(name) ?: throw ErrorTreeNotFound(name)
        subTree?.start()
        return isRunning
    }

    override fun onCancel() {
        subTree?.stop(true)
    }

    override fun onPause() {
        subTree?.pause()
    }

    override fun onResume() {
        subTree?.resume()
    }

    override fun onReset() {
        subTree = null
    }
}