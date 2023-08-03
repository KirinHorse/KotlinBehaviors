package com.xqkj.app.bigclicker.btree.decorators

import org.kirinhorse.kbt.Decorator
import org.kirinhorse.kbt.KBTInput
import org.kirinhorse.kbt.NodeConfig
import com.xqkj.app.bigclicker.btree.BTOffset.offset
import org.kirinhorse.kbt.Types
import org.kirinhorse.kbt.BehaviorTree
import kotlinx.coroutines.delay

@KBTInput("duration", Types.BTType.Int)
class Delay(behaviorTree: BehaviorTree, config: NodeConfig) : Decorator(behaviorTree, config) {
    private val now get() = System.currentTimeMillis()
    var startTime = 0L
    var duration = 0

    override suspend fun onExecute(): Boolean {
        startTime = now
        duration = offset(getInput("duration", Int::class))
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