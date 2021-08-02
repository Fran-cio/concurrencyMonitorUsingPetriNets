package com.bugui_soft.utils;

import java.util.HashMap;

import static com.bugui_soft.utils.Constantes.CANTIDAD_TRANSICIONES;
import static com.bugui_soft.Main.rdp;

public class Politicas {
    private HashMap<Integer, Integer> mapeoT_a_TI;//contador(transicion , contador de su invariante)
    private static Integer[] cuentaT_I;//arreglo de las cuentas de dsiparos de los invariantes


    public Politicas() {
        Integer cuentaT_I1=0;//cuentas del disparo del invariante de transicion 1
        Integer cuentaT_I2=0;//cuentas del disparo del invariante de transicion 2
        Integer cuentaT_I3=0;//cuentas del disparo del invariante de transicion 3
        cuentaT_I = new Integer[]{cuentaT_I1, cuentaT_I2, cuentaT_I3};

        //cargamos el mapeo de transiciones -> contador
        mapeoT_a_TI = new HashMap();
        //cargamos el Tinvariante 0
        mapeoT_a_TI.put(1, 0);
        mapeoT_a_TI.put(10, 0);
        //cargamos el Tinvariante 1
        for (int i = 2; i < 6; i++) {
            mapeoT_a_TI.put(i, 1);
        }
        //cargamos el Tinvariante 2
        for (int i = 6; i < 10; i++) {
            mapeoT_a_TI.put(i, 2);
        }
        //Ignoramos los conlflictos
        mapeoT_a_TI.put(0, null);

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
            System.out.println(cuentaT_I[0]+" "+cuentaT_I[1]+" "+cuentaT_I[2]);
            return 0;
        }
        //chequeo las transiciones comunes
        for (int i = 1; i < CANTIDAD_TRANSICIONES; i++) {
            int cont=mapeoT_a_TI.get(i);
            if ((cuentaT_I[cont] < minimo) && (transPot[i] != 0)) {
                minimo = cuentaT_I[cont];
                posMin = i;
            }
        }

        System.out.println(cuentaT_I[0]+" "+cuentaT_I[1]+" "+cuentaT_I[2]);
        return posMin;
    }

    public void incrementarTI(Integer t) {
        if(t==0){//es el conflicto
            cuentaT_I[0]++;
            cuentaT_I[1]++;
        }
        else{
            int cont=mapeoT_a_TI.get(t);
            cuentaT_I[cont]++;
        }
    }
}
