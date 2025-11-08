package com.wkj.json.mediator

import java.lang.reflect.Modifier


/**
 * ParseAble 接口的 org.json 实现
 * 包装一个 JSONObject，提供类型安全的访问方法。
 */
class OrgJsonParseAble(private val jsonObject: OrgJson = OrgJson()) : ParseAble {

    override fun optString(key: String): String? = jsonObject.optString(key, null)

    override fun optInt(key: String): Int? = jsonObject.opt(key) as? Int

    override fun optBoolean(key: String): Boolean? = jsonObject.opt(key) as? Boolean

    override fun optJsonObject(key: String): ParseAble? {
        return jsonObject.optJSONObject(key)?.let { OrgJsonParseAble(it) }
    }

    override fun optJsonArray(key: String): List<ParseAble?>? {
        return jsonObject.optJSONArray(key)?.let { jsonArray ->
            (0 until jsonArray.length()).map { i ->
                jsonArray.optJSONObject(i)?.let { OrgJsonParseAble(it) }
            }
        }
    }

    /**
     * 将当前的 JSONObject 转换为一个指定的 data class/POJO。
     * 注意：org.json 没有内置的数据绑定功能，所以我们使用 fromJson 来重新序列化和反序列化。
     * 这是一个变通方法，效率不高，但能实现功能。
     */
    override fun <T : Any> convertTo(clazz: Class<T>): JsonResult<T> {
        return try {
            // 将当前 JSONObject 转回字符串，再用 fromJson(json, class) 方法转换
            val jsonString = this.jsonObject.toString()
            OrgJsonConverter.fromJson(jsonString, clazz)
        } catch (e: Exception) {
            JsonResult.Error(e)
        }
    }
}


object OrgJsonConverter : JsonConverter, JsonGenerator {

    // --- JsonConverter 实现 ---

    /**
     * 将 JSON 字符串反序列化为指定类型的对象。
     * org.json 可以通过构造函数将 JSON 映射到符合 JavaBean 规范的类。
     */
    override fun <T> fromJson(json: String, clazz: Class<T>): JsonResult<T> {
        return try {
            val jsonObject = OrgJson(json)
            // 利用 JSONObject(map) 的构造函数来创建实例
            val instance = clazz.getDeclaredConstructor().newInstance()
            jsonObject.keys().forEach { key ->
                try {
                    val field = clazz.getDeclaredField(key)
                    field.isAccessible = true
                    val value = jsonObject.get(key)
                    // 对 JSONObject.NULL 进行特殊处理
                    if (value != OrgJson.NULL) {
                        field.set(instance, value)
                    }
                } catch (e: NoSuchFieldException) {
                    // 忽略 JSON 中有但类中没有的字段
                }
            }
            JsonResult.Success(instance)
        } catch (e: Exception) {
            // 捕获所有可能的异常，如 JSONException, InstantiationException 等
            JsonResult.Error(e)
        }
    }

    /**
     * 将 JSON 字符串解析为一个通用的 ParseAble 对象。
     */
    override fun fromJson(json: String): JsonResult<ParseAble> {
        return try {
            val jsonObject = OrgJson(json)
            JsonResult.Success(OrgJsonParseAble(jsonObject))
        } catch (e: Exception) {
            JsonResult.Error(e)
        }
    }

    /**
     * 将 JSON 数组字符串反序列化为指定类型的对象列表。
     */
    override fun <T> fromJsonArray(json: String, clazz: Class<T>): JsonResult<List<T>> {
        return try {
            val jsonArray = OrgJsonArray(json)
            val list = (0 until jsonArray.length()).map { i ->
                val jsonObjectString = jsonArray.getJSONObject(i).toString()
                when (val result = fromJson(jsonObjectString, clazz)) {
                    is JsonResult.Success -> result.data
                    is JsonResult.Error -> throw result.exception // 如果任何一个元素转换失败，则整个操作失败
                }
            }
            JsonResult.Success(list)
        } catch (e: Exception) {
            JsonResult.Error(e)
        }
    }

    /**
     * 将 JSON 数组字符串解析为一个通用的 ParseAble 对象列表。
     */
    override fun fromJsonArray(json: String): JsonResult<List<ParseAble?>> {
        return try {
            val jsonArray = OrgJsonArray(json)
            val list = (0 until jsonArray.length()).map { i ->
                jsonArray.optJSONObject(i)?.let { OrgJsonParseAble(it) }
            }
            JsonResult.Success(list)
        } catch (e: Exception) {
            JsonResult.Error(e)
        }
    }

    /**
     * 将任何对象序列化为 JSON 字符串。
     * org.json 可以直接处理 JavaBean、Map、Collection 等。
     */
    override fun toJson(data: Any): JsonResult<String> {
        return try {
            val jsonObject = OrgJson()

            // 使用 Java 反射
            // data.javaClass 是获取 Java Class 对象的标准方式
            data.javaClass.methods
                // 1. 过滤出所有符合 JavaBean getter 规范的方法
                .filter { method ->
                    // 方法必须是 public 的
                    Modifier.isPublic(method.modifiers) &&
                            // 没有参数
                            method.parameterCount == 0 &&
                            // 不能是 getClass() 这个特殊方法
                            method.name != "getClass" &&
                            // 名字以 "get" 或 "is" 开头
                            (method.name.startsWith("get") || method.name.startsWith("is"))
                }
                // 2. 遍历这些 getter 方法
                .forEach { getterMethod ->
                    try {
                        // 3. 从方法名推断出 JSON 的 key
                        val key = if (getterMethod.name.startsWith("get")) {
                            // "getName" -> "name"
                            getterMethod.name.substring(3).replaceFirstChar { it.lowercase() }
                        } else {
                            // "isActive" -> "active"
                            getterMethod.name.substring(2).replaceFirstChar { it.lowercase() }
                        }

                        // 4. 调用 getter 方法获取值
                        val value = getterMethod.invoke(data)

                        // 5. 将键值对放入 JSONObject
                        // put 方法会自动处理嵌套对象和 null
                        jsonObject.put(key, value)

                    } catch (e: Exception) {
                        // 忽略单个属性的转换失败，继续处理下一个
                        // 或者你可以在这里记录日志
                        println("Warning: Failed to serialize property from method ${getterMethod.name}. Error: ${e.message}")
                    }
                }

            JsonResult.Success(jsonObject.toString())
        } catch (e: Exception) {
            JsonResult.Error(e)
        }
    }

    /**
     * 将对象列表序列化为 JSON 数组字符串。
     */
    override fun toJsonArray(data: List<Any>): JsonResult<String> {
        return try {
            // JSONArray 可以直接处理集合
            val jsonArray = OrgJsonArray(data)
            JsonResult.Success(jsonArray.toString())
        } catch (e: Exception) {
            JsonResult.Error(e)
        }
    }

    // --- JsonGenerator 实现 ---

    /**
     * 从键值对构建 JSON 字符串。
     */
    override fun buildJson(vararg kvs: Pair<String, Any>): String {
        val jsonObject = OrgJson()
        kvs.forEach { (key, value) -> jsonObject.put(key, value) }
        return jsonObject.toString()
    }

    /**
     * 根据重复次数和构建器，生成一个 JSON 数组字符串。
     */
    override fun buildJsonArray(repeat: Int, jsonBuilder: () -> List<Pair<String, Any>>): String {
        val jsonArray = OrgJsonArray()
        repeat(repeat) {
            val kvs = jsonBuilder()
            val jsonObject = OrgJson()
            kvs.forEach { (key, value) -> jsonObject.put(key, value) }
            jsonArray.put(jsonObject)
        }
        return jsonArray.toString()
    }
}
