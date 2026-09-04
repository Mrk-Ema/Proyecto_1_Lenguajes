package com.mycompany.promptzal;

import java.util.ArrayList;
import java.util.List;

public class Tokenizador {

    private List<Token> tokens = new ArrayList<>();
    private List<Token> errores = new ArrayList<>();
    private String entrada;
    private int posicion = 0;
    private int fila = 1;
    private int columna = 1;

    public void analizar(String contenido) {
        this.tokens.clear();
        this.errores.clear();
        this.posicion = 0;
        this.fila = 1;
        this.columna = 1;
        this.entrada = contenido;
        while (posicion < entrada.length()) {
            char caracterActual = entrada.charAt(posicion);
            if (Character.isWhitespace(caracterActual)) {
                avanzar();
            } else if (Character.isLetter(caracterActual) || caracterActual == '_') {
                leerPalabra();
            } else if (Character.isDigit(caracterActual)) {
                leerNumero();
            } else if (caracterActual == '@') {
                leerDirectiva();
            } else if (caracterActual == '"') {
                leerCadena();
            } else if (caracterActual == '=') {
                tokenUnCaracter("Operador asignacion");
            } else if (caracterActual == '+') {
                tokenUnCaracter("Operador concatenacion");
            } else if (caracterActual == '{' || caracterActual == '}' || caracterActual == '('
                    || caracterActual == ')' || caracterActual == ',' || caracterActual == ';') {
                tokenUnCaracter("Delimitador");
            } else if (caracterActual == '-' && posicion + 1 < entrada.length()
                    && entrada.charAt(posicion + 1) == '>') {
                tokens.add(new Token("->", "Conector", fila, columna));
                avanzar();
                avanzar();
            } else if (caracterActual == '/') {
                comentario();
            } else {
                errores.add(new Token(String.valueOf(caracterActual),
                        "Error lexico: caracter no reconocido", fila, columna));
                avanzar();
            }
        }
    }

    private void avanzar() {
        if (entrada.charAt(posicion) == '\n') {
            fila++;
            columna = 1;
        } else {
            columna++;
        }
        posicion++;
    }

    private void leerPalabra() {
        int f = fila, c = columna;
        StringBuilder sb = new StringBuilder();
        while (posicion < entrada.length()) {
            char ch = entrada.charAt(posicion);
            if (Character.isLetter(ch) || Character.isDigit(ch) || ch == '_') {
                sb.append(ch);
                avanzar();
            } else {
                break;
            }
        }
        String tipo = Diccionario.clasificar(sb.toString());
        tokens.add(new Token(sb.toString(), tipo == null ? "Identificador" : tipo, f, c));
    }

    private void leerNumero() {
        int f = fila, c = columna;
        StringBuilder sb = new StringBuilder();
        while (posicion < entrada.length() && Character.isDigit(entrada.charAt(posicion))) {
            sb.append(entrada.charAt(posicion));
            avanzar();
        }
        if (posicion + 1 < entrada.length() && entrada.charAt(posicion) == '.' && Character.isDigit(entrada.charAt(posicion + 1))) {
            sb.append('.');
            avanzar();
            while (posicion < entrada.length() && Character.isDigit(entrada.charAt(posicion))) {
                sb.append(entrada.charAt(posicion));
                avanzar();
            }
            tokens.add(new Token(sb.toString(), "Literal numerico decimal", f, c));
        } else {
            tokens.add(new Token(sb.toString(), "Literal numerico entero", f, c));
        }
    }

    private void leerDirectiva() {
        int f = fila, c = columna;
        StringBuilder sb = new StringBuilder();
        sb.append(entrada.charAt(posicion));
        avanzar();
        while (posicion < entrada.length() && Character.isLetter(entrada.charAt(posicion))) {
            sb.append(entrada.charAt(posicion));
            avanzar();
        }
        String tipo = Diccionario.clasificar(sb.toString());
        tokens.add(new Token(sb.toString(), tipo == null ? "Directiva" : tipo, f, c));
    }

    private void leerCadena() {
        int f = fila, c = columna;
        StringBuilder sb = new StringBuilder();
        sb.append(entrada.charAt(posicion));
        avanzar();
        boolean cerrada = false;
        while (posicion < entrada.length()) {
            char ch = entrada.charAt(posicion);
            if (ch == '"') {
                sb.append(ch);
                avanzar();
                cerrada = true;
                break;
            }
            if (ch == '\n') {
                break;
            }
            sb.append(ch);
            avanzar();
        }
        if (cerrada) {
            tokens.add(new Token(sb.toString(), "Literal cadena", f, c));
        } else {
            errores.add(new Token(sb.toString(), "Error lexico: cadena sin cerrar", f, c));
        }
    }

    private void comentario() {
        int f = fila, c = columna;
        if (posicion + 1 < entrada.length() && entrada.charAt(posicion + 1) == '/') {
            while (posicion < entrada.length() && entrada.charAt(posicion) != '\n') {
                avanzar();
            }
        } else if (posicion + 1 < entrada.length() && entrada.charAt(posicion + 1) == '*') {
            avanzar();
            avanzar();
            boolean cerrado = false;
            while (posicion + 1 < entrada.length()) {
                if (entrada.charAt(posicion) == '*' && entrada.charAt(posicion + 1) == '/') {
                    avanzar();
                    avanzar();
                    cerrado = true;
                    break;
                }
                avanzar();
            }
            if (!cerrado) {
                errores.add(new Token("/*", "Error lexico: comentario de bloque sin cerrar", f, c));
            }
        } else {
            errores.add(new Token("/", "Error lexico: caracter no reconocido", f, c));
            avanzar();
        }
    }

    private void tokenUnCaracter(String tipo) {
        tokens.add(new Token(String.valueOf(entrada.charAt(posicion)), tipo, fila, columna));
        avanzar();
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public List<Token> getErrores() {
        return errores;
    }
}