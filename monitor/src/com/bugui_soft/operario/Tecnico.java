/*
 * Tecnico
 *
 * Version 1.0
 *
 * Copyright BeerWare
 */

package com.bugui_soft.operario;

import com.bugui_soft.utils.Constantes;
import com.bugui_soft.utils.Rdp;

import java.util.concurrent.TimeUnit;

import static com.bugui_soft.Main.finDePrograma;
import static com.bugui_soft.Main.monitor;

public class Tecnico implements Runnable {
    private final Integer[] tInvariante;

    public Tecnico() {
        //transiciones de T8 a T11
        tInvariante = new Integer[]{0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1};
    }

    public void run() {
        Thread.currentThread().setName("Tecnico");
        while (!finDePrograma) {
            monitor.dispararTransicion(tInvariante);
            try {
                TimeUnit.MILLISECONDS.sleep(Constantes.SLEEP_TECNICO_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
