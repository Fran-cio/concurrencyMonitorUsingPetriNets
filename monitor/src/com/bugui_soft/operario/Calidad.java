package com.bugui_soft.operario;

import com.bugui_soft.utils.Constantes;

import java.util.concurrent.TimeUnit;

import static com.bugui_soft.Main.monitor;

public class Calidad implements Runnable {

    private final Integer[] tInvariante;// transición próxima a disparar

    public Calidad() {
        //Invariante de transiciones del Descartador.(T1)
        tInvariante = new Integer[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public void run() {
        while (true) {
            monitor.dispararTransicion(tInvariante);
            try {
                TimeUnit.MILLISECONDS.sleep(Constantes.SLEEP_CALIDAD_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
