# KotlinBehaviors

Behavior Trees Library in Kotlin, with variables and expressions support.

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.kirinhorse/kotlin-behaviors/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.kirinhorse/kotlin-behaviors)
[![Release](https://github.com/KirinHorse/KotlinBehaviors/actions/workflows/release.yml/badge.svg)](https://github.com/KirinHorse/KotlinBehaviors/actions/workflows/release.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Usage 使用方法

### Config dependency in build.gradle 配置依赖

```groovy
dependencies {
    implementation 'io.github.kirinhorse:kotlin-behaviors:0.0.3'
}
```

### Construct your behavior tree 构造行为树

```kotlin
//@formatter:off
val root = KBTFactory.SEQ(
    mutableListOf(
        KBTFactory.print("Tree start ===>>>"),
        KBTFactory.delay(1000),
        KBTFactory.repeat(2,
            KBTFactory.SEQ(mutableListOf(
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
```

### Construct behavior tree by script 使用脚本构造行为树

```text
#NODE_START
SEQ{
    print(text=Tree Start ===>>>)
    delay(duration=1000)
    repeat(times=2){
        SEQ{
            print(text=Some actions execute)
            delay(duration=500)
        }
    }
    print(text=Tree end <<<===)
}
#NODE_END
```

```kotlin
val script = readFile() //load text above
val tree = BTFactory.createTree(script)
runBlocking { tree.start() }
```