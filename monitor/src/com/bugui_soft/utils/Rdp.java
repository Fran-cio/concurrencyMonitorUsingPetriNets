package com.bugui_soft.utils;


import java.util.Arrays;
import java.util.HashMap;

import static com.bugui_soft.Main.exchanger;
import static com.bugui_soft.Main.monitor;
import static com.bugui_soft.utils.Constantes.CANTIDAD_PLAZAS;
import static com.bugui_soft.utils.Constantes.CANTIDAD_TRANSICIONES;
import static com.bugui_soft.utils.Utilidades.*;

public class Rdp {

    private final HashMap<Integer, Integer[]> arcosEntrantes;
    private final HashMap<Integer, Integer[]> arcosSalientes;
    private final Integer[][] mtxEntrantes; //matriz de incidencia
    private final Integer[][] mtxSalientes; //matriz de incidencia
    private final Integer[][] mtxIncidencia; //matriz de incidencia
    private final Integer[] marcadoInicial; //marcado inicial
    private static Integer[] marcadoActual;
    private static Long[] timeStamp;//X0, tiempo de sensibilizado inicial
    private final VectorTSensibilizadas tSensibilizadasActual;

    public Rdp() {
        arcosEntrantes = new HashMap<>();
        arcosSalientes = new HashMap<>();

        genArcos();

        mtxEntrantes = new Integer[arcosEntrantes.size()][arcosSalientes.size()];
        mtxSalientes = new Integer[arcosEntrantes.size()][arcosSalientes.size()];

        genMtxEntrada();
        genMtxSalida();

        mtxIncidencia = restarMatrices(mtxSalientes, mtxEntrantes);
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

    private void genMtxEntrada() {
        for (int i = 0; i < arcosEntrantes.size(); i++) {
            for (int j = 0; j < arcosSalientes.size(); j++) {
                mtxEntrantes[i][j] = 0;
            }
        }
        for (int i = 0; i < arcosEntrantes.size(); i++) {
            for (int j = 0; j < arcosEntrantes.get(i).length; j++) {
                mtxEntrantes[i][arcosEntrantes.get(i)[j]] = 1;
            }
        }
    }

    private void genMtxSalida() {
        for (int i = 0; i < arcosEntrantes.size(); i++) {
            for (int j = 0; j < arcosSalientes.size(); j++) {
                mtxSalientes[i][j] = 0;
            }
        }

        for (int i = 0; i < arcosSalientes.size(); i++) {
            for (int j = 0; j < arcosSalientes.get(i).length; j++) {
                mtxSalientes[arcosSalientes.get(i)[j]][i] = 1;
            }
        }
    }

    private void genArcos() {
        //plazas -> transiciones
        arcosEntrantes.put(0, new Integer[]{0});
        arcosEntrantes.put(1, new Integer[]{1, 2});
        arcosEntrantes.put(2, new Integer[]{0});
        arcosEntrantes.put(3, new Integer[]{1});
        arcosEntrantes.put(4, new Integer[]{10});
        arcosEntrantes.put(5, new Integer[]{3});
        arcosEntrantes.put(6, new Integer[]{2, 8});
        arcosEntrantes.put(7, new Integer[]{9});
        arcosEntrantes.put(8, new Integer[]{4});
        arcosEntrantes.put(9, new Integer[]{3, 7});
        arcosEntrantes.put(10, new Integer[]{8});
        arcosEntrantes.put(11, new Integer[]{6});
        arcosEntrantes.put(12, new Integer[]{5});
        arcosEntrantes.put(13, new Integer[]{4, 6});
        arcosEntrantes.put(14, new Integer[]{7});
        arcosEntrantes.put(15, new Integer[]{3, 6});
        arcosEntrantes.put(16, new Integer[]{2, 7});
        arcosEntrantes.put(17, new Integer[]{2, 6});

        //transiciones -> plazas
        arcosSalientes.put(0, new Integer[]{1});
        arcosSalientes.put(1, new Integer[]{2, 4});
        arcosSalientes.put(2, new Integer[]{5, 2});
        arcosSalientes.put(3, new Integer[]{8, 6, 16, 17});
        arcosSalientes.put(4, new Integer[]{9, 12, 15});
        arcosSalientes.put(5, new Integer[]{0, 13});
        arcosSalientes.put(6, new Integer[]{14});
        arcosSalientes.put(7, new Integer[]{10, 13, 15, 17});
        arcosSalientes.put(8, new Integer[]{7, 9, 16});
        arcosSalientes.put(9, new Integer[]{6, 11});
        arcosSalientes.put(10, new Integer[]{0, 3});
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

    private void actualizarTSensibilizadas() {
        Integer[] nuevoTS= genTSensibilizadas();
        setSensibilizadas(nuevoTS);
        //cuando hay un cambio en las sensibilizadas, actualizamos el tiempo inicial de sensibilizado
        setTimeStamp(nuevoTS);
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

