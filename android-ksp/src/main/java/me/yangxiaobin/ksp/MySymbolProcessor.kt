package me.yangxiaobin.ksp

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

/**
 *
 * Kotlin poet : https://square.github.io/kotlinpoet/
 *
 * Kotlin KSP : https://kotlinlang.org/docs/ksp-overview.html
 *
 */
class MySymbolProcessor : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        TODO("Not yet implemented")
    }
}
