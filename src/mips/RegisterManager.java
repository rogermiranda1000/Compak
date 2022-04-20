package mips;

public class RegisterManager {
    private static final int lengthRegisters = 10;
    private Register[] registers = new Register[lengthRegisters];

    private RegisterManager() {
        for(int i=0; i < lengthRegisters; i++) {
            registers[i] = new Register(-1);
        }
    }

    private void checkIfWeCanFreeRegisters(int actualPosition) {
        for(int i=0; i < lengthRegisters; i++) {
            if(registers[i].getPositionThatEnds() < actualPosition) registers[i].setStatus(false);
        }
    }

    private int lookForFreeRegister() throws NoMoreRegistersException {
        for(int i=0; i < lengthRegisters; i++) {
            if(this.registers[i].getStatus()) return i;
        }
        throw new NoMoreRegistersException("ERROR: No more registers available");
    }

    private void freeRegister(int id) {
        registers[id].setStatus(false);
    }
}
