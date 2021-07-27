package com.bugui_soft.operario;

import java.util.concurrent.TimeUnit;

import static com.bugui_soft.Main.monitor;

public class Tecnico implements Runnable {
    private final Integer[] tInvariante;
    private Integer tActual; //proxima transicion a disparar

    public  Tecnico() {
        tActual= 8;
        //Invariante de transiciones del Productor.
        tInvariante = new Integer[4];
        for (int i = 0; i < tInvariante.length; i++) {
            tInvariante[i] = 8 + i; //transiciones de T8 a T11
        }
    }

    public void run() {
        while (true) {
            Main.monitor.dispararTransicion();
            Main.rdp.disparar(tInvariante[tActual]);
            tActual++;
            // verifico si estoy en la ultima transición
            if (tActual > tInvariante[tInvariante.length - 1]) {
                tActual = tInvariante[0];
            }
        }
    }

}
