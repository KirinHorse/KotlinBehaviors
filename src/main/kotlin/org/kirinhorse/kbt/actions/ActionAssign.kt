package org.kirinhorse.kbt.actions

import org.kirinhorse.kbt.Action
import org.kirinhorse.kbt.Component
import org.kirinhorse.kbt.KBTInput
import org.kirinhorse.kbt.KBTOutput
import org.kirinhorse.kbt.NodeConfig
import org.kirinhorse.kbt.Types

@KBTInput("value", Types.KBTType.Unknown)
@KBTOutput("variant", Types.KBTType.Unknown)
class ActionAssign(component: Component, config: NodeConfig) : Action(component, config) {
    override suspend fun onExecute(): Boolean {
        val inValue = inputs?.values?.get("value") ?: return false
        val result = inValue.read() ?: return false
        setOutput("variant", result)
        return isRunning
    }

    override fun onCancel() {}
}