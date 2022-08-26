

package me.yangxiaobin.android.mids


/**
 * 待填充的集合，只能是 MutableList
 * 必须是静态属性
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class StaticListSub


/**
 * 向 @ToBePopulated 的集合中填充元素
 * 必须是静态函数
 */
@Suppress("SpellCheckingInspection")
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class StaticAddFuntion
