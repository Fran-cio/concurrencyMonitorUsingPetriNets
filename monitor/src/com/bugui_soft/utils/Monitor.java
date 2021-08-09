package com.bugui_soft.utils;

import java.util.Arrays;
import java.util.concurrent.*;
import static com.bugui_soft.Main.rdp;
import static com.bugui_soft.utils.Constantes.CANTIDAD_TRANSICIONES;

public class Monitor {
    private final Politicas politica;
    private static final Semaphore mutex = new Semaphore(1); // "cola" de entrada al monitor
    private final Semaphore[] colasCondition; // Array con las colas de condiciones

    public Monitor() {
        politica = new Politicas();
        //mutex = new Semaphore(1);
        colasCondition = new Semaphore[CANTIDAD_TRANSICIONES];
        for (int i = 0; i < CANTIDAD_TRANSICIONES; i++)
            colasCondition[i] = new Semaphore(0);
    }

    public void dispararTransicion(Integer[] tInvariantes) {
        // intenta tomar el mutex si esta ocupado se va a dormir hasta que se desocupe
        try {
            mutex.acquire();//entrada al monitor
            Integer[] transPot = getTransPotencialDelOperario(tInvariantes);

            //Si este Operario  tiene transiciones potenciales (Sensibilizadas por el marcado
            if (Arrays.stream(transPot).anyMatch(n -> n != 0)) {

                Integer tDisparable = politica.cualDisparar(transPot);
                Boolean seDisparo = rdp.disparar(tDisparable);

                if (!seDisparo) {
                    /**TODO: Borrar comentario
                     * Update: Se soluciono sacando el Synchronized en la entrada del metodo.
                     *
                       *Esta parte del código anda mal.
                     * 1.Considero necesario agregar un actualizarTSensibilizadas() aca adentro para evitar que se acumulen
                     *  los tiempos
                     * 2.Al ejecutar el acquire de la cola de condicion el programa muere por completo. No se si hay
                     *  algo mal con los semáforos o que dentro de exit() nunca sucede el release como deberia
                     * 3.El método getPotencialColas nunca he visto que devuelva algo distinto a todos 0, lo cual me
                     *  hace que nunca suceda el release. No se si el error esta ahi, ya que el programa no me permite
                     *  ver mas alla del acquire del semaforo de condiciones
                     *
                     * Situaciones que probe:
                     * -Sacando el acquire() y el exit() de las lineas 48,49. Se produce un deadlock
                     *  porque los timestamp no se actualizan y se queda sin avanzar
                     * -Poniendo ademas de lo anterior, el metodo actualizarTSensibilizadas() (Poniendolo publico) se solu
                     *  ciona el problema y el programa se ejecuta bien, sigue sucediendo que varias veces el beta se supera,
                     *  pero el programa sigue su curso.
                     *
                     *  El beta esta bajo lo cual lleva a que se de esta situacion, podemos dejarlo alto y el programa se
                     *  ejecuta bien. De todas formas esto no quita una parte del codigo falla, es decir, que las colasCondicion
                     *  no andan, asi que hay que revisar que sucede o acomodar el proyecto para que no esten (aunque me parece
                     *  importante que esten porque hace a la escencia del monitor en primera instacia ademas de que no tiene
                     *  porque no andar)
                     *
                     *  Update: Se soluciono sacando el Synchronized en la entrada del metodo.
                     **/
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
     * Pregunta si hay alguien en una cola de condición, en ese caso le da el mutex a él, y sino lo libera
     */
    private void exit() {
        Integer[] transicionesEjecutables = getTransPotencialColas();
        boolean hayHilosEsperando = Arrays.stream(transicionesEjecutables).anyMatch(n -> n > 0);
        if (hayHilosEsperando) {
            Integer cualDisparar = politica.cualDisparar(transicionesEjecutables);

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
            //si hay  transiciones sensibilizadas de ese operario
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
}
