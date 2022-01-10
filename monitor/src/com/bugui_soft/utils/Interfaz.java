package com.bugui_soft.utils;

import javax.swing.*;

import static com.bugui_soft.Main.*;

public class Interfaz extends JFrame implements Runnable{
    private static final Object lock = new Object();
    private static Interfaz interfaz;

    private JPanel ventana;
    private JProgressBar barraInvariante1;
    private JProgressBar barraInvariante2;
    private JProgressBar barraInvariante3;
    private JLabel Marcado;
    private JPanel panelMarcado;
    private JPanel panelTitulo;
    private JPanel panelInvariantes;
    private JPanel panelBarras;
    private JLabel Titulo;

    private Interfaz() { }

    public static Interfaz getInstanceOfInterfaz() {
        synchronized (lock) {
            if (interfaz == null) {
                interfaz = new Interfaz();
                interfaz.setLayout(null);
                interfaz.setTitle("Buguisoft - TpFinal - Prog. Concurrente");
                interfaz.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                interfaz.setContentPane(interfaz.ventana);
                interfaz.setLocationRelativeTo(null);

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
        while (!Rdp.milInvariantes) {
            try {
                Integer[] invariantes = exchangerGUI.exchange(null);
                updateBarra(invariantes); //Actualiza el Fronted
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Para asegurar que no se violo ningun invariante, se muestra el marcado final
        String marcado = monitor.printMarcado();
        Marcado.setText("Marcado Final: " + marcado);
    }

    private void updateBarra(Integer[] invariantes){
        int porcentajeInvariante1 = (invariantes[0]*100)/Constantes.NUMERO_DE_TRANS_A_EJECUTAR;
        int porcentajeInvariante2 = (invariantes[1]*100)/Constantes.NUMERO_DE_TRANS_A_EJECUTAR;
        int porcentajeInvariante3 = (invariantes[2]*100)/Constantes.NUMERO_DE_TRANS_A_EJECUTAR;

        barraInvariante1.setValue(porcentajeInvariante1);
        barraInvariante2.setValue(porcentajeInvariante2);
        barraInvariante3.setValue(porcentajeInvariante3);
    }
}

