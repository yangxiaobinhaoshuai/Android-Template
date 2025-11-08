package me.yangxiaobin.android.embedding_compat

import android.view.View
import com.wkj.json.mediator.OrgJsonConverter
import me.yangxiaobin.common_ui.EmptyFragment


data class User(val name: String, val age: Int, val isStudent: Boolean)
data class Person(var name: String? = null, var age: Int? = null)
data class Department(val name: String, val employees: List<Person>)


class JsonMediatorFragment : EmptyFragment() {

    private val converter = OrgJsonConverter

    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

        val user = User("Alice", 30, true)
        val result = converter.toJson(user)

        logD("Serialization should succeed: $result.")

    }

}
