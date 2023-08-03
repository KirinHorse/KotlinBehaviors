package com.xqkj.app.bigclicker.btree.expression.functions

import org.kirinhorse.kbt.expression.Expression
import org.kirinhorse.kbt.expression.ExpArgument
import org.kirinhorse.kbt.expression.ExpFunction

class ExpSub(exp: Expression, keyWord: String, position: Int) : ExpFunction(exp, keyWord, position) {
    private fun fix(value: Int, a: Int, b: Int) = b.coerceAtMost(a.coerceAtLeast(value))

    override fun onResolve(): ExpArgument {
        if (args.size != 3) throw error
        val (text, start, end) = args
        if (!text.isText || !start.isInt || !end.isInt) throw error
        val length = text.text.length
        val startIndex = fix(0, start.int, length)
        val endIndex = fix(end.int, startIndex, length)
        val value = text.text.substring(startIndex, endIndex)
        return ExpArgument(exp, "", value)
    }
}

class ExpLength(exp: Expression, keyWord: String, position: Int) : ExpFunction(exp, keyWord, position) {
    override fun onResolve(): ExpArgument {
        if (args.size != 1) throw error
        val arg = args[0]
        val value = when {
            arg.isText -> arg.text.length
            arg.isVector2 -> arg.vector2.length()
            else -> throw error
        }
        return ExpArgument(exp, "", value)
    }
}

class ExpContains(exp: Expression, keyWord: String, position: Int) : ExpFunction(exp, keyWord, position) {
    override fun onResolve(): ExpArgument {
        if (args.size != 2) throw error
        val (text, target) = args
        val value = when {
            text.isText && target.isText -> text.text.contains(target.text)
            else -> throw error
        }
        return ExpArgument(exp, "", value)
    }
}

class ExpIndexOf(exp: Expression, keyWord: String, position: Int) : ExpFunction(exp, keyWord, position) {
    override fun onResolve(): ExpArgument {
        if (args.size != 2) throw error
        val (text, target) = args
        val value = when {
            text.isText && target.isText -> text.text.indexOf(target.text)
            else -> throw error
        }
        return ExpArgument(exp, "", value)
    }
}