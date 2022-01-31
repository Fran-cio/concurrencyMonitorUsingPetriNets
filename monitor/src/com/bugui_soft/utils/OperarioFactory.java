/*
 * OperarioFactory
 *
 * Version 1.0
 *
 * Copyright BeerWare
 */

package com.bugui_soft.utils;

import com.bugui_soft.operario.*;

import java.util.HashMap;

public class OperarioFactory {
    private static final Object lock = new Object();
    private static OperarioFactory operarioFactory;
    private static HashMap<String, Runnable> mapa;

    private OperarioFactory() {
    }

    public static OperarioFactory getInstanceOfOperarioFactory() {
        synchronized (lock) {
            if (operarioFactory == null) {
                operarioFactory = new OperarioFactory();
                mapa = new HashMap<>();
                mapa.put(Constantes.PRODUCTOR, new Productor());
                mapa.put(Constantes.TECNICO, new Tecnico());
                mapa.put(Constantes.CALIDAD, new Calidad());
                mapa.put(Constantes.DESCARTADOR, new Descartador());
            } else {
                System.out.println("Ya existe una instancia del Operario Factory, no se creará otra");
            }
            return operarioFactory;
        }
    }

    /**
     * Se implementa el patron Factory para obtener una instacia del operario que se pida.
     */
    public Runnable getOperario(String nombre) {
        return mapa.get(nombre);
    }

}
