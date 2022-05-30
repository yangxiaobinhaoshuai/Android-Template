package me.yangxiaobin.module_service_provider

import java.util.concurrent.ConcurrentHashMap

object ServiceProvider {

    private val serviceMapping: MutableMap<Class<*>, Class<*>> = mutableMapOf()

    /**
     * Keep each instance single.
     */
    private val instanceCache: MutableMap<Class<*>, Any> = ConcurrentHashMap()


    public fun <T> requireService(clazz: Class<T>): T = requireNotNull(getServiceOrNull(clazz)) { "Instance of $clazz can't be null." }


    @Suppress("UNCHECKED_CAST")
    public fun <T> getServiceOrNull(clazz: Class<T>): T? = instanceCache.getOrElse(clazz) {
        val instance = newInstanceViaReflection(clazz)
        if (instance != null) instanceCache[clazz] = instance
        instance
    } as? T

    private fun <T> newInstanceViaReflection(clazz: Class<T>): T? = try {
        clazz.newInstance()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    fun dumpRegistry(tag: String = "") {

        val message = """
            class registry : $serviceMapping
            instance registry :$instanceCache
        """.trimIndent()

        println(tag + message)
    }

}
