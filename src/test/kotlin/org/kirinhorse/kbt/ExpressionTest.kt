package org.kirinhorse.kbt

import org.kirinhorse.kbt.expression.ExpFactory
import org.kirinhorse.kbt.types.KBTVector2
import kotlin.test.Test
import kotlin.test.assertEquals

class ExpressionTest {
    private fun createVariants(vararg pairs: Pair<String, Any>): Variants {
        val vps = pairs.associate { it.first to Variants.Variant(it.first, Types.getType(it.second)!!, it.second) }
        return Variants(vps.toMutableMap())
    }

    private val variants = createVariants(
        "check" to true,
        "intX" to 3,
        "intY" to 5,
        "nIntX" to -3,
        "nIntY" to -5,
        "floatX" to 5.5f,
        "floatY" to 3.2f,
        "nFloatX" to -3.6f,
        "nFloatY" to -4.5f,
        "text1" to "我是中国人",
        "text2" to "  ",
        "text3" to "I'm chinese",
        "vector2" to KBTVector2(3, 4),
    )

    private val successful = mapOf(
        "(10,10)" to KBTVector2(10, 10),
        "100+ \$intX" to 103,
        "(((10,10)))" to KBTVector2(10, 10),
        "(50 , 60) * random(1, 1)" to KBTVector2(50, 60),
        "!\$check" to false,
        "\"56789\"" to "56789",
        "56789" to 56789,
        "5*3/(4+2)-(-5+1)*2" to 10.5f,
        "!\$check||\$intX>0&&\$nIntY>=0||\$intX<=0&&\$nIntY<0" to false,
        "\$intX<0?\$vector2:\$intY>0? (0,\$intY):(0,0)" to KBTVector2(0, 5),
        "!\$check?0:length(\$text1)<=10?1:-1" to 1,
        "\$check?length(\$text1)>10?1:-1:0" to -1,
        "\$check?-10:20+length(\$text1)>10?-1:-2" to -10,
        "length(\$vector2)==5&& contains(\$text3,\"'\") && length(\$text1) > indexOf(\$text3,\"e\")" to false,
        "random(2,3) >= 2" to true,
        "random(2.5,3) < 3" to true,
        "length(random((2,3),1)) < 5" to true,
    )

    @Test
    fun evaluate() {
        for ((text, expected) in successful.entries) {
            val result = ExpFactory.evaluate(variants, text)
            assertEquals(expected, result)
        }
    }
}