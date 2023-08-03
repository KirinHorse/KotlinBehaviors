package com.xqkj.app.bigclicker.btree.expression.symbols

import org.kirinhorse.kbt.expression.Expression
import org.kirinhorse.kbt.expression.ExpArgument
import org.kirinhorse.kbt.expression.ExpOperator

class ExpConditional(exp: Expression, keyWord: String, position: Int) : ExpOperator(exp, keyWord, position) {
    companion object {
        val delimiter = listOf("?", ":")
    }

    private lateinit var condition: ExpArgument
    private lateinit var left: ExpArgument
    private lateinit var right: ExpArgument

    override fun parseArgs() {
        condition = joinToArg(tokens, 0, position)
        val resText = tokens.subList(position + 1, tokens.size).joinToString("")
        val resTokens = Expression.tokenize(resText, delimiter)
        var resPos = 0
        var inCond = 0
        for (pos in resTokens.indices) {
            val token = resTokens[pos]
            if (token == "?") inCond++
            else if (token == ":") {
                if (inCond == 0) {
                    resPos = pos
                    break
                }
                inCond--
            }
        }
        left = joinToArg(resTokens, 0, resPos)
        right = joinToArg(resTokens, resPos + 1, resTokens.size)
    }

    override fun onResolve(): ExpArgument {
        logBuffer?.append("计算：$condition?$left:$right => ")
        while (condition.isExpression) condition = Expression(exp.variants, condition.expression).evaluate()
        if (!condition.isBool) throw error
        var arg = if (condition.bool) left else right
        while (arg.isExpression) arg = Expression(exp.variants, arg.expression).evaluate()
        logBuffer?.append("$condition?$left:$right = ")
        return arg
    }
}