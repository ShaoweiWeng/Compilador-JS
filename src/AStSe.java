import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;

public class AStSe {

    private static final String OK = "tipo_ok";
    private static final String ERROR = "tipo_error";
    private static final String ENT = "entero";
    private static final String CAD = "cadena";
    private static final String LOG = "logico";
    private static final String FUNC = "funcion";
    private static final String VOID = "vacio";

    private Integer numParam, TSId = 0;

    private AFD AL;
    Stack<Pair<String, Atributos>> pila;
    Stack<Pair<String, Atributos>> pilaAux;
    private String charLeido;
    private HashMap<String, HashMap<String, String[]>> tablaDescendente;
    private HashMap<String, Integer> accionesSem;
    private final String j1[] = { "1", "{1}", ";", "E", "=" }; // Todos los elementos que hay que insertar en la pila y
                                                               // el ID
    // de la regla
    private final String j2[] = { "2", "{2}", ";", ")", "L", "(" };
    private final String j3[] = { "3", "{3}", ";", "E", "*=" };
    // S
    private final String s1[] = { "4", "{4}", "J", "id" };
    private final String s2[] = { "5", "{5}", ";", "E", "put" };
    private final String s3[] = { "6", "{6}", ";", "id", "get" };
    private final String s4[] = { "7", "{7}", ";", "X", "return" };
    private final String s5[] = { "8", "{8}", ";", "break" };
    // E
    private final String e1[] = { "9", "{9}", "N", "R" };
    // N
    private final String n1[] = { "10", "{10}", "N", "R", "||" };
    private final String n2[] = { "11", "{11}" };
    // R
    private final String r1[] = { "12", "{12}", "M", "U" };
    // M
    private final String m1[] = { "13", "{13}", "M", "U", "&&" };
    private final String m2[] = { "14", "{14}" };
    // U
    private final String u1[] = { "15", "{15}", "O", "D" };
    // O
    private final String o1[] = { "16", "{16}", "O", "D", "==" };
    private final String o2[] = { "17", "{17}", "O", "D", "!=" };
    private final String o3[] = { "18", "{18}" };
    // D
    private final String d1[] = { "19", "{19}", "W", "V" };
    // W
    private final String w1[] = { "20", "{20}", "W", "V", "+" };
    private final String w2[] = { "21", "{21}", "W", "V", "-" };
    private final String w3[] = { "22", "{22}" };
    // V
    private final String v1[] = { "23", "{23}", "Y", "id" };
    // Y
    private final String y1[] = { "24", "{24}", ")", "L", "(" };
    private final String y2[] = { "25", "{25}" };
    // V (otra vez)
    private final String v2[] = { "26", "{26}", ")", "E", "(" };
    private final String v3[] = { "27", "{27}", "ent" };
    private final String v4[] = { "28", "{28}", "cad" };
    private final String v5[] = { "29", "{29}", "false" };
    private final String v6[] = { "30", "{30}", "true" };
    // L
    private final String l1[] = { "31", "{31}", "Q", "E" };
    private final String l2[] = { "32", "{32}" };
    // Q
    private final String q1[] = { "33", "{33}", "Q", "E", "," };
    private final String q2[] = { "34", "{34}" };
    // X
    private final String x1[] = { "35", "{35}", "E" };
    private final String x2[] = { "36", "{36}" };
    // B
    private final String b1[] = { "37", "{38}", ";", "T", "id", "{37}", "let" };
    private final String b2[] = { "38", "{39}", "S", ")", "E", "(", "if" };
    private final String b3[] = { "39", "{40}", "S" };
    private final String b4[] = { "40", "{41}", "}", "G", "{", ")", "E", "(", "switch" };
    // G
    private final String g1[] = { "41", "{42}", "G", "C", ":", "ent", "case" };
    private final String g2[] = { "42", "{43}", "C", ":", "default" };
    private final String g3[] = { "43", "{44}" };
    // T
    private final String t1[] = { "44", "{45}", "int" };
    private final String t2[] = { "45", "{46}", "boolean" };
    private final String t3[] = { "46", "{47}", "string" };
    // F
    private final String f1[] = { "47", "{51}", "}", "C", "{", "{50}", ")", "A", "(", "{49}", "H", "id", "{48}",
            "function" };
    // H
    private final String h1[] = { "48", "{52}", "T" };
    private final String h2[] = { "49", "{53}", "void" };
    // A
    private final String a1[] = { "50", "{54}", "K", "id", "T" };
    private final String a2[] = { "51", "{55}", "void" };
    // K
    private final String k1[] = { "52", "{56}", "K", "id", "T", "," };
    private final String k2[] = { "53", "{57}" };
    // C
    private final String c1[] = { "54", "{58}", "C", "B" };
    private final String c2[] = { "55", "{59}" };
    // P
    private final String p1[] = { "56", "{60}", "P", "B" };
    private final String p2[] = { "57", "{60}", "P", "F" };
    private final String p3[] = { "58" };

