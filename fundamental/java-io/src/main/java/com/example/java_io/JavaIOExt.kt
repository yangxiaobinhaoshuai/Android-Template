package com.example.java_io

import java.io.Closeable
import java.io.File


fun Closeable.closeSafely() {
    try {
        this.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * TODO
 */
fun File.ensureExistence(){
    if (!this.exists()) {
        if (this.isFile) this.createNewFile()
        if (this.isDirectory) this.mkdirs()
    }
}
