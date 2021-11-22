package me.yangxiaobin.android.kotlin.codelab.base

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder

open class AbsService : Service(), LogAbility {

    override val LogAbility.TAG: String
        get() = "AbsService:${this.javaClass.simpleName.take(11)}"

    private val logPrefix by lazy { this.getLogSuffix }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        logI("$logPrefix attachBaseContext,newBase:$newBase")
    }


    override fun onCreate() {
        super.onCreate()
        logI("$logPrefix onCreate")
    }

    override fun onBind(intent: Intent?): IBinder? {
        logI("$logPrefix onBind,intent:$intent")
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        logI("$logPrefix onUnbind,intent:$intent")
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        logI("$logPrefix onRebind,intent:$intent")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        logI("$logPrefix onStartCommand,intent:$intent,flags:$flags,startId:$startId")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        logI("$logPrefix onDestroy")
    }
}
