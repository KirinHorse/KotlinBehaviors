package org.kirinhorse.kbt.controls

import org.kirinhorse.kbt.Control
import org.kirinhorse.kbt.NodeConfig
import org.kirinhorse.kbt.BehaviorTree

class Sequence(behaviorTree: BehaviorTree, config: NodeConfig) : Control(behaviorTree, config) {
    override suspend fun onExecute(): Boolean {
        for (child in children) {
            if (isPaused) pauseDeferred?.await()
            if (!isRunning || !child.execute()) return false
        }
        return true
    }
}