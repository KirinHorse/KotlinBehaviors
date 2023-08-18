package org.kirinhorse.kbt

import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class BehaviorTreeTest {
    fun v(key: String) = "$$key"

    @Test
    fun testScript() {
        val exp = "\$x<\$y?\"less\":\$t"
        val script = """#VARIANTS_START
            |b:Bool=true
            |x:Int=5
            |y:Int=7
            |f:Float=8.0
            |t:Text=Some Text
            |v2:Vector2=(10,10)
            |#VARIANTS_END
            |#NODE_START
            |SEQ{
            |   delay(duration={${'$'}x*1000})
            |   print(text={$exp})
            |   repeat(times={${'$'}y/2}){
            |       SEQ{
            |           print(text=Tree execute)
            |           delay(duration={${'$'}f*50})
            |       }
            |   }
            |   print(text=Tree end ===>>>)
            |}
            |#NODE_END""".trimMargin()
        val tree = BehaviorTree(BehaviorTreeConfig.decode(script))
        println(tree.config.encode())
        runBlocking { tree.start() }
        assert(true)
    }

    @Test
    fun testTree() {
        //@formatter:off
        val root = KBTFactory.SEQ(
            mutableListOf(
                KBTFactory.delay(2000),
                KBTFactory.print("Tree start ===>>>"),
                KBTFactory.repeat(3,
                    KBTFactory.SEQ(mutableListOf(
                        KBTFactory.delay(1000),
                        KBTFactory.print("Some actions execute"),
                        KBTFactory.delay(500)
                    ))
                ),
                KBTFactory.print("Tree end <<<===")
            )
        )
        //@formatter:on
        val config = BehaviorTreeConfig(VariantsConfig(mutableMapOf()), root)
        val tree = BehaviorTree(config)
        runBlocking { tree.start() }
        assert(true)
    }
}