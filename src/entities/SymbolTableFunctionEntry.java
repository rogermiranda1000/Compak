package entities;

public class SymbolTableFunctionEntry extends SymbolTableEntry {
    private final VariableTypes returnType;
    private final VariableTypes[] arguments;

    public SymbolTableFunctionEntry(VariableTypes returnType, String name, VariableTypes[] arguments, SymbolTable scope) {
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
