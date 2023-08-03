package com.xqkj.app.bigclicker.btree.controls

import org.kirinhorse.kbt.Control
import org.kirinhorse.kbt.KBTInput
import org.kirinhorse.kbt.NodeConfig
import org.kirinhorse.kbt.Types
import org.kirinhorse.kbt.BehaviorTree
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

@KBTInput(key = "minSuccessTimes", type = Types.BTType.Int)
class Parallel(behaviorTree: BehaviorTree, config: NodeConfig) : Control(behaviorTree, config) {
    override suspend fun onExecute(): Boolean {
        val minSuccessTimes = getInput("minSuccessTimes", Int::class)
        return coroutineScope {
            val results = children.map {
                async { return@async it.execute() }
            }
            return@coroutineScope results.awaitAll().count { it } >= minSuccessTimes
        }
    }
}