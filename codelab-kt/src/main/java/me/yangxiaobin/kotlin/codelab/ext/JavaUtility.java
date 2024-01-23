package me.yangxiaobin.kotlin.codelab.ext;

import java.util.Arrays;
import java.util.stream.Collectors;

public class JavaUtility {

   public static String getStackTrace (int stackDepth) {

      Throwable throwable = new Throwable();
      StackTraceElement[] stackTrace = throwable.getStackTrace();

      return Arrays.stream(stackTrace)
              .limit(stackDepth)
              .map(StackTraceElement::toString)
              .collect(Collectors.joining("\r\n"));
   }
}
