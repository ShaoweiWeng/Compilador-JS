import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class AFD {

    // ------------------------------------------------------- Atributos
    // -------------------------------------------------------
    private HashMap<String, HashMap<String, String>> tablaTransiciones; // mapa con estado y sus posibles estados
                                                                        // siguientes
    private HashMap<String, Integer> tablaSimbolos; // tabla de simbolos con lex(el ID) como key y pos del linkedList
                                                    // como value
    private HashMap<String, String> palReservadas; // map <key = palabra reservada, value = formato token>
    private HashMap<String, String> token; // map <key = estado final, value = formato token>
    private HashSet<String> dels; // set delimitadores para hacer los o.c
    private final String delimitadores[] = { " ", "\t", "\n", "\r" };
    private final String estados[] = { "S", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" };
    private final String transiciones[] = { "EOF", ",", ";", ":", "(", ")", "{", "}", "+", "-" };
    private ArrayList<Atributos> atributos;
    private String estadoActual;
    private Integer tsPos = 0; // posicion de la tabla de simbolos
    private String lex;
    private int num = 0, cont = 0;
    public int nLineas = 1;
    public boolean reciclar = false;

    /**
     * ------------------------ Constructor -------------------------
     * Crea el automata finito determinista con todos sus estados y
     * transiciones posibles.
     */
    public AFD() {
        this.estadoActual = "S";
        this.tablaTransiciones = new HashMap<>();
        this.tablaSimbolos = new HashMap<>();
        this.palReservadas = new HashMap<>();
        this.token = new HashMap<>();
        this.dels = new HashSet<>();
        this.atributos = new ArrayList<>();
        int i = 1, j = 2, k = 0;
        for (String tokens : Escribir.tokens)
            token.put("" + j++, tokens);
        for (String del : delimitadores)
            dels.add(del);
        for (String valPR : Escribir.valPR)
            palReservadas.put(Escribir.keyPR[k++], valPR);
        for (String s : estados)
            tablaTransiciones.put(s, new HashMap<String, String>());
        // Ramas S -> final
        for (String del : delimitadores)
            tablaTransiciones.get("S").put(del, "S");
        for (String transicion : transiciones)
            tablaTransiciones.get("S").put(transicion, "" + i++);

        // Rama A
        tablaTransiciones.get("S").put("*", "A");
        tablaTransiciones.get("A").put("=", "11");
        // Rama B
        tablaTransiciones.get("S").put("=", "B");
        insertarTodo("B", "16");
        tablaTransiciones.get("B").put("=", "12");
        // Rama C
        tablaTransiciones.get("S").put("&", "C");
        tablaTransiciones.get("C").put("&", "14");
        // Rama D
        tablaTransiciones.get("S").put("|", "D");
        tablaTransiciones.get("D").put("|", "15");
        // Rama E
        tablaTransiciones.get("S").put("\"", "E");
        insertarTodo("E", "E");
        tablaTransiciones.get("E").put("\"", "17");
        tablaTransiciones.get("E").put("\\", "F");
        tablaTransiciones.get("E").remove("\n");
        tablaTransiciones.get("E").remove("\r");
        // Rama F
        insertarTodo("F", "E");
        tablaTransiciones.get("F").remove("\n");
        tablaTransiciones.get("E").remove("\r");
        // Rama G
        tablaTransiciones.get("S").put("_", "G");
        insertarLetra("S", "G");
        insertarTodo("G", "18");
        insertarDigito("G", "G");
        insertarLetra("G", "G");
        tablaTransiciones.get("G").put("_", "G");
        // Rama H
        insertarDigito("S", "H");
        insertarTodo("H", "19");
        insertarDigito("H", "H");
        // Rama I
        tablaTransiciones.get("S").put("/", "I");
        tablaTransiciones.get("I").put("/", "J");
        // Rama J
        insertarTodo("J", "J");
        tablaTransiciones.get("J").put("\n", "S");
        tablaTransiciones.get("J").put("\r", "S");
        // Rama K
        tablaTransiciones.get("S").put("!", "K");
        tablaTransiciones.get("K").put("=", "13");
    }

    /**
     * --------------------------- insertarLetra ---------------------------
     * Esta funcion inserta en la tabla de transiciones todas las letras
     * minusculas y mayusculas.
     * 
     * @param estadoActual El estado desde el que parte la transicion.
     * @param estadoFuturo El estado donde finaliza después de la transición.
     */
    private void insertarLetra(String estadoAcutual, String estadoFuturo) {
        for (char letra = 'a'; letra <= 'z'; letra++)
            tablaTransiciones.get(estadoAcutual).put(String.valueOf(letra), estadoFuturo);
        for (char letra = 'A'; letra <= 'Z'; letra++)
            tablaTransiciones.get(estadoAcutual).put(String.valueOf(letra), estadoFuturo);
    }

    /**
     * --------------------------- insertarDigito ----------------------------
     * Inserta en la tabla de transiciones todos los dígitos.
     * 
     * @param estadoActual El estado desde el que parte la transicion.
     * @param estadoFuturo El estado donde finaliza después de la transición.
     */
    private void insertarDigito(String estadoAcutual, String estadoFuturo) {
        for (char letra = '0'; letra <= '9'; letra++)
            tablaTransiciones.get(estadoAcutual).put(String.valueOf(letra), estadoFuturo);
    }

    /**
     * --------------------------- insertarTodo ----------------------------
     * Esta funcion inserta en la tabla de transiciones todos los caracteres
     * imprimibles entre 33 y 126, asi como los delimitadores especificados.
     * 
     * @param estadoActual El estado desde el que parte la transición.
     * @param estadoFuturo El estado donde finaliza después de la transición.
     */
    private void insertarTodo(String estadoAcutual, String estadoFuturo) {
        for (int i = 33; i <= 126; i++)
            tablaTransiciones.get(estadoAcutual).put(String.valueOf((char) i), estadoFuturo);
        for (String del : delimitadores)
            tablaTransiciones.get(estadoAcutual).put(del, estadoFuturo);
    }

    /**
     * ----------------------------------- transicion
     * ------------------------------------
     * Analiza el caracter leido y realiza la transicion del automata, si es una
     * transicion invalida (es decir no existe dicha transicion desde un estado 'a'
     * hasta
     * otro estado 'b') llama al gestor de errores y vuelve al estado inicial.
     *
     * @param caracter Caracter leido del fichero utilizado para la transicion de
     *                 estados
     */
    public String transicion(String caracter) {
        String res = null;
        String estadoFuturo, estadoActualAux;
        if ((estadoFuturo = tablaTransiciones.get(estadoActual).get(caracter)) == null) {
            gestorErr(caracter);
            this.estadoActual = "S"; // Resetear
        } else {
            estadoActualAux = this.estadoActual; // Guardar el estado pasado
            this.estadoActual = estadoFuturo; // Actualizar el nuevo estado
            String gen;
            // Transiciones directas de S a un estado final
            if ((gen = token.get(estadoFuturo)) != null) {
                Escribir.genToken(gen);
                switch (estadoFuturo) {
                    case "11":
                        res = "*=";
                        break;
                    case "12":
                        res = "==";
                        break;
                    case "13":
                        res = "!=";
                        break;
                    case "14":
                        res = "&&";
                        break;
                    case "15":
                        res = "||";
                        break;
                    default:
                        res = caracter;
                        break;
                }
                this.estadoActual = "S";
            } else { // El resto de estados finales no directas desde el axioma
                switch (estadoFuturo) {
                    case "16":
                        Escribir.genToken(Escribir.ASIG);
                        res = "=";
                        this.estadoActual = "S";
                        reciclar = !dels.contains(caracter); // Si no es un delimitador, reciclar = true
                        break;
                    case "17":
                        if (this.cont > 64)
                            gestorErr("MAXCAD");
                        else {
                            Escribir.genToken("CADENA", lex);
                            res = "cad";
                        }
                        this.estadoActual = "S";
                        break;
                    case "18":
                        if ((gen = palReservadas.get(lex)) != null) { // Buscar palabras reservadas
                            Escribir.genToken(gen);
                            res = lex;
                            this.estadoActual = "S";
                        } else { // Si no es ninguna palabra reservada entonces buscar en la tabla de simbolos
                            Integer pos = tablaSimbolos.get(lex);
                            if (pos == null) { // Si no existe en la TS entonces incluirlo
                                pos = this.tsPos;
                                tablaSimbolos.put(lex, pos);
                                atributos.add(pos, new Atributos());
                                this.tsPos++;
                            }
                            Escribir.genToken("ID", pos);
                            res = "id";
                            this.estadoActual = "S";
                        }
                        reciclar = !dels.contains(caracter); // Si no es un delimitador, reciclar = true
                        break;
                    case "19":
                        if (this.num > Math.pow(2, 15))
                            gestorErr("MAXENT");
                        else {
                            Escribir.genToken("ENT", this.num);
                            res = "ent";
                        }
                        this.estadoActual = "S";
                        reciclar = !dels.contains(caracter); // Si no es un delimitador, reciclar = true
                        break;
                }
                // Resto de estados no finales con acciones semanticas
                switch (estadoActualAux) {
                    case "S":
                        switch (estadoFuturo) {
                            case "E": // Inicio de una cadena
                                this.cont = 0;
                                this.lex = ""; // lex := vacio
                                break;
                            case "G":
                                this.lex = "";
                                this.lex += caracter; // Concatenar
                                break;
                            case "H":
                                this.num = Integer.parseInt(caracter); // Primer digito
                                break;
                        }
                        break;
                    case "E":
                        if (estadoFuturo == "E") {
                            this.lex += caracter; // Concatenar
                            this.cont++;
                        }
                        break;
                    case "F":
                        if (estadoFuturo == "E") {
                            this.lex += caracter; // Concatenar
                            this.cont++;
                        }
                        break;
                    case "G":
                        if (estadoFuturo == "G")
                            this.lex += caracter; // Concatenar
                        break;
                    case "H":
                        if (estadoFuturo == "H")
                            this.num = this.num * 10 + Integer.parseInt(caracter); // Incluyendo nuevo digito del numero
                        break;
                }
            }
        }
        return res;
    }

    /**
     * ----------------------------------- gestorErr
     * ------------------------------------
     * Lanza el mensaje de error correspondiente (indicada por el parametro que le
     * han
     * pasado) por la salida de error estandar.
     *
     * @param caso Tipo de error que ha ocurrido.
     */
    private void gestorErr(String caso) {
        switch (caso) {
            case "MAXCAD":
                System.err.println("Cadena supera los 64 caracteres"); // ERROR MAXCAD
                break;
            case "MAXENT":
                System.err.println("Entero fuera de rango, el numero es mayor que 2^15"); // ERROR MAXENT
                break;
            case "\n":
            case "\r":
                switch (this.estadoActual) { // Errores debido a un salto de linea indebido
                    case "A":
                        System.err.println("Transicion no valida en la línea " + (this.nLineas - 1) + " por CR tras *");
                        break;
                    case "K":
                        System.err.println("Transicion no valida en la línea " + (this.nLineas - 1) + " por CR tras !");
                        break;
                    case "C":
                        System.err.println("Transicion no valida en la línea " + (this.nLineas - 1) + " por CR tras &");
                        break;
                    case "D":
                        System.err.println("Transicion no valida en la línea " + (this.nLineas - 1) + " por CR tras |");
                        break;
                    case "E":
                        System.err.println("Cadena no valida en la linea " + (this.nLineas - 1)
                                + " se esperaba un \" pero se encontro CR");
                        break;
                    case "I":
                        System.err.println("Transicion no valida en la línea " + (this.nLineas - 1) + " por CR tras /");
                        break;
                    case "F":
                        System.err
                                .println("Transicion no valida en la línea " + (this.nLineas - 1) + " por CR tras \\");
                        break;
                }
                break;
            default: // Resto de casos de errores
                System.err.println("Transicion no valida en la línea " + this.nLineas);
                System.err.println("El estado actual es: " + this.estadoActual);
                System.err.println("Y se ha leido el caracter: " + caso);
        }
        System.err.println("-------------------------------------"); // Separacion de errores
    }

    /**
     * -------------- getTS ----------------
     * Getter de la tabla de simbolos.
     */
    public HashMap<String, Integer> getTS() {
        return tablaSimbolos;
    }
}