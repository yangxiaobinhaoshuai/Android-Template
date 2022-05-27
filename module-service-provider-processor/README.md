

# APT / KAPT / KSP （MetaProgramming)
> Wiki
把源程序作为输入

1. read
2. generate
3. analysis
4. transform


## APT

## Kapt


## KSP

基于 Kt compiler plugin 实现
KSP 顾名思义，在 Symbols 级别对 Kotlin 的 AST 进行处理
需要注意 KSP 不能用来修改原代码，只能用来生成新代码，compiler 可以修改代码的话，会让人非常困惑，所以 KSP 通过使 source code readOnly 来避免对源程序修改

KSP 和 Kotlin 语言中反射的 KType 类似
可以把 KSP 理解为 Kotlin 语言的预处理器

### What is KSP?
KSP stands for 'Kotlin Symbol Processing', it's an API for building lightweight compiler plugins.

### Design principles
- Model Kt language. (Works well in kt environment)
- Support mutli-platforms
- Incrementality
- Java interoperability
- Easy api
- Easy to migrate old Kapt processor.

### Characteristics
- Simplified api.
- minimal learning curve.
- 2 times faster than kapt.
- Kotlin friendly

### Work Flow
1. Processors read and analysis source programme.
2. Processors generate code.
3. Kt compiler compile generated code and source together.



## Kotlin Compiler Plugin (KCP)

缺点：
1. API not stable.
2. Complex api.
3. Allow modifying existing code.

## JavaPoet / Kotlin Poet

### Why


- Kotlin Poet 不支持多轮 code generation
- 
KotlinPoet is a Kotlin and Java API for generating .kt source files.


### References
- [掘金 KSP](https://juejin.cn/post/6979759813467062309)
- [KSP Doc](https://kotlinlang.org/docs/ksp-overview.html)
- [Kotlin poet Doc](https://square.github.io/kotlinpoet/interop-ksp/)
- [Kotlin Compiler Plugin Doc](https://kotlinlang.org/docs/all-open-plugin.html)
- [Google KSP github](https://github.com/google/ksp)
- [KSP Intro youtube](https://www.youtube.com/watch?v=bv-VyGM3HCY)
- [Writing Your First Kotlin Compiler Plugin.pdf](https://resources.jetbrains.com/storage/products/kotlinconf2018/slides/5_Writing%20Your%20First%20Kotlin%20Compiler%20Plugin.pdf)
