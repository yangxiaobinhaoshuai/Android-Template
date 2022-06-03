package me.yangxiaobin.android_kcp.module_service

import com.android.build.api.artifact.MultipleArtifact
import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.instrumentation.*
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.Variant
import com.google.auto.service.AutoService
import javassist.ClassPool
import me.yangxiaobin.android_kcp.PLog
import me.yangxiaobin.android_kcp.YangSubPluginArtifact
import me.yangxiaobin.android_kcp.YangSubPluginOption
import me.yangxiaobin.lib.ext.getAppExtension
import me.yangxiaobin.lib.ext.touch
import me.yangxiaobin.lib.log.ILog
import me.yangxiaobin.lib.log.LogLevel
import me.yangxiaobin.lib.transform.AbsLegacyTransform
import me.yangxiaobin.logger.RawLogger
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformOutputs
import org.gradle.api.artifacts.transform.TransformParameters
import org.gradle.api.artifacts.transform.TransformSpec
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import java.io.File
import java.util.function.Function


/**
 * TODO 未能实现 find 一边 annotation mapping， 然后在插桩
 */
private const val SERVICE_PROVIDER_PATH = "me.yangxiaobin.module_service_provider.ServiceProvider"
private const val CLASS_SUFFIX = ".class"

private val pluginLogger = RawLogger
private val pluginLogD = fun(message: String) = pluginLogger.d(LOG_TAG, message)
private const val LOG_TAG = "ModuleService-plugin"


@AutoService(KotlinCompilerPluginSupportPlugin::class)
class ModuleServicePlugin : KotlinCompilerPluginSupportPlugin, me.yangxiaobin.lib.BasePlugin() {

    override fun apply(p: Project) {
        super<me.yangxiaobin.lib.BasePlugin>.apply(p)
        PLog.d("ModuleServicePlugin applied to ${p.name}.")

        p.extensions.create("moduleService", ModuleServiceExt::class.java)

//        p.afterEvaluate {
//            p.getAppExtension?.registerTransform(object : AbsLegacyTransform(p) {
//
//
//                init {
//                    println("---> legacy transform")
//                }
//                override fun getClassTransformer(): Function<ByteArray, ByteArray>? {
//                    return Function { bs: ByteArray ->
//                        println("---> bssss :$bs.")
//                        bs
//                    }
//                }
//
//                override fun transform(invocation: TransformInvocation) {
//                    super.transform(invocation)
//                    println("---> tttttt ")
//                }
//            })
//        }

        doInstrumentation(p)
    }

    private fun doInstrumentation(target: Project) {
        val androidComponents = target.extensions.getByType(AndroidComponentsExtension::class.java)

        androidComponents.beforeVariants {  }
        androidComponents.onVariants { variant: Variant ->

//            variant.instrumentation.transformClassesWith(
//                ModuleServiceClassVisitorFactory::class.java,
//                InstrumentationScope.ALL
//            ) { param: ModuleServiceParam ->
//                param.annotationName.set(target.extensions.findByType(ModuleServiceExt::class.java)?.annotations?.firstOrNull())
//            }
//
//            variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COPY_FRAMES)


            // Process some file after asm instrumentation.
//            val taskProvider: TaskProvider<ModifyClassesTask> =
//                target.tasks.register("${variant.name}ModifyClasses", ModifyClassesTask::class.java)
//
//            @Suppress("UnstableApiUsage")
//            variant.artifacts.use(taskProvider)
//                //.wiredWithFiles()
//                .wiredWith(ModifyClassesTask::allClasses, ModifyClassesTask::output)
//                .toTransform(MultipleArtifact.ALL_CLASSES_DIRS)

//            @Suppress("UnstableApiUsage")
//            variant.artifacts.use(taskProvider)
//                .wiredWith(ModifyClassesTask::allJars, ModifyClassesTask::jarsOutput)
//                .toTransform(MultipleArtifact.ALL_CLASSES_JARS)

//            target.tasks.register("${variant.name}AarUpload",AarUploadTask::class.java) {
//                it.aar.set(variant.artifacts.get(SingleArtifact.AAR))
//            }


//            target.tasks.register("${variant.name}GetAllClasses",GetAllClassesTask::class.java) {
//                it.allClasses.set(variant.artifacts.getAll(MultipleArtifact.ALL_CLASSES_DIRS))
//                it.allJarsWithClasses.set(variant.artifacts.getAll(MultipleArtifact.ALL_CLASSES_JARS))
//            }

//            val updateArtifact = target.tasks.register("${variant.name}UpdateArtifact",UpdateArtifactTask::class.java){
//                it.initialArtifact.set(variant.artifacts.get(SingleArtifact.AAR))
//            }
//            val finalArtifact = target.tasks.register("${variant.name}ConsumeArtifact",ConsumeArtifactTask::class.java) {
//                it.finalArtifact.set(variant.artifacts.get(SingleArtifact.AAR))
//            }
//            variant.artifacts.use(updateArtifact)
//                .wiredWithFiles(
//                    UpdateArtifactTask::initialArtifact,
//                    UpdateArtifactTask::updatedArtifact)
//                .toTransform(SingleArtifact.AAR)


            val assetCreationTask = target.tasks.register("create${variant.name}Asset",AssetCreatorTask::class.java)
            variant.artifacts.use(assetCreationTask)
                .wiredWith(AssetCreatorTask::outputDirectory)
                .toAppendTo(MultipleArtifact.ASSETS)

        }
    }

