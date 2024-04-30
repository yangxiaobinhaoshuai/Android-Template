package me.yangxiaobin.kotlin.codelab.design_pattern;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.functions.Function0;

class Test {

   public static void main (String[] args) {

      // 1.
      ResponseChainHandler<Integer,String> handler = new ResponseChainHandler<>(0);
      handler.add((Interceptor.Chain<Integer, String> chain) -> "");
      handler.getProcessed();

      // 2.
      ParameterizedResponseChainHandler<Integer,String,Float> ph = new ParameterizedResponseChainHandler<>(0);
      String processed = ph.getProcessed(1F);

      // 3.
      StrategyHandler<Integer, Integer> strategyHandler = new StrategyHandler<>();
      strategyHandler.addStrategy(new StrategyAware<>() {
         @Override
         public Integer execute (Integer param) {
            return null;
         }

         @Override
         public boolean condition () {
            return false;
         }
      });
      strategyHandler.execute(0);
   }
}
