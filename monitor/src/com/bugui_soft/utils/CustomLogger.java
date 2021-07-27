package com.bugui_soft.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomLogger implements Runnable {
    private static Integer numDisp;
    private static Integer contador;
    private PrintWriter pw;

    public CustomLogger() {
        try {
            System.out.println("Entrando al constructor del logger");
            FileWriter file = new FileWriter("Data/Log.txt");
            pw = new PrintWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        pw.println("T" + numDisp);
        System.out.println("T" + numDisp);
    }

    public static void  setNumDisparo(Integer numDisparo) {
        if (numDisparo == null)
            numDisp = 50;
        else
            numDisp = numDisparo;

    }
}
