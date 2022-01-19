/*
 * Descartador
 *
 * Version 1.0
 *
 * Copyright BeerWare
 */

package com.bugui_soft.operario;

import com.bugui_soft.utils.Constantes;


public class Descartador extends Operario implements Runnable {

    public Descartador() {
        super();
        //Invariante de transiciones del Descartador.(T1 y T10)
        Integer[] tInvariante = new Integer[]{1, 10};
        setTInvariante(tInvariante);
    }

    public void run() {
        Thread.currentThread().setName("Descartador");
        ATrabajar(Constantes.SLEEP_DESCARTADOR_MS);
    }
}
