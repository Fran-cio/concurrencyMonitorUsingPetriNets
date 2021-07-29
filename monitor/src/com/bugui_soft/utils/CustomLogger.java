package com.bugui_soft.utils;

import java.io.FileWriter;
import java.io.IOException;

public class CustomLogger implements Runnable {
    private static Integer numDisp;
    private static Integer contador = 1000;
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
                if (contador == 0)
                    file.close();
                else {
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
