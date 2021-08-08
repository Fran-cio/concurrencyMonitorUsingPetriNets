package com.bugui_soft.utils;

import static com.bugui_soft.utils.Constantes.CANTIDAD_TRANSICIONES;
import static com.bugui_soft.utils.Monitor.*;

import java.util.Random;
import java.util.concurrent.TimeoutException;

public class VectorTSensibilizadas {
    private final Integer[] alfa;
    private final Integer[] beta;
    private Integer[] sensibilizada;

    public VectorTSensibilizadas(Integer[] transiciones) {
        alfa = new Integer[CANTIDAD_TRANSICIONES];
        beta = new Integer[CANTIDAD_TRANSICIONES];

        Random rd = new Random();
        for (int i = 0; i < CANTIDAD_TRANSICIONES; i++) {
            alfa[i] = rd.nextInt(200);
            beta[i] = alfa[i] + rd.nextInt(500);
        }

        this.sensibilizada = transiciones;
    }

    public void setSensibilizado(Integer[] nuevaTS) {
        sensibilizada = nuevaTS;
    }

    public Integer[] getSensibilizada() {
        return sensibilizada;
    }

    public boolean estaSensibilizado(Integer disparo) {
        if (sensibilizada[disparo] > 0) { //sensibilizado por tokens
            Long[] timeStamp = Rdp.getTimeStamp();
            long tiempoActual = System.currentTimeMillis();
            long tiempoMinVentana = timeStamp[disparo] + alfa[disparo];
            long tiempoMaxVentana = timeStamp[disparo] + beta[disparo] + 100000000;
            boolean estamosEnVentana = tiempoActual >= tiempoMinVentana && tiempoActual <= tiempoMaxVentana;
            boolean antesDeAlfa = tiempoActual < tiempoMinVentana;

            if (estamosEnVentana) return true;

            getMutex().release();

            try {
                estaAntesDeAlfa(antesDeAlfa, tiempoMinVentana, tiempoActual);
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
