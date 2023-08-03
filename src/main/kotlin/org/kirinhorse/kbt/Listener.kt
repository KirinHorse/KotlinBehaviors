package org.kirinhorse.kbt

interface NodeListener {
    fun beforeExecute(node: Node)
    fun onPaused(node: Node)
    fun onResumed(node: Node)
    fun afterExecute(node: Node, result: Boolean)
    fun onCanceled(node: Node)
}

interface TreeListener {
    fun onStart(behaviorTree: BehaviorTree)
    fun onPause(behaviorTree: BehaviorTree)
    fun onResume(behaviorTree: BehaviorTree)
    fun onStop(behaviorTree: BehaviorTree)
}