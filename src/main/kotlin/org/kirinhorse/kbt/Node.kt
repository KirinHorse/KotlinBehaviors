package org.kirinhorse.kbt

import kotlinx.coroutines.CompletableDeferred
import kotlin.reflect.KClass

abstract class Node(val tree: BehaviorTree, val config: NodeConfig) {
    private val inputs = BTInPort.create(tree, config)
    private val outputs = BTOutPort.create(tree, config)

    var isRunning = false
        private set

    var pauseDeferred: CompletableDeferred<Boolean>? = null
        private set

    val isPaused get() = pauseDeferred?.isCompleted == false

    suspend fun execute(): Boolean {
        tree.nodeListeners.forEach { it.beforeExecute(this) }
        isRunning = true
        val result = onExecute() && isRunning
        isRunning = false
        tree.nodeListeners.forEach { it.afterExecute(this, result) }
        return result
    }

    fun pause() {
        if (!isRunning) return
        pauseDeferred = CompletableDeferred()
        onPause()
        tree.nodeListeners.forEach { it.onPaused(this) }
    }

    fun resume() {
        if (!isPaused) return
        pauseDeferred?.complete(true)
        onResume()
        tree.nodeListeners.forEach { it.onResumed(this) }
    }

    fun cancel() {
        isRunning = false
        if (isPaused) pauseDeferred?.complete(false)
        onCancel()
        tree.nodeListeners.forEach { it.onCanceled(this) }
    }

    fun reset() {
        isRunning = false
        pauseDeferred = null
        outputs?.reset()
        onReset()
    }

    fun <T : Any> getInput(key: String, clazz: KClass<T>): T {
        if (inputs == null) throw ErrorInputNotFound(key)
        return inputs.get(key, clazz) ?: throw ErrorDataEmpty(key)
    }

    fun <T : Any> getInputOrNull(key: String, clazz: KClass<T>): T? {
        if (inputs == null) return null
        return inputs.get(key, clazz)
    }

    fun <T : Any> setOutput(key: String, value: T?) {
        if (outputs == null) throw ErrorInputNotFound(key)
        outputs.set(key, value)
    }

    abstract suspend fun onExecute(): Boolean
    abstract fun onCancel()
    open fun onPause() {}
    open fun onResume() {}
    open fun onReset() {}
}