package org.kirinhorse.kbt.decorators

import org.kirinhorse.kbt.BehaviorTree
import org.kirinhorse.kbt.Component
import org.kirinhorse.kbt.Decorator
import org.kirinhorse.kbt.NodeConfig

class Not(component: Component, config: NodeConfig) : Decorator(component, config) {
    override suspend fun onExecute(): Boolean {
        if (child == null) return false
        return !child.execute()
    }
}