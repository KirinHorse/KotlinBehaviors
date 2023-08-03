package org.kirinhorse.kbt.expression

abstract class ExpSymbol(exp: Expression, keyWord: String, position: Int) : ExpOperator(exp, keyWord, position) {
    lateinit var left: ExpArgument
    lateinit var right: ExpArgument

    override fun parseArgs() {
        left = joinToArg(tokens, 0, position)
        right = joinToArg(tokens, position + 1, tokens.size)
        logBuffer?.append("计算：$left$keyWord$right => ")
        while (left.isExpression) left = Expression(exp.variants, left.expression).evaluate()
        while (right.isExpression) right = Expression(exp.variants, right.expression).evaluate()
        logBuffer?.append("$left$keyWord$right = ")
    }
}