    // I gramatica aumentada
    private final String i1[] = { "0", "{62}", "P", "{61}" };

    public AStSe(AFD AL) {
        this.AL = AL;
        this.pila = new Stack<>();
        this.tablaDescendente = new HashMap<>();
        pila.push(new Pair<String, Atributos>("EOF", new Atributos()));
        pila.push(new Pair<String, Atributos>("I", new Atributos()));
        this.numParam = 0;

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
        // i
        tablaDescendente.get("I").put("break", i1);
        tablaDescendente.get("I").put("function", i1);
        tablaDescendente.get("I").put("get", i1);
        tablaDescendente.get("I").put("if", i1);
        tablaDescendente.get("I").put("let", i1);
        tablaDescendente.get("I").put("put", i1);
        tablaDescendente.get("I").put("return", i1);
        tablaDescendente.get("I").put("switch", i1);
        tablaDescendente.get("I").put("id", i1);
        tablaDescendente.get("I").put("EOF", i1);

        for (int i = 1; i <= 62; i++)
            accionesSem.put("{" + i + "}", i);

    }

    public void analisis(File file) {
        try (FileReader archivo = new FileReader(file)) {
            Pair<String, String> sigTok = lector(archivo);
            String[] listPila;
            Integer semActID;
            while (!pila.peek().getFirst().equals("EOF")) {
                if (!tablaDescendente.containsKey(pila.peek().getFirst())) {
                    // Tratamiento de Terminal
                    if (sigTok.getFirst().equals(pila.peek().getFirst())) {
                        pilaAux.push(pila.pop());
                        if (pila.peek().getFirst().equals("id"))
                            pilaAux.peek().setFirst(sigTok.getSecond());
                        sigTok = lector(archivo);
                    } else { // en caso de error
                        System.err.println("Error en la linea: " + AL.nLineas + " no se esperaba: " + sigTok);
                        System.err.println("-------------------------------------"); // Separacion de errores
                        pila.pop();
                    }
                } else if ((semActID = accionesSem.get(pila.peek().getFirst())) != null) { // Tratamiento acciones
                                                                                           // semánticas
                    execSemAct(semActID);
                    pila.pop();
                } else { // Tratamiento de No Terminal
                    if ((listPila = tablaDescendente.get(pila.peek().getFirst()).get(sigTok.getFirst())) != null) {
                        pilaAux.push(pila.pop());
                        Escribir.parse(listPila[0]);
                        for (int i = 1; i < listPila.length; i++) {
                            pila.push(new Pair<String, Atributos>(listPila[i], new Atributos()));
                        }
                    } else { // en caso de error
                        System.err.println("Error en la linea: " + AL.nLineas + " no se esperaba: " + sigTok);
                        System.err.println("-------------------------------------"); // Separacion de errores
                        pila.pop();
                        if (pila.peek().getFirst().equals("EOF")) {
                            pila.push(new Pair<String, Atributos>("P", new Atributos()));
                            sigTok = lector(archivo);
                        }
                    }
                }
            }
            if (!sigTok.getFirst().equals("EOF") || !pilaAux.pop().getFirst().equals("I")) {
                System.err.println("ERROR SEMANTICO");
            }
        } catch (IOException e) {
            System.err.println("Error de lectura: " + e.getMessage());
        }
    }

    private Pair<String, String> lector(FileReader archivo) throws IOException {
        int aux;
        Pair<String, String> token;
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
        return new Pair<String, String>("EOF", null);
    }

    private void popX(int x) {
        for (int i = 0; i < x; i++) {
            pilaAux.pop();
        }
    }

