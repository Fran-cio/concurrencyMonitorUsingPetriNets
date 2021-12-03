package com.bugui_soft.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import com.bugui_soft.Main;

import static com.bugui_soft.utils.Constantes.CANTIDAD_PLAZAS;
import static com.bugui_soft.utils.Constantes.CONTADOR_DEL_RPOGRAMA;
import static com.bugui_soft.Main.exchanger;


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
        while (!Rdp.isHome) {
            try {
                Integer numDisp = exchanger.exchange(null);
                //System.out.println("T" + numDisp);
                file.write("T" + numDisp + " ");
                file.flush();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }
}
