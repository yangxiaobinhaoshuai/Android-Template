package com.wkj.common.scripts.annotation


import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter

public class TimeTrackMethodVisitor(
    mv: MethodVisitor,
    access: Int,
    private val methodName: String,
    descriptor: String,
    private val className: String,
    private val config: TimeTrackConfig,
    private val onFound: () -> Unit
) : AdviceAdapter(Opcodes.ASM9, mv, access, methodName, descriptor) {

    private var hasAnnotation = false
    private var startTimeVar = -1

    override fun visitAnnotation(desc: String, visible: Boolean): AnnotationVisitor? {
        println("    🔍 Checking annotation: $desc")

        // 匹配 @TimeTrack 注解
        if (desc.contains("TimeTrack")) {
            println("    ✓ Found @TimeTrack annotation!")
            hasAnnotation = true
            onFound()
        }

        return super.visitAnnotation(desc, visible)
    }

    override fun onMethodEnter() {

        // ⭐ 强制打印日志，不管什么情况
        mv.visitLdcInsn("TIMETRACK_TEST")
        mv.visitLdcInsn("========== METHOD INSTRUMENTED: $className.$methodName ==========")
        mv.visitMethodInsn(INVOKESTATIC, "android/util/Log", "e",  // 用 ERROR 级别
            "(Ljava/lang/String;Ljava/lang/String;)I", false)
        mv.visitInsn(POP)
        
        if (!hasAnnotation) return

        println("    📝 Injecting time tracking code at method enter")

        startTimeVar = newLocal(Type.LONG_TYPE)

        // long startTime = System.currentTimeMillis();
        mv.visitMethodInsn(
            INVOKESTATIC,
            "java/lang/System",
            "currentTimeMillis",
            "()J",
            false
        )
        mv.visitVarInsn(LSTORE, startTimeVar)
    }

    override fun onMethodExit(opcode: Int) {
        if (!hasAnnotation || startTimeVar == -1) return

        println("    📝 Injecting time tracking code at method exit")

        val durationVar = newLocal(Type.LONG_TYPE)

        // long duration = System.currentTimeMillis() - startTime;
        mv.visitMethodInsn(
            INVOKESTATIC,
            "java/lang/System",
            "currentTimeMillis",
            "()J",
            false
        )
        mv.visitVarInsn(LLOAD, startTimeVar)
        mv.visitInsn(LSUB)
        mv.visitVarInsn(LSTORE, durationVar)

        // 阈值判断
        val threshold = config.threshold.get()
        if (threshold > 0) {
            val skipLabel = newLabel()
            mv.visitVarInsn(LLOAD, durationVar)
            mv.visitLdcInsn(threshold)
            mv.visitInsn(LCMP)
            mv.visitJumpInsn(IFLT, skipLabel)
            printLog(durationVar)
            mv.visitLabel(skipLabel)
            mv.visitFrame(F_SAME, 0, null, 0, null)
        } else {
            printLog(durationVar)
        }
    }

    private fun printLog(durationVar: Int) {
        val tag = config.tag.get()
        val simpleClassName = className.replace('/', '.')

        // Log.d(tag, "ClassName.method: XXms")
        mv.visitLdcInsn(tag)

        // new StringBuilder()
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder")
        mv.visitInsn(DUP)
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false)

        // append("ClassName.method: ")
        mv.visitLdcInsn("$simpleClassName.$methodName: ")
        mv.visitMethodInsn(
            INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "append",
            "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
            false
        )

        // append(duration)
        mv.visitVarInsn(LLOAD, durationVar)
        mv.visitMethodInsn(
            INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "append",
            "(J)Ljava/lang/StringBuilder;",
            false
        )

        // append("ms")
        mv.visitLdcInsn("ms")
        mv.visitMethodInsn(
            INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "append",
            "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
            false
        )

        // toString()
        mv.visitMethodInsn(
            INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "toString",
            "()Ljava/lang/String;",
            false
        )

        // Log.d(tag, message)
        mv.visitMethodInsn(
            INVOKESTATIC,
            "android/util/Log",
            "d",
            "(Ljava/lang/String;Ljava/lang/String;)I",
            false
        )
        mv.visitInsn(POP)
    }
}
