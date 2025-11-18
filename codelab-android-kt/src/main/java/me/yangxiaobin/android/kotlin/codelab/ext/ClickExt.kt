package me.yangxiaobin.android.kotlin.codelab.ext

import android.view.View
import me.yangxiaobin.kotlin.codelab.design_pattern.Transform
import me.yangxiaobin.kotlin.codelab.design_pattern.TransformInterceptor
import me.yangxiaobin.kotlin.codelab.design_pattern.TransformProcessor
import me.yangxiaobin.kotlin.codelab.design_pattern.assembleTransformPipeline


typealias ClickTransform = Transform<View, Unit>
typealias ClickProcessor = TransformProcessor<View, Unit>
typealias ClickInterceptor = TransformInterceptor<View, Unit>

// Examples of ClickProcessors:
/*val debounceInterceptor = interceptTransform<View, Unit> { next, v ->
    if (!AntiShake.isFastClick(v)) {
        next(v)
    }
}

val logClickInterceptor = interceptTransform<View, Unit> { next, v ->
    Log.d("CLICK", "clicked view=${v.id}")
    next(v)
}

val costInterceptor = interceptTransform<View, Unit> { next, v ->
    val start = System.currentTimeMillis()
    next(v)
    Log.d("CLICK", "cost = ${System.currentTimeMillis() - start}ms")
}*/


typealias ClickBinder = View.((View) -> Unit) -> Unit


fun View.setConfigurableClickListener(
    binder: ClickBinder = { block -> setOnClickListener(block) },
    vararg processors: ClickProcessor,
    realOnClick: (View) -> Unit,
) {
    val finalTransform: ClickTransform =
        assembleTransformPipeline(
            transform = realOnClick,
            processors = processors
        )

    binder { v ->
        finalTransform(v)
    }
}

/**
 * Listener style
 * fun View.setMyClickListener(listener: View.OnClickListener) { ... }
 */
fun View.setConfigurableClickListener(
    listenerExt: View.(View.OnClickListener) -> Unit,
    vararg processors: ClickProcessor,
    realOnClick: (View) -> Unit,
) {
    val finalTransform = assembleTransformPipeline(
        transform = realOnClick,
        processors = processors,
    )

    listenerExt(View.OnClickListener { v ->
        finalTransform(v)
    })
}