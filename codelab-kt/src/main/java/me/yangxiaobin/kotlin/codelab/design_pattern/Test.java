package me.yangxiaobin.kotlin.codelab.design_pattern;

class Test {

   public static void main (String[] args) {

      ResponseChainHandler<Integer,String> handler = new ResponseChainHandler<>(0);
      handler.add((ParametricInterceptor.Chain<Integer, String> chain) -> "");
      handler.getProcessed();



      ParameterizedResponseChainHandler<Integer,String,Float> ph = new ParameterizedResponseChainHandler<>(0);
      String processed = ph.getProcessed(1F);
   }
}
