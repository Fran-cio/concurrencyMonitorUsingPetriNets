package com.bugui_soft.utils;
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
     * @param tDisparable trans a disparar
     * @return TRUE si se puedo disparar o FALSE si no se puedo disparar.
     */
    public boolean cambiarEstadoDeRed(Integer tDisparable) {
        // intenta tomar el mutex si esta ocupado se va a dormir hasta que se desocupe
        colaDeEntrada();
      
        Boolean seDisparo = rdp.disparar(tDisparable);
        notificar();

        if (!seDisparo){
            enviarAColaDeCondicion(tDisparable);
            return false;
        }
        return true;


    }

    private void enviarAColaDeCondicion(Integer tDisparable) {
        try {
            colasCondition[tDisparable].acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void colaDeEntrada(){
        try{
            mutex.acquire();
        } catch (InterruptedException e) {
            System.out.println("El hilo " + Thread.currentThread().getName() + " se interrumpió en el monitor");
            System.exit(1);
        }
    }
    /**
     * Pregunta si hay alguien esperando el mutex (Probablemente haya salido de una ventana temporal) y si lo hay, se lo
     * cede
     * Si no hay nadie, notifica a alguien en las colas de condicion que pueda disparar (ademas ajusta la politica)
     * y este sale del monitor
     */
    public void notificar() {
        liberarUno();
        
        if(mutex.availablePermits()!=0){
            System.out.println("Error en el mutex: la entrada al monitor ha dejado de ser binaria: ");
            System.exit(1); //Si el semaforo deja de ser binario muere aca
        }
        mutex.release();
    }

    /**
     * Devuelve los hilos que estan en las colas de condición que estan listos para ejecutarse, es decir
     * que tienen transiciones sensibilizadas
     */

    private Integer[] getTransPotencialColas() {
        Integer[] aux = new Integer[CANTIDAD_TRANSICIONES];
        for (int i = 0; i < CANTIDAD_TRANSICIONES; i++) {
            //si hay hilos esperando y transiciones sensibilizadas
            if ((colasCondition[i].getQueueLength() != 0) && (rdp.getSensibilizadas()[i] == 1) &&
            !VectorTSensibilizadas.estaEsperando[i])
                aux[i] = 1;
            else
                aux[i] = 0;
        }
        return aux;
    }

    /**
     * Se obtienen las transiciones con hilos esperando en colas con transiciones sensibilizadas y
     *  que no esten esperando la ventana de estas
     * la politica decide cual notificar para mantener la carga de los invariantes.
     */
    public void liberarUno(){
        Integer[] transicionesEjecutables = getTransPotencialColas();
        Integer cualDisparar = politica.cualDisparar(transicionesEjecutables);
        colasCondition[cualDisparar].release();
    
    }

    public Politicas getPolitica() {
        return politica;
    }

    public static Semaphore getMutex() {
        return mutex;
    }

    public String printMarcado() {
        return rdp.printMarcadoActual();
    }
}