    private void execSemAct(int semActID) {
        switch (semActID) {
            case 1:
                SemAct1();
                break;
            case 2:
                SemAct2();
                break;
            case 3:
                SemAct3();
                break;
            case 4:
                SemAct4();
                break;
            case 5:
                SemAct5();
                break;
            case 6:
                SemAct6();
                break;
            case 7:
                SemAct7();
                break;
            case 8:
                SemAct8();
                break;
            case 9:
                SemAct9();
                break;
            case 10:
                SemAct10();
                break;
            case 11:
                SemAct11();
                break;
            case 12:
                SemAct12();
                break;
            case 13:
                SemAct13();
                break;
            case 14:
                SemAct14();
                break;
            case 15:
                SemAct15();
                break;
            case 16:
                SemAct16();
                break;
            case 17:
                SemAct17();
                break;
            case 18:
                SemAct18();
                break;
            case 19:
                SemAct19();
                break;
            case 20:
                SemAct20();
                break;
            case 21:
                SemAct21();
                break;
            case 22:
                SemAct22();
                break;
            case 23:
                SemAct23();
                break;
            case 24:
                SemAct24();
                break;
            case 25:
                SemAct25();
                break;
            case 26:
                SemAct26();
                break;
            case 27:
                SemAct27();
                break;
            case 28:
                SemAct28();
                break;
            case 29:
                SemAct29();
                break;
            case 30:
                SemAct30();
                break;
            case 31:
                SemAct31();
                break;
            case 32:
                SemAct32();
                break;
            case 33:
                SemAct33();
                break;
            case 34:
                SemAct34();
                break;
            case 35:
                SemAct35();
                break;
            case 36:
                SemAct36();
                break;
            case 37:
                SemAct37();
                break;
            case 38:
                SemAct38();
                break;
            case 39:
                SemAct39();
                break;
            case 40:
                SemAct40();
                break;
            case 41:
                SemAct41();
                break;
            case 42:
                SemAct42();
                break;
            case 43:
                SemAct43();
                break;
            case 44:
                SemAct44();
                break;
            case 45:
                SemAct45();
                break;
            case 46:
                SemAct46();
                break;
            case 47:
                SemAct47();
                break;
            case 48:
                SemAct48();
                break;
            case 49:
                SemAct49();
                break;
            case 50:
                SemAct50();
                break;
            case 51:
                SemAct51();
                break;
            case 52:
                SemAct52();
                break;
            case 53:
                SemAct53();
                break;
            case 54:
                SemAct54();
                break;
            case 55:
                SemAct55();
                break;
            case 56:
                SemAct56();
                break;
            case 57:
                SemAct57();
                break;
            case 58:
                SemAct58();
                break;
            case 59:
                SemAct59();
                break;
            case 60:
                SemAct60();
                break;
            case 61:
                SemAct61();
                break;
            case 62:
                SemAct62();
                break;
        }
    }

    private void SemAct1() {
        pilaAux.elementAt(pilaAux.size() - 4).getSecond()
                .setTipo(pilaAux.elementAt(pilaAux.size() - 2).getSecond().getTipo());
        popX(3);
    }

    private void SemAct2() {
        pilaAux.elementAt(pilaAux.size() - 4).getSecond()
                .setTipo(pilaAux.elementAt(pilaAux.size() - 2).getSecond().getTipo());
        popX(4);
    }

    private void SemAct3() {
        pilaAux.elementAt(pilaAux.size() - 4).getSecond()
                .setTipo(pilaAux.elementAt(pilaAux.size() - 2).getSecond().getTipo());
        popX(3);
    }

    private boolean SemAct4() {
        boolean checkErr = false;
        Atributos J = pilaAux.elementAt(pilaAux.size() - 1).getSecond();
        Atributos id = AL.pilaTS.peek()
                .getAtributos(Integer.parseInt(pilaAux.elementAt(pilaAux.size() - 2).getFirst()));
        Atributos S = pilaAux.elementAt(pilaAux.size() - 3).getSecond();

        if (id.getTipo().equals(FUNC)) {
            if (id.getTipoParam().equals(J.getTipo()))
                S.setTipo(OK);
            else {
                S.setTipo(ERROR);
                System.err.println("Para la funcion: " + id.getLex() +
                 " se esperaban los parametros: " + id.getTipoParam() + " en vez de: " + J.getTipo() + ". En la linea: " + AL.nLineas);
                checkErr=true;
            }
        } else if (id.getTipo().equals(J.getTipo()))
            S.setTipo(OK);
        else {
            S.setTipo(ERROR);
            System.err.println("Para la variable: " + id.getLex() + 
            " se esperaba el tipo: " + id.getTipo() + " en vez de: " + J.getTipo() + ". En la linea: " + AL.nLineas);
            checkErr=true;
        }   
        popX(2);
        return checkErr;
    }

    private boolean SemAct5() {
        boolean checkErr = false;
        Atributos E = pilaAux.elementAt(pilaAux.size() - 2).getSecond();
        Atributos S = pilaAux.elementAt(pilaAux.size() - 4).getSecond();

        if (E.getTipo().equals(ENT) || E.getTipo().equals(CAD))
            S.setTipo(OK);
        else {
            S.setTipo(ERROR);
            System.err.println("Se esperaba una expresion de tipo cadena o entero, no: " + E.getTipo() + ". En la linea: " + AL.nLineas);
            checkErr=true;
        }
        popX(3);
        return checkErr;
    }

