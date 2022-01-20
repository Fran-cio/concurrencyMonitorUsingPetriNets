package com.bugui_soft.utils;

public final class Constantes {
    private Constantes() {}
    /*---------------------------------------Operarios-----------------------------------------------------------------*/
    public static final String TECNICO = "Operario.Tecnico"; //HILOS VERDES
    public static final String PRODUCTOR = "Operario.Productor"; //HILOS AMARILLOS
    public static final String DESCARTADOR = "Operario.Descartador"; //HILOS ROJOS
    public static final String CALIDAD = "Operario.Calidad"; //HILOS CELESTES
    public static final Integer CANTIDAD_TECNICOS = 3;
    public static final Integer CANTIDAD_PRODUCTORES = 3;
    public static final Integer SLEEP_PRODUCTOR_MS = 5;
    public static final Integer SLEEP_TECNICO_MS = 5;
    public static final Integer SLEEP_CALIDAD_MS = 5;
    public static final Integer SLEEP_DESCARTADOR_MS = 5;
    /*---------------------------------------RedDePetri-----------------------------------------------------------------*/
    public static final Integer[] MARCADO_INICIAL= {3, 0, 1, 1, 0, 0, 2, 0, 0, 1, 0, 3, 0, 2, 0, 2, 2, 3};
    public static final Integer CANTIDAD_PLAZAS = 18;
    public static final Integer CANTIDAD_TRANSICIONES = 11;
    /*---------------------------------------Temporilizacion-----------------------------------------------------------------*/
    public static final Integer[] INV_1={1,10};   //! TODO: nombre muy confuso no es un invariante en si, es un auxiliar!
    public static final Integer[] INV_2={3,4,5}; //TODO: se saco Saco T0, T2* y T6* porque el profe lo recomendo (?) no lo termine de captar pero funciono like, despues expliquenmelo thx by juli
    public static final Integer[] INV_3={7,8,9}; 
    public static final Integer REFERENCIA_INVARIANTE_1 = 2;
    public static final Integer REFERENCIA_INVARIANTE_2 = 2;//TODO: nombre confuso , cambiarlo
    public static final Integer REFERENCIA_INVARIANTE_3 = 2;
    public static final Integer ANCHO_DE_VENTANA = 4;
    /*---------------------------------------Ejecución-----------------------------------------------------------------*/
    public static final Integer NUMERO_DE_TRANS_A_EJECUTAR=1000;
    public static final Integer ERROR_EXIT_STATUS = -1;
}
