package com.bugui_soft.operario;

import static com.bugui_soft.Main.*;

public class Descartador implements Runnable {

    private Integer[] tInvariante;
    private int tActual;// transición próxima a disparar

    public Descartador() {
        tActual = 2;
        //Invariante de transiciones del Productor.
        tInvariante = new Integer[2];
        for (int i = 0; i < tInvariante.length; i++) {
            tInvariante[i] = 2 + i; //transiciones de T2 a T3
        }
    }

    public void run() {
        while (true) {
            monitor.dispararTransicion();
            rdp.disparar(tInvariante[tActual]);
            tActual++;
            // verifico si estoy en la ultima transición
            if (tActual > tInvariante[tInvariante.length - 1]) {
                tActual = tInvariante[0];
            }

        }

    }

}
