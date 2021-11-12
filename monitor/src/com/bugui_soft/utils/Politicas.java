package com.bugui_soft.utils;

import java.util.HashMap;

import static com.bugui_soft.utils.Constantes.CANTIDAD_TRANSICIONES;

public class Politicas {
    private static final Object lock = new Object();
    private static Politicas politicas;
    private static HashMap<Integer, Integer> mapeoTaTI;//contador(transicion , contador de su invariante)
    private static Integer[] cuentaTI;//arreglo de las cuentas de dsiparos de los invariantes


    private Politicas() { }

    public static Politicas getInstanceOfPoliticas() {
        synchronized (lock) {
            if (politicas == null) {
                politicas = new Politicas();
                int cuentaTI1 = 0;//cuentas del disparo del invariante de transicion 1
                int cuentaTI2 = 0;//cuentas del disparo del invariante de transicion 2
                int cuentaTI3 = 0;//cuentas del disparo del invariante de transicion 3
                cuentaTI = new Integer[]{cuentaTI1, cuentaTI2, cuentaTI3};

                //cargamos el mapeo de transiciones -> contador
                mapeoTaTI = new HashMap<Integer, Integer>();
                //cargamos el Tinvariante 0
                mapeoTaTI.put(1, 0);
                mapeoTaTI.put(10, 0);
                //cargamos el Tinvariante 1
                for (int i = 2; i < 6; i++) {
                    mapeoTaTI.put(i, 1);
                }
                //cargamos el Tinvariante 2
                for (int i = 6; i < 10; i++) {
                    mapeoTaTI.put(i, 2);
                }
                //Ignoramos los conlflictos
                mapeoTaTI.put(0, null);
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
            int invSelected = mapeoTaTI.get(i);
            if ((cuentaTI[invSelected] < minimo) && (transPot[i] != 0)) { //cuentaTI[invSelected] devuelve la cantidad de veces que se disparó ese invariante
                minimo = cuentaTI[invSelected];
                posMin = i;
            }
        }

        return posMin;
    }

    public void incrementarTI(Integer t) {
        if (t == 0) {//es el conflicto
            cuentaTI[0]++;
            cuentaTI[1]++;
        } else {
            int cont = mapeoTaTI.get(t);
            cuentaTI[cont]++;
        }
    }
}
