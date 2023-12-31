package org.kirinhorse.kbt.actions

import org.kirinhorse.kbt.Action
import org.kirinhorse.kbt.Component
import org.kirinhorse.kbt.KBTInput
import org.kirinhorse.kbt.NodeConfig
import org.kirinhorse.kbt.Types

@KBTInput("text", Types.KBTType.Text)
class ActionPrint(component: Component, config: NodeConfig) : Action(component, config) {
    override suspend fun onExecute(): Boolean {
        val text = getInput("text", String::class)
        println(text)
        return true
    }

    override fun onCancel() {}
}