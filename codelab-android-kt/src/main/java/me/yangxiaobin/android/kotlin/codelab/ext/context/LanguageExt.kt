package me.yangxiaobin.android.kotlin.codelab.ext.context

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat


/**
 *
 * Needs appcompat 1.6.1
 */
private suspend fun changeAppLanguage(languageTag: String) {
    val localeList = LocaleListCompat.forLanguageTags(languageTag)
   AppCompatDelegate.setApplicationLocales(localeList)
}
