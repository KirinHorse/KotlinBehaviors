package org.kirinhorse.kbt.expression

import org.kirinhorse.kbt.expression.functions.ExpAbs
import org.kirinhorse.kbt.expression.functions.ExpCeil
import org.kirinhorse.kbt.expression.functions.ExpContains
import org.kirinhorse.kbt.expression.functions.ExpFloor
import org.kirinhorse.kbt.expression.functions.ExpIndexOf
import org.kirinhorse.kbt.expression.functions.ExpLength
import org.kirinhorse.kbt.expression.functions.ExpMax
import org.kirinhorse.kbt.expression.functions.ExpMin
import org.kirinhorse.kbt.expression.functions.ExpRandom
import org.kirinhorse.kbt.expression.functions.ExpRound
import org.kirinhorse.kbt.expression.functions.ExpSub
import org.kirinhorse.kbt.expression.symbols.ExpAnd
import org.kirinhorse.kbt.expression.symbols.ExpConditional
import org.kirinhorse.kbt.expression.symbols.ExpDiv
import org.kirinhorse.kbt.expression.symbols.ExpEqual
import org.kirinhorse.kbt.expression.symbols.ExpGreaterThan
import org.kirinhorse.kbt.expression.symbols.ExpGreaterThanOrEqual
import org.kirinhorse.kbt.expression.symbols.ExpLessThan
import org.kirinhorse.kbt.expression.symbols.ExpLessThanOrEqual
import org.kirinhorse.kbt.expression.symbols.ExpMinus
import org.kirinhorse.kbt.expression.symbols.ExpNot
import org.kirinhorse.kbt.expression.symbols.ExpNotEqual
import org.kirinhorse.kbt.expression.symbols.ExpOr
import org.kirinhorse.kbt.expression.symbols.ExpPlus
import org.kirinhorse.kbt.expression.symbols.ExpRem
import org.kirinhorse.kbt.expression.symbols.ExpTimes
import org.kirinhorse.kbt.expression.symbols.ExpVector2
import org.kirinhorse.kbt.expression.symbols.ExpXor
import org.kirinhorse.kbt.Variants
import kotlin.reflect.KClass

object ExpFactory {
    class OperatorBuilder<T : ExpOperator>(
        private val keyWord: String, val priority: Int, val leftCombine: Boolean, clazz: KClass<T>
    ) {
        private val constructor = clazz.constructors.first()

        fun build(exp: Expression, position: Int) = constructor.call(exp, keyWord, position)
    }

    val optBuilders = mutableMapOf<String, OperatorBuilder<*>>()

    private fun <T : ExpOperator> registerOpt(keyWord: String, priority: Int, leftCombine: Boolean, clazz: KClass<T>) {
        optBuilders[keyWord] = OperatorBuilder(keyWord, priority, leftCombine, clazz)
    }

    private const val p_fun = 18

    init {
        registerOpt("+", 12, true, ExpPlus::class)
        registerOpt("-", 12, true, ExpMinus::class)
        registerOpt("*", 13, true, ExpTimes::class)
        registerOpt("/", 13, true, ExpDiv::class)
        registerOpt("%", 13, true, ExpRem::class)
        registerOpt(",", 1, true, ExpVector2::class)
        registerOpt("&&", 5, true, ExpAnd::class)
        registerOpt("&", 5, true, ExpAnd::class)
        registerOpt("||", 4, true, ExpOr::class)
        registerOpt("|", 4, true, ExpOr::class)
        registerOpt("!", 15, false, ExpNot::class)
        registerOpt("^", 15, false, ExpXor::class)
        registerOpt("==", 9, true, ExpEqual::class)
        registerOpt("=", 9, true, ExpEqual::class)
        registerOpt("!=", 9, true, ExpNotEqual::class)
        registerOpt(">=", 10, true, ExpGreaterThanOrEqual::class)
        registerOpt(">", 10, true, ExpGreaterThan::class)
        registerOpt("<=", 10, true, ExpLessThanOrEqual::class)
        registerOpt("<", 10, true, ExpLessThan::class)
        registerOpt("?", 3, false, ExpConditional::class)
        registerOpt("max", p_fun, true, ExpMax::class)
        registerOpt("min", p_fun, true, ExpMin::class)
        registerOpt("abs", p_fun, true, ExpAbs::class)
        registerOpt("floor", p_fun, true, ExpFloor::class)
        registerOpt("ceil", p_fun, true, ExpCeil::class)
        registerOpt("round", p_fun, true, ExpRound::class)
        registerOpt("sub", p_fun, true, ExpSub::class)
        registerOpt("length", p_fun, true, ExpLength::class)
        registerOpt("contains", p_fun, true, ExpContains::class)
        registerOpt("indexOf", p_fun, true, ExpIndexOf::class)
        registerOpt("random", p_fun, true, ExpRandom::class)
    }

    val keyWords = optBuilders.keys.toList()

    fun createOperator(exp: Expression, keyWord: String, position: Int) = optBuilders[keyWord]?.build(exp, position)

    fun evaluate(variants: Variants, exp: String): Any? {
        val arg = Expression(variants, exp).evaluate()
        return when {
            arg.isBool -> arg.bool
            arg.isInt -> arg.int
            arg.isFloat -> arg.float
            arg.isText -> arg.text
            arg.isVector2 -> arg.vector2
            else -> null
        }
    }
}