package com.yxb.kv


import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

/**
 * MMKV implementation of KvMediator
 *
 * MMKV GitHub: https://github.com/Tencent/MMKV
 *
 * Usage:
 * ```
 * // Initialize MMKV in Application.onCreate()
 * MMKV.initialize(this)
 *
 * // Create instance
 * val kv = MmkvMediator.create("my_storage")
 * ```
 */
class MMKVMediator private constructor(
    private val mmkv: MMKV
) : KvMediator {

    private val mutex = Mutex()

    // Flow cache for observing changes
    private val flowCache = ConcurrentHashMap<String, MutableStateFlow<Any?>>()

    companion object {
        /**
         * Create a new MmkvMediator instance
         * @param mmkvId The MMKV instance ID (defaults to MMKV.defaultMMKV())
         * @param mode The MMKV mode (defaults to MMKV.SINGLE_PROCESS_MODE)
         */
        fun create(
            mmkvId: String? = null,
            mode: Int = MMKV.SINGLE_PROCESS_MODE
        ): MMKVMediator {
            val mmkv = if (mmkvId == null) {
                MMKV.defaultMMKV()
            } else {
                MMKV.mmkvWithID(mmkvId, mode)
            }
            return MMKVMediator(mmkv)
        }

        /**
         * Create with custom crypt key
         */
        fun createWithCryptKey(
            mmkvId: String,
            cryptKey: String?,
            mode: Int = MMKV.SINGLE_PROCESS_MODE
        ): MMKVMediator {
            val mmkv = MMKV.mmkvWithID(mmkvId, mode, cryptKey)
            return MMKVMediator(mmkv)
        }
    }

    // --- Single Read Operations (Async) ---

    override suspend fun getString(key: String, defaultValue: String?): String? {
        return try {
            mmkv.decodeString(key, defaultValue)
        } catch (e: Exception) {
            defaultValue
        }
    }

    override suspend fun getInt(key: String, defaultValue: Int): Int {
        return try {
            mmkv.decodeInt(key, defaultValue)
        } catch (e: Exception) {
            defaultValue
        }
    }

    override suspend fun getLong(key: String, defaultValue: Long): Long {
        return try {
            mmkv.decodeLong(key, defaultValue)
        } catch (e: Exception) {
            defaultValue
        }
    }

    override suspend fun getFloat(key: String, defaultValue: Float): Float {
        return try {
            mmkv.decodeFloat(key, defaultValue)
        } catch (e: Exception) {
            defaultValue
        }
    }

    override suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return try {
            mmkv.decodeBool(key, defaultValue)
        } catch (e: Exception) {
            defaultValue
        }
    }

    override suspend fun getStringSet(key: String, defaultValue: Set<String>?): Set<String>? {
        return try {
            mmkv.decodeStringSet(key, defaultValue)
        } catch (e: Exception) {
            defaultValue
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
            try {
                val editor = MmkvEditor(mmkv, ::notifyFlowUpdate)
                block(editor)
                editor.commit()
                KvResult.Success(Unit)
            } catch (e: Exception) {
                KvResult.Error(e)
            }
        }
    }

    // --- Other Operations ---

    override suspend fun contains(key: String): Boolean {
        return try {
            mmkv.containsKey(key)
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getAllKeys(): Set<String> {
        return withContext(Dispatchers.IO) {
            try {
                mmkv.allKeys()?.toSet() ?: emptySet()
            } catch (e: Exception) {
                emptySet()
            }
        }
    }

    // --- Private Helper Methods ---

    private fun getOrCreateFlow(key: String, defaultValue: Any?): Flow<Any?> {
        return flowCache.getOrPut(key) {
            val currentValue = getCurrentValue(key, defaultValue)
            MutableStateFlow(currentValue)
        }
    }

    private fun getCurrentValue(key: String, defaultValue: Any?): Any? {
        return when (defaultValue) {
            is String? -> mmkv.decodeString(key, defaultValue)
            is Int -> mmkv.decodeInt(key, defaultValue)
            is Long -> mmkv.decodeLong(key, defaultValue)
            is Float -> mmkv.decodeFloat(key, defaultValue)
            is Boolean -> mmkv.decodeBool(key, defaultValue)
            is Set<*> -> {
                @Suppress("UNCHECKED_CAST")
                mmkv.decodeStringSet(key, defaultValue as? Set<String>)
            }

            else -> defaultValue
        }
    }

    private fun notifyFlowUpdate(key: String, value: Any?) {
        flowCache[key]?.value = value
    }

    /**
     * Inner class for batch editing operations
     */
    private class MmkvEditor(
        private val mmkv: MMKV,
        private val onUpdate: (String, Any?) -> Unit
    ) : KvEditor {

        private val pendingUpdates = mutableMapOf<String, Any?>()
        private val pendingRemovals = mutableSetOf<String>()
        private var shouldClear = false

        override fun putString(key: String, value: String?): KvEditor {
            pendingUpdates[key] = value
            return this
        }

        override fun putInt(key: String, value: Int): KvEditor {
            pendingUpdates[key] = value
            return this
        }

        override fun putLong(key: String, value: Long): KvEditor {
            pendingUpdates[key] = value
            return this
        }

        override fun putFloat(key: String, value: Float): KvEditor {
            pendingUpdates[key] = value
            return this
        }

        override fun putDouble(key: String, value: Double): KvEditor {
            pendingUpdates[key] = value
            return this
        }

        override fun putBoolean(key: String, value: Boolean): KvEditor {
            pendingUpdates[key] = value
            return this
        }

        override fun putStringSet(key: String, value: Set<String>?): KvEditor {
            pendingUpdates[key] = value
            return this
        }

        override fun remove(key: String): KvEditor {
            pendingRemovals.add(key)
            pendingUpdates.remove(key)
            return this
        }

        override fun clear(): KvEditor {
            shouldClear = true
            pendingUpdates.clear()
            pendingRemovals.clear()
            return this
        }

        fun commit() {
            if (shouldClear) {
                mmkv.clearAll()
                return
            }

            // Apply all pending updates
            pendingUpdates.forEach { (key, value) ->
                when (value) {
                    is String? -> mmkv.encode(key, value)
                    is Int -> mmkv.encode(key, value)
                    is Long -> mmkv.encode(key, value)
                    is Float -> mmkv.encode(key, value)
                    is Boolean -> mmkv.encode(key, value)
                    is Set<*> -> {
                        @Suppress("UNCHECKED_CAST")
                        mmkv.encode(key, value as? Set<String>)
                    }
                }
                onUpdate(key, value)
            }

            // Apply all pending removals
            pendingRemovals.forEach { key ->
                mmkv.removeValueForKey(key)
                onUpdate(key, null)
            }
        }
    }
}