package me.yangxiaobin.ksp_kt_module.ir

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrReturn
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.*
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid
import org.jetbrains.kotlin.name.FqName

class DebugLogIrOperator(
    private val debugLogAnnotations: List<String>,
    pluginContext: IrPluginContext,
) : IrOperator(pluginContext) {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun visitFunction(irFuncDeclaration: IrFunction): IrStatement {

        // 判断函数是否包含注解
        if (debugLogAnnotations.none { irFuncDeclaration.annotations.hasAnnotation(FqName(it)) }) {
            return super.visitFunction(irFuncDeclaration)
        }

        // get `println` FunctionDescriptor
        val printlnFunc: IrSimpleFunctionSymbol = pluginContext.referenceFunctions(FqName("kotlin.io.println"))
                .find { fsymbol: IrSimpleFunctionSymbol ->

                    // 一个参数 && 参数类型是 any && return 类型是 unit
                    fsymbol.owner.valueParameters.size == 1
                            && fsymbol.owner.valueParameters[0].type == pluginContext.irBuiltIns.anyNType
                            && fsymbol.owner.returnType == pluginContext.irBuiltIns.unitType
                }!!


        // create local variable store TimeSource
        val monotonicClass: IrClassSymbol = pluginContext.referenceClass(FqName("kotlin.time.TimeSource.Monotonic"))!!
        val timeMarkClass: IrClassSymbol = pluginContext.referenceClass(FqName("kotlin.time.TimeMark"))!!

        val markNowFunc: IrSimpleFunctionSymbol = monotonicClass.functions.find {
            it.owner.name.identifier == "markNow"
                    && it.owner.valueParameters.isEmpty()
                    && it.owner.returnType == timeMarkClass.defaultType
        }!!

        val elapsedNow: IrSimpleFunctionSymbol = timeMarkClass.functions.find {
            it.owner.name.identifier == "elapsedNow"
        }!!

        // add mark Local
        //  store TimeMark instance
        val markLocal: IrVariable = irFuncDeclaration.createTemporaryVariable(
            "tmp_time$0",
            markNowFunc.call(
                IrGetObjectValueImpl(
                    UNDEFINED_OFFSET,
                    UNDEFINED_OFFSET,
                    monotonicClass.defaultType,
                    monotonicClass
                )
            )
        )

        // add return Local
        //  store original return type
        val returnLocal: IrVariable = irFuncDeclaration.createTemporaryVariable("tmp_return$0", irFuncDeclaration.returnType, true)

        // the function with unit return type may not have any return expression, we need to add a mock one
        var finalReturn: IrReturn? = null
        if (irFuncDeclaration.returnType == pluginContext.irBuiltIns.unitType) {
            finalReturn = IrReturnImpl(
                UNDEFINED_OFFSET,
                UNDEFINED_OFFSET,
                type = irFuncDeclaration.returnType,
                returnTargetSymbol = irFuncDeclaration.symbol,
                value = IrGetObjectValueImpl(
                    UNDEFINED_OFFSET,
                    UNDEFINED_OFFSET,
                    type = pluginContext.irBuiltIns.unitType,
                    symbol = pluginContext.irBuiltIns.unitClass
                )
            )
        }

        (irFuncDeclaration.body?.statements as? MutableList)?.run {
            // println function name before execute function body
            add(0, printlnFunc.staticCall("⇢  ${irFuncDeclaration.name}".ir()))
            add(1, markLocal)
            add(2, returnLocal)
            if (finalReturn != null) {
                add(size, finalReturn)
            }
        }

        irFuncDeclaration.transformChildrenVoid(object : IrElementTransformerVoid() {

            override fun visitReturn(expression: IrReturn): IrExpression {
                // if return target is current function, println elapsed time before return
                if (expression.returnTargetSymbol != irFuncDeclaration.symbol) {
                    return super.visitReturn(expression)
                }

                // println function name before execute function body
                val psta: IrCallImpl = printlnFunc.staticCall(
                    IrStringConcatenationImpl(
                        UNDEFINED_OFFSET,
                        UNDEFINED_OFFSET,
                        pluginContext.irBuiltIns.stringType,
                        listOf(
                            "⇠ ${irFuncDeclaration.name} [ran in ".ir(),
                            elapsedNow.call(
                                IrGetValueImpl(
                                    UNDEFINED_OFFSET,
                                    UNDEFINED_OFFSET,
                                    markLocal.symbol
                                )
                            ),
                            " ms]".ir()
                        )
                    )
                )

                // pass return expression value to local variable
                val set = IrSetValueImpl(
                        startOffset = UNDEFINED_OFFSET,
                        endOffset = UNDEFINED_OFFSET,
                        type = pluginContext.irBuiltIns.unitType,
                        symbol = returnLocal.symbol,
                        value = expression.value,
                        origin = IrStatementOrigin.EQ
                )

                // return local variable value
                expression.value = IrGetValueImpl(
                    startOffset = UNDEFINED_OFFSET,
                    endOffset = UNDEFINED_OFFSET,
                    symbol = returnLocal.symbol
                )

                return IrCompositeImpl(
                    expression.startOffset,
                    expression.endOffset,
                    type = expression.type,
                    origin = null,
                    statements = listOf(
                        set,
                        psta,
                        expression
                    )
                )
            }
        })

        return super.visitFunction(irFuncDeclaration)
    }
}