    interface ModuleServiceParam : InstrumentationParameters {
        @get:Input
        val annotationName: Property<String?>
    }

    abstract class ModuleServiceClassVisitorFactory : AsmClassVisitorFactory<ModuleServiceParam> {

        private val annotationString: String get() = parameters.get().annotationName.get() ?: ""

        override fun createClassVisitor(
            classContext: ClassContext,
            nextClassVisitor: ClassVisitor,
        ): ClassVisitor {

            println("---> createClassVisitor visitor  :${nextClassVisitor.hashCode()}")

            return object : ClassVisitor(Opcodes.ASM7, nextClassVisitor) {

                init {
                    println("---> class visitor construct : ${this.hashCode()}. ${classContext.currentClassData.className}")
                }

                override fun visitAnnotation(
                    descriptor: String?,
                    visible: Boolean,
                ): AnnotationVisitor {
                    // return super.visitAnnotation(descriptor, visible)
                    return object : AnnotationVisitor(Opcodes.ASM7) {


                        override fun visit(name: String?, value: Any?) {
                            super.visit(name, value)

                            if (name == "kClazzInterface" && value is org.objectweb.asm.Type){
                                pluginLogD("visitAnnotation, annotation name: value :${value.className}.")
                            }


                        }


                    }
                }
            }
        }

        override fun isInstrumentable(classData: ClassData): Boolean {
            val annotations: List<String> = classData.classAnnotations
            // Contain specific annotation
            val processed = annotations.contains(annotationString) || classData.className == SERVICE_PROVIDER_PATH || classData.className.contains("mainactivity",true)
            if (processed) pluginLogD("Module plugin processed file name :${classData.className}.")
            return processed
        }
    }

    abstract class ModuleServiceClassVisitorFactory1 : AsmClassVisitorFactory<ModuleServiceParam> {

        private val annotationString: String get() = parameters.get().annotationName.get() ?: ""

        override fun createClassVisitor(
            classContext: ClassContext,
            nextClassVisitor: ClassVisitor,
        ): ClassVisitor {

            println("---> createClassVisitor1 visitor  :${nextClassVisitor.hashCode()}")

            return object : ClassVisitor(Opcodes.ASM7, nextClassVisitor) {

                init {
                    println("---> class visitor1 construct : ${this.hashCode()}. ${classContext.currentClassData.className}")
                }

                override fun visitAnnotation(
                    descriptor: String?,
                    visible: Boolean,
                ): AnnotationVisitor {
                    // return super.visitAnnotation(descriptor, visible)
                    return object : AnnotationVisitor(Opcodes.ASM7) {


                        override fun visit(name: String?, value: Any?) {
                            super.visit(name, value)

                            if (name == "kClazzInterface" && value is org.objectweb.asm.Type){
                                pluginLogD("visitAnnotation11, annotation name: value :${value.className}.")
                            }


                        }


                    }
                }
            }
        }

        override fun isInstrumentable(classData: ClassData): Boolean {
            val annotations: List<String> = classData.classAnnotations
            // Contain specific annotation
            val processed = annotations.contains(annotationString) || classData.className == SERVICE_PROVIDER_PATH || classData.className.contains("mainactivity",true)
            if (processed) pluginLogD("Module plugin processed 111 file name :${classData.className}.")
            return processed
        }
    }


    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        val p: Project = kotlinCompilation.target.project

        val ext: ModuleServiceExt? = p.extensions.findByType(ModuleServiceExt::class.java)

        val annotationList: List<String> = ext?.annotations ?: emptyList()


