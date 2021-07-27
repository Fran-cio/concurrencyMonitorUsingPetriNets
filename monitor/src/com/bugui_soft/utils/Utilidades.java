package com.bugui_soft.utils;

import static com.bugui_soft.utils.Constantes.CANTIDAD_TRANSICIONES;
import static com.bugui_soft.utils.Constantes.CANTIDAD_PLAZAS;

public class Utilidades {

    //Calcula el producto punto pero solo para los valores de plazas y transiciones definidos
    public static Integer[] productoMatricial (Integer[][] matriz, Integer[] vector) {
        Integer[] marcadoResultante = new Integer[CANTIDAD_PLAZAS];
        for (int i = 0; i < CANTIDAD_PLAZAS; i++) {
            Integer aux = 0;
            for (int j = 0; j < CANTIDAD_TRANSICIONES; j++){
                aux += matriz[i][j] * vector[j];
            }
            marcadoResultante[i] = aux;
        }
        return marcadoResultante;
    }

    public static Integer[] sumarVectores(Integer[] vector1, Integer[] vector2) {
        Integer[] resultante = new Integer[CANTIDAD_PLAZAS];
        for (int i = 0; i < CANTIDAD_PLAZAS; i++) {
            resultante[i] = vector1[i] + vector2[i];
        }

        return resultante;
    }


}