    private boolean SemAct6() {
        boolean checkErr = false;
        Atributos id = AL.pilaTS.peek()
                .getAtributos(Integer.parseInt(pilaAux.elementAt(pilaAux.size() - 2).getFirst()));
        Atributos S = pilaAux.elementAt(pilaAux.size() - 4).getSecond();

        if (id.getTipo().equals(ENT) || id.getTipo().equals(CAD))
            S.setTipo(OK);
        else {
            S.setTipo(ERROR);
            System.err.println("Se esperaba que la variable: " + id.getLex() +
            " fuera de tipo cadena o entero, no: " + id.getTipo() + ". En la linea: " + AL.nLineas);
            checkErr=true;
        }
        popX(3);
        return checkErr;
    }

    private void SemAct7() {
        pilaAux.elementAt(pilaAux.size() - 4).getSecond()
                .setTipo(pilaAux.elementAt(pilaAux.size() - 2).getSecond().getTipo());
        popX(3);
    }

    private void SemAct8() {
        pilaAux.elementAt(pilaAux.size() - 3).getSecond().setTipo(OK);
        popX(2);
    }

    private boolean SemAct9() {
        boolean checkErr = false;
        Atributos N = pilaAux.elementAt(pilaAux.size() - 1).getSecond();
        Atributos R = pilaAux.elementAt(pilaAux.size() - 2).getSecond();
        Atributos E = pilaAux.elementAt(pilaAux.size() - 3).getSecond();

        if (N.getTipo().equals(OK) || (R.getTipo().equals(N.getTipo()) && N.getTipo().equals(LOG)))
            E.setTipo(R.getTipo());
        else {
            E.setTipo(ERROR);
            System.err.println("Se esperaba una expresion de tipo logico, no: " + R.getTipo() + ". En la linea: " + AL.nLineas);
            checkErr=true;
        }
        popX(2);
        return checkErr;
    }

    private boolean SemAct10() {
        boolean checkErr = false;
        Atributos N1 = pilaAux.elementAt(pilaAux.size() - 1).getSecond();
        Atributos R = pilaAux.elementAt(pilaAux.size() - 2).getSecond();
        Atributos N = pilaAux.elementAt(pilaAux.size() - 4).getSecond();

        if (R.getTipo().equals(LOG) && (N1.getTipo().equals(OK) || N1.getTipo().equals(LOG)))
            N.setTipo(LOG);
        else {
            N.setTipo(ERROR);
            System.err.println("Se esperaba una expresion de tipo logico, no: " + R.getTipo() + ". En la linea: " + AL.nLineas);
            checkErr=true;
        }
        popX(3);
        return checkErr;
    }

    private void SemAct11() {
        pilaAux.elementAt(pilaAux.size() - 1).getSecond().setTipo(OK);
    }

    private boolean SemAct12() {
        boolean checkErr = false;
        Atributos M = pilaAux.elementAt(pilaAux.size() - 1).getSecond();
        Atributos U = pilaAux.elementAt(pilaAux.size() - 2).getSecond();
        Atributos R = pilaAux.elementAt(pilaAux.size() - 3).getSecond();

        if (M.getTipo().equals(OK) || (U.getTipo().equals(M.getTipo()) && M.getTipo().equals(LOG)))
            R.setTipo(U.getTipo());
        else {
            R.setTipo(ERROR);
            System.err.println("Se esperaba una expresion de tipo logico, no: " + U.getTipo() + ". En la linea: " + AL.nLineas);
            checkErr=true;
        }
        popX(2);
        return checkErr;
    }

    private boolean SemAct13() {
        boolean checkErr = false;
        Atributos M1 = pilaAux.elementAt(pilaAux.size() - 1).getSecond();
        Atributos U = pilaAux.elementAt(pilaAux.size() - 2).getSecond();
        Atributos M = pilaAux.elementAt(pilaAux.size() - 4).getSecond();

        if (U.getTipo().equals(LOG) && (M1.getTipo().equals(OK) || M1.getTipo().equals(LOG)))
            M.setTipo(LOG);
        else {
            M.setTipo(ERROR);
            System.err.println("Se esperaba una expresion de tipo logico, no: " + U.getTipo() + ". En la linea: " + AL.nLineas);
            checkErr=true;
        }
        popX(3);
        return checkErr;
    }

    private void SemAct14() {
        pilaAux.elementAt(pilaAux.size() - 1).getSecond().setTipo(OK);
    }

    private boolean SemAct15() {
        boolean checkErr = false;
        Atributos O = pilaAux.elementAt(pilaAux.size() - 1).getSecond();
        Atributos D = pilaAux.elementAt(pilaAux.size() - 2).getSecond();
        Atributos U = pilaAux.elementAt(pilaAux.size() - 3).getSecond();

        if (O.getTipo().equals(OK))
            U.setTipo(D.getTipo());
        else if (D.getTipo().equals(O.getTipo()) && D.getTipo().equals(ENT))
            U.setTipo(LOG);
        else {
            U.setTipo(ERROR);
            System.err.println("Se esperaba una expresion de tipo entero, no: " + D.getTipo() + ". En la linea: " + AL.nLineas);
            checkErr=true;
        }
        popX(2);
        return checkErr;
    }

