package me.yangxiaobin.json_mediator

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wkj.json.mediator.JsonResult
import com.wkj.json.mediator.OrgJsonConverter
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith

// 假设你的实现代码和这些测试类在同一个包或模块中
// 如果不在，请确保 import 路径正确

// --- 测试用的数据类 ---
data class User(val name: String, val age: Int, val isStudent: Boolean)
data class Person(var name: String? = null, var age: Int? = null)
data class Department(val name: String, val employees: List<Person>)


@RunWith(AndroidJUnit4::class)
class OrgJsonConverterTest {

    private val converter = OrgJsonConverter

    //================================================================
    // 1. 序列化测试 (toJson / toJsonArray)
    //================================================================

    @Test
    fun toJson_shouldSerializeSimpleDataClass_successfully() {
        val user = User("Alice", 30, true)
        val result = converter.toJson(user)

        assertTrue(result is JsonResult.Success, "Serialization should succeed")
        val jsonString = (result as JsonResult.Success).data

        val jsonObject = JSONObject(jsonString)
        assertEquals("Alice", jsonObject.getString("name"))
        assertEquals(30, jsonObject.getInt("age"))
        assertEquals(true, jsonObject.getBoolean("isStudent"))
    }

    @Test
    fun toJsonArray_shouldSerializeListOfObjects_successfully() {
        val people = listOf(Person("Bob", 42), Person("Charlie", 25))
        val result = converter.toJsonArray(people)

        assertTrue(result is JsonResult.Success, "Array serialization should succeed")
        val jsonString = (result as JsonResult.Success).data

        assertTrue(jsonString.startsWith("[") && jsonString.endsWith("]"))
        assertTrue(jsonString.contains("""{"name":"Bob","age":42}"""))
        assertTrue(jsonString.contains("""{"name":"Charlie","age":25}"""))
    }

    //================================================================
    // 2. 反序列化测试 (fromJson / fromJsonArray)
    //================================================================

    @Test
    fun fromJson_toSpecificClass_shouldDeserializeSuccessfully() {
        val jsonString = """{"name":"David","age":35}"""
        val result = converter.fromJson(jsonString, Person::class.java)

        assertTrue(result is JsonResult.Success, "Deserialization should succeed")
        val person = (result as JsonResult.Success).data
        assertEquals("David", person.name)
        assertEquals(35, person.age)
    }

    @Test
    fun fromJson_withInvalidJson_shouldReturnError() {
        val invalidJson = """{"name":"Eve", "age":}""" // 语法错误
        val result = converter.fromJson(invalidJson, Person::class.java)

        assertTrue(result is JsonResult.Error, "Deserialization with invalid JSON should fail")
    }

    @Test
    fun fromJson_toParseAbleObject_shouldWorkCorrectly() {
        val jsonString = """{"id":123, "active":true, "user":{"name":"Frank"}}"""
        val result = converter.fromJson(jsonString)

        assertTrue(result is JsonResult.Success, "Parsing to ParseAble should succeed")
        val parsed = (result as JsonResult.Success).data

        assertEquals(123, parsed.optInt("id"))
        assertEquals(true, parsed.optBoolean("active"))
        assertNull(parsed.optString("nonexistentKey"))

        val userObj = parsed.optJsonObject("user")
        assertNotNull(userObj)
        assertEquals("Frank", userObj?.optString("name"))
    }

    @Test
    fun fromJsonArray_toListOfSpecificClass_shouldDeserializeSuccessfully() {
        val jsonArrayString = """[{"name":"Grace","age":28},{"name":"Heidi","age":40}]"""
        val result = converter.fromJsonArray(jsonArrayString, Person::class.java)

        assertTrue(result is JsonResult.Success, "Array deserialization should succeed")
        val people = (result as JsonResult.Success).data
        assertEquals(2, people.size)
        assertEquals("Grace", people[0].name)
        assertEquals(40, people[1].age)
    }

    //================================================================
    // 3. ParseAble 接口功能测试
    //================================================================

    @Test
    fun parseAble_convertTo_shouldConvertToDataClass() {
        val jsonString = """{"name":"Ivan","age":50}"""
        val parseResult = converter.fromJson(jsonString)
        val parsed = (parseResult as JsonResult.Success).data

        val convertResult = parsed.convertTo(Person::class.java)
        assertTrue(convertResult is JsonResult.Success)
        val person = (convertResult as JsonResult.Success).data
        assertEquals("Ivan", person.name)
        assertEquals(50, person.age)
    }

    //================================================================
    // 4. JsonGenerator 接口功能测试
    //================================================================

    @Test
    fun buildJson_shouldGenerateCorrectJsonString() {
        val jsonString = converter.buildJson("key1" to "value1", "key2" to 100, "key3" to true)
        val jsonObject = JSONObject(jsonString)

        assertEquals("value1", jsonObject.getString("key1"))
        assertEquals(100, jsonObject.getInt("key2"))
        assertEquals(true, jsonObject.getBoolean("key3"))
    }

    @Test
    fun buildJsonArray_shouldGenerateRepeatedStructureCorrectly() {
        var counter = 0
        val jsonArrayString = converter.buildJsonArray(3) {
            counter++
            listOf("id" to counter, "type" to "item")
        }

        assertTrue(jsonArrayString.contains("""{"id":1,"type":"item"}"""))
        assertTrue(jsonArrayString.contains("""{"id":2,"type":"item"}"""))
        assertTrue(jsonArrayString.contains("""{"id":3,"type":"item"}"""))
        assertEquals(3, org.json.JSONArray(jsonArrayString).length())
    }

    //================================================================
    // 5. 复杂嵌套场景测试
    //================================================================

    @Test
    fun shouldHandleComplexNestedObjectSerializationAndDeserialization() {
        // 准备数据
        val dept = Department(
            name = "Engineering",
            employees = listOf(Person("Judy", 33), Person("Mallory", 29))
        )

        // 序列化
        val toJsonResult = converter.toJson(dept)
        assertTrue(toJsonResult is JsonResult.Success)
        val jsonString = (toJsonResult as JsonResult.Success).data

        // 验证序列化结果
        assertTrue(jsonString.contains("Engineering"))
        assertTrue(jsonString.contains("Judy"))

        // 反序列化为 ParseAble
        val parseResult = converter.fromJson(jsonString)
        assertTrue(parseResult is JsonResult.Success)
        val parsed = (parseResult as JsonResult.Success).data

        // 验证 ParseAble
        assertEquals("Engineering", parsed.optString("name"))
        val employeesArray = parsed.optJsonArray("employees")
        assertNotNull(employeesArray)
        assertEquals(2, employeesArray?.size)
        assertEquals("Judy", employeesArray?.get(0)?.optString("name"))
    }
}
