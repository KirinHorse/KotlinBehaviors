package org.kirinhorse.kbt.expression

import com.xqkj.app.bigclicker.btree.expression.functions.ExpAbs
import com.xqkj.app.bigclicker.btree.expression.functions.ExpCeil
import com.xqkj.app.bigclicker.btree.expression.functions.ExpContains
import com.xqkj.app.bigclicker.btree.expression.functions.ExpFloor
import com.xqkj.app.bigclicker.btree.expression.functions.ExpIndexOf
import com.xqkj.app.bigclicker.btree.expression.functions.ExpLength
import com.xqkj.app.bigclicker.btree.expression.functions.ExpMax
import com.xqkj.app.bigclicker.btree.expression.functions.ExpMin
import com.xqkj.app.bigclicker.btree.expression.functions.ExpRandom
import com.xqkj.app.bigclicker.btree.expression.functions.ExpRound
import com.xqkj.app.bigclicker.btree.expression.functions.ExpSub
import com.xqkj.app.bigclicker.btree.expression.symbols.ExpAnd
import com.xqkj.app.bigclicker.btree.expression.symbols.ExpConditional
import com.xqkj.app.bigclicker.btree.expression.symbols.ExpDiv
import com.xqkj.app.bigclicker.btree.expression.symbols.ExpEqual
import com.xqkj.app.bigclicker.btree.expression.symbols.ExpGreaterThan
import com.xqkj.app.bigclicker.btree.expression.symbols.ExpGreaterThanOrEqual
import com.xqkj.app.bigclicker.btree.expression.symbols.ExpLessThan
import com.xqkj.app.bigclicker.btree.expression.symbols.ExpLessThanOrEqual
import com.xqkj.app.bigclicker.btree.expression.symbols.ExpMinus
import com.xqkj.app.bigclicker.btree.expression.symbols.ExpNot
import com.xqkj.app.bigclicker.btree.expression.symbols.ExpNotEqual
import com.xqkj.app.bigclicker.btree.expression.symbols.ExpOr
import com.xqkj.app.bigclicker.btree.expression.symbols.ExpPlus
import com.xqkj.app.bigclicker.btree.expression.symbols.ExpRem
import com.xqkj.app.bigclicker.btree.expression.symbols.ExpTimes
import com.xqkj.app.bigclicker.btree.expression.symbols.ExpVector2
import com.xqkj.app.bigclicker.btree.expression.symbols.ExpXor
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