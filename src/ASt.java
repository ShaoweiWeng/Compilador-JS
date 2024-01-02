import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;

public class ASt {
    private AFD AL;
    Stack<String> pila;
    private String charLeido;
    private HashMap<String, HashMap<String, String[]>> tablaDescendente;
    private final String j1[] = { "1", ";", "E", "=" }; // Todos los elementos que hay que insertar en la pila y el ID
                                                        // de la regla
    private final String j2[] = { "2", ";", ")", "L", "(" };
    private final String j3[] = { "3", ";", "E", "*=" };
    // S
    private final String s1[] = { "4", "J", "id" };
    private final String s2[] = { "5", ";", "E", "put" };
    private final String s3[] = { "6", ";", "id", "get" };
    private final String s4[] = { "7", ";", "X", "return" };
    private final String s5[] = { "8", ";", "break" };
    // E
    private final String e1[] = { "9", "N", "R" };
    // N
    private final String n1[] = { "10", "N", "R", "||" };
    private final String n2[] = { "11" };
    // R
    private final String r1[] = { "12", "M", "U" };
    // M
    private final String m1[] = { "13", "M", "U", "&&" };
    private final String m2[] = { "14" };
    // U
    private final String u1[] = { "15", "O", "D" };
    // O
    private final String o1[] = { "16", "O", "D", "==" };
    private final String o2[] = { "17", "O", "D", "!=" };
    private final String o3[] = { "18" };
    // D
    private final String d1[] = { "19", "W", "V" };
    // W
    private final String w1[] = { "20", "W", "V", "+" };
    private final String w2[] = { "21", "W", "V", "-" };
    private final String w3[] = { "22" };
    // V
    private final String v1[] = { "23", "Y", "id" };
    // Y
    private final String y1[] = { "24", ")", "L", "(" };
    private final String y2[] = { "25" };
    // V (otra vez)
    private final String v2[] = { "26", ")", "E", "(" };
    private final String v3[] = { "27", "ent" };
    private final String v4[] = { "28", "cad" };
    private final String v5[] = { "29", "false" };
    private final String v6[] = { "30", "true" };
    // L
    private final String l1[] = { "31", "Q", "E" };
    private final String l2[] = { "32" };
    // Q
    private final String q1[] = { "33", "Q", "E", "," };
    private final String q2[] = { "34" };
    // X
    private final String x1[] = { "35", "E" };
    private final String x2[] = { "36" };
    // B
    private final String b1[] = { "37", ";", "T", "id", "let" };
    private final String b2[] = { "38", "S", ")", "E", "(", "if" };
    private final String b3[] = { "39", "S" };
    private final String b4[] = { "40", "}", "G", "{", ")", "E", "(", "switch" };
    // G
    private final String g1[] = { "41", "G", "C", ":", "ent", "case" };
    private final String g2[] = { "42", "C", ":", "default" };
    private final String g3[] = { "43" };
    // T
    private final String t1[] = { "44", "int" };
    private final String t2[] = { "45", "boolean" };
    private final String t3[] = { "46", "string" };
    // F
    private final String f1[] = { "47", "}", "C", "{", ")", "A", "(", "H", "id", "function" };
    // H
    private final String h1[] = { "48", "T" };
    private final String h2[] = { "49", "void" };
    // A
    private final String a1[] = { "50", "K", "id", "T" };
    private final String a2[] = { "51", "void" };
    // K
    private final String k1[] = { "52", "K", "id", "T", "," };
    private final String k2[] = { "53" };
    // C
    private final String c1[] = { "54", "C", "B" };
    private final String c2[] = { "55" };
    // P
    private final String p1[] = { "56", "P", "B" };
    private final String p2[] = { "57", "P", "F" };
    private final String p3[] = { "58" };

