package com.bugui_soft.utils;

import java.util.concurrent.ThreadFactory;

class FabricaHilos implements ThreadFactory {

    public FabricaHilos() {
        //
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        return t;
    }
}
