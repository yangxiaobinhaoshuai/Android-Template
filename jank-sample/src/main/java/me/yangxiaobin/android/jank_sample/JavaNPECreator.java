package me.yangxiaobin.android.jank_sample;

import androidx.annotation.NonNull;

class JavaNPECreator {

    private MyListener mListener;

    public void createNPE(){
        addListener(mListener);
    }

    public void createNPE1(){
        mListener.addListener(new MyListener());
    }


    public void addListener (@NonNull MyListener myListener) {

    }

}
