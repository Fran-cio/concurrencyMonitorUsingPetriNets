package com.bugui_soft.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static com.bugui_soft.utils.Constantes.CANTIDAD_TRANSICIONES;
import static com.bugui_soft.utils.Constantes.CANTIDAD_PLAZAS;

public class Utilidades {

    private Utilidades() {}

    //Calcula el producto punto pero solo para los valores de plazas y transiciones definidos
    @Contract(pure = true)
    public static Integer @NotNull [] productoMatricial(Integer[][] matriz, Integer[] vector) {
        Integer[] productoMatricial = new Integer[CANTIDAD_PLAZAS];
        //plazas filas de la matriz, transiciones columnas
        for (int i = 0; i < CANTIDAD_PLAZAS; i++) {
            int aux = 0;
            for (int j = 0; j < CANTIDAD_TRANSICIONES; j++) {
                aux += matriz[i][j] * vector[j];
            }
            productoMatricial[i] = aux;
        }
        return productoMatricial;
    }

    /**
     * Es la suma vectorial estandar
     */
    @Contract(pure = true)
    public static Integer @NotNull [] sumarVectores(Integer[] vector1, Integer[] vector2) {
        Integer[] resultante = new Integer[CANTIDAD_PLAZAS];
        for (int i = 0; i < CANTIDAD_PLAZAS; i++) {
            resultante[i] = vector1[i] + vector2[i];
        }

        return resultante;
    }

}
