package org.kirinhorse.kbt

class BehaviorTree(val config: BehaviorTreeConfig) {
    val mainComponent = Component(this, config.mainConfig)
    val components = config.componentsConfig.mapValues { Component(this@BehaviorTree, it.value) }

    val treeListeners = mutableListOf<TreeListener>()
    val nodeListeners = mutableListOf<NodeListener>()

    suspend fun start() {
        for (l in treeListeners) l.onStart(this)
        mainComponent.start()
        stop(false)
    }

    fun pause() {
        mainComponent.pause()
        for (l in treeListeners) l.onPause(this)
    }

    fun resume() {
        mainComponent.resume()
        for (l in treeListeners) l.onResume(this)
    }

    fun stop(cancel: Boolean = true) {
        mainComponent.cancel()
        mainComponent.reset()
        for (l in treeListeners) l.onStop(this)
    }

    fun addTreeListener(listener: TreeListener) = treeListeners.add(listener)
    fun removeTreeListener(listener: TreeListener) = treeListeners.remove(listener)
    fun addNodeListener(listener: NodeListener) = nodeListeners.add(listener)
    fun removeNodeListener(listener: NodeListener) = nodeListeners.remove(listener)
}