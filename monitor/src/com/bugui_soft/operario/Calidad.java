package com.bugui_soft.operario;

import com.bugui_soft.Main;

public class Calidad implements Runnable {

   private Integer tInvariante;// transición próxima a disparar

    public Calidad() {
        tInvariante=1; //dispara solo T1
    }

    public void run() {
        while (true) {
            Main.monitor.dispararTransicion();
            Main.rdp.disparar(tInvariante);


        }

    }

}
