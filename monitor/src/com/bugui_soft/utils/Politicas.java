package com.bugui_soft.utils;

import static com.bugui_soft.Main.rdp;

public class Politicas {
    /**
     * Devuelve la transición menos disparada
     */
    public Integer cualDisparar(Integer[] transPot) {
        Integer posMin = 0;
        try {
            posMin = obtenerMenor(transPot);
            rdp.getDispContador()[posMin]++;
        } catch (IllegalStateException e) {
            System.out.println("Se produjo un deadlock, revisar red de petri");
            e.printStackTrace();
        }
        return posMin;
    }

    private Integer obtenerMenor(Integer[] transPot) throws IllegalStateException {
        Integer minimo = Integer.MAX_VALUE;
        int posMin = 0;
        for (int i = 0; i < transPot.length; i++) {
            if ((rdp.getDispContador()[i] < minimo) && (transPot[i] != 0)) {
                minimo = rdp.getDispContador()[i];
                posMin = i;
            }
        }
        if (minimo == Integer.MAX_VALUE) {
            throw new IllegalStateException();
        }
        return posMin;
    }
}
