package com.bugui_soft.utils;

import javax.swing.*;

import java.awt.*;

import static com.bugui_soft.Main.*;

public class Interfaz extends JFrame implements Runnable{
    private static final Object lock = new Object();
    private static Interfaz interfaz;

    private JPanel frame;
    private JProgressBar progressBar1;
    private JProgressBar progressBar2;
    private JProgressBar progressBar3;
    private JLabel Marcado;
    private JLabel Titulo;

    private Interfaz() { }

    public static Interfaz getInstanceOfInterfaz() {
        synchronized (lock) {
            if (interfaz == null) {
                Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
                interfaz = new Interfaz();
                interfaz.setLayout(null);
                interfaz.setTitle("Buguisoft - TpFinal - Prog. Concurrente");
                interfaz.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                interfaz.setContentPane(interfaz.frame);
                interfaz.setLocationRelativeTo(null);
                //interfaz.setSize(screenSize.height/4,screenSize.width/4);
                interfaz.pack();
                interfaz.setVisible(true);
            } else {
                System.out.println("Ya existe una instancia de interfaz, no se creará otra");
            }
            return interfaz;
        }
    }


    @Override
    public void run() {
        /*Va a correr hasta que se ejecuten 1000 transiciones*/
        Integer[] invariantes;
        while (!Rdp.milInvariantes) {
            try {
                invariantes = exchangerGUI.exchange(null);

                progressBar1.setValue((invariantes[0]*100)/Constantes.NUMERO_DE_TRANS_A_EJECUTAR);
                progressBar2.setValue((invariantes[1]*100)/Constantes.NUMERO_DE_TRANS_A_EJECUTAR);
                progressBar3.setValue((invariantes[2]*100)/Constantes.NUMERO_DE_TRANS_A_EJECUTAR);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String marcado = monitor.printMarcado();
        Marcado.setText("Marcado Final: " + marcado);
    }
}
