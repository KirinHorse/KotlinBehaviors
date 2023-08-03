package org.kirinhorse.kbt.expression

abstract class ExpFunction(exp: Expression, keyWord: String, position: Int) : ExpOperator(exp, keyWord, position) {
    companion object {
        val argsKeyWords = listOf(",")
    }

    val args = mutableListOf<ExpArgument>()

    override fun parseArgs() {
        if (position >= tokens.size - 1) throw error
        var argsText = tokens[position + 1]
        if (!argsText.startsWith('(') || !argsText.endsWith(')')) throw error
        argsText = argsText.substring(1, argsText.length - 1)
        val argsTokens = Expression.tokenize(argsText, argsKeyWords)
        for (token in argsTokens) {
            if (token == ",") continue
            var arg = ExpArgument(exp, token)
            while (arg.isExpression) arg = Expression(exp.variants, arg.expression).evaluate()
            args.add(arg)
        }
    }
}