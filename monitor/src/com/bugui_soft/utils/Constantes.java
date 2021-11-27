package com.bugui_soft.utils;

public final class Constantes {
    private Constantes() {}

    public static final String TECNICO = "com.bugui_soft.Operario.Tecnico"; //HILOS VERDES
    public static final String PRODUCTOR = "com.bugui_soft.Operario.Productor"; //HILOS AMARILLOS
    public static final String DESCARTADOR = "Descartador"; //HILOS ROJOS
    public static final String CALIDAD = "Operario.Calidad"; //HILOS CELESTES
    public static final Integer CANTIDAD_OPERARIOS = 4;
    public static final Integer CANTIDAD_TECNICOS = 3;
    public static final Integer CANTIDAD_PRODUCTORES = 3;
    public static final Integer CANTIDAD_DESCARTADORES = 1;
    public static final Integer CANTIDAD_CALIDAD = 1;
    public static final Integer CANTIDAD_PLAZAS = 18;
    public static final Integer CANTIDAD_TRANSICIONES = 11;
    public static final Integer CONTADOR_DEL_RPOGRAMA = 10;
    public static final Integer SLEEP_PRODUCTOR_MS = 5;
    public static final Integer SLEEP_TECNICO_MS = 5;
    public static final Integer SLEEP_CALIDAD_MS = 5;
    public static final Integer SLEEP_DESCARTADOR_MS = 5;
}
