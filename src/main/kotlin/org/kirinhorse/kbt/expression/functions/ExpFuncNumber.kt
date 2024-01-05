package org.kirinhorse.kbt.expression.functions

import org.kirinhorse.kbt.expression.ExpArgument
import org.kirinhorse.kbt.expression.ExpFunction
import org.kirinhorse.kbt.expression.Expression
import org.kirinhorse.kbt.types.KBTVector2
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

@Suppress("IMPLICIT_CAST_TO_ANY")
class ExpRandom(exp: Expression, keyWord: String, position: Int) : ExpFunction(exp, keyWord, position) {
    override fun onResolve(): ExpArgument {
        if (args.size != 2) throw error
        val (left, right) = args
        val value = when {
            left.isInt && right.isInt -> Random.nextInt(left.int, right.int + 1)
            left.isNumber && right.isNumber -> Random.nextDouble(left.float.toDouble(), right.float.toDouble())
                .toFloat()

            left.isVector2 && right.isVector2 -> {
                val x = left.vector2.x
                val y = left.vector2.y
                val dx = right.vector2.x
                val dy = right.vector2.y
                val rx = Random.nextDouble((x - dx).toDouble(), (x + dx).toDouble()).toFloat()
                val ry = Random.nextDouble((y - dy).toDouble(), (y + dy).toDouble()).toFloat()
                KBTVector2(rx, ry)
            }

            left.isVector2 && right.isNumber -> {
                val x = left.vector2.x
                val y = left.vector2.y
                val dr = right.float
                val rx = Random.nextDouble((x - dr).toDouble(), (x + dr).toDouble()).toFloat()
                val ry = Random.nextDouble((y - dr).toDouble(), (y + dr).toDouble()).toFloat()
                KBTVector2(rx, ry)
            }

            else -> throw error
        }
        return ExpArgument(exp, "", value)
    }
}
