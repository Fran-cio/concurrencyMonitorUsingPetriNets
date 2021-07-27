package com.bugui_soft.utils;

import com.bugui_soft.operario.*;

public class OperarioFactory {

    public Runnable getOperario(String nombre) {
        switch (nombre) {
            case Constantes.PRODUCTOR:
                return new Productor();
            case Constantes.TECNICO:
                return new Tecnico();
            case Constantes.CALIDAD:
                return new Calidad();
            default:
                return new Descartador();
        }
    }

}
