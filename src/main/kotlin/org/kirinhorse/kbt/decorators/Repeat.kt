package org.kirinhorse.kbt.decorators

import org.kirinhorse.kbt.BehaviorTree
import org.kirinhorse.kbt.Decorator
import org.kirinhorse.kbt.KBTInput
import org.kirinhorse.kbt.NodeConfig
import org.kirinhorse.kbt.Types

@KBTInput("times", Types.KBTType.Int)
@KBTInput("stop", Types.KBTType.Bool)
class Repeat(behaviorTree: BehaviorTree, config: NodeConfig) : Decorator(behaviorTree, config) {
    override suspend fun onExecute(): Boolean {
        val times = getInputOrNull("times", Int::class)
        var exe = 0
        while (getInputOrNull("stop", Boolean::class) != true) {
            if (times != null && times >= 0 && exe++ >= times) break
            if (isPaused) pauseDeferred?.await()
            if (!isRunning || child?.execute() == false) return false
        }
        return true
    }
}