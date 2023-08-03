package org.kirinhorse.kbt

class BehaviorTree(val config: BehaviorTreeConfig) {
    val variants = Variants(config.variantsConfig)
    val treeListeners = mutableListOf<TreeListener>()
    val nodeListeners = mutableListOf<NodeListener>()

    val root = KBTFactory.createNode(this, config.nodeConfig)

    suspend fun start() {
        for (l in treeListeners) l.onStart(this)
        root.execute()
        stop(false)
    }

    fun pause() {
        root.pause()
        for (l in treeListeners) l.onPause(this)
    }

    fun resume() {
        root.resume()
        for (l in treeListeners) l.onResume(this)
    }

    fun stop(cancel: Boolean = true) {
        if (cancel) root.cancel()
        root.reset()
        for (l in treeListeners) l.onStop(this)
    }
}