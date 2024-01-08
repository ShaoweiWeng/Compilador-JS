import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class AFD {

    // ------------------------------------------------------- Atributos
    // -------------------------------------------------------
    private HashMap<String, HashMap<String, String>> tablaTransiciones; // mapa con estado y sus posibles estados
                                                                        // siguientes
    public Stack<TablaSimbolos> pilaTS; // tablas de simbolos siendo la TSAct la cima de la pila
    private HashMap<String, String> palReservadas; // map <key = palabra reservada, value = formato token>
    private HashMap<String, String> token; // map <key = estado final, value = formato token>
    private HashSet<String> dels; // set delimitadores para hacer los o.c
    private final String delimitadores[] = { " ", "\t", "\n", "\r" };
    private final String estados[] = { "S", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" };
    private final String transiciones[] = { "EOF", ",", ";", ":", "(", ")", "{", "}", "+", "-" };

    private String estadoActual;
    // private Integer tsPos = 0; // posicion de la tabla de simbolos
    private String lex;
    private int num = 0, cont = 0;
    public int nLineas = 1;
    public boolean reciclar = false, zonaDecl = false;

    /**
     * ------------------------ Constructor -------------------------
     * Crea el automata finito determinista con todos sus estados y
     * transiciones posibles.
     */
    public AFD() {
        this.estadoActual = "S";
        this.tablaTransiciones = new HashMap<>();
        this.pilaTS = new Stack<>();
        this.palReservadas = new HashMap<>();
        this.token = new HashMap<>();
        this.dels = new HashSet<>();
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
    public Pair<String, Pair<String, Integer>> transicion(String caracter) {
        Pair<String, Pair<String, Integer>> res = new Pair<String, Pair<String,Integer>>(null, new Pair<String,Integer>(null, null));
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
                        res.setFirst("*=");
                        break;
                    case "12":
                        res.setFirst("==");
                        break;
                    case "13":
                        res.setFirst("!=");
                        break;
                    case "14":
                        res.setFirst("&&");
                        break;
                    case "15":
                        res.setFirst("||");
                        break;
                    default:
                        res.setFirst(caracter);
                        break;
                }
                this.estadoActual = "S";
            } else { // El resto de estados finales no directas desde el axioma
                switch (estadoFuturo) {
                    case "16":
                        Escribir.genToken(Escribir.ASIG);
                        res.setFirst("=");
                        this.estadoActual = "S";
                        reciclar = !dels.contains(caracter); // Si no es un delimitador, reciclar = true
                        break;
                    case "17":
                        if (this.cont > 64)
                            gestorErr("MAXCAD");
                        else {
                            Escribir.genToken("CADENA", lex);
                            res.setFirst("cad");
                            Pair<String, Integer> auxPair = res.getSecond();
                            auxPair.setFirst(lex);
                            res.setSecond(auxPair);
                        }
                        this.estadoActual = "S";
                        break;
                    case "18":
                        if ((gen = palReservadas.get(lex)) != null) { // Buscar palabras reservadas
                            Escribir.genToken(gen);
                            res.setFirst(lex);
                            this.estadoActual = "S";
                        } else { // Si no es ninguna palabra reservada entonces buscar en la tabla de simbolos
                            Integer pos = null;
                            if (zonaDecl) {
                                pos = pilaTS.peek().getID(lex);
                                if (pos != null)
                                    gestorErr("DUPID");
                                else {
                                    pos = pilaTS.peek().getPos();
                                    pilaTS.peek().insertar(lex);
                                    Escribir.genToken("ID", pos);
                                    res.setFirst("id");
                                    Pair<String, Integer> auxPair = res.getSecond();
                                    auxPair.setFirst(""+pos);
                                    auxPair.setSecond(pilaTS.peek().getTSID());
                                    res.setSecond(auxPair);
                                }
                            } else {
                                boolean encontrado = false;
                                TablaSimbolos TS = pilaTS.peek();
                                for (int i = pilaTS.size()-1; i >= 0 && !encontrado ; i--) {
                                    TS = pilaTS.elementAt(i);
                                    pos = TS.getID(lex);
                                    if (pos != null) {
                                        encontrado = true;
                                    }
                                }
                                /*for (TablaSimbolos TS : pilaTS) { //En qué orden va este for-Each?? deberia primero local y luego global
                                    pos = TS.getID(lex);
                                    if (pos != null) {
                                        encontrado = true;
                                        break;
                                    }
                                }*/

                                if (!encontrado) { // Si no se ha encontrado es decir que tenemos una declaracion
                                                   // implicita
                                    TablaSimbolos TSG = pilaTS.firstElement();
                                    pos = TSG.getPos();
                                    TSG.insertar(lex);
                                    TSG.getAtributos(pos).setTipo("entero");
                                    TSG.getAtributos(pos).setDespl(TSG.getDesp() + 1);
                                }
                                Escribir.genToken("ID", pos);
                                res.setFirst("id");
                                Pair<String, Integer> auxPair = res.getSecond();
                                auxPair.setSecond(TS.getTSID());
                                auxPair.setFirst(""+pos);
                                res.setSecond(auxPair);
                            }
                            this.estadoActual = "S";
                        }
                        reciclar = !dels.contains(caracter); // Si no es un delimitador, reciclar = true
                        break;
                    case "19":
                        if (this.num > Math.pow(2, 15))
                            gestorErr("MAXENT");
                        else {
                            Escribir.genToken("ENT", this.num);
                            res.setFirst("ent");
                            Pair<String, Integer> auxPair = res.getSecond();
                            auxPair.setFirst(""+this.num);
                            res.setSecond(auxPair);
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
            case "DUPID":
                System.err.println("Doble declaracion de la misma variable en la linea " + (this.nLineas));
                //System.err.println("Doble declaracion de la variable " + lex + " en la linea " + (this.nLineas - 1)); //Sugerencia
                break;
            case "MAXCAD":
                System.err.println("Cadena supera los 64 caracteres"); // ERROR MAXCAD
                //System.err.println("Cadena " + lex + " supera los 64 caracteres"); // ERROR MAXCAD // Sugerencia
                break;
            case "MAXENT":
                System.err.println("Entero fuera de rango, el numero es mayor que 2^15"); // ERROR MAXENT
                break;
            case "\n":
            case "\r":
                switch (this.estadoActual) { // Errores debido a un salto de linea indebido
                    case "A":
                        System.err.println("Error en la línea " + (this.nLineas - 1) + " por CR tras *");
                        break;
                    case "K":
                        System.err.println("Error en la línea " + (this.nLineas - 1) + " por CR tras !");
                        break;
                    case "C":
                        System.err.println("Error en la línea " + (this.nLineas - 1) + " por CR tras &");
                        break;
                    case "D":
                        System.err.println("Error en la línea " + (this.nLineas - 1) + " por CR tras |");
                        break;
                    case "E":
                        System.err.println("Cadena no valida en la linea " + (this.nLineas - 1)
                                + " se esperaba un \" pero se encontro CR");
                        break;
                    case "I":
                        System.err.println("Error en la línea " + (this.nLineas - 1) + " por CR tras /");
                        break;
                    case "F":
                        System.err
                                .println("Error en la línea " + (this.nLineas - 1) + " por CR tras \\");
                        break;
                }
                break;
            default: // Resto de casos de errores
                System.err.println("Error en la línea " + this.nLineas);
                System.err.println("Tras la lectura del caracter: " + caso);
        }
        System.err.println("-------------------------------------"); // Separacion de errores
    }

    /**
     * -------------- getTS ----------------
     * Getter de la tabla de simbolos.
     */
    public Stack<TablaSimbolos> getTS() {
        return pilaTS;
    }
}