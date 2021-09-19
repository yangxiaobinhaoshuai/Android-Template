package me.yangxiaobin.android.codelab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.yangxiaobin.android.codelab.log.L
import me.yangxiaobin.kotlin.codelab.log.logD
import me.yangxiaobin.kotlin.codelab.log.logE

class MainActivity : AppCompatActivity() {

    private val tag = "Sample-app"
    val logD = L.logD(tag)
    val logE = L.logE(tag)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logD("onCreate")
    }


}
