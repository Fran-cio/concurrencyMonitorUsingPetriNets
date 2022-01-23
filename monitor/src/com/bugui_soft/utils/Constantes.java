package com.bugui_soft.utils;

public final class Constantes {
    private Constantes() {
    }

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
    public static final Integer[] MARCADO_INICIAL = {3, 0, 1, 1, 0, 0, 2, 0, 0, 1, 0, 3, 0, 2, 0, 2, 2, 3};
    public static final Integer CANTIDAD_PLAZAS = 18;
    public static final Integer CANTIDAD_TRANSICIONES = 11;
    /*---------------------------------------Temporilizacion-----------------------------------------------------------------*/
    public static final Integer[] TRANS_TEMP_DE_INV_1 = {1, 10};
    public static final Integer[] TRANS_TEMP_DE_INV_2 = {3, 4, 5};
    public static final Integer[] TRANS_TEMP_DE_INV_3 = {7, 8, 9};
    /* Se coloca lo relacionado al tiempo de los invariantes en función de un tiempo máximo invariante 1, pero podría
     * hacerse con cualquier otro, esto es para mantener siempre un lineamiento en la conducta de la ejecución temporal
     */
    public static final Integer TIEMPO_MAXIMO_INVARIANTE_1 = 4;
    public static final Integer TIEMPO_MAXIMO_INVARIANTE_2 = (TIEMPO_MAXIMO_INVARIANTE_1 / 2) - 1;
    public static final Integer TIEMPO_MAXIMO_INVARIANTE_3 = TIEMPO_MAXIMO_INVARIANTE_1 / 2;
    public static final Integer ANCHO_DE_VENTANA = TIEMPO_MAXIMO_INVARIANTE_1 * 5;
    /*---------------------------------------Ejecución-----------------------------------------------------------------*/
    public static final Integer NUMERO_DE_TRANS_A_EJECUTAR = 1000;
    public static final Integer ERROR_EXIT_STATUS = -1;
}
