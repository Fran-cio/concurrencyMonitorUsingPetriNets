package com.bugui_soft.utils;

import static com.bugui_soft.utils.Constantes.CANTIDAD_TRANSICIONES;
import static com.bugui_soft.utils.Monitor.*;

import java.util.Random;
import java.util.concurrent.TimeoutException;

public class VectorTSensibilizadas {
    private static final Object lock = new Object();
    private static VectorTSensibilizadas vectorTSensibilizadas;
    private static Integer[] alfa;
    private static Integer[] beta;
    private Integer[] sensibilizada;

    private VectorTSensibilizadas() {}

    public static VectorTSensibilizadas getInstanceOfVectorTSensibilizadas(Integer[] transiciones) {
        synchronized (lock) {
            if (vectorTSensibilizadas == null) {
                vectorTSensibilizadas = new VectorTSensibilizadas();
                alfa = new Integer[CANTIDAD_TRANSICIONES];
                beta = new Integer[CANTIDAD_TRANSICIONES];

                Random rd = new Random();
                for (int i = 0; i < CANTIDAD_TRANSICIONES; i++) {
                    alfa[i] = 0;// rd.nextInt(200);
                    beta[i] = alfa[i] + rd.nextInt(5000) + 5000;
                }

                vectorTSensibilizadas.sensibilizada = transiciones;
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

    public boolean estaSensibilizado(Integer disparo) {
        /**TODO: Borrar comentario
         *Considero necesario aplicar el booleano esperando
         * ya que aca se encuentran muchos hilos despues de sacar el synchronized
         **/
        if (sensibilizada[disparo] > 0) { //sensibilizado por tokens
            Long[] timeStamp = Rdp.getTimeStamp();
            long tiempoActual = System.currentTimeMillis();
            long tiempoMinVentana = timeStamp[disparo] + alfa[disparo];
            long tiempoMaxVentana = timeStamp[disparo] + beta[disparo];
            boolean estamosEnVentana = tiempoActual >= tiempoMinVentana && tiempoActual <= tiempoMaxVentana;
            boolean antesDeAlfa = tiempoActual < tiempoMinVentana;

            if (estamosEnVentana) return true;

            /**TODO: Borrar comentario
             *Movi el release adentro del metodo, faltaba el acquire y el return, ahora eso anda bien
            **/

            try {
                estaAntesDeAlfa(antesDeAlfa, tiempoMinVentana, tiempoActual);
                return true;
            } catch (TimeoutException e) {
                System.out.println("El hilo " + Thread.currentThread() + " se pasó la ventana de tiempo");
                sensibilizada[disparo] = 0;
            }
        }
        return false;
    }

/**Espera el tiempo necesario para disparar en caso de superar la ventana.
 * @exception TimeoutException porque superó el tiempo máximo de la ventana.
 * */
//TODO:las lineas 71 y 78/83 estan comentadas para evitar deadlock, hay que revisar como lo solucionamos
    private void estaAntesDeAlfa(boolean antesDeAlfa, long tiempoMinVentana, long tiempoActual) throws TimeoutException {
        if (antesDeAlfa) {
            long tiempoDormir = tiempoMinVentana - tiempoActual;
            try {
                Thread.sleep(tiempoDormir);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            throw new TimeoutException();
        }
    }
}
