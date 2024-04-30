package me.yangxiaobin.kotlin.codelab.design_pattern

interface StrategyAware<IN, OUT> {
    fun execute(param: IN): OUT
    fun condition(): Boolean
}

class StrategyHandler<I, O> {

    /**
     * ArrayList
     */
    private val list: MutableList<StrategyAware<I, O>> = mutableListOf()

    fun addStrategy(strategy: StrategyAware<I, O>) = apply {
        list += strategy
    }

    @Throws(NoSuchElementException::class)
    fun execute(param: I): O = list.first { it.condition() }.execute(param)

}




