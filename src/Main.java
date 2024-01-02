import java.io.File;

public class Main {
    // Analiza el fichero pasado como primer argumento en la linea de comandos y
    // genera token.txt y TS.txt
    public static void main(String[] args) {
        File file = new File(args[0]);
        AFD automata = new AFD();
        AStSe aSt = new AStSe(automata);
        aSt.analisis(file);
    }
}