package com.guangdong.cn.utils;

import java.util.Comparator;
import java.util.concurrent.ConcurrentSkipListSet;

//所有线程共同写入到的一个set集合:单例
public class SetFactory {
    private static ConcurrentSkipListSet<String> set;
    static {
        set = new ConcurrentSkipListSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
    }

    public static ConcurrentSkipListSet<String> getSet(){
        return set;
    }

}
