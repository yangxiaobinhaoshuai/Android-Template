package me.yangxiaobin.android.codelab.npe;

public class NPECreator {

    private String nullString;

    /**
     *    java.lang.NullPointerException: Attempt to invoke virtual method 'int java.lang.String.hashCode()' on a null object reference
     */
    public void createNPE(){
        nullString.hashCode();
    }
}
