package org.kirinhorse.kbt.actions

import org.kirinhorse.kbt.Action
import org.kirinhorse.kbt.Component
import org.kirinhorse.kbt.ErrorComponentNotFound
import org.kirinhorse.kbt.KBTFactory
import org.kirinhorse.kbt.KBTInput
import org.kirinhorse.kbt.NodeConfig
import org.kirinhorse.kbt.Types

@KBTInput("name", Types.KBTType.Text)
class ActionComponent(component: Component, config: NodeConfig) : Action(component, config) {
    private var target: Component? = null
    private val targetVariants get() = target?.variants?.map

    override suspend fun onExecute(): Boolean {
        val name = getInput("name", String::class)
        if (target == null) {
            //加载组件
            target = component.tree.components[name] ?: //
                    KBTFactory.loader?.loadComponent(component.tree, name) ?: //
                    throw ErrorComponentNotFound(name)

            config.inputs?.forEach { (key, value) ->
                if (inputs?.values?.get(key) != null) return@forEach
                val type = target!!.variants.getVariant(key)?.type ?: return@forEach
                inputs?.setExtraValue(key, type, value)
            }
            config.outputs?.forEach { (key, value) ->
                if (outputs?.outValues?.get(key) != null) return@forEach
                outputs?.setExtraValue(key, value)
            }
        }
        //输入组件参数
        config.inputs?.forEach { (key, _) ->
            val variant = targetVariants?.get(key) ?: return@forEach
            target?.variants?.set(key, getInputOrNull(key, variant.type.clazz))
        }
        //执行组件
        val result = target?.start() ?: !isRunning
        if (result) {
            //输出组件参数
            config.outputs?.forEach { (key, _) ->
                val variant = targetVariants?.get(key) ?: return@forEach
                setOutput(key, variant.value)
            }
        }
        return result
    }

    override fun onCancel() {
        target?.cancel()
    }

    override fun onPause() {
        target?.pause()
    }

    override fun onResume() {
        target?.resume()
    }

    override fun onReset() {
        target = null
    }
}