package entities;

/**
 * Enum VariableTypes. It consists in all types defined in Compak's language.
 */
public enum VariableTypes {
    INT(4),
    BIG(16),
    FLO(4),
    STR(1),
    BIT(1),
    AF(4),

    VOID(0), // para funciones
    UNKNOWN(0); // aún no se ha computado

    private int size;
    private VariableTypes(int size) {
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }

    public static VariableTypes tokenToVariableType(Token t) {
        switch (t) {
            case STR:
                return VariableTypes.STR;

            case BIG:
                return VariableTypes.BIG;

            case INT:
                return VariableTypes.INT;

            case BIT:
                return VariableTypes.BIT;

            case FLO:
                return VariableTypes.FLO;

            default:
                return VariableTypes.UNKNOWN;
        }
    }
}
