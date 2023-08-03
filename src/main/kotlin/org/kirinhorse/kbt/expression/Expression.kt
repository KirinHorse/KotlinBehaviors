package org.kirinhorse.kbt.expression

import com.xqkj.app.bigclicker.btree.expression.ExpErrorUnknownSymbol
import org.kirinhorse.kbt.Variants

class Expression(val variants: Variants, val expText: String) {
    companion object {
        const val STRING_WRAPPER_CHAR = '\"'

        fun tokenize(text: String, keyWords: List<String>): List<String> {
            val tokens = mutableListOf<String>()
            var depth = 0
            var insideString = false
            var currentToken = StringBuffer()
            var index = 0
            while (index < text.length) {
                var add = true
                val char = text[index]
                when {
                    char == '(' -> depth++
                    char == ')' -> depth--
                    char == STRING_WRAPPER_CHAR -> insideString = !insideString
                    depth == 0 && !insideString -> {
                        val rest = text.substring(index)
                        var maxKeyWord: String? = null
                        for (symbol in keyWords) {
                            if (!rest.startsWith(symbol)) continue
                            if (maxKeyWord == null || maxKeyWord.length < symbol.length) {
                                maxKeyWord = symbol
                            }
                        }
                        if (maxKeyWord != null) {
                            if (currentToken.isNotBlank()) {
                                tokens.add(currentToken.toString().trim())
                                currentToken = StringBuffer()
                            }
                            tokens.add(maxKeyWord)
                            index += maxKeyWord.length - 1
                            add = false
                        }
                    }
                }
                if (add) currentToken.append(char)
                index++
            }
            tokens.add(currentToken.toString().trim())
            //println("解析: $text => ${tokens.joinToString("|")}")
            return tokens
        }
    }

    val tokens = tokenize(expText, ExpFactory.keyWords)

    fun evaluate(): ExpArgument {
        val text = expText.trim()
        var minOpt: ExpOperator? = null
        for (i in tokens.indices) {
            val token = tokens[i]
            val opt = ExpFactory.createOperator(this, token, i) ?: continue
            if (minOpt == null) minOpt = opt
            else if (ExpOperator.comparator.compare(minOpt, opt) > 0) minOpt = opt
        }
        if (minOpt != null) return minOpt.resolve()
        if (text.startsWith('$')) {
            val variant = variants.getVariant(text.substring(1))
            return ExpArgument(this, "", variant.value)
        }
        if (text.startsWith('(') && text.endsWith(')')) {
            return ExpArgument(this, text.substring(1, text.length - 1))
        }
        val arg = ExpArgument(this, text)
        if (arg.isExpression) throw ExpErrorUnknownSymbol(this)
        return arg
    }
}