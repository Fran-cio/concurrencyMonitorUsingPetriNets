package com.bugui_soft.operario;

import com.bugui_soft.utils.Constantes;
import com.bugui_soft.utils.Rdp;

import java.util.concurrent.TimeUnit;

import static com.bugui_soft.Main.monitor;
import static com.bugui_soft.Main.rdp;

public class Calidad implements Runnable {

    private final Integer[] tInvariante;// transición próxima a disparar

    public Calidad() {
        //Invariante de transiciones del Descartador.(T1)
        tInvariante = new Integer[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public void run() {
        Thread.currentThread().setName("Calidad");
        while (!Rdp.isHome) {
            monitor.dispararTransicion(tInvariante);
            try {
                TimeUnit.MILLISECONDS.sleep(Constantes.SLEEP_CALIDAD_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
