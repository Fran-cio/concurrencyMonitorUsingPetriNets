package com.bugui_soft.utils;

import java.util.concurrent.ThreadFactory;

public class HilosFactory implements ThreadFactory {

    public Thread newThread(Runnable r) {
        return new Thread(r);
    }

}