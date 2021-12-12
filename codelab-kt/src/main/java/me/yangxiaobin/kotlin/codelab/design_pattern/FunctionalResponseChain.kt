package me.yangxiaobin.kotlin.codelab.design_pattern


/**
 * Composes a list of single argument functions from right to left.
 */
fun <T> compose(vararg functions: (T) -> T): (T) -> T =
    { x: T -> functions.foldRight(x, { f: (T) -> T, composed: T -> f(composed) }) }

fun <T> compose(functions: List<(T) -> T>): (T) -> T =
    { x: T -> functions.foldRight(x, { f: (T) -> T, composed: T -> f(composed) }) }


typealias Conversion<BEFORE, AFTER> = (BEFORE) -> AFTER

typealias FunctionalInterceptor<BEFORE, AFTER> = (Conversion<BEFORE, AFTER>) -> (BEFORE) -> AFTER


//fun <BEFORE, AFTER> createFunctionalInterceptor(
//    intercept: (Conversion<BEFORE, AFTER>, before: BEFORE) -> AFTER,
//): (Conversion<BEFORE, AFTER>) -> (BEFORE) -> AFTER =
//    { convert: Conversion<BEFORE, AFTER> ->
//        { before: BEFORE ->
//            intercept(convert, before)
//        }
//    }

fun <BEFORE, AFTER> createFunctionalInterceptor(
    intercept: (Conversion<BEFORE, AFTER>, before: BEFORE) -> AFTER,
): (Conversion<BEFORE, AFTER>) -> (BEFORE) -> AFTER =

    fun(convert: Conversion<BEFORE, AFTER>): (BEFORE) -> AFTER =

        fun(before: BEFORE): AFTER = intercept.invoke(convert, before)


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
fun <BEFORE, AFTER> convertViaFunctionalChains(
    initialValue: BEFORE,
    coreConversion: Conversion<BEFORE, AFTER>,
    vararg interceptors: FunctionalInterceptor<BEFORE, AFTER>,
): AFTER = compose(*interceptors).invoke(coreConversion)(initialValue)


