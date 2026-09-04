package com.mycompany.promptzal;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Diccionario {

    private static final Set<String> DIRECTIVAS = new HashSet<>(Arrays.asList("@modelo", "@rol", "@formato"));
    private static final Set<String> RESERVADAS = new HashSet<>(Arrays.asList("AGENTE", "contexto", "variable", "EJECUTAR", "EXPORTAR"));
    private static final Set<String> COMANDOS = new HashSet<>(Arrays.asList("PREGUNTAR", "GENERAR", "RESUMIR", "ANALIZAR", "TRADUCIR", "CLASIFICAR", "EXTRAER", "CODIFICAR"));
    private static final Set<String> FUNCIONES = new HashSet<>(Arrays.asList("CARGAR"));
    private static final Set<String> CONECTORES = new HashSet<>(Arrays.asList("SOBRE", "DESDE", "EN", "COMO"));

    public static String clasificar(String palabra) {
        if (DIRECTIVAS.contains(palabra)) return "Directiva";
        if (RESERVADAS.contains(palabra)) return "Palabra reservada";
        if (COMANDOS.contains(palabra)) return "Comando de IA";
        if (FUNCIONES.contains(palabra)) return "Funcion";
        if (CONECTORES.contains(palabra)) return "Conector";
        return null;
    }
}