package me.yangxiaobin.kotlin.codelab.design_pattern;

class Test {

   public static void main (String[] args) {

      // 1.
      ResponseChainHandler<Integer,String> handler = new ResponseChainHandler<>(0);
      handler.add((Interceptor.Chain<Integer, String> chain) -> "");
      handler.getProcessed();

      // 2.
      ParameterizedResponseChainHandler<Integer,String,Float> ph = new ParameterizedResponseChainHandler<>(0);
      String processed = ph.getProcessed(1F);

   }
}
