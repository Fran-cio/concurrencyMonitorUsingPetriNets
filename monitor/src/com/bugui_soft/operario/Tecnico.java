/*
 * Tecnico
 *
 * Version 1.0
 *
 * Copyright BeerWare
 */

package com.bugui_soft.operario;

import com.bugui_soft.utils.Constantes;


public class Tecnico extends Operario implements Runnable {

    public Tecnico() {
        //transiciones de T6 a T9
        super();
        Integer[] tInvariante = new Integer[]{6, 7, 8, 9};
        setTInvariante(tInvariante);
    }

    public void run() {
        Thread.currentThread().setName(Constantes.TECNICO);
        aTrabajar(Constantes.SLEEP_TECNICO_MS);
    }

}
