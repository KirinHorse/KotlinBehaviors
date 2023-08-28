package org.kirinhorse.kbt

class Components(private val tree: BehaviorTree, val config: ComponentsConfig) {
    fun getComponent(name: String): Node {
        val cfg = config.getComponent(name)
        if (cfg != null) return KBTFactory.createNode(tree, cfg)
        return KBTFactory.loader?.loadComponent(name) ?: throw ErrorComponentNotFound(name)
    }
}
