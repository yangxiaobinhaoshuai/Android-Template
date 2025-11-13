package com.yxb.kv

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

/**
 * SharedPreferences implementation of KvMediator
 *
 * Usage:
 * ```
 * val kv = SharedPreferencesMediator.create(context, "user_preferences")
 * ```
 */
class SharedPreferencesMediator private constructor(
    private val sharedPreferences: SharedPreferences
) : KvMediator {

    private val mutex = Mutex()

    // Flow cache for observing changes
    private val flowCache = ConcurrentHashMap<String, MutableStateFlow<Any?>>()

    // Listener for SharedPreferences changes
    private val changeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        key?.let { updateFlow(it) }
    }

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(changeListener)
    }

    companion object {
        /**
         * Create a new SharedPreferencesMediator instance
         * @param context Android context
         * @param name The name of the SharedPreferences file
         * @param mode The operating mode (defaults to MODE_PRIVATE)
         */
        fun create(
            context: Context,
            name: String = "default_preferences",
            mode: Int = Context.MODE_PRIVATE
        ): SharedPreferencesMediator {
            val sp = context.getSharedPreferences(name, mode)
            return SharedPreferencesMediator(sp)
        }

        /**
         * Create using default SharedPreferences
         */
        fun createDefault(context: Context): SharedPreferencesMediator {
            val sp = android.preference.PreferenceManager.getDefaultSharedPreferences(context)
            return SharedPreferencesMediator(sp)
        }
    }

    // --- Single Read Operations (Async) ---

    override suspend fun getString(key: String, defaultValue: String?): String? {
        return withContext(Dispatchers.IO) {
            try {
                sharedPreferences.getString(key, defaultValue)
            } catch (e: Exception) {
                defaultValue
            }
        }
    }

    override suspend fun getInt(key: String, defaultValue: Int): Int {
        return withContext(Dispatchers.IO) {
            try {
                sharedPreferences.getInt(key, defaultValue)
            } catch (e: Exception) {
                defaultValue
            }
        }
    }

    override suspend fun getLong(key: String, defaultValue: Long): Long {
        return withContext(Dispatchers.IO) {
            try {
                sharedPreferences.getLong(key, defaultValue)
            } catch (e: Exception) {
                defaultValue
            }
        }
    }

    override suspend fun getFloat(key: String, defaultValue: Float): Float {
        return withContext(Dispatchers.IO) {
            try {
                sharedPreferences.getFloat(key, defaultValue)
            } catch (e: Exception) {
                defaultValue
            }
        }
    }

    override suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                sharedPreferences.getBoolean(key, defaultValue)
            } catch (e: Exception) {
                defaultValue
            }
        }
    }

    override suspend fun getStringSet(key: String, defaultValue: Set<String>?): Set<String>? {
        return withContext(Dispatchers.IO) {
            try {
                sharedPreferences.getStringSet(key, defaultValue)?.toSet() // Return immutable copy
            } catch (e: Exception) {
                defaultValue
            }
        }
    }

    // --- Reactive Read Operations (Flow) ---

    override fun observeString(key: String, defaultValue: String?): Flow<String?> {
        return getOrCreateFlow(key, defaultValue).map { it as? String }
    }

    override fun observeInt(key: String, defaultValue: Int): Flow<Int> {
        return getOrCreateFlow(key, defaultValue).map { it as? Int ?: defaultValue }
    }

    override fun observeLong(key: String, defaultValue: Long): Flow<Long> {
        return getOrCreateFlow(key, defaultValue).map { it as? Long ?: defaultValue }
    }

    override fun observeFloat(key: String, defaultValue: Float): Flow<Float> {
        return getOrCreateFlow(key, defaultValue).map { it as? Float ?: defaultValue }
    }

    override fun observeBoolean(key: String, defaultValue: Boolean): Flow<Boolean> {
        return getOrCreateFlow(key, defaultValue).map { it as? Boolean ?: defaultValue }
    }

    override fun observeStringSet(key: String, defaultValue: Set<String>?): Flow<Set<String>?> {
        return getOrCreateFlow(key, defaultValue).map {
            @Suppress("UNCHECKED_CAST")
            it as? Set<String>
        }
    }

    // --- Write/Edit Operations (Async) ---

    override suspend fun edit(block: (KvEditor) -> Unit): KvResult<Unit> {
        return mutex.withLock {
            withContext(Dispatchers.IO) {
                try {
                    val editor = SharedPreferencesEditor(sharedPreferences.edit())
                    block(editor)
                    val success = editor.commit()
                    if (success) {
                        KvResult.Success(Unit)
                    } else {
                        KvResult.Error(Exception("Failed to commit SharedPreferences changes"))
                    }
                } catch (e: Exception) {
                    KvResult.Error(e)
                }
            }
        }
    }

    // --- Other Operations ---

    override suspend fun contains(key: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                sharedPreferences.contains(key)
            } catch (e: Exception) {
                false
            }
        }
    }

    // --- Public Utility Methods ---

    /**
     * Get all keys stored in SharedPreferences
     */
    override suspend fun getAllKeys(): Set<String> {
        return withContext(Dispatchers.IO) {
            sharedPreferences.all.keys
        }
    }

    /**
     * Get all entries as a map
     */
    suspend fun getAll(): Map<String, Any?> {
        return withContext(Dispatchers.IO) {
            sharedPreferences.all.toMap()
        }
    }

    /**
     * Clean up resources
     */
    fun dispose() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(changeListener)
        flowCache.clear()
    }

    // --- Private Helper Methods ---

    private fun getOrCreateFlow(key: String, defaultValue: Any?): Flow<Any?> {
        return flowCache.getOrPut(key) {
            val currentValue = getCurrentValueSync(key, defaultValue)
            MutableStateFlow(currentValue)
        }
    }

    private fun getCurrentValueSync(key: String, defaultValue: Any?): Any? {
        return when (defaultValue) {
            is String? -> sharedPreferences.getString(key, defaultValue)
            is Int -> sharedPreferences.getInt(key, defaultValue)
            is Long -> sharedPreferences.getLong(key, defaultValue)
            is Float -> sharedPreferences.getFloat(key, defaultValue)
            is Boolean -> sharedPreferences.getBoolean(key, defaultValue)
            is Set<*> -> {
                @Suppress("UNCHECKED_CAST")
                sharedPreferences.getStringSet(key, defaultValue as? Set<String>)?.toSet()
            }

            else -> defaultValue
        }
    }

    private fun updateFlow(key: String) {
        flowCache[key]?.let { flow ->
            val currentValue = flow.value
            val newValue = getCurrentValueSync(key, getDefaultForValue(currentValue))
            flow.value = newValue
        }
    }

    private fun getDefaultForValue(value: Any?): Any? {
        return when (value) {
            is String? -> null
            is Int -> 0
            is Long -> 0L
            is Float -> 0f
            is Boolean -> false
            is Set<*> -> null
            else -> null
        }
    }

    /**
     * Inner class for batch editing operations
     */
    private class SharedPreferencesEditor(
        private val editor: SharedPreferences.Editor
    ) : KvEditor {

        override fun putString(key: String, value: String?): KvEditor {
            editor.putString(key, value)
            return this
        }

        override fun putInt(key: String, value: Int): KvEditor {
            editor.putInt(key, value)
            return this
        }

        override fun putLong(key: String, value: Long): KvEditor {
            editor.putLong(key, value)
            return this
        }

        override fun putFloat(key: String, value: Float): KvEditor {
            editor.putFloat(key, value)
            return this
        }

        override fun putDouble(key: String, value: Double): KvEditor {
            editor.putLong(key, value.toRawBits())
            return this
        }

        override fun putBoolean(key: String, value: Boolean): KvEditor {
            editor.putBoolean(key, value)
            return this
        }

        override fun putStringSet(key: String, value: Set<String>?): KvEditor {
            editor.putStringSet(key, value)
            return this
        }

        override fun remove(key: String): KvEditor {
            editor.remove(key)
            return this
        }

        override fun clear(): KvEditor {
            editor.clear()
            return this
        }

        fun commit(): Boolean {
            return editor.commit()
        }

        fun apply() {
            editor.apply()
        }
    }
}
