package me.yangxiaobin.kotlin.codelab.design_pattern;

import java.util.List;

public class JavaChainImpl<IN, OUT> implements JavaInterceptor.JavaChain<IN, OUT> {

    private final List<JavaInterceptor<IN, OUT>> mJavaInterceptors;

    private final int index;

    private final IN origin;


    public JavaChainImpl (List<JavaInterceptor<IN, OUT>> javaInterceptors, int index, IN origin) {
        mJavaInterceptors = javaInterceptors;
        this.index = index;
        this.origin = origin;
    }


    @Override
    public OUT proceed (IN input) {
        JavaInterceptor<IN, OUT> nextInterceptor = mJavaInterceptors.get(index + 1);
        JavaChainImpl<IN, OUT> nextChain = new JavaChainImpl<>(mJavaInterceptors, index + 1, origin);
        return nextInterceptor.intercept(nextChain);
    }
}
