package com.bugui_soft.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import com.bugui_soft.Main;
import static com.bugui_soft.utils.Constantes.CONTADOR_DEL_RPOGRAMA;
import static com.bugui_soft.Main.exchanger;
import static com.bugui_soft.Main.rdp;


public class CustomLogger implements Runnable {
    private static Integer contador = CONTADOR_DEL_RPOGRAMA;
    private FileWriter file;

    public CustomLogger() {
        try {
            file = new FileWriter("Data/Log.txt");
        } catch (IOException e) {
            e.printStackTrace();
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
