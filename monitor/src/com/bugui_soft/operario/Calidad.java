/*
 * Calidad
 *
 * Version 1.0
 *
 * Copyright BeerWare
 */

package com.bugui_soft.operario;

import com.bugui_soft.utils.Constantes;

public class Calidad extends Operario implements Runnable {

    public Calidad() {
        //Invariante de transiciones del Descartador.(T1)
        Integer[] tInvariante = new Integer[]{0};
        setTInvariante(tInvariante);
    }

    public void run() {
        Thread.currentThread().setName(Constantes.CALIDAD);
        aTrabajar(Constantes.SLEEP_CALIDAD_MS);
    }

}
