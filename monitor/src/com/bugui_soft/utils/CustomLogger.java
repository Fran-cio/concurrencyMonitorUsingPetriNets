package com.bugui_soft.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import com.bugui_soft.Main;

public class CustomLogger implements Runnable {
    private static Integer numDisp;
    private static Integer contador = 10;
    private Boolean esPrimera = true; //primera ejecución del run, cuando se llama del start
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
        if (!esPrimera) {
            try {
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

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        esPrimera = false;
    }

    public static void setNumDisparo(Integer numDisparo) {
        if (numDisparo == null)
            numDisp = 50;
        else
            numDisp = numDisparo;

    }

}
