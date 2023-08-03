package com.xqkj.app.bigclicker.btree.expression.symbols

import org.kirinhorse.kbt.expression.Expression
import org.kirinhorse.kbt.expression.ExpArgument
import org.kirinhorse.kbt.expression.ExpSymbol

class ExpEqual(exp: Expression, keyWord: String, position: Int) : ExpSymbol(exp, keyWord, position) {
    override fun onResolve(): ExpArgument = ExpArgument(exp, "", left == right)
}

class ExpNotEqual(exp: Expression, keyWord: String, position: Int) : ExpSymbol(exp, keyWord, position) {
    override fun onResolve(): ExpArgument = ExpArgument(exp, "", left != right)
}


class ExpGreaterThan(exp: Expression, keyWord: String, position: Int) : ExpSymbol(exp, keyWord, position) {
    override fun onResolve(): ExpArgument {
        val value = when {
            left.isFloat && right.isFloat -> left.float > right.float
            else -> throw error
        }
        return ExpArgument(exp, "", value)
    }
}

class ExpGreaterThanOrEqual(exp: Expression, keyWord: String, position: Int) : ExpSymbol(exp, keyWord, position) {
    override fun onResolve(): ExpArgument {
        val value = when {
            left.isFloat && right.isFloat -> left.float >= right.float
            else -> throw error
        }
        return ExpArgument(exp, "", value)
    }
}

class ExpLessThan(exp: Expression, keyWord: String, position: Int) : ExpSymbol(exp, keyWord, position) {
    override fun onResolve(): ExpArgument {
        val value = when {
            left.isFloat && right.isFloat -> left.float < right.float
            else -> throw error
        }
        return ExpArgument(exp, "", value)
    }
}

class ExpLessThanOrEqual(exp: Expression, keyWord: String, position: Int) : ExpSymbol(exp, keyWord, position) {
    override fun onResolve(): ExpArgument {
        val value = when {
            left.isFloat && right.isFloat -> left.float <= right.float
            else -> throw error
        }
        return ExpArgument(exp, "", value)
    }
}