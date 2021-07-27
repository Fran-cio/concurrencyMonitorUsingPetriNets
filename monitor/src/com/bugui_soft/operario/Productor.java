package com.bugui_soft.operario;

import com.bugui_soft.Main;

public class Productor implements Runnable {

    private Integer[] tInvariante;
    private Integer tActual;// transición próxima a disparar

    public  Productor() {
        tActual=4;
        //Invariante de transiciones del Productor.
        tInvariante = new Integer[4];
        for (int i = 0; i < tInvariante.length; i++) {
            tInvariante[i] = 4 + i; //transiciones de T4 a T7
        }
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
