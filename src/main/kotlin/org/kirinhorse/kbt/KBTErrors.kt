package org.kirinhorse.kbt

abstract class Error : Exception() {
    abstract val msg: String
    override val message get() = msg
}

class ErrorDataEmpty(private val key: String) : Error() {
    override val msg get() = "参数不能为空：${key}"
}

class ErrorInputNotFound(private val key: String) : Error() {
    override val msg get() = "参数名称不存在:${key}"
}

class ErrorVariantNotFound(private val key: String) : Error() {
    override val msg get() = "未找到变量:${key}"
}

class ErrorTypeWrong(private val key: String) : Error() {
    override val msg get() = "数据类型错误:${key}"
}

class ErrorActionNotSupport(private val type: String) : Error() {
    override val msg get() = "当前设备不支持该操作:$type"
}