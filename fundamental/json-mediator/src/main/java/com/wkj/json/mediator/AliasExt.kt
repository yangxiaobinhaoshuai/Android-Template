package com.wkj.json.mediator

import com.google.gson.Gson
import com.squareup.moshi.Moshi


typealias OrgJson = org.json.JSONObject
typealias OrgJsonArray = org.json.JSONArray

val gson = Gson()
val moshi = Moshi.Builder().build()
val kotlinxJson = kotlinx.serialization.json.Json

fun test(){
    val testJson = ""
}
