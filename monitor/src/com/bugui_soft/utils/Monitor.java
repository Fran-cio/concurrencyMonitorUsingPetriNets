package com.bugui_soft.utils;

import java.util.Arrays;
import java.util.concurrent.*;

import static com.bugui_soft.utils.Constantes.CANTIDAD_TRANSICIONES;

public class Monitor {
    private final Politicas politica;
    private final Semaphore mutex; // "cola" de entrada al monitor
    private final Semaphore[] colasCondition; // Array con las colas de condiciones
    private final Rdp redDePetri;

    public Monitor() {
        politica = new Politicas();
        mutex = new Semaphore(1);
        colasCondition = new Semaphore[CANTIDAD_TRANSICIONES];
        for (int i = 0; i < CANTIDAD_TRANSICIONES; i++)
            colasCondition[i] = new Semaphore(0);

        redDePetri = new Rdp();
    }

    public synchronized void dispararTransicion(Integer[] tInvariantes) {
        // intenta tomar el mutex si esta ocupado se va a dormir hasta que se desocupe
        try {
            mutex.acquire();//entrada al monitor

            while (true) {
                Integer tDisparable = politica.cualDisparar(getTransPotencialDelOperario(tInvariantes));
                Boolean seDisparo = redDePetri.disparar(tDisparable);

                if (!seDisparo) {
                    try {
                        exit();
                        colasCondition[tDisparable].acquire();

                        //Cuando me despierten intentaré disparar de nuevo!
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {//si disparé
                    exit();
                    break;//me voy del monitor
                }
            }
        } catch (InterruptedException e) {
            System.out.println("El hilo " + Thread.currentThread().getName() + " se interrumpió en el monitor");
        }
    }

    /**
     * Pregunta si hay alguien en una cola de condición, en ese caso le da el mutex a él, y sino lo libera
     */
    private void exit() {
        Integer[] transicionesEjecutables = getTransPotencialColas();
        boolean hayColasEsperando = Arrays.stream(transicionesEjecutables).anyMatch(n -> n > 0);
        if (hayColasEsperando) {
            Integer cualDisparar = politica.cualDisparar(transicionesEjecutables);
            //Le cedo el monitor
            colasCondition[cualDisparar].release();
        } else {
            mutex.release();
        }
    }

    /**
     * Devuelve los hilos que estan en las colas de condición que estan listos para ejecutarse, esdecir
     * que tienen transiciones sensibilizadas
     */

    private Integer[] getTransPotencialColas() {
        Integer[] aux = new Integer[CANTIDAD_TRANSICIONES];
        for (int i = 0; i < CANTIDAD_TRANSICIONES; i++) {
            //si hay hilos esperando y transiciones sensibilizadas
            if ((colasCondition[i].getQueueLength() != 0) && (redDePetri.getSensibilizadas()[i] == 1))
                aux[i] = 1;
            else
                aux[i] = 0;
        }
        return aux;
    }

    /**
     * Devuelve un Array de las transiciones sensibilizadas del invariante de transiciones del hilo operario
     */
    private Integer[] getTransPotencialDelOperario(Integer[] tInvariantes) {

        Integer[] aux = new Integer[CANTIDAD_TRANSICIONES];
        for (int i = 0; i < CANTIDAD_TRANSICIONES; i++) {
            //si hay  transiciones sensibilizadas de ese operario
            if ((redDePetri.getSensibilizadas()[i] == 1) && (tInvariantes[i] == 1))
                aux[i] = 1;
            else
                aux[i] = 0;
        }
        return aux;
    }
}
