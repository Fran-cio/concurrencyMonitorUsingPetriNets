package com.bugui_soft;

import java.util.ArrayList;
import java.util.concurrent.Exchanger;
import com.bugui_soft.utils.*;
import static com.bugui_soft.utils.Constantes.*;
/*
TODO:
    -Interrumpir cada hilo por separado asi no matamos todo de una
    -Comentar bien todo lo que falta
    -Comprender correctamente los elementos del codigo
 */
public class Main {
    //crear el monitor
    private static final OperarioFactory operarioFactory = OperarioFactory.getInstanceOfOperarioFactory();
    private static final ArrayList<Runnable> operarios = new ArrayList<>(Constantes.CANTIDAD_OPERARIOS);
    private static final HilosFactory hilosFactory = HilosFactory.getInstanceOfThreadFactory();
    public static final Monitor monitor = Monitor.getInstanceOfMonitor();
    public static final CustomLogger logger = CustomLogger.getInstanceOfCustomLogger();
    public static final Exchanger<Integer> exchanger = new Exchanger<>();
    public static boolean finDePrograma = false;

    public static void main(String[] args) {
        cargarOperarios();
        //creaamos los hilos
        Thread log = hilosFactory.newThread(logger);
        log.start();
        //ponemos los hilos de operarios a correr
        for (Runnable operario : operarios) hilosFactory.newThread(operario).start();
        try {
            log.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*Una vez que se ejecutan las 1000 transiciones el logger muere, se realiza el join,
         y se pide finalizar el programa*/
        finalizarPrograma();
    }
    private static void cargarOperarios() {
        for (int i = 0; i < CANTIDAD_PRODUCTORES; i++)
            operarios.add(operarioFactory.getOperario(PRODUCTOR));

        for (int i = 0; i < CANTIDAD_TECNICOS; i++)
            operarios.add(operarioFactory.getOperario(TECNICO));

        operarios.add(operarioFactory.getOperario(DESCARTADOR));
        operarios.add(operarioFactory.getOperario(CALIDAD));
    }

    public static void finalizarPrograma() {
        /*los workers velan por este estado, cuando se cumple todos mueren una vez que hayan terminado sus tareas.*/
        finDePrograma = true;
        monitor.printMarcado();
        System.out.println("Se acabó el programa");
        System.exit(0);
    }
}
