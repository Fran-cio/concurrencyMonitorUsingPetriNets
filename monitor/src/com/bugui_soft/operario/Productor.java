package com.bugui_soft.operario;

import java.util.concurrent.TimeUnit;

import static com.bugui_soft.Main.monitor;

public class Productor implements Runnable {

    private final Integer[] tInvariante;

    public Productor() {
        //Invariante de transiciones del Productor.(T4 a T7)
        tInvariante = new Integer[]{0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0};
    }

    public void run() {
        while (true) {
            Main.monitor.dispararTransicion();
            Main.rdp.disparar(tInvariante[tActual]);
            tActual++;
            // verifico si estoy en la ultima transición
            if(tActual > tInvariante[tInvariante.length-1])  {
                tActual=tInvariante[0];
            }

        }

    }

}
