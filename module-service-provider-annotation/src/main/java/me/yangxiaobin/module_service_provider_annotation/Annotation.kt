package me.yangxiaobin.module_service_provider_annotation


/**
 * 允许的参数类型有：
 *
 * 对应于 Java 原生类型的类型（Int、 Long等）；
 * 字符串；
 * 类（Foo::class）；
 * 枚举；
 * 其他注解；
 * 上面已列类型的数组。
 */

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ModuleService


/**
 * Calculate method time spending.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class DebugLog