    public ASt(AFD AL) {
        this.AL = AL;
        this.pila = new Stack<>();
        this.tablaDescendente = new HashMap<>();
        pila.push("EOF");
        pila.push("P");

        for (char letra = 'A'; letra <= 'Y'; letra++)
            tablaDescendente.put(String.valueOf(letra), new HashMap<String, String[]>());
        // a
        tablaDescendente.get("A").put("boolean", a1);
        tablaDescendente.get("A").put("int", a1);
        tablaDescendente.get("A").put("string", a1);
        tablaDescendente.get("A").put("void", a2);
        // b
        tablaDescendente.get("B").put("break", b3);
        tablaDescendente.get("B").put("get", b3);
        tablaDescendente.get("B").put("put", b3);
        tablaDescendente.get("B").put("return", b3);
        tablaDescendente.get("B").put("id", b3);
        tablaDescendente.get("B").put("if", b2);
        tablaDescendente.get("B").put("let", b1);
        tablaDescendente.get("B").put("switch", b4);
        // c
        tablaDescendente.get("C").put("break", c1);
        tablaDescendente.get("C").put("case", c2);
        tablaDescendente.get("C").put("get", c1);
        tablaDescendente.get("C").put("if", c1);
        tablaDescendente.get("C").put("let", c1);
        tablaDescendente.get("C").put("put", c1);
        tablaDescendente.get("C").put("return", c1);
        tablaDescendente.get("C").put("switch", c1);
        tablaDescendente.get("C").put("id", c1);
        tablaDescendente.get("C").put("default", c2);
        tablaDescendente.get("C").put("}", c2);
        // d
        tablaDescendente.get("D").put("ent", d1);
        tablaDescendente.get("D").put("cad", d1);
        tablaDescendente.get("D").put("id", d1);
        tablaDescendente.get("D").put("(", d1);
        tablaDescendente.get("D").put("true", d1);
        tablaDescendente.get("D").put("false", d1);
        // e
        tablaDescendente.get("E").put("ent", e1);
        tablaDescendente.get("E").put("cad", e1);
        tablaDescendente.get("E").put("id", e1);
        tablaDescendente.get("E").put("(", e1);
        tablaDescendente.get("E").put("true", e1);
        tablaDescendente.get("E").put("false", e1);
        // f
        tablaDescendente.get("F").put("function", f1);
        // g
        tablaDescendente.get("G").put("case", g1);
        tablaDescendente.get("G").put("default", g2);
        tablaDescendente.get("G").put("}", g3);
        // h
        tablaDescendente.get("H").put("boolean", h1);
        tablaDescendente.get("H").put("int", h1);
        tablaDescendente.get("H").put("void", h2);
        tablaDescendente.get("H").put("string", h1);
        // j
        tablaDescendente.get("J").put("*=", j3);
        tablaDescendente.get("J").put("=", j1);
        tablaDescendente.get("J").put("(", j2);
        // k
        tablaDescendente.get("K").put(",", k1);
        tablaDescendente.get("K").put(")", k2);
        // l
        tablaDescendente.get("L").put("ent", l1);
        tablaDescendente.get("L").put("cad", l1);
        tablaDescendente.get("L").put("id", l1);
        tablaDescendente.get("L").put("(", l1);
        tablaDescendente.get("L").put(")", l2);
        tablaDescendente.get("L").put("true", l1);
        tablaDescendente.get("L").put("false", l1);
        // m
        tablaDescendente.get("M").put(",", m2);
        tablaDescendente.get("M").put(";", m2);
        tablaDescendente.get("M").put(")", m2);
        tablaDescendente.get("M").put("||", m2);
        tablaDescendente.get("M").put("&&", m1);
        // n
        tablaDescendente.get("N").put(",", n2);
        tablaDescendente.get("N").put(")", n2);
        tablaDescendente.get("N").put(";", n2);
        tablaDescendente.get("N").put("||", n1);
        // o
        tablaDescendente.get("O").put(",", o3);
        tablaDescendente.get("O").put(";", o3);
        tablaDescendente.get("O").put(")", o3);
        tablaDescendente.get("O").put("&&", o3);
        tablaDescendente.get("O").put("||", o3);
        tablaDescendente.get("O").put("!=", o2);
        tablaDescendente.get("O").put("==", o1);
        // p
        tablaDescendente.get("P").put("break", p1);
        tablaDescendente.get("P").put("function", p2);
        tablaDescendente.get("P").put("get", p1);
        tablaDescendente.get("P").put("if", p1);
        tablaDescendente.get("P").put("let", p1);
        tablaDescendente.get("P").put("put", p1);
        tablaDescendente.get("P").put("return", p1);
        tablaDescendente.get("P").put("switch", p1);
        tablaDescendente.get("P").put("id", p1);
        tablaDescendente.get("P").put("EOF", p3);
        // q
        tablaDescendente.get("Q").put(",", q1);
        tablaDescendente.get("Q").put(")", q2);
        // r
        tablaDescendente.get("R").put("ent", r1);
        tablaDescendente.get("R").put("cad", r1);
        tablaDescendente.get("R").put("id", r1);
        tablaDescendente.get("R").put("(", r1);
        tablaDescendente.get("R").put("true", r1);
        tablaDescendente.get("R").put("false", r1);
        // s
        tablaDescendente.get("S").put("break", s5);
        tablaDescendente.get("S").put("get", s3);
        tablaDescendente.get("S").put("put", s2);
        tablaDescendente.get("S").put("return", s4);
        tablaDescendente.get("S").put("id", s1);
        // t
        tablaDescendente.get("T").put("boolean", t2);
        tablaDescendente.get("T").put("int", t1);
        tablaDescendente.get("T").put("string", t3);
        // u
        tablaDescendente.get("U").put("ent", u1);
        tablaDescendente.get("U").put("cad", u1);
        tablaDescendente.get("U").put("id", u1);
        tablaDescendente.get("U").put("(", u1);
        tablaDescendente.get("U").put("true", u1);
        tablaDescendente.get("U").put("false", u1);
        // v
        tablaDescendente.get("V").put("ent", v3);
        tablaDescendente.get("V").put("cad", v4);
        tablaDescendente.get("V").put("id", v1);
        tablaDescendente.get("V").put("(", v2);
        tablaDescendente.get("V").put("true", v6);
        tablaDescendente.get("V").put("false", v5);
        // w
        tablaDescendente.get("W").put(",", w3);
        tablaDescendente.get("W").put(";", w3);
        tablaDescendente.get("W").put(")", w3);
        tablaDescendente.get("W").put("+", w1);
        tablaDescendente.get("W").put("-", w2);
        tablaDescendente.get("W").put("&&", w3);
        tablaDescendente.get("W").put("||", w3);
        tablaDescendente.get("W").put("!=", w3);
        tablaDescendente.get("W").put("==", w3);
        // x
        tablaDescendente.get("X").put("ent", x1);
        tablaDescendente.get("X").put("cad", x1);
        tablaDescendente.get("X").put("id", x1);
        tablaDescendente.get("X").put(";", x2);
        tablaDescendente.get("X").put("(", x1);
        tablaDescendente.get("X").put("true", x1);
        tablaDescendente.get("X").put("false", x1);
        // y
        tablaDescendente.get("Y").put(",", y2);
        tablaDescendente.get("Y").put(";", y2);
        tablaDescendente.get("Y").put("(", y1);
        tablaDescendente.get("Y").put(")", y2);
        tablaDescendente.get("Y").put("+", y2);
        tablaDescendente.get("Y").put("-", y2);
        tablaDescendente.get("Y").put("&&", y2);
        tablaDescendente.get("Y").put("||", y2);
        tablaDescendente.get("Y").put("!=", y2);
        tablaDescendente.get("Y").put("==", y2);
    }

