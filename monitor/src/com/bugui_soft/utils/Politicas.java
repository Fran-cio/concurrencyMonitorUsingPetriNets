package com.bugui_soft.utils;

import static com.bugui_soft.utils.Constantes.CANTIDAD_TRANSICIONES;
import static com.bugui_soft.Main.rdp;

public class Politicas {
    /**
     * Devuelve la transición menos disparada
     */
    public Integer cualDisparar(Integer[] transPot) {
        return obtenerMenor(transPot);

    }

    private Integer obtenerMenor(Integer[] transPot) {
        Integer minimo = Integer.MAX_VALUE;
        int posMin = 0;
        if(transPot[0] != 0){return 0;}
        for (int i = 0; i < CANTIDAD_TRANSICIONES; i++) {System.out.println(rdp.getDispContador()[i]);
            if ((rdp.getDispContador()[i] < minimo) && (transPot[i] != 0)) {
                minimo = rdp.getDispContador()[i];
                posMin = i;
            }
        }


        return posMin;
    }
}
