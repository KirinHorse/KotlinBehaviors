package org.kirinhorse.kbt.actions

import org.kirinhorse.kbt.Action
import org.kirinhorse.kbt.NodeConfig
import org.kirinhorse.kbt.BehaviorTree

abstract class TreeAction(behaviorTree: BehaviorTree, config: NodeConfig) : Action(behaviorTree, config) {
    override fun onCancel() {}
}

class ActionPause(behaviorTree: BehaviorTree, config: NodeConfig) : TreeAction(behaviorTree, config) {
    override suspend fun onExecute(): Boolean {
        tree.pause()
        return true
    }
}

class ActionResume(behaviorTree: BehaviorTree, config: NodeConfig) : TreeAction(behaviorTree, config) {
    override suspend fun onExecute(): Boolean {
        tree.resume()
        return true
    }
}

class ActionStop(behaviorTree: BehaviorTree, config: NodeConfig) : TreeAction(behaviorTree, config) {
    override suspend fun onExecute(): Boolean {
        tree.stop(true)
        return true
    }
}
