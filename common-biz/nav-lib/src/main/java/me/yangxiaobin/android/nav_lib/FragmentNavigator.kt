package me.yangxiaobin.android.nav_lib

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.setFragmentResultListener


/**
 * TODO
 */
suspend fun Fragment.navWithResult(requestKey: String) {

    this.setFragmentResultListener(requestKey){ key: String, bundle: Bundle ->

    }



}
