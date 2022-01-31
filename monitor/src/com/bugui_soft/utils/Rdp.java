/*
 * Rdp
 *
 * Version 1.0
 *
 * Copyright BeerWare
 */

package com.bugui_soft.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static com.bugui_soft.Main.*;
import static com.bugui_soft.utils.Constantes.*;
import static com.bugui_soft.utils.Utilidades.*;

public class Rdp {

    private static final Object lock = new Object();
    private static Rdp rdp;
    private static Integer[][] mtxIncidencia; //matriz de incidencia
    private static Integer[] marcadoActual;
    private static Long[] timeStamp;//X0, tiempo de sensibilizado inicial
    private static VectorTSensibilizadas transicionesSensibilizadasActual;
    public static boolean esInvarianteMil;

    private Rdp() {
    }

    public static Rdp getInstanceOfRdp() {
        synchronized (lock) {
            if (rdp == null) {
                rdp = new Rdp();
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

                //marcado inicial
                marcadoActual = MARCADO_INICIAL.clone();

                //array de estado de sensibilización de transiciones
                Integer[] transicionesSensibilizadasInicial = generarTransicionesSensibilizadas();
                transicionesSensibilizadasActual =
                        VectorTSensibilizadas.getInstanceOfVectorTSensibilizadas(transicionesSensibilizadasInicial);
                timeStamp = new Long[CANTIDAD_TRANSICIONES];
                for (int i = 0; i < CANTIDAD_TRANSICIONES; i++) {
                    timeStamp[i] = System.currentTimeMillis();
                }
            } else {
                System.out.println("Ya existe una instancia de Rdp, no se creará otra");
            }
            return rdp;
        }
    }

    /**
     * Dispara la red de petri, cambiando su marcado y transiciones sensibilizadas
     *
     * @return False: no disparo, True: disparó
     */

    public Boolean disparar(Integer disparo) {
        if (transicionesSensibilizadasActual.estaSensibilizado(disparo)) {
            try {
                exchangerLogger.exchange(disparo);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
            actualizarMarcado(disparo);
            esInvarianteMil = monitor.getPolitica().esInvarianteMil();
            actualizarTSensibilizadas();
            monitor.getPolitica().incrementarInvariante(disparo);
            return true;
        }
        actualizarTSensibilizadas();
        return false;
    }

    private void actualizarMarcado(@NotNull Integer disparo) {
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
        Integer[] nuevoTS = generarTransicionesSensibilizadas();
        //cuando hay un cambio en las sensibilizadas, actualizamos el tiempo inicial de sensibilizado
        setTimeStamp(nuevoTS);
        setSensibilizadas(nuevoTS);

        /*
            Sabiendo el ultimo marcado y completando la red con las trans que sobran en las ejecuciones podemos corroborar
            si se ha violado o no el invariante de transicion, comparando el marcado obtenido en el programa y el alcanzado
            en el PIPE. (El formato prensentado en la impresion es la misma que se va a ver en el PIPE)
         */
        if (!cumpleInvariantesP()) { //Un chequeo mas para verificar la sanidad de la concurrencia
            System.out.println("Se ha violado los invariantes de plaza");
            System.exit(1);
        }
    }

    @Contract(pure = true)
    private static Integer @NotNull [] generarTransicionesSensibilizadas() {
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

    private static boolean cumpleInvariantesP() {
        boolean Ip1, Ip2, Ip3, Ip4, Ip5, Ip6, Ip7, Ip8, Ip9, Ip10;
        Ip1 = (marcadoActual[16] + marcadoActual[10] + marcadoActual[5] == 2);
        Ip2 = (marcadoActual[17] + marcadoActual[14] + marcadoActual[5] == 3);
        Ip3 = (marcadoActual[15] + marcadoActual[14] + marcadoActual[8] == 2);
        Ip4 = (marcadoActual[0] + marcadoActual[1] + marcadoActual[12] + marcadoActual[4] + marcadoActual[5] + marcadoActual[8] == 3);
        Ip5 = (marcadoActual[10] + marcadoActual[11] + marcadoActual[14] + marcadoActual[7] == 3);
        Ip6 = (marcadoActual[12] + marcadoActual[13] + marcadoActual[14] == 2);
        Ip7 = (marcadoActual[1] + marcadoActual[2] == 1);
        Ip8 = (marcadoActual[3] + marcadoActual[4] == 1);
        Ip9 = (marcadoActual[5] + marcadoActual[6] + marcadoActual[7] == 2);
        Ip10 = (marcadoActual[10] + marcadoActual[8] + marcadoActual[9] == 1);

        return (Ip1 && Ip2 && Ip3 && Ip4 && Ip5 && Ip6 && Ip7 && Ip8 && Ip9 && Ip10);
    }

    private void setSensibilizadas(Integer[] nuevaTS) {
        transicionesSensibilizadasActual.setSensibilizado(nuevaTS);
    }

    public Integer[] getSensibilizadas() {
        return transicionesSensibilizadasActual.getSensibilizada();
    }

    /**
     * Actualizar el tiempo de sensibilizado inicial
     **/
    public void setTimeStamp(Integer[] nuevaTS) {
        for (int i = 0; i < CANTIDAD_TRANSICIONES; i++) {
            if (!nuevaTS[i].equals(transicionesSensibilizadasActual.getSensibilizada()[i])) {
                timeStamp[i] = System.currentTimeMillis();
            }
        }
    }

    public static Long[] getTimeStamp() {
        return timeStamp;
    }

    public String printMarcadoActual() {
        StringBuilder texto = new StringBuilder();
        texto.append("[");
        int i;
        for (i = 0; i < (CANTIDAD_PLAZAS - 1); i++) {
            texto.append(marcadoActual[i]).append(",");
        }
        texto.append(marcadoActual[i]).append("]\n");
        return texto.toString();
    }
}