    private boolean SemAct16() {
        boolean checkErr = false;
        Atributos O1 = pilaAux.elementAt(pilaAux.size() - 1).getSecond();
        Atributos D = pilaAux.elementAt(pilaAux.size() - 2).getSecond();
        Atributos O = pilaAux.elementAt(pilaAux.size() - 4).getSecond();

        if (D.getTipo().equals(ENT) && (O1.getTipo().equals(OK) || O1.getTipo().equals(ENT)))
            O.setTipo(ENT);
        else {
            O.setTipo(ERROR);
            System.err.println("Se esperaba una expresion de tipo entero, no: " + D.getTipo() + ". En la linea: " + AL.nLineas);
            checkErr=true;
        }
        popX(3);
        return checkErr;
    }

    private boolean SemAct17() {
        boolean checkErr = false;
        Atributos O1 = pilaAux.elementAt(pilaAux.size() - 1).getSecond();
        Atributos D = pilaAux.elementAt(pilaAux.size() - 2).getSecond();
        Atributos O = pilaAux.elementAt(pilaAux.size() - 4).getSecond();

        if (D.getTipo().equals(ENT) && (O1.getTipo().equals(OK) || O1.getTipo().equals(ENT)))
            O.setTipo(ENT);
        else {
            O.setTipo(ERROR);
            System.err.println("Se esperaba una expresion de tipo entero, no: " + D.getTipo() + ". En la linea: " + AL.nLineas);
            checkErr=true;
        }
        popX(3);
        return checkErr;
    }

    private void SemAct18() {
        pilaAux.elementAt(pilaAux.size() - 1).getSecond().setTipo(OK);
    }

    private boolean SemAct19() {
        boolean checkErr = false;
        Atributos W = pilaAux.elementAt(pilaAux.size() - 1).getSecond();
        Atributos V = pilaAux.elementAt(pilaAux.size() - 2).getSecond();
        Atributos D = pilaAux.elementAt(pilaAux.size() - 3).getSecond();

        if (W.getTipo().equals(OK) || (V.getTipo().equals(W.getTipo()) && V.getTipo().equals(ENT)))
            D.setTipo(V.getTipo());
        else {
                D.setTipo(ERROR);
                System.err.println("Se esperaba una expresion de tipo entero, no: " + V.getTipo() + ". En la linea: " + AL.nLineas);
                checkErr=true;
        } 
        popX(2);
        return checkErr;
    }

    private boolean SemAct20() {
        boolean checkErr = false;
        Atributos W1 = pilaAux.elementAt(pilaAux.size() - 1).getSecond();
        Atributos V = pilaAux.elementAt(pilaAux.size() - 2).getSecond();
        Atributos W = pilaAux.elementAt(pilaAux.size() - 4).getSecond();

        if (V.getTipo().equals(ENT) && (W1.getTipo().equals(OK) || W1.getTipo().equals(ENT)))
            W.setTipo(ENT);
        else {
            W.setTipo(ERROR);
            System.err.println("Se esperaba una expresion de tipo entero, no: " + V.getTipo() + ". En la linea: " + AL.nLineas);
            checkErr=true;
        }
        popX(3);
        return checkErr;
    }

    private boolean SemAct21() {
        boolean checkErr = false;
        Atributos W1 = pilaAux.elementAt(pilaAux.size() - 1).getSecond();
        Atributos V = pilaAux.elementAt(pilaAux.size() - 2).getSecond();
        Atributos W = pilaAux.elementAt(pilaAux.size() - 4).getSecond();

        if (V.getTipo().equals(ENT) && (W1.getTipo().equals(OK) || W1.getTipo().equals(ENT)))
            W.setTipo(ENT);
        else {
            W.setTipo(ERROR);
            System.err.println("Se esperaba una expresion de tipo entero, no: " + V.getTipo() + ". En la linea: " + AL.nLineas);
            checkErr=true;
        }
        popX(3);
        return checkErr;
    }

    private void SemAct22() {
        pilaAux.elementAt(pilaAux.size() - 1).getSecond().setTipo(OK);
    }

