import kotlinx.coroutines.flow.Flow

sealed class KvResult<out T> {
    data class Success<T>(val data: T) : KvResult<T>()
    data class Error(val exception: Exception) : KvResult<Nothing>()
    object NotFound : KvResult<Nothing>() // 特殊情况：当 key 不存在时
}


interface KvEditor {
    fun putString(key: String, value: String?): KvEditor
    fun putInt(key: String, value: Int): KvEditor
    fun putLong(key: String, value: Long): KvEditor
    fun putFloat(key: String, value: Float): KvEditor
    fun putBoolean(key: String, value: Boolean): KvEditor
    fun putStringSet(key: String, value: Set<String>?): KvEditor
    fun remove(key: String): KvEditor
    fun clear(): KvEditor
}


interface KvMediator {

    // --- 单个读取操作 (异步) ---
    suspend fun getString(key: String, defaultValue: String? = null): String?
    suspend fun getInt(key: String, defaultValue: Int = 0): Int
    suspend fun getLong(key: String, defaultValue: Long = 0L): Long
    suspend fun getFloat(key: String, defaultValue: Float = 0f): Float
    suspend fun getBoolean(key: String, defaultValue: Boolean = false): Boolean
    suspend fun getStringSet(key: String, defaultValue: Set<String>? = null): Set<String>?

    // --- 响应式读取 (返回 Flow) ---
    fun observeString(key: String, defaultValue: String? = null): Flow<String?>
    fun observeInt(key: String, defaultValue: Int = 0): Flow<Int>
    fun observeLong(key: String, defaultValue: Long = 0L): Flow<Long>
    fun observeFloat(key: String, defaultValue: Float = 0f): Flow<Float>
    fun observeBoolean(key: String, defaultValue: Boolean = false): Flow<Boolean>
    fun observeStringSet(key: String, defaultValue: Set<String>? = null): Flow<Set<String>?>

    // --- 写入/编辑操作 (异步) ---
    /**
     * 在一个原子事务中执行多个写入操作。
     * @param block 一个 lambda，其接收者是 KvEditor 实例。
     */
    suspend fun edit(block: (KvEditor) -> Unit): KvResult<Unit>
    suspend fun putString(key: String, value: String?): KvResult<Unit> =
        edit { it.putString(key, value) }

    suspend fun putInt(key: String, value: Int): KvResult<Unit> = edit { it.putInt(key, value) }
    suspend fun putLong(key: String, value: Long): KvResult<Unit> = edit { it.putLong(key, value) }
    suspend fun putFloat(key: String, value: Float): KvResult<Unit> =
        edit { it.putFloat(key, value) }

    suspend fun putBoolean(key: String, value: Boolean): KvResult<Unit> =
        edit { it.putBoolean(key, value) }

    suspend fun putStringSet(key: String, value: Set<String>?): KvResult<Unit> =
        edit { it.putStringSet(key, value) }

    // --- 其他操作 ---
    suspend fun remove(key: String): KvResult<Unit> = edit { it.remove(key) }
    suspend fun contains(key: String): Boolean
    suspend fun clear(): KvResult<Unit> = edit { it.clear() }
}


