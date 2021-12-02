package com.bugui_soft.utils;

import javax.swing.*;

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
    private static Integer estaEsperando;

    private VectorTSensibilizadas() {}

    public static VectorTSensibilizadas getInstanceOfVectorTSensibilizadas(Integer[] transiciones) {
        synchronized (lock) {
            if (vectorTSensibilizadas == null) {
                vectorTSensibilizadas = new VectorTSensibilizadas();
                alfa = new Integer[CANTIDAD_TRANSICIONES];
                beta = new Integer[CANTIDAD_TRANSICIONES];
                estaEsperando=0;

                Random rd = new Random();
                for (int i = 0; i < CANTIDAD_TRANSICIONES; i++) {
                    alfa[i] = rd.nextInt(200);
                    beta[i] = alfa[i] + rd.nextInt(2500);
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
        /*
            TODO: Comentario informativo de avance
            Fran:Intente hacer un arreglo de esta esperando y que varios intenten entrar en la ventana, por alguna razon esto
            da problemas de concurrencia. Asi que implemente un flag que si ya hay alguien esperando, que no entre
            es una cuestion de diseño, prefiero hacerlo de forma de antes pero asi lo hizo Mico en la clase
         */
        if (sensibilizada[disparo] > 0 && estaEsperando==0) { //sensibilizado por tokens
            Long[] timeStamp = Rdp.getTimeStamp();
            long tiempoActual = System.currentTimeMillis();
            long tiempoMinVentana = timeStamp[disparo] + alfa[disparo];
            long tiempoMaxVentana = timeStamp[disparo] + beta[disparo];
            boolean estamosEnVentana = tiempoActual >= tiempoMinVentana && tiempoActual <= tiempoMaxVentana;
            boolean antesDeAlfa = tiempoActual < tiempoMinVentana;

            if (estamosEnVentana) return true;
            try {
                estaAntesDeAlfa(antesDeAlfa, tiempoMinVentana, tiempoActual);
                return true;
            } catch (TimeoutException e) {
                System.out.println("La transición T" + disparo + " se pasó la ventana de tiempo");
                sensibilizada[disparo] = 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

/**Espera el tiempo necesario para disparar en caso de superar la ventana.
 * @exception TimeoutException porque superó el tiempo máximo de la ventana.
 * */
    private void estaAntesDeAlfa(boolean antesDeAlfa, long tiempoMinVentana, long tiempoActual) throws TimeoutException, InterruptedException {
        if (antesDeAlfa) {
            estaEsperando=1;
            Monitor.getMutex().release();
            long tiempoDormir = tiempoMinVentana - tiempoActual;
            try {
                Thread.sleep(tiempoDormir);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Monitor.getMutex().acquire();
            estaEsperando=0;
        } else {
            throw new TimeoutException();
        }
    }
}
