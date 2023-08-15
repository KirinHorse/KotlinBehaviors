package org.kirinhorse.kbt.expression.functions

import org.kirinhorse.kbt.expression.Expression
import org.kirinhorse.kbt.expression.ExpArgument
import org.kirinhorse.kbt.expression.ExpFunction
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.random.Random

class ExpMax(exp: Expression, keyWord: String, position: Int) : ExpFunction(exp, keyWord, position) {
    override fun onResolve(): ExpArgument {
        if (args.size < 2) throw error
        val isInt = args.all { it.isInt }
        val isFloat = args.all { it.isFloat }
        val value = when {
            isInt -> args.maxOf { it.int }
            isFloat -> args.maxOf { it.float }
            else -> throw error
        }
        return ExpArgument(exp, "", value)
    }
}

class ExpMin(exp: Expression, keyWord: String, position: Int) : ExpFunction(exp, keyWord, position) {
    override fun onResolve(): ExpArgument {
        if (args.size < 2) throw error
        val isInt = args.all { it.isInt }
        val isFloat = args.all { it.isFloat }
        val value = when {
            isInt -> args.minOf { it.int }
            isFloat -> args.minOf { it.float }
            else -> throw error
        }
        return ExpArgument(exp, "", value)
    }
}

class ExpAbs(exp: Expression, keyWord: String, position: Int) : ExpFunction(exp, keyWord, position) {
    override fun onResolve(): ExpArgument {
        if (args.size != 1) throw error
        val arg = args.first()
        val value = when {
            arg.isInt -> abs(arg.int)
            arg.isFloat -> abs(arg.float)
            else -> throw error
        }
        return ExpArgument(exp, "", value)
    }
}

class ExpFloor(exp: Expression, keyWord: String, position: Int) : ExpFunction(exp, keyWord, position) {
    override fun onResolve(): ExpArgument {
        if (args.size != 1) throw error
        val arg = args[0]
        val value = when {
            arg.isNumber -> floor(arg.float).toInt()
            else -> throw error
        }
        return ExpArgument(exp, "", value)
    }
}


class ExpCeil(exp: Expression, keyWord: String, position: Int) : ExpFunction(exp, keyWord, position) {
    override fun onResolve(): ExpArgument {
        if (args.size != 1) throw error
        val arg = args[0]
        val value = when {
            arg.isNumber -> ceil(arg.float).toInt()
            else -> throw error
        }
        return ExpArgument(exp, "", value)
    }
}

class ExpRound(exp: Expression, keyWord: String, position: Int) : ExpFunction(exp, keyWord, position) {
    override fun onResolve(): ExpArgument {
        if (args.size != 1) throw error
        val arg = args[0]
        val value = when {
            arg.isNumber -> arg.float.roundToInt()
            else -> throw error
        }
        return ExpArgument(exp, "", value)
    }
}

class ExpRandom(exp: Expression, keyWord: String, position: Int) : ExpFunction(exp, keyWord, position) {
    override fun onResolve(): ExpArgument {
        if (args.size != 2) throw error
        val (from, to) = args
        val value = when {
            from.isInt && to.isInt -> Random.nextInt(from.int, to.int + 1)
            from.isNumber && to.isNumber -> Random.nextDouble(from.float.toDouble(), to.float.toDouble()).toFloat()
            else -> throw error
        }
        return ExpArgument(exp, "", value)
    }
}
