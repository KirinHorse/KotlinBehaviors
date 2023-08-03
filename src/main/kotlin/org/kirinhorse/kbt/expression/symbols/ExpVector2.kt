package com.xqkj.app.bigclicker.btree.expression.symbols

import org.kirinhorse.kbt.expression.Expression
import org.kirinhorse.kbt.expression.ExpArgument
import org.kirinhorse.kbt.expression.ExpSymbol
import com.xqkj.app.bigclicker.btree.types.KBTVector2

class ExpVector2(exp: Expression, keyWord: String, position: Int) : ExpSymbol(exp, keyWord, position) {
    override fun onResolve(): ExpArgument {
        val x = resolve(left) ?: throw error
        val y = resolve(right) ?: throw error
        return ExpArgument(exp, "", KBTVector2(x, y))
    }

    private fun resolve(arg: ExpArgument): Float? {
        return when {
            arg.isNumber -> arg.float
            arg.isExpression -> {
                val result = Expression(exp.variants, arg.expression).evaluate()
                if (result.isNumber) result.float else null
            }

            else -> null
        }
    }
}