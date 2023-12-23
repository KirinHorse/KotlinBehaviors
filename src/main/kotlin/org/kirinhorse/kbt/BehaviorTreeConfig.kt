package org.kirinhorse.kbt

data class BehaviorTreeConfig(
    val mainConfig: ComponentConfig, val componentsConfig: Map<String, ComponentConfig>
)