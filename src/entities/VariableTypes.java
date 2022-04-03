package entities;

public enum VariableTypes {
    INT(4),
    BIG(16),
    FLO(4),
    STR(1),
    BIT(1),

    VOID(0), // para funciones
    UNKNOWN(0); // aún no se ha computado

    private int size;
    private VariableTypes(int size) {
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }
}
