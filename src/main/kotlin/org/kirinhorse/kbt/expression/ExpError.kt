package com.xqkj.app.bigclicker.btree.expression

import org.kirinhorse.kbt.Error
import org.kirinhorse.kbt.expression.ExpOperator
import org.kirinhorse.kbt.expression.Expression

abstract class ExpError(val exp: Expression) : Error()

class ExpErrorOperator(private val opt: ExpOperator) : ExpError(opt.exp) {
    override val msg get() = "表达式错误：${exp.expText}中的${opt.keyWord}: 第${opt.position}"
}

class ExpErrorUnknownSymbol(exp: Expression) : ExpError(exp) {
    override val msg get() = "表达式错误：无法识别${exp.expText}"
}