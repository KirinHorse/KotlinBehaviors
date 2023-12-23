package org.kirinhorse.kbt.decorators

import org.kirinhorse.kbt.Component
import org.kirinhorse.kbt.Decorator
import org.kirinhorse.kbt.KBTInput
import org.kirinhorse.kbt.NodeConfig
import org.kirinhorse.kbt.Types

@KBTInput("status", Types.KBTType.Bool)
class Force(component: Component, config: NodeConfig) : Decorator(component, config) {
    override suspend fun onExecute(): Boolean {
        val status = getInput("status", Boolean::class)
        child?.execute()
        return status
    }
}