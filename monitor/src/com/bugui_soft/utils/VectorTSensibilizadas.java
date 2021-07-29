package com.bugui_soft.utils;

public class VectorTSensibilizadas {
    private Integer[] sensibilizada;

    public VectorTSensibilizadas(Integer[] t) {
        this.sensibilizada = t;
    }

    public void setSensibilizado(Integer[] nuevaTS) {
        sensibilizada = nuevaTS;
    }

    public Integer[] getSensibilizada() {
        return sensibilizada;
    }

    public boolean estaSensibilizado(Integer disparo) {
        if (sensibilizada[disparo] > 0) {
            return true;
            //TODO: chequear ventana de tiempo
        }
        return false;

    }
}
