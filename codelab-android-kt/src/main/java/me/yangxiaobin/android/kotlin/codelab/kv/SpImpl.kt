import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext

/**
 * KvMediator 接口的 SharedPreferences 实现。
 *
 * @param prefs SharedPreferences 实例。建议使用 applicationContext 来获取，以避免内存泄漏。
 */
class SpKvMediator(private val prefs: SharedPreferences) : KvMediator {

    // --- 单个读取操作 ---

    override suspend fun getString(key: String, defaultValue: String?): String? =
        withContext(Dispatchers.IO) {
            prefs.getString(key, defaultValue)
        }

    override suspend fun getInt(key: String, defaultValue: Int): Int = withContext(Dispatchers.IO) {
        prefs.getInt(key, defaultValue)
    }

    override suspend fun getLong(key: String, defaultValue: Long): Long =
        withContext(Dispatchers.IO) {
            prefs.getLong(key, defaultValue)
        }

    override suspend fun getFloat(key: String, defaultValue: Float): Float =
        withContext(Dispatchers.IO) {
            prefs.getFloat(key, defaultValue)
        }

    override suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        withContext(Dispatchers.IO) {
            prefs.getBoolean(key, defaultValue)
        }

    override suspend fun getStringSet(key: String, defaultValue: Set<String>?): Set<String>? =
        withContext(Dispatchers.IO) {
            prefs.getStringSet(key, defaultValue)
        }

    // --- 响应式读取操作 ---

    private inline fun <T> observeKey(
        key: String,
        crossinline getValue: () -> T,
    ): Flow<T> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
            if (key == changedKey) {
                trySend(getValue())
            }
        }
        // 1. 发送初始值
        trySend(getValue())
        // 2. 注册监听器
        prefs.registerOnSharedPreferenceChangeListener(listener)
        // 3. 当 Flow 关闭时，取消注册
        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    override fun observeString(key: String, defaultValue: String?): Flow<String?> =
        observeKey(key) { prefs.getString(key, defaultValue) }

    override fun observeInt(key: String, defaultValue: Int): Flow<Int> =
        observeKey(key) { prefs.getInt(key, defaultValue) }

    override fun observeLong(key: String, defaultValue: Long): Flow<Long> =
        observeKey(key) { prefs.getLong(key, defaultValue) }

    override fun observeFloat(key: String, defaultValue: Float): Flow<Float> =
        observeKey(key) { prefs.getFloat(key, defaultValue) }

    override fun observeBoolean(key: String, defaultValue: Boolean): Flow<Boolean> =
        observeKey(key) { prefs.getBoolean(key, defaultValue) }

    override fun observeStringSet(key: String, defaultValue: Set<String>?): Flow<Set<String>?> =
        observeKey(key) { prefs.getStringSet(key, defaultValue) }

    // --- 写入/编辑操作 ---

    override suspend fun edit(block: (KvEditor) -> Unit): KvResult<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val editor = prefs.edit()
                val spEditor = SpKvEditor(editor)
                block(spEditor)
                editor.apply() // 使用 apply() 在后台异步写入磁盘
                KvResult.Success(Unit)
            } catch (e: Exception) {
                KvResult.Error(e)
            }
        }

    // --- 其他操作 ---

    override suspend fun contains(key: String): Boolean = withContext(Dispatchers.IO) {
        prefs.contains(key)
    }
}

/**
 * KvEditor 接口的 SharedPreferences 实现。
 * 这是一个内部辅助类，将操作委托给 SharedPreferences.Editor。
 */
private class SpKvEditor(private val editor: SharedPreferences.Editor) : KvEditor {
    override fun putString(key: String, value: String?): KvEditor =
        apply { editor.putString(key, value) }

    override fun putInt(key: String, value: Int): KvEditor = apply { editor.putInt(key, value) }
    override fun putLong(key: String, value: Long): KvEditor = apply { editor.putLong(key, value) }
    override fun putFloat(key: String, value: Float): KvEditor =
        apply { editor.putFloat(key, value) }

    override fun putBoolean(key: String, value: Boolean): KvEditor =
        apply { editor.putBoolean(key, value) }

    override fun putStringSet(key: String, value: Set<String>?): KvEditor =
        apply { editor.putStringSet(key, value) }

    override fun remove(key: String): KvEditor = apply { editor.remove(key) }
    override fun clear(): KvEditor = apply { editor.clear() }
}
