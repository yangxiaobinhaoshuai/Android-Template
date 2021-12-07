package me.yangxiaobin.kotlin.codelab.result


sealed class Result<T> {

    open fun get():T? = null

    fun getOrThrow():T = when (this) {
        is Success -> get()
        is Error -> throw throwable
    }

    data class Success<T>(val data: T) : Result<T>() {
        override fun get():T = data
    }

    data class Error<T>(val throwable: Throwable) : Result<T>()
}
