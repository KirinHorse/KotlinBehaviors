package org.kirinhorse.kbt

import org.kirinhorse.kbt.KBTHelper.subBetween

class NodeConfig(
    var name: String?,
    val type: String,
    val inputs: MutableMap<String, String>?,
    val outputs: MutableMap<String, String>?,
    val children: MutableList<NodeConfig>?
) {
    private fun encodeMap(map: Map<String, String>?): String {
        if (map.isNullOrEmpty()) return ""
        return map.entries.joinToString(",") { "${it.key}=${it.value}" }
    }

    private fun encodeList(list: List<NodeConfig>?, indent: Int): String {
        if (list.isNullOrEmpty()) return ""
        val result = list.joinToString("\n", "\n", "\n") { it.encode(indent) }
        return ":$result"
    }

    fun encode(indent: Int): String {
        var indents = ""
        for (i in 0 until indent) indents += "\t"
        val nameStr = if (name.isNullOrBlank()) "" else "[$name]"
        val inputsStr = "(${encodeMap(inputs)})"
        val outputsStr = "(${encodeMap(outputs)})"
        val body = encodeList(children, indent + 1)
        return "$indents$type$nameStr$inputsStr=>$outputsStr$body"
    }

    companion object {
        private fun decodeMap(text: String?): MutableMap<String, String>? {
            if (text.isNullOrBlank()) return null
            val map = mutableMapOf<String, String>()
            text.split(',').forEach {
                if (it.isBlank()) return@forEach
                val (key, value) = it.split('=')
                map[key.trim()] = value.trim()
            }
            return map
        }

        private fun decodeList(text: String?): MutableList<NodeConfig>? {
            if (text.isNullOrBlank()) return null
            val list = mutableListOf<NodeConfig>()
            text.trimIndent().split('\n').forEach { list.add(decode(it)) }
            return list
        }

        fun decode(text: String): NodeConfig {
            val trimText = text.trim()
            val line1 = trimText.substringBefore(':')
            val splits = line1.split("->")
            val text1 = splits[0]
            val name = text1.subBetween('[', ']')
            val inputs = decodeMap(text1.subBetween('(', ')'))
            val type = text1.substringBefore(if (name != null) '[' else if (inputs != null) '(' else ':').trim()
            val text2 = if (splits.size > 1) splits[1] else null
            val outputs = decodeMap(text2?.subBetween('(', ')'))
            val children = decodeList(text.substringAfter(':', ""))
            return NodeConfig(name, type, inputs, outputs, children)
        }
    }
}