    private boolean SemAct23() {
        boolean checkErr = false;
        Atributos Y = pilaAux.elementAt(pilaAux.size() - 1).getSecond();
        Atributos id = AL.pilaTS.peek()
                .getAtributos(Integer.parseInt(pilaAux.elementAt(pilaAux.size() - 2).getFirst()));
        Atributos V = pilaAux.elementAt(pilaAux.size() - 3).getSecond();

        if (id.getTipo().equals(FUNC)) {
            if (id.getTipoParam().equals(Y.getTipo()))
                V.setTipo(id.getTipoRetorno());
            else {
                V.setTipo(ERROR);
                System.err.println("Para la funcion: " + id.getLex() +
                " se esperaban los parametros: " + id.getTipoParam() + " en vez de: " + Y.getTipo() + ". En la linea: " + AL.nLineas);
                checkErr=true;
            }
        } else if (Y.getTipo().equals(OK))
            V.setTipo(id.getTipo());
        else {
            V.setTipo(ERROR);
            System.err.println("Para la variable: " + id.getLex() + " no se esperaban parametros" + ". En la linea: " + AL.nLineas);
            checkErr=true;
        }
        popX(2);
        return checkErr;
    }

    private void SemAct24() {
        pilaAux.elementAt(pilaAux.size() - 4).getSecond()
                .setTipo(pilaAux.elementAt(pilaAux.size() - 2).getSecond().getTipo());
        popX(3);
    }

    private void SemAct25() {
        pilaAux.elementAt(pilaAux.size() - 1).getSecond().setTipo(OK);
    }

    private void SemAct26() {
        pilaAux.elementAt(pilaAux.size() - 4).getSecond()
                .setTipo(pilaAux.elementAt(pilaAux.size() - 2).getSecond().getTipo());
        popX(3);
    }

    private void SemAct27() {
        pilaAux.elementAt(pilaAux.size() - 2).getSecond().setTipo(ENT);
        popX(1);
    }

    private void SemAct28() {
        pilaAux.elementAt(pilaAux.size() - 2).getSecond().setTipo(CAD);
        popX(1);
    }

    private void SemAct29() {
        pilaAux.elementAt(pilaAux.size() - 2).getSecond().setTipo(LOG);
        popX(1);
    }

    private void SemAct30() {
        pilaAux.elementAt(pilaAux.size() - 2).getSecond().setTipo(LOG);
        popX(1);
    }

    private boolean SemAct31() {
        boolean checkErr = false;
        Atributos Q = pilaAux.elementAt(pilaAux.size() - 1).getSecond();
        Atributos E = pilaAux.elementAt(pilaAux.size() - 2).getSecond();
        Atributos L = pilaAux.elementAt(pilaAux.size() - 3).getSecond();

        if (Q.getTipo().equals(OK)) {
            L.setTipo(E.getTipo());
        } else if (!E.getTipo().equals(ERROR) && !Q.getTipo().equals(ERROR))
            L.setTipo(E.getTipo() + "*" + Q.getTipo());
        else {
            L.setTipo(ERROR);
            System.err.println("Error al recibir expresion defectuosa" + ". En la linea: " + AL.nLineas);
            checkErr = true;
        }
        popX(2);
        return checkErr;
    }

    private void SemAct32() {
        pilaAux.elementAt(pilaAux.size() - 1).getSecond().setTipo(VOID);
    }

    private boolean SemAct33() {
        boolean checkErr = false;
        Atributos Q1 = pilaAux.elementAt(pilaAux.size() - 1).getSecond();
        Atributos E = pilaAux.elementAt(pilaAux.size() - 2).getSecond();
        Atributos Q = pilaAux.elementAt(pilaAux.size() - 4).getSecond();

        if (Q1.getTipo().equals(OK)) {
            Q.setTipo(E.getTipo());
        } else if (!E.getTipo().equals(ERROR) && !Q1.getTipo().equals(ERROR))
            Q.setTipo(E.getTipo() + "*" + Q1.getTipo());
        else {
            Q.setTipo(ERROR);
            System.err.println("Error al recibir expresion defectuosa" + ". En la linea: " + AL.nLineas);
            checkErr = true;
        }
        popX(3);
        return checkErr;
    }

    private void SemAct34() {
        pilaAux.elementAt(pilaAux.size() - 1).getSecond().setTipo(OK);
    }

    private void SemAct35() {
        pilaAux.elementAt(pilaAux.size() - 2).getSecond()
                .setTipo(pilaAux.elementAt(pilaAux.size() - 1).getSecond().getTipo());
        popX(1);
    }

    private void SemAct36() {
        pilaAux.elementAt(pilaAux.size() - 1).getSecond().setTipo(OK);
    }

    private void SemAct37() {
        AL.zonaDecl = true;
    }

    private void SemAct38() {
        TablaSimbolos TSAct = AL.pilaTS.peek();
        Atributos T = pilaAux.elementAt(pilaAux.size() - 2).getSecond();
        Atributos id = TSAct.getAtributos(Integer.parseInt(pilaAux.elementAt(pilaAux.size() - 3).getFirst()));
        Atributos B = pilaAux.elementAt(pilaAux.size() - 5).getSecond();

        AL.zonaDecl = false;
        B.setTipo(OK);
        id.setTipo(T.getTipo());
        id.setDespl(TSAct.getDesp());
        TSAct.setDesp(TSAct.getDesp() + T.getDespl());
        popX(4);
    }

