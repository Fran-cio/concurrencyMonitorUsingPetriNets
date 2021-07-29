package com.bugui_soft;

import java.util.ArrayList;

import com.bugui_soft.utils.*;

import static com.bugui_soft.utils.Constantes.*;

public class Main {
    //crear el monitor
    private static final OperarioFactory operarioFactory = new OperarioFactory();
    private static final ArrayList<Runnable> operarios = new ArrayList<>(Constantes.CANTIDAD_OPERARIOS);
    private static final HilosFactory hilosFactory = new HilosFactory();
    public static final Monitor monitor = new Monitor();
    public static final Rdp rdp = new Rdp();
    public static final CustomLogger logger = new CustomLogger();

    public static void main(String[] args) {
        cargarOperarios();
        //crear y correr hilos
        hilosFactory.newThread(logger).start();
        for (Runnable operario : operarios) hilosFactory.newThread(operario).start();

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
