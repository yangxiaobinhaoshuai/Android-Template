
# KCP


### 使用探索

暂无可用文档，需要 debug gradle build

可用接口：
- IrGenerationExtension，用于增/删/改/查代码

- DiagnosticSuppressor，用于抑制语法错误，Jetpack Compose有使用

- StorageComponentContainerContributor，用于实现IOC


### KCP 相比 KSP 的缺点
1. API not stable.
2. Complex api.
3. Allow modifying existing code.
4. not easy
5. not fast
6. 仅限于修改当前 project 的 kt 源文件  (其他模块无法获取，非 kt 文件不会处理）


### 相比 KSP 的优点
1. Modify funcs & classes
3. APT is Jvm only , but kcp not



### Annotation Processor Vs KCP
APT
1. Works in compile time
2. emit source
3. work on java or kotlin

KCP
1. Works in compile time
2. emit bytecode
3. work ONLY on kt


### Steps
1. Intellij idea plugin (optional)
2. Gradle plugin
3. Jvm / js / native extension


### Prior arts
- noarg  constructor
- Android-extension (findViewById)
- parcelize
- kotlin - serialization

### Ref
- [Kotlin Compiler Plugin Doc](https://kotlinlang.org/docs/all-open-plugin.html)

- [Writing Your First Kotlin Compiler Plugin.pdf](https://resources.jetbrains.com/storage/products/kotlinconf2018/slides/5_Writing%20Your%20First%20Kotlin%20Compiler%20Plugin.pdf)

- [Writing Your First Kotlin Compiler Plugin YTB](https://www.youtube.com/watch?v=w-GMlaziIyo)

- [What does a compiler do?](https://www.youtube.com/watch?v=iTdJJq_LyoY)

- [KMPerferHugo demo(Hugo demo)](https://github.com/roths/KMPerformance)

- [KCP 埋点插件](https://toutiao.io/posts/uaiab50/preview)


- [ZacSweers](https://github.com/ZacSweers/redacted-compiler-plugin)

- [experiment with kotlin compiler](https://www.youtube.com/watch?v=dXg2golPDqM)

- [k2 compiler](https://www.youtube.com/watch?v=iTdJJq_LyoY)

- [Kt compiler 架构理解](https://github.com/ahinchman1/Kotlin-Compiler-Crash-Course)

- [gradle-recipes](https://github.com/android/gradle-recipes)

