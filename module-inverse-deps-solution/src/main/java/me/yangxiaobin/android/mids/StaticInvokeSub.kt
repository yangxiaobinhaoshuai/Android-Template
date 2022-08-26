
package me.yangxiaobin.android.mids


/**
 * 标记函数
 * 只能是静态函数
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class StaticInvokeSub

/**
 * 将在 @InvokeSub 标记的函数中调用
 * 只能是静态函数
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class StaticInvocation
