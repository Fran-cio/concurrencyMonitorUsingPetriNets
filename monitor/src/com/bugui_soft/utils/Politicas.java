package com.bugui_soft.utils;

public class Politicas {

    public int decidir(int[] transiciones){
       int val= (int) ((Math.random()*10) %transiciones.length); //ACA LA RE FLASHE VER AH
       return val;
    }
}
