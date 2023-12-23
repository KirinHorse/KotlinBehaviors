package org.kirinhorse.kbt

abstract class Decorator(component: Component, config: NodeConfig) : Node(component, config) {
    val child = if (config.children.isNullOrEmpty()) null
    else KBTFactory.createNode(component, config.children.first())

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