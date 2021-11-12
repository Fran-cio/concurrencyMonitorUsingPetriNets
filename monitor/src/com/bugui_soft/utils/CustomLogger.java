package com.bugui_soft.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import com.bugui_soft.Main;
import static com.bugui_soft.utils.Constantes.CONTADOR_DEL_RPOGRAMA;
import static com.bugui_soft.Main.exchanger;
import static com.bugui_soft.Main.rdp;


public class CustomLogger implements Runnable {
    private static final Object lock = new Object();
    private static CustomLogger customLogger;
    private static Integer contador = CONTADOR_DEL_RPOGRAMA;
    private static FileWriter file;

    private CustomLogger() { }

    public static CustomLogger getInstanceOfCustomLogger() {
        synchronized (lock) {
            if (customLogger == null) {
                try {
                    customLogger = new CustomLogger();
                    file = new FileWriter("data/Log.txt");
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
        while (true) {
            System.out.println("Loggea: " + Thread.currentThread().getName());
            try {
                Integer numDisp = exchanger.exchange(null);
                //condicion de llegada para el log
                //cuando se cumple un ciclo es true, de esta manera no quedan transiciones sueltas que nos rompa la expresión regular
                boolean isHome = Arrays.equals(rdp.getMarcadoActual(), rdp.getMarcadoInicial());
                if (contador <= 0 && isHome) {
                    file.write("T" + numDisp + " ");
                    file.close();
                    Main.finalizarPrograma();
                } else {
                    file.write("T" + numDisp + " ");
                    System.out.println("T" + numDisp);
                    contador--;
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
