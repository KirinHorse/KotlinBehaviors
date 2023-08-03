package com.xqkj.app.bigclicker.btree.expression.symbols

import org.kirinhorse.kbt.expression.Expression
import org.kirinhorse.kbt.expression.ExpArgument
import org.kirinhorse.kbt.expression.ExpSymbol

class ExpPlus(exp: Expression, keyWord: String, position: Int) : ExpSymbol(exp, keyWord, position) {
    override fun onResolve(): ExpArgument {
        val value: Any = when {
            left.isInt && right.isInt -> left.int + right.int
            left.isNumber && right.isNumber -> left.float + right.float
            left.isVector2 && right.isVector2 -> left.vector2.plus(right.vector2)
            left.isText || right.isText -> "${left}${right}"
            else -> throw error
        }
        return ExpArgument(exp, "", value)
    }
}

class ExpMinus(exp: Expression, keyWord: String, position: Int) : ExpSymbol(exp, keyWord, position) {
    override fun onResolve(): ExpArgument {
        val value: Any = when {
            left.isNull -> when {
                right.isInt -> -right.int
                right.isFloat -> -right.float
                right.isVector2 -> -right.vector2
                else -> throw error
            }

            left.isInt && right.isInt -> left.int - right.int
            left.isNumber && right.isNumber -> left.float - right.float
            left.isVector2 && right.isVector2 -> left.vector2.minus(right.vector2)
            else -> throw error
        }
        return ExpArgument(exp, "", value)
    }
}

class ExpTimes(exp: Expression, keyWord: String, position: Int) : ExpSymbol(exp, keyWord, position) {
    override fun onResolve(): ExpArgument {
        val value: Any = when {
            left.isInt && right.isInt -> left.int * right.int
            left.isNumber && right.isNumber -> left.float * right.float
            left.isVector2 && right.isNumber -> left.vector2.times(right.float)
            left.isNumber && right.isVector2 -> right.vector2.times(left.float)
            else -> throw error
        }
        return ExpArgument(exp, "", value)
    }
}

class ExpDiv(exp: Expression, keyWord: String, position: Int) : ExpSymbol(exp, keyWord, position) {
    override fun onResolve(): ExpArgument {
        val value: Any = when {
            left.isNumber && right.isNumber -> left.float / right.float
            left.isVector2 && right.isNumber -> left.vector2.div(right.float)
            else -> throw error
        }
        return ExpArgument(exp, "", value)
    }
}

class ExpRem(exp: Expression, keyWord: String, position: Int) : ExpSymbol(exp, keyWord, position) {
    override fun onResolve(): ExpArgument {
        val value = when {
            left.isInt && right.isInt -> left.int % right.int
            left.isNumber && right.isNumber -> left.float % right.float
            else -> throw error
        }
        return ExpArgument(exp, "", value)
    }
}