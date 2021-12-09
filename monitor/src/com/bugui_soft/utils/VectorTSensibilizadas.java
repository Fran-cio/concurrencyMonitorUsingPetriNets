package com.bugui_soft.utils;

import javax.swing.*;

import static com.bugui_soft.utils.Constantes.*;
import static com.bugui_soft.utils.Monitor.*;

import java.util.Random;
import java.util.concurrent.TimeoutException;

public class VectorTSensibilizadas {
    private static final Object lock = new Object();
    private static VectorTSensibilizadas vectorTSensibilizadas;
    private static Integer[] alfa;
    private static Integer[] beta;
    private static Integer[] sensibilizada;
    private static Integer[] estaEsperando;

    private VectorTSensibilizadas() {}

    public static VectorTSensibilizadas getInstanceOfVectorTSensibilizadas(Integer[] transiciones) {
        synchronized (lock) {
            if (vectorTSensibilizadas == null) {
                vectorTSensibilizadas = new VectorTSensibilizadas();
                alfa = new Integer[CANTIDAD_TRANSICIONES];
                beta = new Integer[CANTIDAD_TRANSICIONES];
                estaEsperando= new Integer[CANTIDAD_TRANSICIONES];

                for (int i = 0; i < CANTIDAD_TRANSICIONES; i++) {
                    alfa[i] = 0;
                    beta[i] = Integer.MAX_VALUE;
                    estaEsperando[i]=0;
                }
                Random rd = new Random();
                for (int i = 0; i < INV_1.length; i++) {
                    alfa[INV_1[i]] = 10 + rd.nextInt(5);
                    beta[INV_1[i]] = alfa[INV_1[i]]+ 100 + rd.nextInt(100);
                    estaEsperando[i]=0;
                }
                for (int i = 0; i < INV_2.length; i++) {
                    alfa[INV_2[i]] = 20 + rd.nextInt(10);
                    beta[INV_2[i]] = alfa[INV_3[i]]+ 100 + rd.nextInt(100);
                    estaEsperando[i]=0;
                }
                for (int i = 0; i < INV_3.length; i++) {
                    alfa[INV_3[i]] = rd.nextInt(5);
                    beta[INV_3[i]] = alfa[INV_3[i]]+ 50 + rd.nextInt(100);
                    estaEsperando[i]=0;
                }
                /*
                Propongo generar algunas tras de alpha 0 para asi tenemos algunas trans temporales y otras que no
                 */

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

    public boolean estaSensibilizado(Integer disparo) {
        /*
            TODO: Comentario informativo
            Fran: No parecen ejecutarse transiciones invalidas, tampoco parece violarse la seccion critica. Si colocan un
            breakpoint en la linea 73, va ver como siempre que una trans le gana a otra, se da en la transicion, 3. Esto,
            lleva a darme a entender que esta andando bien, pero como el invariante 1 es el mas sensible a quedar
            desensibilizado, en la gran mayoria de los casos se da que los otros invariantes le ganan la ventana de trans
            lo cual lleva a que esa rama tienda a no poder avanzar. De ser este el problema, preferiria optar por quedarme
            con la otra solucion.
         */
        if (sensibilizada[disparo] > 0 && estaEsperando[disparo]==0) { //sensibilizado por tokens
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

/**Espera el tiempo necesario para disparar en caso de superar la ventana.
 * @exception TimeoutException porque superó el tiempo máximo de la ventana.
 * */
    private void estaAntesDeAlfa(boolean antesDeAlfa, long tiempoMinVentana, long tiempoActual,Integer disparo) throws TimeoutException, InterruptedException {
        if (antesDeAlfa) {
            estaEsperando[disparo]=1;
            if(Monitor.getMutex().availablePermits()!=0) {
               System.out.println("El mutex ha dejado de ser binario");
               System.exit(1);//Se puede sacar: Si el semaforo deja de ser binario muere aca
            }

            Monitor.getMutex().release();
            long tiempoDormir = tiempoMinVentana - tiempoActual;
            try {
                Thread.sleep(tiempoDormir);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Monitor.getMutex().acquire();
            estaEsperando[disparo]=0;
        } else {
            throw new TimeoutException();
        }
    }
}
