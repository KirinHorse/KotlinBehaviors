package org.kirinhorse.kbt.actions

import org.kirinhorse.kbt.Action
import org.kirinhorse.kbt.Component
import org.kirinhorse.kbt.NodeConfig

abstract class TreeAction(component: Component, config: NodeConfig) : Action(component, config) {
    override fun onCancel() {}
}

class ActionPause(component: Component, config: NodeConfig) : TreeAction(component, config) {
    override suspend fun onExecute(): Boolean {
        tree.pause()
        return true
    }
}

class ActionResume(component: Component, config: NodeConfig) : TreeAction(component, config) {
    override suspend fun onExecute(): Boolean {
        tree.resume()
        return true
    }
}

class ActionStop(component: Component, config: NodeConfig) : TreeAction(component, config) {
    override suspend fun onExecute(): Boolean {
        tree.stop(true)
        return true
    }
}
