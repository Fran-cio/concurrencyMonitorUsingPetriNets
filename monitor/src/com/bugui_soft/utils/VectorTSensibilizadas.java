/*
 * VectorTSensibilizadas
 *
 * Version 1.0
 *
 * Copyright BeerWare
 */

package com.bugui_soft.utils;

import com.bugui_soft.Main;

import static com.bugui_soft.utils.Constantes.*;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class VectorTSensibilizadas {
    private static final Object lock = new Object();
    private static VectorTSensibilizadas vectorTSensibilizadas;
    private static Integer[] alfa;
    private static Integer[] beta;
    private static Integer[] sensibilizada;
    public static Boolean[] estaEsperando;

    private VectorTSensibilizadas() {
    }
/**
 * Los criterios para que tiempos asignar a cada invariante son con el unico fin de un funcionamiento correcto, notamos
 * que hay problemas en la temporizacion cuando el tiempo del invariante 3 es demasiado grande, por lo tanto se temporizo
 * dentro de un umbral alfa donde no nos cause problemas al momento de ejecutar el programa. Los Beta son para que haya
 * un conjunto de pasos de ventana para que se vea que se pueden dar porque que no afecten la el avance de la red.
 *
 * Todos los tiempos estan dados en mili segundos.
 */
    public static VectorTSensibilizadas getInstanceOfVectorTSensibilizadas(Integer[] transiciones) {
        synchronized (lock) {
            if (vectorTSensibilizadas == null) {
                vectorTSensibilizadas = new VectorTSensibilizadas();
                alfa = new Integer[CANTIDAD_TRANSICIONES];
                beta = new Integer[CANTIDAD_TRANSICIONES];
                estaEsperando= new Boolean[CANTIDAD_TRANSICIONES];

                vectorTSensibilizadas.setVentanasTemporalesBase();
                vectorTSensibilizadas.setVentanasTemporales();

                sensibilizada = transiciones;
            } else {
                System.out.println("Ya existe una instancia de vector t sensibilizadas, no se creará otra");
            }
            return vectorTSensibilizadas;
        }
    }

    public void setSensibilizado(Integer[] nuevaTS) {
        sensibilizada = nuevaTS;
    }

    public Integer[] getSensibilizada() {
        return sensibilizada;
    }

    /**
     * Se chequea si esta sensibilizada tanto de token como temporalmente, si es asi, avanza e intenta dispararse
     *
     * @param disparo: Numero transicion a dispararse
     * @return Si se pudo disparar o no
     **/
    public boolean estaSensibilizado(Integer disparo) {
        if (sensibilizada[disparo] > 0 && !estaEsperando[disparo]) {
            /*
             * Si esta sensibilizado por tokens y no hay ningun hilo ya esperando por esa transicion, entra a este if
             */
            Long[] timeStamp = Rdp.getTimeStamp();
            long tiempoActual = System.currentTimeMillis();
            long tiempoMinVentana = timeStamp[disparo] + alfa[disparo];
            long tiempoMaxVentana = timeStamp[disparo] + beta[disparo];
            boolean estamosEnVentana = tiempoActual >= tiempoMinVentana && tiempoActual <= tiempoMaxVentana;
            boolean antesDeAlfa = tiempoActual < tiempoMinVentana;

            if (estamosEnVentana) return true;
            try {
                estaAntesDeAlfa(antesDeAlfa, tiempoMinVentana, tiempoActual,disparo);
                if(sensibilizada[disparo]==1) {
                    return true;
                }
            } catch (TimeoutException e) {
                System.out.println("La transición T" + disparo + " se pasó la ventana de tiempo");
                sensibilizada[disparo] = 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

/**
 * Espera el tiempo necesario para disparar en caso de superar la ventana.
 *
 * @exception TimeoutException porque superó el tiempo máximo de la ventana.
 **/
    private void estaAntesDeAlfa(boolean antesDeAlfa, long tiempoMinVentana, long tiempoActual,Integer disparo) throws TimeoutException, InterruptedException {
        if (antesDeAlfa) {
            estaEsperando[disparo]=true;
            if(Monitor.getMutex().availablePermits()!=0) {
               System.out.println("El mutex ha dejado de ser binario");
               System.exit(ERROR_EXIT_STATUS);//Se puede sacar: Si el semaforo deja de ser binario muere aca
            }


            //Antes de irse a dormir, libera otra transicion potencial
            Main.monitor.exit(); //! TODO: aca esta el crimen linea 111 y 118
            long tiempoDormir = tiempoMinVentana - tiempoActual;
            try {
                Thread.sleep(tiempoDormir);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Monitor.getMutex().acquire();

            
            estaEsperando[disparo]=false;
        } else {
            throw new TimeoutException();
        }
    }

    /**
     * Se asignan valores base para las temporizaciones, los cuales funcionan como si no hubiera semantica temporal
     * corriendo
     */
    private void setVentanasTemporalesBase(){
        for (int i = 0; i < CANTIDAD_TRANSICIONES; i++) {
            alfa[i] = 0;
            beta[i] = Integer.MAX_VALUE;
            estaEsperando[i] = false;
        }
    }

    /**
     * Se asignan las ventanas temporales, en base a los valores dados en la clase constantes. Estos estan definidos en
     * base a valores que se consideran que se comportan correctamente, siendo este, que la carga de invariantes no se
     * afecte y que ademas no se exageren la cantidad de ventanas superadas.
     */
    private void setVentanasTemporales(){
        Random rd = new Random();

        for (Integer integer : INV_1) {
            alfa[integer] =  rd.nextInt(REFERENCIA_INVARIANTE_1);
            beta[integer] = alfa[integer] + REFERENCIA_INVARIANTE_1 * ANCHO_DE_VENTANA +
                    rd.nextInt(REFERENCIA_INVARIANTE_1 * ANCHO_DE_VENTANA);
        }
        for (Integer integer : INV_2) {
            alfa[integer] =  rd.nextInt(REFERENCIA_INVARIANTE_2);
            beta[integer] = alfa[integer] + REFERENCIA_INVARIANTE_2 * ANCHO_DE_VENTANA +
                    rd.nextInt(REFERENCIA_INVARIANTE_2 * ANCHO_DE_VENTANA);
        }
        for (Integer integer : INV_3) {
            alfa[integer] = rd.nextInt(REFERENCIA_INVARIANTE_3);
            beta[integer] = alfa[integer] + REFERENCIA_INVARIANTE_3 * ANCHO_DE_VENTANA +
                    rd.nextInt(REFERENCIA_INVARIANTE_3 * ANCHO_DE_VENTANA);
        }//TODO: porque elinvariante 3 tenia desventaja?
    }
}
