package mips;

import entities.TokenDataPair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class RegisterManager {
    private static final int lengthRegisters = 10;
    private Register[] registers = new Register[lengthRegisters];

    private RegisterManager() {
        for(int i=0; i < lengthRegisters; i++) {
            registers[i] = new Register(-1);
        }
    }

    public static ArrayList<Integer> getUsageOfRegisters(ArrayList<String> lines) {
        HashMap<Integer, ArrayList<Integer>> registersEnding = new HashMap<>();

        int lineWeAreAt = 1;
        for(String line: lines) {

            String[] tokens = line.split(" ");

            for(String token : tokens) {
                if(token.matches("^t\\d+$")) {
                    int registerNumber = Integer.parseInt(token.substring(1));
                    //Si no esta directament mapejat el agreugem a la taula
                    if(!registersEnding.containsKey(registerNumber)) registersEnding.put(registerNumber, new ArrayList<>());

                    registersEnding.get(registerNumber).add(lineWeAreAt);
                }

            }
            lineWeAreAt++;
        }

        ArrayList<Integer> endsUsageAt = new ArrayList<>();
        for(Integer idRegister : registersEnding.keySet()) {
            Collections.sort(registersEnding.get(idRegister));

            endsUsageAt.add(registersEnding.get(idRegister).get(registersEnding.get(idRegister).size()-1));
        }
        return endsUsageAt;
    }

    public static void kColoringGraphRegisterGenerator(ArrayList<String> lines) {
        ArrayList<Integer> endingUsageOfRegisters = getUsageOfRegisters(lines);
        System.out.println("TEST");
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
