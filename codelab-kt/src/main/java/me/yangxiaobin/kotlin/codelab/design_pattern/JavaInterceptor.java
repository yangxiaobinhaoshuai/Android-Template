package me.yangxiaobin.kotlin.codelab.design_pattern;

public interface JavaInterceptor<IN, OUT> {

    OUT intercept (JavaChain<IN, OUT> chain);

    public interface JavaChain<IN, OUT> {
        OUT proceed (IN input);
    }
}
