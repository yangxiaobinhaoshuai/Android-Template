package com.yxb.kv

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// Sample Usage here.
private val Context.dataStorePreferences: DataStore<Preferences> by preferencesDataStore(
    name = "settings"
)

fun Context.getDsKvMediator(): KvMediator {
    return DataStoreMediator(dataStorePreferences)
}

fun Context.getSpKvMediator(): KvMediator {
    return SharedPreferencesMediator.createDefault(this)
}

fun Context.getMMKVMediator(): KvMediator {
    return MMKVMediator.create("default_mmkv")
}