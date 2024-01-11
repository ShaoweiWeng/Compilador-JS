import java.io.*;
import java.util.Set;
import java.util.Map.Entry;

public class Escribir {

    // ---------------------------------------------------- tokens ----------------------------------------------------
    public static final String tokens[] = { "<COMA,>\n", "<PCOMA,>\n", "<PP,>\n", "<PARIZ,>\n", "<PARDE,>\n",
            "<LLAVEIZ,>\n", "<LLAVEDE,>\n", "<SUM,>\n", "<RES,>\n", "<MULA,>\n", "<EQUAL,>\n", "<NOTEQ,>\n", "<AND,>\n",
            "<OR,>\n" };

    public static final String valPR[] = { "<BOOL,>\n", "<BREAK,>\n", "<CASE,>\n", "<FUNCTION,>\n", "<GET,>\n",
            "<IF,>\n", "<INT,>\n", "<LET,>\n", "<PUT,>\n", "<RETURN,>\n", "<STRING,>\n", "<SWITCH,>\n", "<VOID,>\n",
            "<DEFAULT,>\n", "<FALSE,>\n", "<TRUE,>\n" };

    public static final String keyPR[] = { "boolean", "break", "case", "function", "get",
            "if", "int", "let", "put", "return", "string", "switch", "void",
            "default", "false", "true" };

    public static final String EOF = "<EOF,>\n";
    public static final String ASIG = "<ASIG,>\n";

    /**
     * -------------- genToken ----------------
     * Genera todos los tokens sin atributos.
     * 
     * @param token El token que ha de generar.
     */
    public static void genToken(String token) {
        try (FileWriter escribir = new FileWriter("token.txt", true)) {
            escribir.write(token);
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    /**
     * ------------------- genToken ---------------------
     * Genera el token de los numeros enteros y el token
     * de los identificadores.
     * 
     * @param token El token que ha de generar.
     * @param num   Numero entero, atributo del token.
     */
    public static void genToken(String token, int num) {
        if (token == "ENT")
            genToken("<ENT," + num + ">\n");
        else if (token == "ID")
            genToken("<ID," + num + ">\n");
    }

    /**
     * -------------------- genToken -----------------------
     * Genera el token de las cadenas de texto.
     * 
     * @param token El token que ha de generar.
     * @param lex   Cadena de texto, el atributo del token.
     */
    public static void genToken(String token, String lex) {
        if (token == "CADENA")
            genToken("<CADENA," + "\"" + lex + "\"" + ">\n");
    }

    /**
     * --------------------------------- printTS ------------------------------------
     * Volcado completo "legible" con toda la informacion de la tabla de simbolos.
     * 
     * @param numTs   El ID de la tabla de simbolos.
     * @param lexemas Todos los identificadores guardados en la tabla de simbolos.
     */
    public static void printTS(TablaSimbolos ts) {
        Integer numTS = ts.getTSID();
        Set<Entry<String, Integer>> lexemas = ts.getEntrys().entrySet();
        
        String fichero = "TS.txt";
        String encabezado;
        if (numTS == 0) encabezado = "Tabla Simbolos Grobal # " + numTS + " : \n";
        else encabezado = "Tabla Simbolos Local # " + numTS + " : \n";
        String separador = "----------------------------------------------------------\n";
        String atributos = "ATRIBUTOS :\n";
        String[] lexOrdenado = ordenar(lexemas);

        try (FileWriter escribir = new FileWriter(fichero, true);) {
            int i;
            // Escribo el token
            escribir.write(encabezado);
            if (lexOrdenado.length != 0) { // Por si no existe ninguna ID
                for (i = 0; i < lexOrdenado.length; i++) {
                    String lexema = " * LEXEMA : " + "'" + lexOrdenado[i] + "'\n"; // nombre variable ID
                    escribir.write(lexema);
                    escribir.write(atributos);
                    
                    Atributos atrib = ts.getAtributos(i);
                    String Tipo = " + Tipo : '" + atrib.getTipo() + "'\n";
                    escribir.write(Tipo);
                    if (!atrib.getTipo().equals("funcion")) {
                        String Desp = " + Despl : " + atrib.getDespl() + "\n";
                        escribir.write(Desp);
                    }
                    else { // tipo funcion
                        String numParam = " + numParam : " + atrib.getNumParam() + "\n";
                        escribir.write(numParam);
                        if (atrib.getNumParam() > 0) {
                            String TipoParam = " + TipoParam : '" + atrib.getTipoParam() + "'\n";
                            escribir.write(TipoParam);
                        }
                        String TipoRetorno = " + TipoRetorno : '" + atrib.getTipoRetorno() + "'\n";
                        escribir.write(TipoRetorno);
                        String EtiqFuncion = " + EtiqFuncion : '" + atrib.getEtiqFuncion() + "'\n";
                        escribir.write(EtiqFuncion);
                    }

                    escribir.write(separador);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    /**
     * ---------------------------- ordenar -------------------------------
     * Ordena las entradas de la tabla de simbolos con su posicion real en
     * la TS.
     * 
     * @param lexemas Identificadores guardados en la tabla de simbolos.
     */
    private static String[] ordenar(Set<Entry<String, Integer>> lexemas) {
        String lexOrdenado[] = new String[lexemas.size()];
        for (Entry<String, Integer> lex : lexemas)
            lexOrdenado[lex.getValue()] = lex.getKey();
        return lexOrdenado;
    }

    public static void parse(String regla) {
        String fichero = "parse.txt";
        try {
            // comprobamos si existe el fichero
            boolean existeFichero = new File(fichero).exists();
            FileWriter escribir = new FileWriter(fichero, true);
            if (!existeFichero) escribir.write("Descendente");
            escribir.write(" " + regla);
            escribir.close();
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }
}