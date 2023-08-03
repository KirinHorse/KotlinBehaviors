package org.kirinhorse.kbt.decorators

import kotlinx.coroutines.delay
import org.kirinhorse.kbt.BehaviorTree
import org.kirinhorse.kbt.Decorator
import org.kirinhorse.kbt.KBTInput
import org.kirinhorse.kbt.NodeConfig
import org.kirinhorse.kbt.Types

@KBTInput("duration", Types.BTType.Int)
class Delay(behaviorTree: BehaviorTree, config: NodeConfig) : Decorator(behaviorTree, config) {
    private val now get() = System.currentTimeMillis()
    var startTime = 0L
    var duration = 0

    override suspend fun onExecute(): Boolean {
        startTime = now
        duration = getInput("duration", Int::class)
        while (duration > 0) {
            delay(duration.toLong())
            if (isPaused) pauseDeferred?.await()
            calDuration()
        }
        return isRunning && child?.execute() != false
    }

    private fun calDuration() {
        val passed = (now - startTime).toInt()
        duration -= passed
    }

    override fun onPause() {
        calDuration()
        super.onPause()
    }

    override fun onResume() {
        startTime = System.currentTimeMillis()
        super.onResume()
    }
}