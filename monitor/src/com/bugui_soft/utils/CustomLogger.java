package com.bugui_soft.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import com.bugui_soft.Main;
import static com.bugui_soft.utils.Constantes.CONTADOR_DEL_RPOGRAMA;
import static com.bugui_soft.Main.exchanger;

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
                boolean isHome= Arrays.equals(Main.rdp.getMarcadoActual(),Main.rdp.getMarcadoInicial());
                if (contador <= 0 && isHome)
                {   file.write("T" + numDisp+" ");
                    file.close();
                System.out.println(Main.rdp.getMarcadoActual()[0]+""+Main.rdp.getMarcadoActual()[11]);}

                else {
                    System.out.println(Main.rdp.getMarcadoActual()[0]+""+Main.rdp.getMarcadoActual()[11]);
                    file.write("T" + numDisp+" ");
                    System.out.println("T" + numDisp);
                    contador--;
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        esPrimera = false;
    }
}
