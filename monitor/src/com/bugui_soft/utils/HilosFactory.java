/*
 * Hilos factory
 *
 * Version 1.0
 *
 * Copyright BeerWare
 */
package com.bugui_soft.utils;

import java.util.concurrent.ThreadFactory;

public class HilosFactory implements ThreadFactory {
    private static final Object lock = new Object();
    private static HilosFactory hilosFactory;
    private HilosFactory() { }

    public static HilosFactory getInstanceOfThreadFactory() {
        synchronized (lock) {
            if (hilosFactory == null) {
                hilosFactory = new HilosFactory();
            } else {
                System.out.println("Ya existe una instancia de thread factory, no se creará otra");
            }
            return hilosFactory;
        }
    }
    public Thread newThread(Runnable r) {
        return new Thread(r);
    }
}