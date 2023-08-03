package org.kirinhorse.kbt

abstract class Decorator(tree: BehaviorTree, config: NodeConfig) : Node(tree, config) {
    val child = if (config.children.isNullOrEmpty()) null
    else KBTFactory.createNode(tree, config.children.first())

    override fun onPause() {
        child?.pause()
    }

    override fun onResume() {
        child?.resume()
    }

    override fun onCancel() {
        child?.cancel()
    }

    override fun onReset() {
        child?.reset()
    }
}