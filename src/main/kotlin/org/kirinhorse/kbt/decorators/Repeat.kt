package org.kirinhorse.kbt.decorators

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.kirinhorse.kbt.Component
import org.kirinhorse.kbt.Decorator
import org.kirinhorse.kbt.KBTInput
import org.kirinhorse.kbt.NodeConfig
import org.kirinhorse.kbt.Types

@KBTInput("times", Types.KBTType.Int)
@KBTInput("stop", Types.KBTType.Bool)
class Repeat(component: Component, config: NodeConfig) : Decorator(component, config) {
    override suspend fun onExecute(): Boolean {
        val times = getInputOrNull("times", Int::class)
        var exe = 0
        return withContext(Dispatchers.Default) {
            while (getInputOrNull("stop", Boolean::class) != true) {
                if (times != null && times >= 0 && exe++ >= times) break
                if (isPaused) pauseDeferred?.await()
                if (!isRunning || child?.execute() == false) return@withContext false
            }
            true
        }
    }
}