        return kotlinCompilation.target.project.provider {
            annotationList.map { annotation: String ->
                YangSubPluginOption(optKey = KEY_ANNOTATION_SUB_PLUGIN_OPTION, optVal = annotation)
            }
        }
    }

    override fun getCompilerPluginId(): String = MODULE_INSTRUMENT_PLUGIN_ID

    override fun getPluginArtifact(): SubpluginArtifact =
        YangSubPluginArtifact(artifactId = "kcp-kt-module-instrument")

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true

    private companion object {
        const val MODULE_INSTRUMENT_PLUGIN_ID = "moduleInstrument"
        const val KEY_ANNOTATION_SUB_PLUGIN_OPTION = "annotation"
    }
}

open class ModuleServiceExt {
    var annotations: List<String> = emptyList()
}

abstract class ModifyClassesTask : DefaultTask() {

//    @get:InputFiles
//    abstract val allClasses: ListProperty<Directory>
//
//
//    @get:OutputFiles
//    abstract val output: DirectoryProperty


    @get:InputFiles
    abstract val allJars: ListProperty<RegularFile>

    @get:OutputFiles
    abstract val jarsOutput: RegularFileProperty

    @TaskAction
    fun taskAction() {

        allJars.get().forEach {  regularFile: RegularFile ->
            println("---> regularFile :${regularFile.asFile.path}")
        }


        val pool = ClassPool(ClassPool.getDefault())

//        allClasses.get().forEach { directory: Directory ->
//
//            println("Directory : ${directory.asFile.absolutePath}")
//
//            directory.asFile.walk().filter(File::isFile).forEach { file: File ->
//
//                println("---> file,isdir :${file.isDirectory} name :${file.name} , ${output.get().asFile}")
//
//                file.copyTo((output.get().asFile).touch(), true)
//
//
//                //if (file.name == "SomeSource.class") {
////                if (file.name == SERVICE_PROVIDER_PATH + CLASS_SUFFIX) {
////
////                    println("File : ${file.absolutePath}")
////
////                    val interfaceClass: CtClass = pool.makeInterface("com.android.api.tests.SomeInterface")
////                    println("Adding $interfaceClass")
////                    interfaceClass.writeFile(output.get().asFile.absolutePath)
////
////                    FileInputStream(file).use {
////                        val ctClass: CtClass = pool.makeClass(it);
////                        ctClass.addInterface(interfaceClass)
////                        ctClass.getDeclaredMethod("toString")?.insertBefore("{ System.out.println(\"Some Extensive Tracing\"); }")
////                        println("---> out dir :${output.get().asFile.absolutePath}------>")
////                        ctClass.writeFile(output.get().asFile.absolutePath)
////                    }
////                }
//            }
//        }
    }
}


abstract class AarUploadTask : DefaultTask() {

    @get:InputFile
    abstract val aar: RegularFileProperty

    @TaskAction
    fun taskAction() {
        println("Uploading ${aar.get().asFile.absolutePath} to fantasy server...")
    }
}


abstract class GetAllClassesTask : DefaultTask() {

    @get:InputFiles
    abstract val allClasses: ListProperty<Directory>

    @get:InputFiles
    abstract val allJarsWithClasses: ListProperty<RegularFile>

    @TaskAction
    fun taskAction() {

        allClasses.get().forEach { directory ->
            println("Directory : ${directory.asFile.absolutePath}")
            directory.asFile.walk().filter(File::isFile).forEach { file ->
                println("File : ${file.absolutePath}")
            }
            allJarsWithClasses.get().forEach { file ->
                println("JarFile : ${file.asFile.absolutePath}")
            }
        }
    }
}


abstract class UpdateArtifactTask: DefaultTask() {
    @get: InputFiles
    abstract val  initialArtifact: RegularFileProperty

    @get: OutputFile
    abstract val updatedArtifact: RegularFileProperty

    @TaskAction
    fun taskAction() {
        val versionCode = "artifactTransformed = true"
        println("artifactPresent = " + initialArtifact.isPresent)
        updatedArtifact.get().asFile.writeText(versionCode)
    }
}
abstract class ConsumeArtifactTask: DefaultTask() {
    @get: InputFiles
    abstract val finalArtifact: RegularFileProperty

    @TaskAction
    fun taskAction() {
        println(finalArtifact.get().asFile.readText())
    }
}


abstract class AssetCreatorTask: DefaultTask() {
    @get:OutputFiles
    abstract val outputDirectory: DirectoryProperty

    @ExperimentalStdlibApi
    @TaskAction
    fun taskAction() {
        outputDirectory.get().asFile.mkdirs()
        File(outputDirectory.get().asFile, "custom_asset.txt")
            .writeText("some real asset file")
    }
}