    public void analisis(File file) {
        try (FileReader archivo = new FileReader(file)) {
            String sigTok = lector(archivo);
            String[] listPila;
            while (!pila.peek().equals("EOF")) {
                if (!tablaDescendente.containsKey(pila.peek())) {
                    // Tratamiento de Terminal
                    if (sigTok.equals(pila.peek())) {
                        pila.pop();
                        sigTok = lector(archivo);
                    } else {
                        System.err.println("ERROR SINTACTICO EN LA LINEA: " + AL.nLineas + " no se esperaba: " + sigTok);
                        pila.pop();
                    }
                //else if (accion semantica)
                } else { // Tratamiento de No Terminal
                    if ((listPila = tablaDescendente.get(pila.peek()).get(sigTok)) != null) {
                        pila.pop();
                        Escribir.parse(listPila[0]);
                        for (int i = 1; i < listPila.length; i++) {
                            pila.push(listPila[i]);
                        }
                    } else {
                        System.err.println("ERROR SINTACTICO EN LA LINEA: " + AL.nLineas + " no se esperaba: " + sigTok);
                        pila.pop();
                        if (pila.peek().equals("EOF")) {
                            pila.push("P");
                            sigTok = lector(archivo);
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Error de lectura: " + e.getMessage());
        }
    }

    private String lector(FileReader archivo) throws IOException {
        int aux;
        String token;
        if (AL.reciclar && (token = AL.transicion(this.charLeido)) != null) {
            AL.reciclar = false;
            return token;
        } else {
            AL.reciclar = false; // asegurar que despues del reciclaje se ponga a falso y no haya reciclado doble
            while ((aux = archivo.read()) != -1) {
                String caracter = String.valueOf((char) aux);
                // Si el caracter es una nueva línea, incrementa el contador de líneas
                if (caracter.equals("\n"))
                    AL.nLineas++;

                if ((token = AL.transicion(caracter)) != null) {
                    this.charLeido = caracter;
                    return token;
                }
            }
        }
        Escribir.genToken(Escribir.EOF); // Genera token fin de fichero
        return "EOF";
    }
}