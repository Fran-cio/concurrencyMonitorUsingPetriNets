package com.bugui_soft.utils;

//import jdk.jfr.Unsigned;

import java.util.Arrays;
import static com.bugui_soft.Main.logger;
import static com.bugui_soft.utils.Constantes.*;
import static com.bugui_soft.utils.CustomLogger.setNumDisparo;
import static com.bugui_soft.utils.Utilidades.productoMatricial;
import static com.bugui_soft.utils.Utilidades.sumarVectores;

public class Rdp {
    private final Integer[][] mtxIncidencia; //matriz de incidencia
    private final Integer[] marcadoInicial; //marcado inicial
    private Integer[] marcadoActual;
    private final VectorTSensibilizadas tSensibilizadasActual;
    public static final Integer[] dispContador = new Integer[CANTIDAD_TRANSICIONES];

    public Rdp() {
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

        //array de estado de sensibilización de transiciones
        Integer[] tSensibilizadasInicial = new Integer[]{1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0};
        tSensibilizadasActual = new VectorTSensibilizadas(tSensibilizadasInicial);
        //inicializo el contador en 0
        Arrays.fill(dispContador, 0);
    }

    /**
     * Dispara la red de petri, cambiando su marcado y transiciones sensibilizadas
     * , devuelve un Boolean dependiendo de si se pudo disparar o no.
     */
    public Boolean disparar(Integer disparo) {
        if (tSensibilizadasActual.estaSensibilizado(disparo)) {
            actualizarMarcado(disparo);
            actualizarTSensibilizadas();
            dispContador[disparo]++;
            setNumDisparo(disparo);
            logger.run();
            return true;
        }
        return false;
    }

    private void actualizarMarcado(Integer disparo) {
        Integer[] vecDisparar = new Integer[CANTIDAD_TRANSICIONES];
        Arrays.fill(vecDisparar, 0);
        vecDisparar[disparo] = 1;
        try {
            for (int i:dispContador ) {System.out.print(i+" "); }
            System.out.println(Arrays.toString(marcadoActual));
            marcadoActual = sumarVectores(marcadoActual, productoMatricial(mtxIncidencia, vecDisparar));
            System.out.println(Arrays.toString(marcadoActual));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("El valor de disparo es más grande que el número de transiciones");
            e.printStackTrace();
        }
    }

    private void actualizarTSensibilizadas() {
        //creo un arreglo inicializado en 1 por defecto
        Integer[] nuevaTS = new Integer[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        //convierto en 0, los punteros a transiciones NO sensibilizadas
        for (int i = 0; i < CANTIDAD_TRANSICIONES; i++) {//busco por cada transición
            boolean aux = true;
            for (int j = 0; j < CANTIDAD_PLAZAS; j++) { //si NO esta sensibilizada por sus plazas
                //si al menos le falta un token no esta sensibilizada
                if ((mtxIncidencia[j][i] == -1) && (marcadoActual[j] < 1)) {
                    nuevaTS[i] = 0;
                    break;
                }
            }
        }
        setSensibilizadas(nuevaTS);
    }

    private void setSensibilizadas(Integer[] nuevaTS) {
        tSensibilizadasActual.setSensibilizado(nuevaTS);
    }

    public Integer[] getSensibilizadas() {
        return tSensibilizadasActual.getSensibilizada();
    }

    public Integer[] getDispContador() {
        return dispContador;
    }
}

