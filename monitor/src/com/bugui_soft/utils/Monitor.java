package com.bugui_soft.utils;

import java.util.concurrent.*;
public class Monitor {
    boolean disponible;
    private Politicas politica;
    private Semaphore mutex; // "cola" de entrada al monitor
    private Semaphore[] colasCondition; // "cola" de condición 1
   //...

    public Monitor(){
        mutex= new Semaphore(1);
        colasCondition = new Semaphore[11];

    }
    public synchronized void dispararTransicion(){
      // intenta tomar el mutex si esta ocupado se va a dormir hasta que se desocupe
       try {
           mutex.acquire();
       }catch (InterruptedException e){
           System.out.println("Se Interrumpió en desparar transición: ");
       }
       //...

    }
}
