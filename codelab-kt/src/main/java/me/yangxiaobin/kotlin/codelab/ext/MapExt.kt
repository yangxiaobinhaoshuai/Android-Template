package me.yangxiaobin.kotlin.codelab.ext


fun <K, V> LinkedHashMap<K, V>.getByIndex(index: Int): Map.Entry<K, V> =
    this.entries.toList()[index]
