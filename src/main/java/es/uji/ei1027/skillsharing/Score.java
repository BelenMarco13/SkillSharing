package es.uji.ei1027.skillsharing;

public enum Score {
    UNO("1"),
    DOS("2"),
    TRES("3"),
    CUATRO("4"),
    CINCO("5");

    private String valor;

    Score(String valor) {
        this.valor = valor;
    }

    public String getValor(){
        return valor;
    }
}
