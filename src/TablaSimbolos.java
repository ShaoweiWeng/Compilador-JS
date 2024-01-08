import java.util.ArrayList;
import java.util.HashMap;

public class TablaSimbolos {
    private HashMap<String, Integer> entrys; // tabla de simbolos con lex(el ID) como key y pos del linkedList
    private ArrayList<Atributos> atributos;
    private Integer tsPos; // posicion de la tabla de simbolos
    private Integer desp; // posicion de la tabla de simbolos

    private Integer TSID;

    public TablaSimbolos(Integer TSID) {
        this.tsPos = 0;
        this.desp = 0;
        this.entrys = new HashMap<>();
        this.atributos = new ArrayList<>();
        this.TSID = TSID;
    }

    public void insertar(String lex) {
        entrys.put(lex, this.tsPos);
        Atributos atrib = new Atributos();
        atrib.setLex(lex);
        atributos.add(this.tsPos, atrib);
        this.tsPos++;
    }

    public Integer getID(String lex) {
        return entrys.get(lex);
    }

    public Atributos getAtributos(Integer pos) {
        return atributos.get(pos);
    }

    public Integer getPos() {
        return tsPos;
    }

    public Integer getDesp() {
        return desp;
    }

    public void setDesp(Integer desp) {
        this.desp = desp;
    }

    public HashMap<String, Integer> getEntrys() {
        return entrys;
    }

    public Integer getTSID() {
        return TSID;
    }
}