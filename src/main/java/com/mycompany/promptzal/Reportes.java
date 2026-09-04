package com.mycompany.promptzal;

import java.util.List;

public class Reportes {

    public static void imprimirConsola(List<Token> tokens) {
        System.out.printf("%-4s %-20s %-20s %-6s %-8s%n", "#", "Lexema", "Tipo", "Fila", "Columna");
        int n = 1;
        for (Token t : tokens) {
            System.out.printf("%-4d %-20s %-20s %-6d %-8d%n", n, t.getLexema(), t.getTipo(), t.getFila(), t.getColumna());
            n++;
        }
    }

    public static void imprimirErrores(List<Token> errores) {
        if (errores.isEmpty()) {
            System.out.println("\nERRORES LEXICOS: ninguno");
            return;
        }
        System.out.println("\nERRORES LEXICOS:");
        System.out.printf("%-4s %-20s %-30s %-6s %-8s%n", "#", "Lexema", "Tipo", "Fila", "Columna");
        int n = 1;
        for (Token e : errores) {
            System.out.printf("%-4d %-20s %-30s %-6d %-8d%n", n, e.getLexema(), e.getTipo(), e.getFila(), e.getColumna());
            n++;
        }
    }

    public static String generarHTML(List<Token> tokens, List<Token> errores, String nombreArchivo) {
        int totalErrores = errores.size();
        int identificadores = contar(tokens, "Identificador");
        int reservadas = contar(tokens, "Palabra reservada");
        int literales = contar(tokens, "Literal cadena") + contar(tokens, "Literal numerico entero") + contar(tokens, "Literal numerico decimal");

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        sb.append("<html lang=\"es\"><head><meta charset=\"UTF-8\">");
        sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        sb.append("<title>Reporte PromptZal</title><style>");
        sb.append("body{background:#121212;color:#e8e6e3;font-family:Arial,sans-serif;padding:25px}");
        sb.append("h1{font-size:22px}");
        sb.append("h2{font-size:16px;color:#c49a6c}");
        sb.append(".subt{color:#999;font-size:13px}");
        sb.append(".tab{border:1px solid #333;background:#1d1d1f;color:#e8e6e3;padding:8px 14px;cursor:pointer}");
        sb.append(".tab.activo{background:#c49a6c;color:#111;font-weight:bold}");
        sb.append(".seccion{display:none;border:1px solid #333;padding:15px}");
        sb.append(".seccion.activa{display:block}");
        sb.append(".tarjeta{background:#1d1d1f;border:1px solid #333;padding:12px;margin:0 10px 10px 0;display:inline-block;min-width:140px}");
        sb.append(".tarjeta .etiqueta{font-size:12px;color:#999}");
        sb.append(".tarjeta .valor{font-size:20px;font-weight:bold}");
        sb.append("table{width:100%;border-collapse:collapse;font-size:13px}");
        sb.append("th{background:#26262a;color:#c49a6c;text-align:left;padding:8px}");
        sb.append("td{padding:7px 8px;border-top:1px solid #333}");
        sb.append(".ok{color:#7cbd8c}.mal{color:#d98d8d}");
        sb.append("</style></head><body>");
        sb.append("<h1>Reporte PromptZal</h1>");
        sb.append("<div class=\"subt\">Analisis lexico de: ").append(escapar(nombreArchivo)).append("</div>");
        sb.append("<div class=\"tabs\">");
        sb.append("<button class=\"tab activo\" id=\"t1\" onclick=\"pestana(1)\">Tokens</button>");
        sb.append("<button class=\"tab\" id=\"t2\" onclick=\"pestana(2)\">Errores</button>");
        sb.append("</div><div>");
        sb.append("<div class=\"seccion activa\" id=\"s1\"><div class=\"tarjetas\">");
        sb.append(tarjeta("Total tokens", String.valueOf(tokens.size()), ""));
        sb.append(tarjeta("Identificadores", String.valueOf(identificadores), "ok"));
        sb.append(tarjeta("Reservadas", String.valueOf(reservadas), ""));
        sb.append(tarjeta("Literales", String.valueOf(literales), ""));
        sb.append(tarjeta("Errores", String.valueOf(totalErrores), totalErrores > 0 ? "mal" : "ok"));
        sb.append("</div><h2 style=\"margin:0 0 12px;font-size:17px\">Tokens encontrados</h2><table>");
        sb.append("<thead><tr><th>#</th><th>Lexema</th><th>Tipo</th><th>Fila</th><th>Columna</th></tr></thead><tbody>");
        int n = 1;
        for (Token t : tokens) {
            sb.append("<tr><td>").append(n).append("</td><td>").append(escapar(t.getLexema())).append("</td><td>").append(escapar(t.getTipo())).append("</td><td>").append(t.getFila()).append("</td><td>").append(t.getColumna()).append("</td></tr>");
            n++;
        }
        sb.append("</tbody></table></div>");
        sb.append("<div class=\"seccion\" id=\"s2\"><div class=\"tarjetas\">");
        sb.append(tarjeta("Total errores", String.valueOf(totalErrores), totalErrores > 0 ? "mal" : "ok"));
        sb.append("</div><h2 style=\"margin:0 0 12px;font-size:17px\">Errores lexicos</h2><table>");
        sb.append("<thead><tr><th>#</th><th>Lexema</th><th>Tipo</th><th>Fila</th><th>Columna</th></tr></thead><tbody>");
        if (errores.isEmpty()) {
            sb.append("<tr><td colspan=\"5\">Sin errores lexicos</td></tr>");
        } else {
            n = 1;
            for (Token e : errores) {
                sb.append("<tr><td>").append(n).append("</td><td>").append(escapar(e.getLexema())).append("</td><td>").append(escapar(e.getTipo())).append("</td><td>").append(e.getFila()).append("</td><td>").append(e.getColumna()).append("</td></tr>");
                n++;
            }
        }
        sb.append("</tbody></table></div>");
        sb.append("</div>");
        sb.append("<script>function pestana(n){for(let i=1;i<=2;i++){document.getElementById('s'+i).className='seccion'+(i===n?' activa':'');document.getElementById('t'+i).className='tab'+(i===n?' activo':'');}}</script>");
        sb.append("</body></html>");
        return sb.toString();
    }

    private static int contar(List<Token> tokens, String tipo) {
        int c = 0;
        for (Token t : tokens) {
            if (t.getTipo().equals(tipo)) {
                c++;
            }
        }
        return c;
    }

    private static String tarjeta(String etiqueta, String valor, String clase) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tarjeta\"><div class=\"etiqueta\">").append(etiqueta).append("</div><div class=\"valor\">");
        if (clase.isEmpty()) {
            sb.append(valor);
        } else {
            sb.append("<span class=\"").append(clase).append("\">").append(valor).append("</span>");
        }
        sb.append("</div></div>");
        return sb.toString();
    }

    private static String escapar(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}