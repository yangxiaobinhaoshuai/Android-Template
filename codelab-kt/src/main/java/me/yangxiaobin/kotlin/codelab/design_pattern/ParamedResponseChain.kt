package me.yangxiaobin.kotlin.codelab.design_pattern

interface ParametricInterceptor<ORIGIN, PROCESSED, PARAM> {
    fun intercept(chain: ParametricInterceptor.Chain<ORIGIN, PROCESSED, PARAM>): PROCESSED
    interface Chain<ORIGIN, PROCESSED, PARAM> {
        fun param(): PARAM
        fun original(): ORIGIN
        fun proceed(origin: ORIGIN): PROCESSED
    }
}

class ParametricChainImpl<O, P, T>(
    private val interceptors: List<ParametricInterceptor<O, P, T>>,
    private val index: Int,
    private val origin: O,
    private val param: T,
) : ParametricInterceptor.Chain<O, P, T> {
    override fun param(): T = param
    override fun original(): O = origin
    override fun proceed(origin: O): P {
        val nextInterceptor = interceptors[index]
        val nextChain = ParametricChainImpl(interceptors, index + 1, origin,param)
        return nextInterceptor.intercept(nextChain)
    }
}

typealias ParametricInterceptorFunc<ORIGIN, PROCESSED, T> = (chain: ParametricInterceptor.Chain<ORIGIN, PROCESSED, T>) -> PROCESSED


/**
 * Usage :
 *
 *      fun <I,O> foo(args): O {
  *        val handler = ParameterizedResponseChainHandler<I, O>(input)
  *
  *        handler.add(this::process)
  *        handler.add(this::convertType)
 *         // or
 *         handler.add { chain ->
 *           val origin = chain.origin()
 *           // process with origin
 *           return chain.proceed(origin)
 *         }
  *
  *        return handler.getProcessed()
  *      }
 *
 *       private fun <I,O> process(chain: Interceptor.Chain<I, O>): O {
 *          val originList = chain.original()
 *          // do stuff with originList
 *          return chain.proceed(originList)
 *       }
 *
 *        private fun <I,O> convertType(chain: Interceptor.Chain<I, O>): O {
 *           val origin = chain.original()
 *          // do stuff
 *          return result
 *        }
 *
 */
class ParameterizedResponseChainHandler<ORIGIN, PROCESSED, T>(private val origin: ORIGIN) {

    private val mInterceptors = mutableListOf<ParametricInterceptor<ORIGIN, PROCESSED, T>>()
    operator fun plus(parametricInterceptor: ParametricInterceptor<ORIGIN, PROCESSED, T>) =
        apply { mInterceptors.add(parametricInterceptor) }

    fun add(interceptor: ParametricInterceptorFunc<ORIGIN, PROCESSED, T>) = apply {
        plus(object : ParametricInterceptor<ORIGIN, PROCESSED, T> {
            override fun intercept(chain: ParametricInterceptor.Chain<ORIGIN, PROCESSED, T>): PROCESSED =
                interceptor.invoke(chain)
        })
    }

    fun getProcessed(param: T): PROCESSED = ParametricChainImpl(mInterceptors, 0, origin, param).proceed(origin)

}

