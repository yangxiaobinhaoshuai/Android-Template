
# Android App Template


## Projects

### codelab-kt

#### Design pattern
1. Response chain handler
   
#### Utils
1. Neat logger


### codelab-androd-kt


### Kotlin extension functions

## 选型理由

### [ViewBinding](https://developer.android.com/topic/libraries/view-binding?hl=zh-cn)


Kt synthetics Vs Jetpack View binding

kt synthetics 由 Jetbrains 团队发布，两大便利
1. synthetics 视图，通过 id 引用 xml view
2. @Parcelize ，自动生成  parcelize 样板代码

2018 废弃，2019 移除 （1.4.20+)

synthetics 弊端：
1. 仅支持 kt
2. 非类型安全，可能为 null
3. 污染全局命名空间
4. 相同 id 编译安全问题

ViewBinding 优势：
1. null safety
2. type safety
3. 若布局和程序不一致，将错误提前到编译期间
4. 提供  kotlin-parcelize 专门插件来支持 @parcelize

viewBinding 和 synthetics plugin 同时 apply 会报错


### View app dependencies tree
1. cd app
2. ./gradlew -q dependencies --configuration debugRuntimeClasspath


### build tips
- Keep eye on  buildFeature & composeOptions config block.
