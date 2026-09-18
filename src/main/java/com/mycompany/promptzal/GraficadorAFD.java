package com.mycompany.promptzal;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class GraficadorAFD {

    public static String generarDOT() {
        StringBuilder sb = new StringBuilder();

        // ===== Encabezado: configuracion del grafo y estado inicial q0 =====
        sb.append("digraph AFD_PromptZal {\n");
        sb.append("  rankdir=LR;\n");
        sb.append("  node [shape=circle, fontname=\"Arial\", fontsize=11];\n");
        sb.append("  edge [fontname=\"Arial\", fontsize=10];\n");
        sb.append("  \"\" [shape=point];\n");
        sb.append("  \"\" -> q0;\n");
        sb.append("  q0 [label=\"q0\\ninicio\"];\n");

        // ===== Bloque 1: PALABRA (q1) = letra | _ -> Identificador, Reservada,
        //        Comando, Funcion, Conector (todas inician con letra o _) =====
        sb.append("  q0 -> q1 [label=\"letra | _\"];\n");
        sb.append("  q1 [shape=doublecircle, label=\"q1\\nPalabra\"];\n");
        sb.append("  q1 -> q1 [label=\"letra | digito | _\"];\n");

        // ===== Bloque 2: NUMERO (q2 entero / q3 decimal) = digito ; digito '.' digito =====
        sb.append("  q0 -> q2 [label=\"digito\"];\n");
        sb.append("  q2 [shape=doublecircle, label=\"q2\\nEntero\"];\n");
        sb.append("  q2 -> q2 [label=\"digito\"];\n");
        sb.append("  q2 -> q3 [label=\". con digito\"];\n");
        sb.append("  q3 [shape=doublecircle, label=\"q3\\nDecimal\"];\n");
        sb.append("  q3 -> q3 [label=\"digito\"];\n");

        // ===== Bloque 3: DIRECTIVA (q4) = @ + letras (@modelo, @rol, @formato) =====
        sb.append("  q0 -> q4 [label=\"@\"];\n");
        sb.append("  q4 [shape=doublecircle, label=\"q4\\nDirectiva\"];\n");
        sb.append("  q4 -> q4 [label=\"letra\"];\n");

        // ===== Bloque 4: CADENA (q5 abierta / q6 cerrada) = \" ... \" =====
        sb.append("  q0 -> q5 [label=\"\\\"\"];\n");
        sb.append("  q5 [label=\"q5\\ncadena\\nabierta\"];\n");
        sb.append("  q5 -> q5 [label=\"caracter (no \\\" ni salto)\"];\n");
        sb.append("  q5 -> q6 [label=\"\\\"\"];\n");
        sb.append("  q6 [shape=doublecircle, label=\"q6\\nCadena\"];\n");
        sb.append("  q5 -> q15 [label=\"salto de linea\"];\n");

        // ===== Bloque 5: OPERADORES / DELIMITADORES de 1 caracter =====
        sb.append("  q0 -> q7 [label=\"=\"];\n");
        sb.append("  q7 [shape=doublecircle, label=\"q7\\nasignacion\"];\n");
        sb.append("  q0 -> q8 [label=\"+\"];\n");
        sb.append("  q8 [shape=doublecircle, label=\"q8\\nconcatenacion\"];\n");
        sb.append("  q0 -> q9 [label=\"{ } ( ) , ;\"];\n");
        sb.append("  q9 [shape=doublecircle, label=\"q9\\nDelimitador\"];\n");

        // ===== Bloque 6: CONECTOR (q10 guion / q11 conector) = - seguido de > =====
        sb.append("  q0 -> q10 [label=\"-\"];\n");
        sb.append("  q10 [label=\"q10\\nguion leido\"];\n");
        sb.append("  q10 -> q11 [label=\">\"];\n");
        sb.append("  q11 [shape=doublecircle, label=\"q11\\nConector (->)\"];\n");
        sb.append("  q10 -> q15 [label=\"otro\"];\n");

        // ===== Bloque 7: COMENTARIOS (q12 linea / q13-q14 bloque) = // y /* */ (se ignoran) =====
        sb.append("  q0 -> q12 [label=\"//\"];\n");
        sb.append("  q12 [label=\"q12\\ncomentario\\nlinea\"];\n");
        sb.append("  q12 -> q12 [label=\"caracter\"];\n");
        sb.append("  q12 -> q0 [label=\"salto de linea\", style=dashed, constraint=false];\n");
        sb.append("  q0 -> q13 [label=\"/*\"];\n");
        sb.append("  q13 [label=\"q13\\ncomentario\\nbloque\"];\n");
        sb.append("  q13 -> q13 [label=\"caracter (no */)\"];\n");
        sb.append("  q13 -> q14 [label=\"*/\"];\n");
        sb.append("  q14 [label=\"q14\\nbloque\\ncerrado\"];\n");
        sb.append("  q14 -> q0 [style=dashed, constraint=false];\n");

        // ===== Bloque 8: ERROR (q15) = caracter invalido ; recuperacion: descarta y continua =====
        sb.append("  q0 -> q15 [label=\"caracter invalido\"];\n");
        sb.append("  q15 [shape=doublecircle, style=dashed, color=\"#b00020\", fontcolor=\"#b00020\", label=\"q15\\nError\"];\n");
        sb.append("  q15 -> q0 [label=\"reanuda\", style=dashed, constraint=false];\n");

        // ===== Leyenda: palabras del lenguaje (dinamica segun Diccionario) =====
        sb.append("  subgraph cluster_leyenda {\n");
        sb.append("    label=\"Palabras del lenguaje\";\n");
        sb.append("    fontsize=11;\n");
        sb.append("    style=filled;\n");
        sb.append("    color=\"#c9c9c9\";\n");
        sb.append("    fillcolor=\"#f4f4f4\";\n");
        sb.append("    leyenda [shape=box, style=\"rounded\", fontname=\"Arial\", fontsize=10, label=\"");
        sb.append("DIRECTIVAS: ").append(String.join(", ", Diccionario.getDirectivas())).append("\\n");
        sb.append("RESERVADAS: ").append(String.join(", ", Diccionario.getReservadas())).append("\\n");
        sb.append("COMANDOS: ").append(String.join(", ", Diccionario.getComandos())).append("\\n");
        sb.append("FUNCIONES: ").append(String.join(", ", Diccionario.getFunciones())).append("\\n");
        sb.append("CONECTORES: ").append(String.join(", ", Diccionario.getConectores()));
        sb.append("\"];\n");
        sb.append("  }\n");

        // ===== Fin del grafo =====
        sb.append("}\n");
        return sb.toString();
    }

    public static String graficar(File archivoPng) throws IOException, InterruptedException {
        File carpeta = archivoPng.getParentFile();
        if (carpeta != null && !carpeta.exists()) {
            carpeta.mkdirs();
        }
        String base = archivoPng.getName().replaceAll("(?i)\\.png$", "");
        File archivoDot = new File(carpeta, base + ".dot");

        Files.write(archivoDot.toPath(), generarDOT().getBytes(StandardCharsets.UTF_8));

        ProcessBuilder pb = new ProcessBuilder("dot", "-Tpng",
                archivoDot.getAbsolutePath(), "-o", archivoPng.getAbsolutePath());
        Process proceso = pb.start();
        int codigo = proceso.waitFor();
        if (codigo != 0) {
            throw new IOException("Graphviz (dot) termino con error. Asegurese de que este instalado.");
        }
        return archivoPng.getAbsolutePath();
    }
}