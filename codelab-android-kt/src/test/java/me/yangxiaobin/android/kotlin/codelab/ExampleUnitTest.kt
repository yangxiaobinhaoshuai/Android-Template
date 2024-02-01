package me.yangxiaobin.android.kotlin.codelab

import me.yangxiaobin.android.kotlin.codelab.ext.dumpToString
import org.junit.Test

import org.junit.Assert.*
import java.util.ArrayList

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun collectionToString(){
        val list = listOf(1,2,3,4,5,6,)

        val javaList = ArrayList<Int>(10)
        javaList.add(1)
        javaList.add(3)
        javaList.add(5)
        javaList.add(7)

        val map = mapOf(1 to "2",2 to  "3", 3 to "4")

        println("""
            list:$list
            list:${list.dumpToString()}  
            javaList:$javaList 
            map :$map
        """.trimIndent())
    }
}
