package org.kirinhorse.kbt.decorators

import org.kirinhorse.kbt.BehaviorTree
import org.kirinhorse.kbt.Decorator
import org.kirinhorse.kbt.KBTInput
import org.kirinhorse.kbt.NodeConfig
import org.kirinhorse.kbt.Types

@KBTInput("status", Types.KBTType.Bool)
class Force(tree: BehaviorTree, config: NodeConfig) : Decorator(tree, config) {
    override suspend fun onExecute(): Boolean {
        val status = getInput("status", Boolean::class)
        child?.execute()
        return status
    }
}