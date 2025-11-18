package com.wkj.common.scripts.annotation

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter

class TimeTrackMethodVisitor(
    api: Int,
    mv: MethodVisitor,
    access: Int,
    private val methodName: String,
    descriptor: String,
    private val className: String,
    private val config: TimeTrackConfig,
    private val onFound: () -> Unit
) : AdviceAdapter(api, mv, access, methodName, descriptor) {

    private var hasAnnotation = false
    private var startTimeVar = -1

    override fun visitAnnotation(desc: String, visible: Boolean): AnnotationVisitor? {
        println("    🔍 Checking annotation: $desc")

        // 更稳妥一点，最好用 descriptor 完整比对
        // if (desc == "Lcom/xxx/TimeTrack;") { ... }
        if (desc.contains("TimeTrack")) {
            println("    ✓ Found @TimeTrack annotation!")
            hasAnnotation = true
            onFound()
        }

        return super.visitAnnotation(desc, visible)
    }

    override fun onMethodEnter() {
        // ⭐ 这里一定要用 this 的 visitXxx，而不是 mv.visitXxx，
        // 这样 AdviceAdapter 才能正确处理 <init> 里的 super() 之前插桩的情况。

        // 无论有没有注解，先打个 log
        visitLdcInsn("TIMETRACK_TEST")
        visitLdcInsn("========== METHOD INSTRUMENTED: $className.$methodName ==========")
        visitMethodInsn(
            INVOKESTATIC,
            "android/util/Log",
            "e",
            "(Ljava/lang/String;Ljava/lang/String;)I",
            false
        )
        visitInsn(POP)

        if (!hasAnnotation) return

        println("    📝 Injecting time tracking code at method enter")

        startTimeVar = newLocal(Type.LONG_TYPE)

        // long startTime = System.currentTimeMillis();
        visitMethodInsn(
            INVOKESTATIC,
            "java/lang/System",
            "currentTimeMillis",
            "()J",
            false
        )
        visitVarInsn(LSTORE, startTimeVar)
    }

    override fun onMethodExit(opcode: Int) {
        if (!hasAnnotation || startTimeVar == -1) return

        println("    📝 Injecting time tracking code at method exit")

        val durationVar = newLocal(Type.LONG_TYPE)

        // long duration = System.currentTimeMillis() - startTime;
        visitMethodInsn(
            INVOKESTATIC,
            "java/lang/System",
            "currentTimeMillis",
            "()J",
            false
        )
        visitVarInsn(LLOAD, startTimeVar)
        visitInsn(LSUB)
        visitVarInsn(LSTORE, durationVar)

        val threshold = config.threshold.get().toLong()

        if (threshold > 0L) {
            val skipLabel = newLabel()

            // if (duration < threshold) goto skipLabel
            visitVarInsn(LLOAD, durationVar)
            visitLdcInsn(threshold)       // 🔴 这里一定要是 long 常量
            visitInsn(LCMP)
            visitJumpInsn(IFLT, skipLabel)

            printLog(durationVar)

            visitLabel(skipLabel)
            // ❌ 不要手写 visitFrame，交给 AGP / ASM 自己算
            // visitFrame(F_SAME, 0, null, 0, null)
        } else {
            printLog(durationVar)
        }
    }

    private fun printLog(durationVar: Int) {
        val tag = config.tag.get()
        val simpleClassName = className.replace('/', '.')

        // 这里同样不要直接用 mv.xxx，统一用 visitXxx

        // Log.d(tag, "ClassName.method: XXms")
        visitLdcInsn(tag)

        // new StringBuilder()
        visitTypeInsn(NEW, "java/lang/StringBuilder")
        visitInsn(DUP)
        visitMethodInsn(
            INVOKESPECIAL,
            "java/lang/StringBuilder",
            "<init>",
            "()V",
            false
        )

        // append("ClassName.method: ")
        visitLdcInsn("$simpleClassName.$methodName: ")
        visitMethodInsn(
            INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "append",
            "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
            false
        )

        // append(duration)
        visitVarInsn(LLOAD, durationVar)
        visitMethodInsn(
            INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "append",
            "(J)Ljava/lang/StringBuilder;",
            false
        )

        // append("ms")
        visitLdcInsn("ms")
        visitMethodInsn(
            INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "append",
            "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
            false
        )

        // toString()
        visitMethodInsn(
            INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "toString",
            "()Ljava/lang/String;",
            false
        )

        // Log.d(tag, message)
        visitMethodInsn(
            INVOKESTATIC,
            "android/util/Log",
            "d",
            "(Ljava/lang/String;Ljava/lang/String;)I",
            false
        )
        visitInsn(POP)
    }
}