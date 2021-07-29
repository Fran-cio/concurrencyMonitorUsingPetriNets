package com.bugui_soft.utils;

import static com.bugui_soft.Main.rdp;

public class Politicas {
    /**
     * Devuelve la transición menos disparada
     */
    public Integer cualDisparar(Integer[] transPot) {
        int posMin = 0;
        posMin = obtenerMenor(transPot);
        return posMin;
    }

    private Integer obtenerMenor(Integer[] transPot) {
        Integer minimo = Integer.MAX_VALUE;
        int posMin = 0;
        for (int i = 0; i < transPot.length; i++) {
            if ((rdp.getDispContador()[i] < minimo) && (transPot[i] != 0)) {
                minimo = rdp.getDispContador()[i];
                posMin = i;
            }
        }

        return posMin;
    }
}
