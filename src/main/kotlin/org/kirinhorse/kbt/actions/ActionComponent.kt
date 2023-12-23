package org.kirinhorse.kbt.actions

import org.kirinhorse.kbt.Action
import org.kirinhorse.kbt.Component
import org.kirinhorse.kbt.KBTInput
import org.kirinhorse.kbt.NodeConfig
import org.kirinhorse.kbt.Types

@KBTInput("name", Types.KBTType.Text)
class ActionComponent(component: Component, config: NodeConfig) : Action(component, config) {
    private var target: Component? = null
    override suspend fun onExecute(): Boolean {
        val name = getInput("name", String::class)
        target = component.tree.components[name] ?: return false
        return target!!.start() ?: true
    }

    override fun onCancel() {
        target?.cancel()
    }

    override fun onPause() {
        target?.pause()
    }

    override fun onResume() {
        target?.resume()
    }

    override fun onReset() {
        target = null
    }
}