package com.example.java_io

import java.io.Closeable


fun Closeable.closeSafely() {
    try {
        this.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