    private boolean SemAct39() {
        boolean checkErr = false;
        Atributos S = pilaAux.elementAt(pilaAux.size() - 1).getSecond();
        Atributos E = pilaAux.elementAt(pilaAux.size() - 3).getSecond();
        Atributos B = pilaAux.elementAt(pilaAux.size() - 6).getSecond();

        if (E.getTipo().equals(LOG))
            B.setTipo(S.getTipo());
        else {
            B.setTipo(ERROR);
            System.err.println("Se esperaba una condicion de tipo logico, no: " + E.getTipo() + ". En la linea: " + AL.nLineas);
            checkErr = true;
        }
        popX(5);
        return checkErr;
    }

    private void SemAct40() {
        pilaAux.elementAt(pilaAux.size() - 2).getSecond()
                .setTipo(pilaAux.elementAt(pilaAux.size() - 1).getSecond().getTipo());
        popX(1);
    }

    private boolean SemAct41() {
        boolean checkErr = false;
        Atributos G = pilaAux.elementAt(pilaAux.size() - 2).getSecond();
        Atributos E = pilaAux.elementAt(pilaAux.size() - 5).getSecond();
        Atributos B = pilaAux.elementAt(pilaAux.size() - 8).getSecond();

        if (E.getTipo().equals(ENT))
            B.setTipo(G.getTipo());
        else {
            B.setTipo(ERROR);
            System.err.println("Se esperaba una expresion de tipo entero, no: " + E.getTipo() + ". En la linea: " + AL.nLineas);
            checkErr = true;
        }
        popX(7);
        return checkErr;
    }

    private boolean SemAct42() {
        boolean checkErr = false;
        Atributos G1 = pilaAux.elementAt(pilaAux.size() - 1).getSecond();
        Atributos C = pilaAux.elementAt(pilaAux.size() - 2).getSecond();
        Atributos G = pilaAux.elementAt(pilaAux.size() - 6).getSecond();

        if (C.getTipo().equals(OK) && G1.getTipo().equals(OK))
            G.setTipo(OK);
        else if (C.getTipo().equals(OK))
            G.setTipo(G1.getTipo());
        else if (G1.getTipo().equals(OK) || C.getTipo().equals(G1.getTipo()))
            G.setTipo(C.getTipo());
        else {
            G.setTipo(ERROR);
            System.err.println("Error al recibir expresiones erroneas" + ". En la linea: " + AL.nLineas);
            checkErr = true;
        }
        popX(5);
        return checkErr;
    }

    private void SemAct43() {
        pilaAux.elementAt(pilaAux.size() - 4).getSecond()
                .setTipo(pilaAux.elementAt(pilaAux.size() - 1).getSecond().getTipo());
        popX(3);
    }

    private void SemAct44() {
        pilaAux.elementAt(pilaAux.size() - 1).getSecond().setTipo(OK);
    }

    private void SemAct45() {
        pilaAux.elementAt(pilaAux.size() - 2).getSecond().setTipo(ENT);
        pilaAux.elementAt(pilaAux.size() - 2).getSecond().setDespl(1);
        popX(1);
    }

    private void SemAct46() {
        pilaAux.elementAt(pilaAux.size() - 2).getSecond().setTipo(LOG);
        pilaAux.elementAt(pilaAux.size() - 2).getSecond().setDespl(1);
        popX(1);
    }

    private void SemAct47() {
        pilaAux.elementAt(pilaAux.size() - 2).getSecond().setTipo(CAD);
        pilaAux.elementAt(pilaAux.size() - 2).getSecond().setDespl(64);
        popX(1);
    }

    private void SemAct48() {
        AL.zonaDecl = true;
    }

    private void SemAct49() {
        TablaSimbolos TSL = new TablaSimbolos(TSId);
        TSId++;
        this.numParam = 0;
        TablaSimbolos TSAct = AL.pilaTS.peek(); // tabla Global TSG
        Atributos id = TSAct.getAtributos(Integer.parseInt(pilaAux.elementAt(pilaAux.size() - 2).getFirst()));
        Atributos H = pilaAux.elementAt(pilaAux.size() - 1).getSecond();

        id.setTipo(FUNC);
        id.setTipoRetorno(H.getTipo());

        AL.pilaTS.push(TSL);
    }

