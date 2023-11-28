package org.kirinhorse.kbt

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
        var indents = ""
        for (i in 0 until indent - 1) indents += "\t"
        return list.joinToString("\n", "{\n", "\n$indents}") { it.encode(indent) }
    }

    fun encode(indent: Int): String {
        var indents = ""
        for (i in 0 until indent) indents += "\t"
        val nameStr = if (name.isNullOrBlank()) "" else "[$name]"
        val inputsStr = if (inputs.isNullOrEmpty()) "" else "(${encodeMap(inputs)})"
        val outputsStr = if (outputs.isNullOrEmpty()) "" else "->(${encodeMap(outputs)})"
        val body = encodeList(children, indent + 1)
        return "$indents$type$nameStr$inputsStr$outputsStr$body"
    }

    companion object {
        private fun decodeMap(text: String?): MutableMap<String, String>? {
            if (text.isNullOrBlank()) return null
            val map = mutableMapOf<String, String>()
            text.split(',').forEach {
                if (it.isBlank()) return@forEach
                val (key, value) = it.split('=', limit = 2)
                map[key.trim()] = value.trim()
            }
            return map
        }

        fun decodeList(text: String?): MutableList<NodeConfig>? {
            if (text.isNullOrBlank()) return null
            val list = mutableListOf<NodeConfig>()
            var parser = Parser(text.trim())
            do {
                if (list.isNotEmpty()) parser = Parser(parser.next!!)
                val builder = KBTFactory.getBuilder(parser.type!!)
                val inputs = decodeMap(parser.inputs) ?: if (builder.inputs.isEmpty()) null else mutableMapOf()
                val outputs = decodeMap(parser.outputs) ?: if (builder.outputs.isEmpty()) null else mutableMapOf()
                val children = decodeList(parser.children) ?: if (!builder.hasChildren) null else mutableListOf()
                val config = NodeConfig(parser.name, parser.type!!, inputs, outputs, children)
                list.add(config)
            } while (parser.hasNext)
            return list
        }

        /***
         * #type[#name](#input)->(#output):#children
         * ```sample:
         * SEQ:
         *  delay(duration=2000)
         *  print(text="some texts")
         * ```
         */
        fun decode(text: String): NodeConfig? = decodeList(text)?.firstOrNull()
    }

    class Parser(val text: String) {
        var type: String? = null
        var name: String? = null
        var inputs: String? = null
        var outputs: String? = null
        var children: String? = null
        var hasNext = false
        var next: String? = null

        private val chars = text.iterator()

        init {
            check()
        }

        private fun check() {
            val sb = StringBuffer()
            while (chars.hasNext()) {
                val c = chars.next()
                when (c) {
                    '[' -> if (name == null) {
                        readType(c, sb)
                        readName()
                        return
                    }

                    '(' -> if (inputs == null) {
                        readType(c, sb)
                        readInputs()
                        return
                    } else if (outputs == null) {
                        readType(c, sb)
                        readOutputs()
                        return
                    }

                    '>' -> if (sb.lastOrNull() == '-' && outputs == null) {
                        if (inputs == null) inputs = ""
                        readType(c, sb)
                    }

                    '{' -> if (children == null) {
                        readType(c, sb)
                        readChildren()
                        return
                    }

                    '\n' -> {
                        readType(c, sb)
                        if (chars.hasNext()) {
                            hasNext = true
                            val remain = StringBuffer()
                            while (chars.hasNext()) remain.append(chars.next())
                            next = remain.toString()
                        }
                        return
                    }
                }
                sb.append(c)
            }
            readType(' ', sb)
        }

        private fun readType(c: Char, sb: StringBuffer) {
            if (type != null) return
            type = if (c == '>') sb.substring(0, sb.length - 1).trim()
            else sb.toString().trim()
        }

        private fun read(left: Char, right: Char): String {
            val sb = StringBuffer()
            var deep = 0
            while (chars.hasNext()) {
                val c = chars.next()
                if (c == left) deep++
                else if (c == right) {
                    if (deep == 0) return sb.toString().trim()
                    deep--
                }
                sb.append(c)
            }
            return sb.toString().trim()
        }

        private fun readName() {
            name = read('[', ']')
            check()
        }

        private fun readInputs() {
            inputs = read('(', ')')
            check()
        }

        private fun readOutputs() {
            outputs = read('(', ')')
            check()
        }

        private fun readChildren() {
            children = read('{', '}')
            check()
        }
    }
}