package org.kirinhorse.kbt

abstract class Control(component: Component, config: NodeConfig) : Node(component, config) {
    val children = config.children?.map { KBTFactory.createNode(component, it) } ?: listOf()

    override fun onPause() {
        children.forEach { it.pause() }
    }

    override fun onResume() {
        children.forEach { it.resume() }
    }

    override fun onCancel() {
        children.forEach { it.cancel() }
    }

    override fun onReset() {
        children.forEach { it.reset() }
    }
}