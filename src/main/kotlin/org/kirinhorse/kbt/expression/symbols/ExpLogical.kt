package com.xqkj.app.bigclicker.btree.expression.symbols

import org.kirinhorse.kbt.expression.Expression
import org.kirinhorse.kbt.expression.ExpArgument
import org.kirinhorse.kbt.expression.ExpSymbol

class ExpAnd(exp: Expression, keyWord: String, position: Int) : ExpSymbol(exp, keyWord, position) {
    override fun onResolve(): ExpArgument {
        val value = when {
            left.isBool && right.isBool -> left.bool && right.bool
            else -> throw error
        }
        return ExpArgument(exp, "", value)
    }
}

class ExpOr(exp: Expression, keyWord: String, position: Int) : ExpSymbol(exp, keyWord, position) {
    override fun onResolve(): ExpArgument {
        val value = when {
            left.isBool && right.isBool -> left.bool || right.bool
            else -> throw error
        }
        return ExpArgument(exp, "", value)
    }
}

class ExpNot(exp: Expression, keyWord: String, position: Int) : ExpSymbol(exp, keyWord, position) {
    override fun onResolve(): ExpArgument {
        val value = when {
            right.isBool -> !right.bool
            else -> !right.isNull
        }
        return ExpArgument(exp, "", value)
    }
}

class ExpXor(exp: Expression, keyWord: String, position: Int) : ExpSymbol(exp, keyWord, position) {
    override fun onResolve(): ExpArgument {
        val value = when {
            left.isBool && right.isBool -> left.bool xor right.bool
            else -> throw error
        }
        return ExpArgument(exp, "", value)
    }
}