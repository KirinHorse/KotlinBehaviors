package org.kirinhorse.kbt

abstract class Leaf(tree: BehaviorTree, config: NodeConfig) : Node(tree, config)

abstract class Action(tree: BehaviorTree, config: NodeConfig) : Leaf(tree, config)

abstract class Condition(tree: BehaviorTree, config: NodeConfig) : Leaf(tree, config)