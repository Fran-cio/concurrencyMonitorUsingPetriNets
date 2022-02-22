/*
 * Main
 *
 * Version 1.0
 *
 * Copyright BeerWare
 */

package com.bugui_soft;

import java.util.ArrayList;
import java.util.concurrent.Exchanger;

import com.bugui_soft.utils.*;

import static com.bugui_soft.utils.Constantes.*;

public class Main {
    //crear el monitor
    private static final OperarioFactory operarioFactory = OperarioFactory.getInstanceOfOperarioFactory();
    private static final HilosFactory hilosFactory = HilosFactory.getInstanceOfThreadFactory();
    private static final ArrayList<Runnable> operarios = new ArrayList<>();

    public static final Monitor monitor = Monitor.getInstanceOfMonitor();

    public static final CustomLogger logger = CustomLogger.getInstanceOfCustomLogger();
    public static final Interfaz interfaz = Interfaz.getInstanceOfInterfaz();

    public static final Exchanger<Integer> exchangerLogger = new Exchanger<>();
    public static final Exchanger<Integer[]> exchangerGUI = new Exchanger<>();
    public static boolean finDePrograma = false;

    public static void main(String[] args) {
        cargarOperarios();
        //crear y correr hilos
        Thread GUI = hilosFactory.newThread(interfaz);
        Thread log = hilosFactory.newThread(logger);
        log.start();
        GUI.start();

        for (Runnable operario : operarios) hilosFactory.newThread(operario).start();
        try {
            log.join();
            GUI.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finalizarPrograma();
    }

    /**
     * Esta funcion utiliza la clase factory y acumula los objetos en un arreglo
     */
    private static void cargarOperarios() {
        for (int i = 0; i < CANTIDAD_PRODUCTORES; i++) {
            operarios.add(operarioFactory.getOperario(PRODUCTOR));
        }

        for (int i = 0; i < CANTIDAD_TECNICOS; i++) {
            operarios.add(operarioFactory.getOperario(TECNICO));
        }

        for (int i = 0; i < CANTIDAD_DESCARTADORES; i++) {
            operarios.add(operarioFactory.getOperario(DESCARTADOR));
        }

        for (int i = 0; i < CANTIDAD_CALIDAD; i++) {
            operarios.add(operarioFactory.getOperario(CALIDAD));
        }

    }

    /**
     * Cuando log termina de escribir se ejecuta esta funcion que termina la ejecucion.
     */
    public static void finalizarPrograma() {
        finDePrograma = true;
        System.out.println("Se acabó el programa");
    }
}
