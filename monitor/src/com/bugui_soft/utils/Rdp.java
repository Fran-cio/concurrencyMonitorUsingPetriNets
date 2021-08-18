package com.bugui_soft.utils;


import java.util.Arrays;
import java.util.HashMap;

import static com.bugui_soft.Main.exchanger;
import static com.bugui_soft.Main.monitor;
import static com.bugui_soft.utils.Constantes.CANTIDAD_PLAZAS;
import static com.bugui_soft.utils.Constantes.CANTIDAD_TRANSICIONES;
import static com.bugui_soft.utils.Utilidades.*;

public class Rdp {

    private final Integer[][] mtxIncidencia; //matriz de incidencia
    private final Integer[] marcadoInicial; //marcado inicial
    private static Integer[] marcadoActual;
    private static Long[] timeStamp;//X0, tiempo de sensibilizado inicial
    private final VectorTSensibilizadas tSensibilizadasActual;

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
        marcadoActual = marcadoInicial.clone();

        //array de estado de sensibilización de transiciones
        Integer[] tSensibilizadasInicial = genTSensibilizadas();
        tSensibilizadasActual = new VectorTSensibilizadas(tSensibilizadasInicial);
        timeStamp = new Long[CANTIDAD_TRANSICIONES];
        for (int i = 0; i < CANTIDAD_TRANSICIONES; i++) {
            timeStamp[i] = System.currentTimeMillis();
        }
    }

    /**
     * Dispara la red de petri, cambiando su marcado y transiciones sensibilizadas
     * , devuelve un Boolean dependiendo de si se pudo disparar o no.
     */
    public Boolean disparar(Integer disparo) {
        if (tSensibilizadasActual.estaSensibilizado(disparo)) {
            actualizarMarcado(disparo);
            actualizarTSensibilizadas();
            monitor.getPolitica().incrementarTI(disparo);
            try {
                exchanger.exchange(disparo);
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void actualizarMarcado(Integer disparo) {
        Integer[] vecDisparar = new Integer[CANTIDAD_TRANSICIONES];
        Arrays.fill(vecDisparar, 0);
        vecDisparar[disparo] = 1;
        try {
            marcadoActual = sumarVectores(marcadoActual, productoMatricial(mtxIncidencia, vecDisparar));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("El valor de disparo es más grande que el número de transiciones");
            e.printStackTrace();
        }
    }

    public void actualizarTSensibilizadas() {
        Integer[] nuevoTS= genTSensibilizadas();

        /**TODO: borrar comentario
         *estabamos calculando el timestamp con los 2 arreglos iguales, asi que les cambie el orden. ahora esto anda bien.
        **/

        //cuando hay un cambio en las sensibilizadas, actualizamos el tiempo inicial de sensibilizado
        setTimeStamp(nuevoTS);
        setSensibilizadas(nuevoTS);

    }

    private Integer[] genTSensibilizadas() {
        //creo un arreglo inicializado en 1 por defecto
        Integer[] nuevaTS = new Integer[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        //convierto en 0, los punteros a transiciones NO sensibilizadas
        for (int i = 0; i < CANTIDAD_TRANSICIONES; i++) {//busco por cada transición
            for (int j = 0; j < CANTIDAD_PLAZAS; j++) { //si NO esta sensibilizada por sus plazas
                //si al menos le falta un token no esta sensibilizada
                if ((mtxIncidencia[j][i] == -1) && (marcadoActual[j] < 1)) {
                    nuevaTS[i] = 0;
                    break;
                }
            }
        }

        return nuevaTS;
    }

    private void setSensibilizadas(Integer[] nuevaTS) {
        tSensibilizadasActual.setSensibilizado(nuevaTS);
    }

    public Integer[] getSensibilizadas() {
        return tSensibilizadasActual.getSensibilizada();
    }

    public Integer[] getMarcadoInicial() {
        return marcadoInicial;
    }

    public Integer[] getMarcadoActual() {
        return marcadoActual;
    }

    /**
     * Actualizar el tiempo de sensibilizado inicial
     **/
    public void setTimeStamp(Integer[] nuevaTS) {
        for (int i = 0; i < CANTIDAD_TRANSICIONES; i++) {
            if (!nuevaTS[i].equals(tSensibilizadasActual.getSensibilizada()[i])) {
                timeStamp[i] = System.currentTimeMillis();
            }
        }
    }

    public static Long[] getTimeStamp() {
        return timeStamp;
    }
}

