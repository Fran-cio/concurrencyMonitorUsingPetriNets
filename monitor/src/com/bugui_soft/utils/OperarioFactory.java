package com.bugui_soft.utils;

import com.bugui_soft.operario.*;

import java.util.HashMap;

public class OperarioFactory {
    private HashMap<String,Runnable> mapa;

    public OperarioFactory() {
        mapa= new HashMap<>();
        mapa.put(Constantes.PRODUCTOR   ,new Productor());
        mapa.put(Constantes.TECNICO     ,new Tecnico());
        mapa.put(Constantes.CALIDAD     ,new Calidad());
        mapa.put(Constantes.DESCARTADOR ,new Descartador());

    }

    public Runnable getOperario(String nombre) {
        return mapa.get(nombre);
    }

}
