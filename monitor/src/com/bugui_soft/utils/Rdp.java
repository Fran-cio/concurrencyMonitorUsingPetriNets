package com.bugui_soft.utils;

import jdk.jfr.Unsigned;

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
    private final Integer[] dispContador;

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

        dispContador = new Integer[CANTIDAD_TRANSICIONES];
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
            System.out.println(Arrays.toString(marcadoActual));
            marcadoActual = sumarVectores(marcadoActual, productoMatricial(mtxIncidencia, vecDisparar));
            System.out.println(Arrays.toString(marcadoActual));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("El valor de disparo es más grande que el número de transiciones");
            e.printStackTrace();
        }
    }

    private void actualizarTSensibilizadas() {
        //creo un arreglo inicializado en 0 por defecto
        int puntero = 0;
        Integer[] nuevaTS = new Integer[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        //convierto en 1, los punteros a transiciones sensibilizadas
        for (int i = 0; i < CANTIDAD_TRANSICIONES; i++) {//busco por cada transición
            boolean aux = true;
            for (int j = 0; j < CANTIDAD_PLAZAS; j++) { //si esta sencibilizada por sus plazas
                //si al menos le falta un token no esta sensibilizada
                if (mtxIncidencia[j][i] == -1){
                    if(marcadoActual[j] < 1){
                        nuevaTS[i] = 0;
                    }
                }
//                if (marcadoActual[j] >= 1 && mtxIncidencia[j][i] == -1) {
//                    aux = false;
//                    break;
//                }
            }
//            if (aux) {
//                nuevaTS[puntero] = 1; //Tsensibilizada
//                puntero++;
//            }
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

