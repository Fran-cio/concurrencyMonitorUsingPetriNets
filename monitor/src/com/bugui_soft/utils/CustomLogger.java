/*
 * CustomLogger
 *
 * Version 1.0
 *
 * Copyright BeerWare
 */

package com.bugui_soft.utils;

import java.io.FileWriter;
import java.io.IOException;

import static com.bugui_soft.Main.exchangerLogger;
import static com.bugui_soft.utils.Constantes.ERROR_EXIT_STATUS;

public class CustomLogger implements Runnable {
    private static final Object lock = new Object();
    private static CustomLogger customLogger;
    private static FileWriter file;

    private CustomLogger() { }

    public static CustomLogger getInstanceOfCustomLogger() {
        synchronized (lock) {
            if (customLogger == null) {
                try {
                    customLogger = new CustomLogger();
                    file = new FileWriter("data/Log.txt", false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Ya existe una instancia de CustomLogger, no se creará otra");
            }
            return customLogger;
        }
    }

    @Override
    public void run() {
        /*Va a correr hasta que se ejecuten 1000 transiciones*/
        try {
            while (!Rdp.milInvariantes) {
                    /*
                     * Para loggear la transicion que se disparo se utilizo una primitiva llamada exchanger vista en clase
                     * que intercambia mensajes entre los hilos que se disparan y se quede esperando los mensajes
                     */
                    Integer numDisp = exchangerLogger.exchange(null);
                    file.write("T" + numDisp + " ");
                    file.flush();
            }
            file.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.exit(ERROR_EXIT_STATUS);
        }
    }
}
