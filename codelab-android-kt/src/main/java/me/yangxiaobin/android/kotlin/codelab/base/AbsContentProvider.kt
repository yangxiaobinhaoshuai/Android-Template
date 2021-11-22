package me.yangxiaobin.android.kotlin.codelab.base

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import me.yangxiaobin.android.kotlin.codelab.ext.currentProcessName
import me.yangxiaobin.android.kotlin.codelab.ext.getCurrentPid
import me.yangxiaobin.android.kotlin.codelab.ext.simpleName

open class AbsContentProvider : ContentProvider(), LogAbility {

    override val LogAbility.TAG: String
        get() = "AbsProvider:${this.javaClass.simpleName.take(11)}"

    private val logPrefix by lazy { "${this.simpleName}(hash:${this.hashCode()}, tName:${Thread.currentThread().name}, tid:${Thread.currentThread().id}, pName:${currentProcessName}, pid:$getCurrentPid)" }

    override fun onCreate(): Boolean {
        logI("$logPrefix onCreate")
        return false
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        logI(
            """
            $logPrefix onCreate
            uri:$uri
            projection:$projection
            selection:$selection
            selectionArgs:$selectionArgs
            sortOrder:$sortOrder
        """.trimIndent()
        )
        return null
    }

    override fun getType(uri: Uri): String? {
        logI("$logPrefix getType,uri:$uri")
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        logI("$logPrefix insert,uri:$uri,values:$values")
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        logI("$logPrefix delete,uri:$uri,selection:$selection,selectionArgs:$selectionArgs")
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        logI(
            """
            $logPrefix update
            uri:$uri
            values:$values
            selection:$selection
            selectionArgs:$selectionArgs
        """.trimIndent()
        )
        return 0
    }
}
