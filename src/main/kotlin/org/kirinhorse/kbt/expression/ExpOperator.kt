package org.kirinhorse.kbt.expression

import com.xqkj.app.bigclicker.btree.expression.ExpErrorOperator

abstract class ExpOperator(val exp: Expression, val keyWord: String, val position: Int) {
    private val priority get() = ExpFactory.optBuilders[keyWord]!!.priority
    private val leftCombine get() = ExpFactory.optBuilders[keyWord]!!.leftCombine

    val logBuffer: StringBuffer? = null
    val tokens get() = exp.tokens

    companion object {
        val comparator: Comparator<ExpOperator> = Comparator { a, b ->
            val result = a.priority - b.priority
            if (result != 0) result
            else if (a.leftCombine || b.leftCombine) 1
            else -1
        }
    }

    fun joinToArg(list: List<String>, start: Int, end: Int): ExpArgument {
        val buffer = StringBuffer()
        for (i in start until end) {
            buffer.append(list[i])
        }
        return ExpArgument(exp, buffer.toString())
    }

    fun resolve(): ExpArgument {
        parseArgs()
        val result = onResolve()
        if (logBuffer != null) {
            logBuffer.append(result.toString())
            println(logBuffer.toString())
        }
        return result
    }

    val error get() = ExpErrorOperator(this)

    abstract fun parseArgs()
    abstract fun onResolve(): ExpArgument

}