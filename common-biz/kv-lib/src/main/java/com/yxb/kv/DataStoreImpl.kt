package com.yxb.kv

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * DataStore 实现的 KvMediator
 *
 * 使用示例：
 * val dataStore = context.dataStorePreferences(name = "settings")
 * val mediator = DataStoreMediator(dataStore)
 */
class DataStoreMediator(
    private val dataStore: DataStore<Preferences>
) : KvMediator {


    // ==================== 单个读取操作 ====================

    override suspend fun getString(key: String, defaultValue: String?): String? {
        return withContext(Dispatchers.IO) {
            try {
                dataStore.data.first()[stringPreferencesKey(key)] ?: defaultValue
            } catch (e: Exception) {
                defaultValue
            }
        }
    }

    override suspend fun getInt(key: String, defaultValue: Int): Int {
        return withContext(Dispatchers.IO) {
            try {
                dataStore.data.first()[intPreferencesKey(key)] ?: defaultValue
            } catch (e: Exception) {
                defaultValue
            }
        }
    }

    override suspend fun getLong(key: String, defaultValue: Long): Long {
        return withContext(Dispatchers.IO) {
            try {
                dataStore.data.first()[longPreferencesKey(key)] ?: defaultValue
            } catch (e: Exception) {
                defaultValue
            }
        }
    }

    override suspend fun getFloat(key: String, defaultValue: Float): Float {
        return withContext(Dispatchers.IO) {
            try {
                dataStore.data.first()[floatPreferencesKey(key)] ?: defaultValue
            } catch (e: Exception) {
                defaultValue
            }
        }
    }

    override suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                dataStore.data.first()[booleanPreferencesKey(key)] ?: defaultValue
            } catch (e: Exception) {
                defaultValue
            }
        }
    }

    override suspend fun getStringSet(key: String, defaultValue: Set<String>?): Set<String>? {
        return withContext(Dispatchers.IO) {
            try {
                dataStore.data.first()[stringSetPreferencesKey(key)] ?: defaultValue
            } catch (e: Exception) {
                defaultValue
            }
        }
    }

    // ==================== 响应式读取 ====================

    override fun observeString(key: String, defaultValue: String?): Flow<String?> {
        val prefKey = stringPreferencesKey(key)
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[prefKey] ?: defaultValue
            }
            .flowOn(Dispatchers.IO)
    }

    override fun observeInt(key: String, defaultValue: Int): Flow<Int> {
        val prefKey = intPreferencesKey(key)
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[prefKey] ?: defaultValue
            }
            .flowOn(Dispatchers.IO)
    }

    override fun observeLong(key: String, defaultValue: Long): Flow<Long> {
        val prefKey = longPreferencesKey(key)
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[prefKey] ?: defaultValue
            }
            .flowOn(Dispatchers.IO)
    }

    override fun observeFloat(key: String, defaultValue: Float): Flow<Float> {
        val prefKey = floatPreferencesKey(key)
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[prefKey] ?: defaultValue
            }
            .flowOn(Dispatchers.IO)
    }

    override fun observeBoolean(key: String, defaultValue: Boolean): Flow<Boolean> {
        val prefKey = booleanPreferencesKey(key)
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[prefKey] ?: defaultValue
            }
            .flowOn(Dispatchers.IO)
    }

    override fun observeStringSet(key: String, defaultValue: Set<String>?): Flow<Set<String>?> {
        val prefKey = stringSetPreferencesKey(key)
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[prefKey] ?: defaultValue
            }
            .flowOn(Dispatchers.IO)
    }

    // ==================== 写入/编辑操作 ====================

    override suspend fun edit(block: (KvEditor) -> Unit): KvResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val editor = DataStoreEditor()
                block(editor)

                dataStore.edit { preferences ->
                    editor.operations.forEach { operation ->
                        operation(preferences)
                    }
                }

                KvResult.Success(Unit)
            } catch (e: Exception) {
                KvResult.Error(e)
            }
        }
    }

    // ==================== 其他操作 ====================

    override suspend fun contains(key: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // 尝试检查是否存在任意类型的 key
                val preferences = dataStore.data.first()
                preferences.contains(stringPreferencesKey(key)) ||
                        preferences.contains(intPreferencesKey(key)) ||
                        preferences.contains(longPreferencesKey(key)) ||
                        preferences.contains(floatPreferencesKey(key)) ||
                        preferences.contains(booleanPreferencesKey(key)) ||
                        preferences.contains(stringSetPreferencesKey(key))
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun getAllKeys(): Set<String> {
        return withContext(Dispatchers.IO) {
            try {
                dataStore.data.first().asMap().keys.map { it.name }.toSet()
            } catch (e: Exception) {
                e.printStackTrace()
                emptySet()
            }
        }
    }

    // ==================== Editor 实现 ====================

    private class DataStoreEditor : KvEditor {
        val operations = mutableListOf<(MutablePreferences) -> Unit>()

        override fun putString(key: String, value: String?): KvEditor {
            operations.add { preferences ->
                if (value == null) {
                    preferences.remove(stringPreferencesKey(key))
                } else {
                    preferences[stringPreferencesKey(key)] = value
                }
            }
            return this
        }

        override fun putInt(key: String, value: Int): KvEditor {
            operations.add { preferences ->
                preferences[intPreferencesKey(key)] = value
            }
            return this
        }

        override fun putLong(key: String, value: Long): KvEditor {
            operations.add { preferences ->
                preferences[longPreferencesKey(key)] = value
            }
            return this
        }

        override fun putFloat(key: String, value: Float): KvEditor {
            operations.add { preferences ->
                preferences[floatPreferencesKey(key)] = value
            }
            return this
        }

        override fun putDouble(key: String, value: Double): KvEditor {
            operations.add { preferences ->
                preferences[doublePreferencesKey(key)] = value
            }
            return this
        }

        override fun putBoolean(key: String, value: Boolean): KvEditor {
            operations.add { preferences ->
                preferences[booleanPreferencesKey(key)] = value
            }
            return this
        }

        override fun putStringSet(key: String, value: Set<String>?): KvEditor {
            operations.add { preferences ->
                if (value == null) {
                    preferences.remove(stringSetPreferencesKey(key))
                } else {
                    preferences[stringSetPreferencesKey(key)] = value
                }
            }
            return this
        }

        override fun remove(key: String): KvEditor {
            operations.add { preferences ->
                // 移除所有可能类型的 key
                preferences.remove(stringPreferencesKey(key))
                preferences.remove(intPreferencesKey(key))
                preferences.remove(longPreferencesKey(key))
                preferences.remove(floatPreferencesKey(key))
                preferences.remove(booleanPreferencesKey(key))
                preferences.remove(stringSetPreferencesKey(key))
            }
            return this
        }

        override fun clear(): KvEditor {
            operations.add { preferences ->
                preferences.clear()
            }
            return this
        }
    }

    companion object {
        /**
         * Create a new DataStoreMediator instance with default settings
         * @param context Application context
         * @param name The DataStore name (defaults to "default_preferences")
         */
        fun create(
            context: Context,
            name: String = "default_preferences"
        ): DataStoreMediator {
            val dataStore = context.createDataStore(name)
            return DataStoreMediator(dataStore)
        }

        /**
         * Create a DataStoreMediator with custom configuration
         * @param context Application context
         * @param name The DataStore name
         * @param corruptionHandler Handler for data corruption
         * @param migrations List of migrations to apply
         * @param scope CoroutineScope for DataStore operations
         */
        fun createWithConfig(
            context: Context,
            name: String,
            corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = null,
            migrations: List<DataMigration<Preferences>> = emptyList(),
            scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        ): DataStoreMediator {
            val dataStore = PreferenceDataStoreFactory.create(
                corruptionHandler = corruptionHandler,
                migrations = migrations,
                scope = scope,
                produceFile = {
                    context.applicationContext.filesDir.resolve("datastore/$name.preferences_pb")
                }
            )
            return DataStoreMediator(dataStore)
        }

        /**
         * Create with SharedPreferences migration
         * Automatically migrates data from SharedPreferences to DataStore
         * @param context Application context
         * @param name The DataStore name
         * @param sharedPrefsName The SharedPreferences name to migrate from
         */
        fun createWithSpMigration(
            context: Context,
            name: String,
            sharedPrefsName: String
        ): DataStoreMediator {
            val dataStore = PreferenceDataStoreFactory.create(
                migrations = listOf(
                    SharedPreferencesMigration(context, sharedPrefsName)
                ),
                produceFile = {
                    context.applicationContext.filesDir.resolve("datastore/$name.preferences_pb")
                }
            )
            return DataStoreMediator(dataStore)
        }

        /**
         * Create with corruption handler that replaces corrupted data
         * @param context Application context
         * @param name The DataStore name
         */
        fun createWithCorruptionHandler(
            context: Context,
            name: String
        ): DataStoreMediator {
            val dataStore = PreferenceDataStoreFactory.create(
                corruptionHandler = ReplaceFileCorruptionHandler(
                    produceNewData = { emptyPreferences() }
                ),
                produceFile = {
                    context.applicationContext.filesDir.resolve("datastore/$name.preferences_pb")
                }
            )
            return DataStoreMediator(dataStore)
        }

        /**
         * Helper method to create DataStore instance
         */
        private fun Context.createDataStore(name: String): DataStore<Preferences> {
            return PreferenceDataStoreFactory.create(
                produceFile = {
                    this.applicationContext.filesDir.resolve("datastore/$name.preferences_pb")
                }
            )
        }
    }
}