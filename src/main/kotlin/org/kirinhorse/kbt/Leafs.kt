package org.kirinhorse.kbt

abstract class Leaf(component: Component, config: NodeConfig) : Node(component, config)

abstract class Action(component: Component, config: NodeConfig) : Leaf(component, config)

abstract class Condition(component: Component, config: NodeConfig) : Leaf(component, config)