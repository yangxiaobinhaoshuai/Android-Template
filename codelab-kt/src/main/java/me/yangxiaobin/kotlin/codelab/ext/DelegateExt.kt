package me.yangxiaobin.kotlin.codelab.ext

import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * @see kotlin.properties.Delegates
 * @see kotlin.properties.Delegates.notNull
 */

object DelegateAdapter{

    public inline fun <T> observable(
        initialValue: T,
        crossinline onChange: (Pair<T, T>) -> Unit,
    ): ReadWriteProperty<Any?, T> =
        object : ObservableProperty<T>(initialValue) {

            override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
                onChange.invoke(oldValue to newValue)
            }

        }

    public inline fun <T> observableOnlyNew(
        initialValue: T,
        crossinline onChange: (T) -> Unit,
    ): ReadWriteProperty<Any?, T> =
        object : ObservableProperty<T>(initialValue) {

            override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
                onChange.invoke(newValue)
            }

        }
}
