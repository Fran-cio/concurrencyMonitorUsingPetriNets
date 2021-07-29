package com.bugui_soft.operario;

import java.util.concurrent.TimeUnit;

import static com.bugui_soft.Main.monitor;

public class Tecnico implements Runnable {
    private final Integer[] tInvariante;
    private Integer tActual; //proxima transicion a disparar

    public Tecnico() {
        //transiciones de T8 a T11
        tInvariante = new Integer[]{0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1};

    }

    public void run() {
        while (true) {
            monitor.dispararTransicion(tInvariante);
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
