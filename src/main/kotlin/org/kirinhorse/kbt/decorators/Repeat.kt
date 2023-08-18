package org.kirinhorse.kbt.decorators

import org.kirinhorse.kbt.BehaviorTree
import org.kirinhorse.kbt.Decorator
import org.kirinhorse.kbt.KBTInput
import org.kirinhorse.kbt.NodeConfig
import org.kirinhorse.kbt.Types

@KBTInput("times", Types.KBTType.Int)
class Repeat(behaviorTree: BehaviorTree, config: NodeConfig) : Decorator(behaviorTree, config) {
    override suspend fun onExecute(): Boolean {
        val times = getInput("times", Int::class)
        for (t in 1..times) {
            if (isPaused) pauseDeferred?.await()
            if (!isRunning || child?.execute() == false) return false
        }
        return true
    }
}