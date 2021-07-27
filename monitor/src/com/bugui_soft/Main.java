package com.bugui_soft;

import java.util.ArrayList;

import com.bugui_soft.utils.*;

import static com.bugui_soft.utils.Constantes.*;

public class Main {
    //crear el monitor
    public static final Monitor monitor = new Monitor();
    private static final OperarioFactory operarioFactory = new OperarioFactory();
    private static final ArrayList<Runnable> operarios = new ArrayList<>(Constantes.CANTIDAD_OPERARIOS);
    public static final Rdp rdp = new Rdp();

    public static void main(String[] args) {
        cargarOperarios();
        //crear hilos

        //correr hilos
    }

    private static void cargarOperarios() {
        for (int i = 0; i < CANTIDAD_PRODUCTORES; i++)
            operarios.add(operarioFactory.getOperario(PRODUCTOR));

        for (int i = 0; i < CANTIDAD_TECNICOS; i++)
            operarios.add(operarioFactory.getOperario(TECNICO));

        operarios.add(operarioFactory.getOperario(DESCARTADOR));
        operarios.add(operarioFactory.getOperario(CALIDAD));
    }
}
