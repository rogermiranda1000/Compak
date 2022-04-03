package entities;

public class SymbolTableFunctionEntries extends SymbolTableEntries {
    private final VariableTypes returnType;
    private final VariableTypes[] arguments;

    public SymbolTableFunctionEntries(VariableTypes returnType, String name, VariableTypes[] arguments, int scope) {
        super(name, 0, scope);
        this.returnType = returnType;
        this.arguments = arguments;
    }

    public VariableTypes getReturnType() {
        return this.returnType;
    }

    public VariableTypes[] getArguments() {
        return arguments;
    }
}
