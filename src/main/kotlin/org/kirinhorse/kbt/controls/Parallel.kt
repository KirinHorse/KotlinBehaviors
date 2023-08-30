package org.kirinhorse.kbt.controls

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.kirinhorse.kbt.BehaviorTree
import org.kirinhorse.kbt.Control
import org.kirinhorse.kbt.KBTInput
import org.kirinhorse.kbt.NodeConfig
import org.kirinhorse.kbt.Types

@KBTInput(key = "success", type = Types.KBTType.Int)
class Parallel(behaviorTree: BehaviorTree, config: NodeConfig) : Control(behaviorTree, config) {
    override suspend fun onExecute(): Boolean {
        val minSuccessTimes = getInputOrNull("success", Int::class) ?: 0
        return coroutineScope {
            val results = children.map { async { it.execute() } }
            results.awaitAll().count { it } >= minSuccessTimes
        }
    }
}