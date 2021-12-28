package com.bugui_soft.utils;

import java.util.Arrays;
import java.util.concurrent.*;
import static com.bugui_soft.utils.Constantes.CANTIDAD_TRANSICIONES;

public class Monitor {
    private static final Rdp rdp = Rdp.getInstanceOfRdp();
    private final static Object lock = new Object();
    private static Monitor monitor;
    private static Politicas politica;
    private static Semaphore mutex; // "cola" de entrada al monitor
    private static Semaphore[] colasCondition; // Array con las colas de condiciones

    private Monitor() {}

    public static Monitor getInstanceOfMonitor() {
        synchronized (lock) {
            if (monitor == null) {
                monitor = new Monitor();
                politica = Politicas.getInstanceOfPoliticas();
                mutex = new Semaphore(1);
                colasCondition = new Semaphore[CANTIDAD_TRANSICIONES];
                for (int i = 0; i < CANTIDAD_TRANSICIONES; i++)
                    colasCondition[i] = new Semaphore(0);
            } else {
                System.out.println("Ya existe una instancia de monitor, no se creará otra");
            }
            return monitor;
        }
    }

    /**
     * Los hilos intentan dispararse en exclusion mutua. Cuando un hilo tiene el mutex, este va a verificar si puede disparar,
     * le va a preguntar a la politica cual disparar, y va verificar el sensibilizado para dispararlo. Si esta previo
     * a la ventana temporal, espera y libera el mutex. Si esta en la ventana, dispara, actualiza el marcado y devuelve
     * el mutex. Si estaba fuera de la ventana, se va a la cola de condicion de esa transcicion, y cuando se pueda volver
     * a disparar, se va lo va a liberar.
     *
     * @param tInvariantes transiciones que pueden hacer esos hilos en particular
     */
    public void dispararTransicion(Integer[] tInvariantes) {
        // intenta tomar el mutex si esta ocupado se va a dormir hasta que se desocupe
        try {
            mutex.acquire();//entrada al monitor
            Integer[] transPot = getTransPotencialDelOperario(tInvariantes);
            //Si este Operario tiene transiciones potenciales (Sensibilizadas por el marcado)
            if (Arrays.stream(transPot).anyMatch(n -> n != 0)) {
                Integer tDisparable = politica.cualDisparar(transPot);
                Boolean seDisparo = rdp.disparar(tDisparable);

                if (!seDisparo) {
                    try {
                        rdp.actualizarTSensibilizadas();
                        exit();
                        colasCondition[tDisparable].acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (InterruptedException e) {
            System.out.println("El hilo " + Thread.currentThread().getName() + " se interrumpió en el monitor");
        }
        exit();
    }

    /**
     * Pregunta si hay alguien en una cola de condición, en ese caso le da el mutex a él, y sino lo libera. La politica
     * de señalizado elegida es Signal And Exit
     */
    private void exit() {
        Integer[] transicionesEjecutables = getTransPotencialColas();
        boolean hayHilosEsperando = Arrays.stream(transicionesEjecutables).anyMatch(n -> n > 0);
        if (hayHilosEsperando) {
            Integer cualDisparar = politica.cualDisparar(transicionesEjecutables);
            colasCondition[cualDisparar].release();
        } else {
            if(mutex.availablePermits()!=0){
                System.out.println("El mutex ha dejado de ser binario");
                System.exit(1); //Se puede sacar: Si el semaforo deja de ser binario muere aca
            }
            mutex.release();
        }
    }

    /**
     * Devuelve los hilos que estan en las colas de condición que estan listos para ejecutarse, es decir
     * que tienen transiciones sensibilizadas
     */

    private Integer[] getTransPotencialColas() {
        Integer[] aux = new Integer[CANTIDAD_TRANSICIONES];
        for (int i = 0; i < CANTIDAD_TRANSICIONES; i++) {
            //si hay hilos esperando y transiciones sensibilizadas
            if ((colasCondition[i].getQueueLength() != 0) && (rdp.getSensibilizadas()[i] == 1))
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
            /* llama a un wrapper para obtenner la transiciones sensibilizadas, y hace la multiplicación para saber si
            * se corresponde con una transición del hilo, si esto es así va a ser una transición sensibilizada,
            * sino no*/
            aux[i] = rdp.getSensibilizadas()[i] * tInvariantes[i];
        }
        return aux;
    }

    public Politicas getPolitica() {
        return politica;
    }

    public static Semaphore getMutex() {
        return mutex;
    }

    public void printMarcado() {
        rdp.printMarcadoActual();
    }
}
