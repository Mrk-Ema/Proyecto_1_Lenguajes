package com.mycompany.promptzal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Diccionario {

    private static final Set<String> DIRECTIVAS = new HashSet<>(Arrays.asList("@modelo", "@rol", "@formato"));
    private static final Set<String> RESERVADAS = new HashSet<>(Arrays.asList("AGENTE", "contexto", "variable", "EJECUTAR", "EXPORTAR"));
    private static final Set<String> COMANDOS = new HashSet<>(Arrays.asList("PREGUNTAR", "GENERAR", "RESUMIR", "ANALIZAR", "TRADUCIR", "CLASIFICAR", "EXTRAER", "CODIFICAR"));
    private static final Set<String> FUNCIONES = new HashSet<>(Arrays.asList("CARGAR"));
    private static final Set<String> CONECTORES = new HashSet<>(Arrays.asList("SOBRE", "DESDE", "EN", "COMO"));

    public static List<String> getDirectivas() {
        List<String> lista = new ArrayList<>(DIRECTIVAS);
        java.util.Collections.sort(lista);
        return lista;
    }

    public static List<String> getReservadas() {
        List<String> lista = new ArrayList<>(RESERVADAS);
        java.util.Collections.sort(lista);
        return lista;
    }

    public static List<String> getComandos() {
        List<String> lista = new ArrayList<>(COMANDOS);
        java.util.Collections.sort(lista);
        return lista;
    }

    public static List<String> getFunciones() {
        List<String> lista = new ArrayList<>(FUNCIONES);
        java.util.Collections.sort(lista);
        return lista;
    }

    public static List<String> getConectores() {
        List<String> lista = new ArrayList<>(CONECTORES);
        java.util.Collections.sort(lista);
        return lista;
    }

    public static String clasificar(String palabra) {
        if (DIRECTIVAS.contains(palabra)) return "Directiva";
        if (RESERVADAS.contains(palabra)) return "Palabra reservada";
        if (COMANDOS.contains(palabra)) return "Comando de IA";
        if (FUNCIONES.contains(palabra)) return "Funcion";
        if (CONECTORES.contains(palabra)) return "Conector";
        return null;
    }
}