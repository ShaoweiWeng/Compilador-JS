public class Atributos {
    private String lex;
    
    private String tipo;
    private Integer despl;
    private Integer numParam;
    private String tipoParam;
    private String tipoRetorno;
    private String etiqFuncion;

    private Integer TSid; // id de la tabla de simbolos donde fue añadido el lexema

    public String getLex() {
        return lex;
    }

    public void setLex(String lex) {
        this.lex = lex;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getDespl() {
        return despl;
    }

    public void setDespl(Integer despl) {
        this.despl = despl;
    }

    public Integer getNumParam() {
        return numParam;
    }

    public void setNumParam(Integer numParam) {
        this.numParam = numParam;
    }

    public String getTipoParam() {
        return tipoParam;
    }

    public void setTipoParam(String tipoParam) {
        this.tipoParam = tipoParam;
    }

    public String getTipoRetorno() {
        return tipoRetorno;
    }

    public void setTipoRetorno(String tipoRetorno) {
        this.tipoRetorno = tipoRetorno;
    }

    public String getEtiqFuncion() {
        return etiqFuncion;
    }

    public void setEtiqFuncion(String etiqFuncion) {
        this.etiqFuncion = etiqFuncion;
    }

    public Integer getTSid() {
        return TSid;
    }

    public void setTSid(Integer TSid) {
        this.TSid = TSid;
    }
}
