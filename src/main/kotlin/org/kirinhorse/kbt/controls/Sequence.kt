package org.kirinhorse.kbt.controls

import org.kirinhorse.kbt.Component
import org.kirinhorse.kbt.Control
import org.kirinhorse.kbt.NodeConfig

class Sequence(component: Component, config: NodeConfig) : Control(component, config) {
    override suspend fun onExecute(): Boolean {
        for (child in children) {
            if (isPaused) pauseDeferred?.await()
            if (!isRunning || !child.execute()) return false
        }
        return true
    }
}