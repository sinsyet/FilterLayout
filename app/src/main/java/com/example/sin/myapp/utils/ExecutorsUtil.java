package com.example.sin.myapp.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ExecutorsUtil {
    private static ExecutorService mInstance;
    static
    {
        mInstance = Executors.newCachedThreadPool();
    }

    public static void exec(Runnable r){
        if (r == null) {
            return;
        }

        mInstance.execute(r);
    }
}
