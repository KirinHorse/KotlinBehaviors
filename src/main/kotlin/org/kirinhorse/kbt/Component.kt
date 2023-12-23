package org.kirinhorse.kbt


data class ComponentConfig(val variantsConfig: VariantsConfig, val nodeConfig: NodeConfig?)
class Component(val tree: BehaviorTree, config: ComponentConfig) {
    val variants = Variants(config.variantsConfig)
    val node = if (config.nodeConfig == null) null else KBTFactory.createNode(this, config.nodeConfig)
    suspend fun start() = node?.execute()

    fun pause() = node?.pause()

    fun resume() = node?.resume()

    fun cancel() = node?.cancel()

    fun reset() = node?.reset()
}