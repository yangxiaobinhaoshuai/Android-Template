package me.yangxiaobin.android.codelab

import androidx.fragment.app.commit
import kotlinx.android.synthetic.main.activity_main.*
import me.yangxiaobin.android.codelab.recyclerview.RecyclerViewFragment
import me.yangxiaobin.android.kotlin.codelab.base.AbsActivity

class MainActivity : AbsActivity() {

    override val contentResId: Int
        get() = R.layout.activity_main


    override fun afterOnCreate() {
        super.afterOnCreate()
        init()
    }
    private fun init(){
        tv_main.setOnClickListener{
            logD(" click tv_main")
            supportFragmentManager.commit {
                addToBackStack(null)
                add(RecyclerViewFragment(),"")
            }
        }
    }

}
