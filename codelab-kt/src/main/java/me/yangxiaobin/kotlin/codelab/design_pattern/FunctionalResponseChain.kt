package me.yangxiaobin.kotlin.codelab.design_pattern


/**
 * Composes a list of single argument functions from right to left.
 */
fun <T> compose(vararg functions: (T) -> T): (T) -> T = { x: T -> functions.foldRight(x) { f: (T) -> T, composed: T -> f(composed) } }

fun <T> compose(functions: List<(T) -> T>): (T) -> T = { x: T -> functions.foldRight(x) { f: (T) -> T, composed: T -> f(composed) } }


typealias Transform<I, O> = (I) -> O

typealias TransformProcessor<I, O> = (Transform<I, O>) -> Transform<I, O>

typealias TransformInterceptor<I, O> = (Transform<I, O>, I: I) -> O


/*fun <I, O> createFunctionalInterceptor(
    intercept: (Conversion<I, O>, I: I) -> O,
): (Conversion<I, O>) -> (I) -> O =
    { convert: Conversion<I, O> ->
        { I: I ->
            intercept(convert, I)
        }
    }*/

fun <I, O> interceptTransform(interceptor: TransformInterceptor<I, O>): TransformProcessor<I, O> =

    fun(transform: Transform<I, O>): (I) -> O =

        fun(input: I): O = interceptor.invoke(transform, input)


/**
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


