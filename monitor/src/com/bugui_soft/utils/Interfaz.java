package com.bugui_soft.utils;

import javax.swing.*;

import static com.bugui_soft.Main.*;

public class Interfaz extends JFrame implements Runnable{
    private static final Object lock = new Object();
    private static Interfaz interfaz;

    
    //TODO: ELIMINAR LOS NEW PARA LOS QUE USEN NETBEANS
    private JPanel ventana = new JPanel();
    private JProgressBar barraInvariante1 = new JProgressBar();
    private JProgressBar barraInvariante2 = new JProgressBar();
    private JProgressBar barraInvariante3 = new JProgressBar();
    private JLabel Marcado = new JLabel();
    private JPanel panelMarcado = new JPanel();
    private JPanel panelTitulo = new JPanel();
    private JPanel panelInvariantes = new JPanel();
    private JPanel panelBarras = new JPanel();
    private JLabel Titulo = new JLabel();
    
    
    

    private Interfaz() { }

    public static Interfaz getInstanceOfInterfaz() {
        synchronized (lock) {
            if (interfaz == null) {
                interfaz = new Interfaz();

                
                //TODO: COMENTAR ESTO PARA LOS QUE USEN netBeans 
                interfaz.panelMarcado.add(interfaz.Marcado);

                interfaz.panelBarras.add(interfaz.barraInvariante1);
                interfaz.panelBarras.add(interfaz.barraInvariante2);
                interfaz.panelBarras.add(interfaz.barraInvariante3);


                interfaz.ventana.add(interfaz.panelMarcado);
                interfaz.ventana.add(interfaz.panelTitulo);
                interfaz.ventana.add(interfaz.panelInvariantes);
                interfaz.ventana.add(interfaz.panelBarras);
                
                //---------------------------

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

