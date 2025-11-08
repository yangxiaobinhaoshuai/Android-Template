package com.wkj.json.mediator

sealed class JsonResult<out T> {
    data class Success<T>(val data: T) : JsonResult<T>()
    data class Error(val exception: Exception, val message: String? = null) : JsonResult<Nothing>()
}

interface JsonGenerator {

    fun buildJson(vararg kvs: Pair<String, Any>): String

    fun buildJsonArray(repeat: Int, jsonBuilder: () -> List<Pair<String, Any>>): String

}

interface ParseAble {

    fun optString(key: String): String?

    fun optInt(key: String): Int?

    fun optBoolean(key: String): Boolean?

    fun optJsonObject(key: String): ParseAble?

    fun optJsonArray(key: String): List<ParseAble?>?

    fun <T : Any> convertTo(clazz: Class<T>): JsonResult<T>

}


interface JsonConverter {

    fun <T> fromJson(json: String, clazz: Class<T>): JsonResult<T>

    fun fromJson(json: String): JsonResult<ParseAble>

    fun <T> fromJsonArray(json: String, clazz: Class<T>): JsonResult<List<T>>

    fun fromJsonArray(json: String): JsonResult<List<ParseAble?>>

    fun toJson(data: Any): JsonResult<String>

    fun toJsonArray(data: List<Any>): JsonResult<String>
}
