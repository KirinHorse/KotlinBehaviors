package org.kirinhorse.kbt.expression

import org.kirinhorse.kbt.Error

abstract class ExpError(val exp: Expression) : Error()

class ExpErrorOperator(private val opt: ExpOperator) : ExpError(opt.exp) {
    override val msg get() = "表达式错误：${exp.expText}中的${opt.keyWord}: 第${opt.position}"
}

class ExpErrorUnknownSymbol(exp: Expression) : ExpError(exp) {
    override val msg get() = "表达式错误：无法识别${exp.expText}"
}