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
// TODO: Preguntar a juan si le parecen bien como estan puestos los invariantes en elos operarios (Creo que fue el que lo hizo)
public class Main {
    //crear el monitor
    private static final OperarioFactory operarioFactory = OperarioFactory.getInstanceOfOperarioFactory();
    private static final ArrayList<Runnable> operarios = new ArrayList<>(Constantes.CANTIDAD_OPERARIOS);
    private static final HilosFactory hilosFactory = HilosFactory.getInstanceOfThreadFactory();
    public static final Monitor monitor = Monitor.getInstanceOfMonitor();
    public static final CustomLogger logger = CustomLogger.getInstanceOfCustomLogger();
    public static final Exchanger<Integer> exchanger = new Exchanger<>();
    public static boolean finDePrograma= false;

    public static void main(String[] args) {
        cargarOperarios();
        //crear y correr hilos
        Thread log = hilosFactory.newThread(logger);
        log.start();
        for (Runnable operario : operarios) hilosFactory.newThread(operario).start();
        try {
            log.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finalizarPrograma();
    }

    /**
     *  Esta funcion utiliza la clase factory y acumula los objetos en un arreglo
     */
    private static void cargarOperarios() {
        for (int i = 0; i < CANTIDAD_PRODUCTORES; i++) {
            operarios.add(operarioFactory.getOperario(PRODUCTOR));
        }
        for (int i = 0; i < CANTIDAD_TECNICOS; i++) {
            operarios.add(operarioFactory.getOperario(TECNICO));
        }
        operarios.add(operarioFactory.getOperario(DESCARTADOR));
        operarios.add(operarioFactory.getOperario(CALIDAD));
    }

    /**
     * Cuando log termina de escribir se ejecuta esta funcion que termina la ejecucion.
     */
    public static void finalizarPrograma() {
        finDePrograma=true;
        monitor.printMarcado();
        System.out.println("Se acabó el programa");
        System.exit(0);
    }
}