    private void SemAct50() {
        AL.zonaDecl = false;
        TablaSimbolos TSG = AL.pilaTS.firstElement(); // tabla Global TSG
        Atributos id = TSG.getAtributos(Integer.parseInt(pilaAux.elementAt(pilaAux.size() - 5).getFirst()));
        Atributos A = pilaAux.elementAt(pilaAux.size() - 2).getSecond();

        id.setTipoParam(A.getTipo());
        id.setNumParam(numParam);
        id.setEtiqFuncion("Et_" + id.getLex());
    }

    private boolean SemAct51() {
        boolean checkErr = false;
        Atributos C = pilaAux.elementAt(pilaAux.size() - 2).getSecond();
        Atributos H = pilaAux.elementAt(pilaAux.size() - 7).getSecond();
        Atributos F = pilaAux.elementAt(pilaAux.size() - 10).getSecond();

        if ((H.getTipo().equals(VOID) && C.getTipo().equals(OK) || H.getTipo().equals(C.getTipo())))
            F.setTipo(OK);
        else {
            F.setTipo(ERROR);
            System.err.println("Se esperaban los un valor de retorno del tipo: " + H.getTipo()
                             + " pero la función retorna: " + C.getTipo() + ". En la linea: " + AL.nLineas);
            checkErr = true;
        }

        TablaSimbolos ts = AL.pilaTS.pop();
        Escribir.printTS(ts.getTSID(), ts.getEntrys().entrySet());
        popX(9);
        return checkErr;
    }

    private void SemAct52() {
        pilaAux.elementAt(pilaAux.size() - 2).getSecond()
                .setTipo(pilaAux.elementAt(pilaAux.size() - 1).getSecond().getTipo());
        popX(1);
    }

    private void SemAct53() {
        pilaAux.elementAt(pilaAux.size() - 2).getSecond().setTipo(VOID);
        popX(1);
    }

    private void SemAct54() {
        TablaSimbolos TSAct = AL.pilaTS.peek();
        Atributos K = pilaAux.elementAt(pilaAux.size() - 1).getSecond();
        Atributos id = TSAct.getAtributos(Integer.parseInt(pilaAux.elementAt(pilaAux.size() - 2).getFirst()));
        Atributos T = pilaAux.elementAt(pilaAux.size() - 3).getSecond();
        Atributos A = pilaAux.elementAt(pilaAux.size() - 4).getSecond();

        if (K.getTipo().equals(OK)) {
            A.setTipo(T.getTipo());
        } else
            A.setTipo(T.getTipo() + "*" + K.getTipo());

        id.setTipo(T.getTipo());
        id.setDespl(TSAct.getDesp());
        TSAct.setDesp(TSAct.getDesp() + T.getDespl());
        this.numParam += 1;
        popX(3);
    }

    private void SemAct55() {
        pilaAux.elementAt(pilaAux.size() - 2).getSecond().setTipo(VOID);
        popX(1);
    }

    private void SemAct56() {
        TablaSimbolos TSAct = AL.pilaTS.peek();
        Atributos K1 = pilaAux.elementAt(pilaAux.size() - 1).getSecond();
        Atributos id = TSAct.getAtributos(Integer.parseInt(pilaAux.elementAt(pilaAux.size() - 2).getFirst()));
        Atributos T = pilaAux.elementAt(pilaAux.size() - 3).getSecond();
        Atributos K = pilaAux.elementAt(pilaAux.size() - 5).getSecond();

        if (K1.getTipo().equals(OK)) {
            K.setTipo(T.getTipo());
        } else
            K.setTipo(T.getTipo() + "*" + K1.getTipo());

        id.setTipo(T.getTipo());
        id.setDespl(TSAct.getDesp());
        TSAct.setDesp(TSAct.getDesp() + T.getDespl());
        this.numParam += 1;
        popX(4);
    }

    private void SemAct57() {
        pilaAux.elementAt(pilaAux.size() - 2).getSecond().setTipo(OK);
    }

    private void SemAct58() {
        Atributos C = pilaAux.elementAt(pilaAux.size() - 3).getSecond();
        Atributos B = pilaAux.elementAt(pilaAux.size() - 2).getSecond();
        Atributos C1 = pilaAux.elementAt(pilaAux.size() - 1).getSecond();

        if (!B.getTipo().equals(OK))
            C.setTipo(B.getTipo());
        else
            C.setTipo(C1.getTipo());
        popX(2);
    }

    private void SemAct59() {
        pilaAux.elementAt(pilaAux.size() - 2).getSecond().setTipo(OK);
    }

    private void SemAct60() {
        popX(2);        
    }

    private void SemAct61() {
        AL.pilaTS.push(new TablaSimbolos(TSId));
        TSId++;
        AL.zonaDecl = false;
    }

    private void SemAct62() {
        TablaSimbolos ts = AL.pilaTS.pop();
        Escribir.printTS(ts.getTSID(), ts.getEntrys().entrySet());
        popX(1);
    }
}