package com.bugui_soft.utils;

import static com.bugui_soft.utils.Constantes.CANTIDAD_TRANSICIONES;
import static com.bugui_soft.utils.Constantes.CANTIDAD_PLAZAS;
import static com.bugui_soft.utils.Utilidades.productoMatricial;
import static com.bugui_soft.utils.Utilidades.sumarVectores;

public class Rdp {
    private Integer nTrans;//numero de transiciones
    private Integer nPlz; //numero de lugares/plazas
    private Integer[][] mtxIncidencia; //matriz de incidencia
    private final Integer[] marcadoInicial; //marcado inicial
    private Integer[] marcadoActual;
    private Integer[] tSensibilizadas; //array de estado de sensibilización de transiciones

    public Rdp() {

        nTrans = CANTIDAD_TRANSICIONES;
        nPlz = CANTIDAD_PLAZAS;

        mtxIncidencia = new Integer[][]{
                {-1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
                {1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0},
                {-1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, -1},
                {0, 0, 1, -1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, -1, 1, 0, 0, 0, 0, -1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 0},
                {0, 0, 0, 1, -1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, -1, 1, 0, 0, -1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, -1, 0, 0},
                {0, 0, 0, 0, 0, 0, -1, 0, 0, 1, 0},
                {0, 0, 0, 0, 1, -1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, -1, 1, -1, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, -1, 0, 0, 0},
                {0, 0, 0, -1, 1, 0, -1, 1, 0, 0, 0},
                {0, 0, -1, 1, 0, 0, 0, -1, 1, 0, 0},
                {0, 0, -1, 1, 0, 0, -1, 1, 0, 0, 0}};


        marcadoInicial = new Integer[]{3, 0, 1, 1, 0, 0, 2, 0, 0, 1, 0, 3, 0, 2, 0, 2, 2, 3};
        marcadoActual = marcadoInicial;

        tSensibilizadas = new Integer[]{1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0};
    }

    public synchronized void disparar(Integer disparo) {
        Integer[] vecDisparar = new Integer[CANTIDAD_TRANSICIONES];
        vecDisparar[disparo] = 1;
        try {
            marcadoActual = sumarVectores(marcadoInicial, productoMatricial(mtxIncidencia, vecDisparar));
            //TODO: hacer una función que re-calcule las sensibilizadas
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public Integer[] getSensibilizada() {
        return tSensibilizadas;
    }

    public Integer[] getMarcadoActual() {
        return marcadoActual;
    }
}

