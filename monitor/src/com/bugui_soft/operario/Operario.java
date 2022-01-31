package com.bugui_soft.operario;

import java.util.concurrent.TimeUnit;

import static com.bugui_soft.Main.finDePrograma;
import static com.bugui_soft.Main.monitor;

public abstract class Operario {
    protected Integer[] tInvariante;

    public Operario() {
    }

    protected void setTInvariante(Integer[] transicionesInvariantes) {
        tInvariante = transicionesInvariantes;
    }

    /**
     * A fines practicos todos los hacen su tarea e intenan cambiar el estado de la red, cada uno avanza haciendo la tarea
     * tenga asignada (Es decir, el vector tInvariante tiene enumeradas en orden las transiciones a ejecutar
     */
    protected void aTrabajar(Integer tiempoDeTrabajo) {
        while (!finDePrograma) {
            for (Integer transicion : tInvariante) {
                while (!monitor.cambiarEstadoDeRed(transicion))

                if (finDePrograma) break;
                try {
                    TimeUnit.MILLISECONDS.sleep(tiempoDeTrabajo);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
