package me.yangxiaobin.kotlin.codelab.design_pattern


/**
 * Composes a list of single argument functions from right to left.
 */
fun <T> compose(vararg functions: (T) -> T): (T) -> T =
    { x: T -> functions.foldRight(x) { f: (T) -> T, composed: T -> f(composed) } }

fun <T> compose(functions: List<(T) -> T>): (T) -> T =
    { x: T -> functions.foldRight(x) { f: (T) -> T, composed: T -> f(composed) } }


typealias Transform<I, O> = (I) -> O

/// Processor = 装饰一个 Transform -> 新的 Transform
typealias TransformProcessor<I, O> = (Transform<I, O>) -> Transform<I, O>

/// Interceptor = 拦截一次调用：拿到「下一个 transform」+「当前输入」
typealias TransformInterceptor<I, O> = (Transform<I, O>, I: I) -> O

/**
 * Same as interceptTransform，Just for design reference, so commented.
 */
/*fun <I, O> createFunctionalInterceptor(
    intercept: (next: Transform<I, O>, input: I) -> O
): TransformProcessor<I, O> = { next: Transform<I, O> ->
    { input: I ->
        intercept(next, input)
    }
}*/

fun <I, O> interceptTransform(interceptor: TransformInterceptor<I, O>): TransformProcessor<I, O> =

    fun(transform: Transform<I, O>): (I) -> O =

        fun(input: I): O = interceptor.invoke(transform, input)


/**
 * assemble = build pipeline + 执行一次
 *
 * 给定初始输入 + 原始 transform + 一堆 processor，跑完整 pipeline。
 *
 * Sample Usage:
 *
 *    val a = createFunctionalInterceptor<Int, String> { param, param1 ->
 *      param.invoke(param1)
 *    }
 *
 *   val b = convertInFunctionalChains<Int, String>(0, { it.toString() }, a)
 *
 */
fun <I, O> assemble(
    initialValue: I,
    transform: Transform<I, O>,
    vararg processors: TransformProcessor<I, O>,
): O = compose(*processors).invoke(transform).invoke(initialValue)


/**
 * assembleTransform = build pipeline 不执行，给你个可复用函数
 */
fun <I, O> assembleTransformPipeline(
    transform: Transform<I, O>,
    vararg processors: TransformProcessor<I, O>,
): Transform<I, O> = compose(*processors).invoke(transform)
