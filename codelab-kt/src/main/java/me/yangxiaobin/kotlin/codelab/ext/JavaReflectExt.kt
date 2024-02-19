package me.yangxiaobin.kotlin.codelab.ext

import java.lang.reflect.Constructor


/**
 * NB. Use it carefully.
 */
inline fun <reified T> T.createInstanceViaReflection(): T {
    // Kotlin reflection does not permit the instantiation of data objects.
    // This creates a new MySingleton instance "by force" (i.e., Java platform reflection)
    // Don't do this yourself!
    @Suppress("UNCHECKED_CAST")
    return (T::class.java.declaredConstructors[0].apply { isAccessible = true } as Constructor<T>).newInstance()
}
