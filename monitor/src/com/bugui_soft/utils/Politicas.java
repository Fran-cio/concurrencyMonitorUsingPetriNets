/*
 * Politicas
 *
 * Version 1.0
 *
 * Copyright BeerWare
 */

package com.bugui_soft.utils;

import java.util.HashMap;

import static com.bugui_soft.Main.exchangerGUI;
import static com.bugui_soft.utils.Constantes.CANTIDAD_TRANSICIONES;
import static com.bugui_soft.utils.Constantes.NUMERO_DE_TRANS_A_EJECUTAR;

public class Politicas {
    private static final Object lock = new Object();
    private static Politicas politicas;
    private static HashMap<Integer, Integer> mapeoTransicionConInvariante;//contador(transicion, contador de su invariante)
    private static Integer[] cuentaDeInvariantes; //arreglo de las cuentas de disparos de los invariantes


    private Politicas() {
    }

    public static Politicas getInstanceOfPoliticas() {
        synchronized (lock) {
            if (politicas == null) {
                politicas = new Politicas();
                int cuentaDeInvariantes1 = 0;//cuentas del disparo del invariante de transicion 1
                int cuentaDeInvariantes2 = 0;//cuentas del disparo del invariante de transicion 2
                int cuentaDeInvariantes3 = 0;//cuentas del disparo del invariante de transicion 3
                cuentaDeInvariantes = new Integer[]{cuentaDeInvariantes1, cuentaDeInvariantes2, cuentaDeInvariantes3};

                //cargamos el mapeo de transiciones -> contador
                mapeoTransicionConInvariante = new HashMap<>();
                //cargamos el Tinvariante 0
                mapeoTransicionConInvariante.put(1, 0);
                mapeoTransicionConInvariante.put(10, 0);
                //cargamos el Tinvariante 1
                for (int i = 2; i < 6; i++) {
                    mapeoTransicionConInvariante.put(i, 1);
                }
                //cargamos el Tinvariante 2
                for (int i = 6; i < 10; i++) {
                    mapeoTransicionConInvariante.put(i, 2);
                }
                //Ignoramos los conlflictos
                mapeoTransicionConInvariante.put(0, null);
            } else {
                System.out.println("Ya existe una instancia de politicas, no se creará otra");
            }
            return politicas;
        }
    }
    /**
     * Devuelve la transición menos disparada
     */
    public Integer cualDisparar(Integer[] transPot) {
        return obtenerMenor(transPot);
    }

    private Integer obtenerMenor(Integer[] transPot) {
        Integer minimo = Integer.MAX_VALUE;
        int posMin = 0;
        //se le otorga prioridad al conflicto ya que pone a su invariante en desventaja
        if (transPot[0] != 0) {
            return 0; //T0
        }
        //chequeo las transiciones comunes
        for (int i = 1; i < CANTIDAD_TRANSICIONES; i++) {
            int invSelected = mapeoTransicionConInvariante.get(i);
            if ((cuentaDeInvariantes[invSelected] < minimo) && (transPot[i] != 0)) { //cuentaTI[invSelected] devuelve la cantidad de veces que se disparó ese invariante
                minimo = cuentaDeInvariantes[invSelected];
                posMin = i;
            }
        }

        return posMin;
    }

    public void incrementarInvariante(Integer transicion) {
        try {
        if (transicion == 5) {//es el conflicto
            cuentaDeInvariantes[1]++;
        } else if(transicion ==10) {
            cuentaDeInvariantes[0]++;
        }
        else if(transicion==9) {
            cuentaDeInvariantes[2]++;
        }
        exchangerGUI.exchange(cuentaDeInvariantes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean milInveriantes(){
        return cuentaDeInvariantes[0] + cuentaDeInvariantes[1] + cuentaDeInvariantes[2] > NUMERO_DE_TRANS_A_EJECUTAR;
    }
}
